package com.wallet.blockchain_wallet.client.wallet;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PublicKey;

@Data
@NoArgsConstructor
public class Transaction {

    private String address;
    private PublicKey sender;
    private PublicKey reciepient;
    private String signature;

    private String amount;

    public Transaction(String address, PublicKey from, PublicKey to, String amount) {
        this.address = address;
        this.sender = from;
        this.reciepient = to;
        this.amount = amount;
    }
}