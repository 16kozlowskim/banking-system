package com.example.accounts;


import com.google.cloud.Timestamp;
import com.google.cloud.spring.data.spanner.core.mapping.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Table(name = "Transfers")
public class Transfer {
    @PrimaryKey
    @Column(name = "AccountId")
    private String accountId;
    @PrimaryKey(keyOrder = 2)
    @Column(name = "Timestamp")
    private Timestamp timestamp;
    @PrimaryKey(keyOrder = 3)
    @Column(name = "TransferId")
    private String transferId;
    @Column(name = "DestAccId")
    private String destAccId;
    @Column(name = "Amount")
    private BigDecimal amount;
}
