package com.wallet.blockchain_wallet.client.protocol.header.response;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class WalletsResponseProcessor implements WalletsResponse{

    @Override
    @SneakyThrows
    public List<String> walletsResponseList(String data) {
        List<String> wallets = new ArrayList<>();
        wallets.add("test123");
        wallets.add("test1234XD");
        log.info("Received WalletsList {}", wallets);
        return wallets;
    }
}
