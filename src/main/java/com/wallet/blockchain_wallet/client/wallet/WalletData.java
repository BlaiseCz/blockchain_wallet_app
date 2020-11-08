package com.wallet.blockchain_wallet.client.wallet;

import com.wallet.blockchain_wallet.client.wallet.signature.PrivateKeyProvider;
import lombok.Data;
import lombok.SneakyThrows;

import java.security.PrivateKey;


@Data
public class WalletData {
    private String privateKey;
    private String publicKey;
    private String walletAddress;

    @SneakyThrows
    public PrivateKey getPrivateKey() {
        return PrivateKeyProvider.getPrivateKeyFromString(privateKey);
    }
}
