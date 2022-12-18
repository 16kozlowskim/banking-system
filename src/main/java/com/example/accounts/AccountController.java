package com.example.accounts;

import com.google.cloud.spanner.Key;
import com.google.cloud.spanner.Statement;
import com.google.cloud.spanner.Value;
import com.google.cloud.spring.data.spanner.core.SpannerOperations;
import com.google.cloud.spring.data.spanner.core.SpannerQueryOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class AccountController {

    private static final String OUTGOING_ACCOUNT_TRANSFERS = "SELECT * FROM Transfers WHERE AccountId='%s'";
    private static final String INCOMING_ACCOUT_TRANSFERS =
            "SELECT * FROM Transfers@{FORCE_INDEX=TransfersByDestAccountIdTimestamp} WHERE DestAccId='%s'";

    private Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private SpannerOperations spannerOperations;

    @PutMapping("accounts")
    public void createAccount(@RequestBody Account account) {
        logger.info("Upsert account: {}", account);
        spannerOperations.upsert(account);
    }

    @GetMapping("accounts")
    public List<Account> getAccounts() {
        return spannerOperations.readAll(Account.class);
    }

    @GetMapping("accounts/{accountId}")
    public Account getAccount(@PathVariable String accountId) {
        Account account = spannerOperations.read(Account.class, Key.of(accountId));
        logger.info("Get account: {}", account);
        return account;
    }

    @GetMapping("accounts/{accountId}/transfers")
    public List<Transfer> getAccountTransfers(@PathVariable String accountId, @RequestParam boolean outgoing) {
        if (outgoing) {
            return spannerOperations
                    .query(Transfer.class,
                            Statement.of(String.format(OUTGOING_ACCOUNT_TRANSFERS, accountId)),
                            new SpannerQueryOptions());

        }
        return spannerOperations
                .query(Transfer.class,
                        Statement.of(String.format(INCOMING_ACCOUT_TRANSFERS, accountId)),
                        new SpannerQueryOptions());

    }

    @PostMapping("accounts/{accountId}/transfers")
    public void transfer(
            @PathVariable String accountId,
            @RequestParam String transferId,
            @RequestParam String destAccId,
            @RequestParam BigDecimal amount) {

        logger.info("Transfer for: {}", amount);

        spannerOperations.performReadWriteTransaction(template -> {
            Account from = template.read(Account.class, Key.of(accountId));
            Account to = template.read(Account.class, Key.of(destAccId));

            from.addToBalance(amount.negate());
            to.addToBalance(amount);

            template.update(from);
            template.update(to);
            Transfer transfer =
                    Transfer.builder()
                            .accountId(accountId)
                            .transferId(transferId)
                            .destAccId(destAccId)
                            .amount(amount)
                            .timestamp(Value.COMMIT_TIMESTAMP).build();
            template.insert(transfer);
            return null;
        });
    }
}
