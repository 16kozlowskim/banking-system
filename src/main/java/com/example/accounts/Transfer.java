package com.example.accounts;


import com.google.cloud.Timestamp;
import com.google.cloud.spring.data.spanner.core.mapping.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Transfer {
    //@Column(name = "AccountId")
    private String accountId;
    //@Column(name = "Timestamp")
    private Timestamp timestamp;
    //@Column(name = "TransferId")
    private String transferId;
    //@Column(name = "DestAccId")
    private String destAccId;
    //@Column(name = "Amount")
    private BigDecimal amount;
}
