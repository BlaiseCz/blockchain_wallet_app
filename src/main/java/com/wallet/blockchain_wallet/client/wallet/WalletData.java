package com.wallet.blockchain_wallet.client.wallet;

import lombok.Data;

import java.security.PrivateKey;
import java.security.PublicKey;

@Data
public class WalletData {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private String walletAddress = "";
}
