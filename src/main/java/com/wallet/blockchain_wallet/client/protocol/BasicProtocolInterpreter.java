package com.wallet.blockchain_wallet.client.protocol;

import com.wallet.blockchain_wallet.client.protocol.header.HeaderProcessor;
import com.wallet.blockchain_wallet.client.protocol.header.NodesResponse;
import com.wallet.blockchain_wallet.client.protocol.header.ProtocolHeader;
import com.wallet.blockchain_wallet.client.wallet.HostInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class BasicProtocolInterpreter implements ProtocolInterpreter {


    @Override
    public void interpretMessage(String message) {
        log.info("received message: " + message);
        Optional<ProtocolHeader> optionalProtocolHeader = ProtocolHeader.getFromCode(message.substring(0, 2));
        optionalProtocolHeader.ifPresent(protocolHeader -> proceed(protocolHeader,
                message.substring(2)));
    }

    private void proceed(ProtocolHeader protocolHeader, String value) {
        switch (protocolHeader) {
            case NOTIFY_WALLET:
                System.out.println("NOTIFY_WALLET");
                break;
            case WALLET_DATA_RESPONSE:
                System.out.println("WALLET_DATA_RESPONSE");
                break;
            case WALLETS_RESPONSE:
                System.out.println("WALLETS_RESPONSE");
                break;
            case NODES_RESPONSE:
                NODES_RESPONSE(value);
                break;
            default:
                break;
        }
    }

    private void NODES_RESPONSE(String value) {
        NodesResponse nodesResponse = new NodesResponse();
        List<HostInfo> hostInfoList = nodesResponse.hostInfoListFromData(value);
    }

}
