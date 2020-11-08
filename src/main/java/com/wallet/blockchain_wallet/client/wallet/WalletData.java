package com.wallet.blockchain_wallet.client.wallet;

import lombok.Data;
import lombok.Getter;

import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@Getter
public class    WalletData {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private String hash = "";
    private Transaction lastTransaction;
}
