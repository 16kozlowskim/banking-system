package com.example.accounts;

import com.google.cloud.spring.data.spanner.core.mapping.Column;
import com.google.cloud.spring.data.spanner.core.mapping.PrimaryKey;
import com.google.cloud.spring.data.spanner.core.mapping.Table;
import com.google.spanner.v1.TypeCode;
import lombok.Data;


import java.math.BigDecimal;

@Data
@Table(name = "Accounts")
public class Account {

    @PrimaryKey
    @Column(name = "AccountId")
    private String accountId;
    @Column(name = "Owner")
    private String owner;
    @Column(name = "Balance", spannerType = TypeCode.NUMERIC)
    private double balance;

    public void addToBalance(double amount) {
        this.balance += amount;
    }
}
