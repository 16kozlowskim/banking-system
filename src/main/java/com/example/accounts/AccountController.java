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
    }

    @GetMapping("accounts")
    public List<Account> getAccounts() {
        return List.of();
    }

    @GetMapping("accounts/{accountId}")
    public Account getAccount(@PathVariable String accountId) {
        Account account = new Account();
        logger.info("Get account: {}", account);
        return account;
    }

    @GetMapping("accounts/{accountId}/transfers")
    public List<Transfer> getAccountTransfers(@PathVariable String accountId, @RequestParam boolean outgoing) {
        if (outgoing) {
            return List.of();

        }
        return List.of();

    }

    @PostMapping("accounts/{accountId}/transfers")
    public void transfer(
            @PathVariable String accountId,
            @RequestParam String transferId,
            @RequestParam String destAccId,
            @RequestParam BigDecimal amount) {

        logger.info("Transfer for: {}", amount);

        spannerOperations.performReadWriteTransaction(template -> null);
    }
}
