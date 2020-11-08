package com.wallet.blockchain_wallet.client.utils;

import com.google.gson.Gson;
import com.wallet.blockchain_wallet.client.wallet.HostInfo;
import com.wallet.blockchain_wallet.client.wallet.WalletData;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class GsonBuilders {

    private static final Gson gson = new Gson();

    public static WalletData jsonToWalletData(String walletDataJson) {
        return gson.fromJson(walletDataJson, WalletData.class);
    }

    public static HostInfo jsonToHostInfo(String hostInfoJson) {
        return gson.fromJson(hostInfoJson, HostInfo.class);
    }
}
