package com.wallet.blockchain_wallet.client.protocol.header;

import lombok.Getter;
import lombok.var;

import java.util.Optional;

public enum ProtocolHeader {

    NOTIFY_NODE("NN"),
    NOTIFY_WALLET("NW"),
    ADD_BLOCK("AB"),
    TRANSACTION("TS"),
    WALLET_DATA_REQUEST("WD"),
    WALLET_DATA_RESPONSE("WR"),
    WALLETS_REQUEST("WS"),
    WALLETS_RESPONSE("WE"),
    NODES_REQUEST("NS"),
    NODES_RESPONSE("NR");


    ProtocolHeader(String code) {
        this.code = code;
    }

    @Getter
    private final String code;

    public static Optional<ProtocolHeader> getFromCode(String code) {
        for (var header : values()) {
            if (header.code.equals(code)) {
                return Optional.of(header);
            }
        }
        return Optional.empty();
    }

}
