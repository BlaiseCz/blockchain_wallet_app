package com.wallet.blockchain_wallet.client.wallet;

import lombok.Data;
import lombok.Getter;


@Data
@Getter
public class WalletData {
    private String privateKey;
    private String publicKey;
    private String walletAddress;
}
