package com.wallet.blockchain_wallet.gui.components;

import com.google.gson.Gson;
import com.wallet.blockchain_wallet.client.wallet.WalletData;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class WalletDataBuilder {

    public static WalletData jsonToWalletData(String data) {
        Gson gson = new Gson();
        return gson.fromJson(data, WalletData.class);
    }
}
