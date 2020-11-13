package com.wallet.blockchain_wallet.client.protocol;

import com.wallet.blockchain_wallet.client.protocol.header.ProtocolHeader;
import com.wallet.blockchain_wallet.client.protocol.header.response.NodesResponseProcessor;
import com.wallet.blockchain_wallet.client.protocol.header.response.WalletDataResponseProcessor;
import com.wallet.blockchain_wallet.client.protocol.header.response.WalletsResponseProcessor;
import com.wallet.blockchain_wallet.gui.components.controllers.MainController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import sun.applet.Main;

import java.util.Optional;

@Slf4j
@Service
public class BasicProtocolInterpreter implements ProtocolInterpreter {

    private final MainController mainController;

    public BasicProtocolInterpreter(@Lazy MainController mainController) {
        this.mainController = mainController;
    }

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
            case WALLET_DATA_RESPONSE:
                log.info("WALLET_DATA_RESPONSE");
                walletDateResponse(value);
                break;
            case WALLETS_RESPONSE:
                log.info("WALLETS_RESPONSE");
                walletsResponse(value);
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
        NodesResponseProcessor nodesResponseProcessor = new NodesResponseProcessor();
        mainController.updateHostInfo(nodesResponseProcessor.hostInfoListFromData(value));
    }

    private void walletDateResponse(String value) {
        WalletDataResponseProcessor walletDataResponseProcessor = new WalletDataResponseProcessor();
        mainController.updateCoins(walletDataResponseProcessor.receiveOwnedCoins(value));
    }

    private void walletsResponse(String value) {
        WalletsResponseProcessor walletsResponseProcessor = new WalletsResponseProcessor();
        mainController.updateWallets(walletsResponseProcessor.walletsResponseList(value));
    }
}
