package com.wallet.blockchain_wallet.client.protocol;

import com.wallet.blockchain_wallet.client.protocol.header.NodesResponse;
import com.wallet.blockchain_wallet.client.protocol.header.ProtocolHeader;
import com.wallet.blockchain_wallet.gui.components.MainController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Slf4j
@Service
public class BasicProtocolInterpreter implements ProtocolInterpreter {

    @Override
    public void interpretMessage(String message) {
        log.info("received message: " + message);
        Optional<ProtocolHeader> optionalProtocolHeader = ProtocolHeader.getFromCode(message.substring(0, 2));
        optionalProtocolHeader.ifPresent(protocolHeader -> proceed(protocolHeader,
                message.substring(2)));
    }

    private void proceed(ProtocolHeader protocolHeader, String value) {
        log.info("Proceeding with header {} with value {}", protocolHeader, value);
        switch (protocolHeader) {
            case NOTIFY_WALLET:
                log.info("NOTIFY_WALLET");
                break;
            case WALLET_DATA_RESPONSE:
                log.info("WALLET_DATA_RESPONSE");
                break;
            case WALLETS_RESPONSE:
                log.info("WALLETS_RESPONSE");
                break;
            case NODES_RESPONSE:
                log.info("NODES_RESPONSE");
                nodesResponse(value);
                break;
            default:
                break;
        }
    }

    private void nodesResponse(String value) {
        NodesResponse nodesResponse = new NodesResponse();
//    TODO KURWA
    }
}
