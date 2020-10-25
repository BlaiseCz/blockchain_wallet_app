package com.wallet.blockchain_wallet.client.wallet;

public interface WalletService {

    void sendMessage(String msg) throws WalletException;

    void getData();
}
