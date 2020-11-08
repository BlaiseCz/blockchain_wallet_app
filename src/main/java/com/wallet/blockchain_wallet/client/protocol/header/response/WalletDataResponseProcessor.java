package com.wallet.blockchain_wallet.client.protocol.header.response;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WalletDataResponseProcessor implements WalletDataResponse {

    @Override
    @SneakyThrows
    public double receiveOwnedCoins(String data) {

        double ownedCoinsResponse = 0.0;

        try {
            String receivedData = data.substring(2);
            int indexOfVerticalBar = receivedData.indexOf("|");
            receivedData = data.substring(0, indexOfVerticalBar);
            ownedCoinsResponse = Double.parseDouble(receivedData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("Received OwnedCoins {}", ownedCoinsResponse);
        return ownedCoinsResponse;
    }
}
