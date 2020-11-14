package com.wallet.blockchain_wallet.client.wallet;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.wallet.blockchain_wallet.client.protocol.BasicProtocolInterpreter;
import com.wallet.blockchain_wallet.client.protocol.ProtocolInterpreter;
import com.wallet.blockchain_wallet.client.utils.IpUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Random;


@Slf4j
@Component
@RequiredArgsConstructor
public class WalletClient implements WalletService {

    /**
     * COMMUNICATION
     */
    private Client client;
    private Server server;

    /**
     * COMMUNICATION DATA
     */
    @Setter
    private HostInfo coreConnectionParams;
    @Setter
    @Getter
    private WalletData walletData;

    /**
     * COMMUNICATION PARAMS
     */
    private int serverPort;
    private static final int CONNECTION_TIMEOUT = 5000;

    /**
     * RESPONSES INTERPRETER
     */
    private final ProtocolInterpreter interpreter;


    @Override
    public void sendMessage(String msg) throws WalletException {
        if (coreConnectionParams == null) {
            throw new WalletException("HostInfo is null!");
        }

        try {
            client.start();
            client.connect(CONNECTION_TIMEOUT, coreConnectionParams.getAddress(), coreConnectionParams.getPort());
            CommunicationObject msgToSend = new CommunicationObject(msg, serverPort);
            client.sendTCP(msgToSend);
            log.info("Sent to localhost:{} msg=\"{}\"", coreConnectionParams.getPort(), msgToSend.getText());
        } catch (Exception e) {
            e.printStackTrace();
            throw new WalletException("Could not send TCP packet to " + coreConnectionParams.getAddress() + ":" + coreConnectionParams.getPort());
        }
    }

    public void closeClient() {
        client.close();
    }

    public void joinToWeb(HostInfo hostInfo) throws
                                             IOException,
                                             WalletException {
        this.coreConnectionParams = hostInfo;
        this.client = new Client();
        this.server = new Server();
        registerCommunicationClass();
        startServer();
    }

    private void registerCommunicationClass() {
        Kryo clientKryo = client.getKryo();
        clientKryo.register(CommunicationObject.class);
        Kryo serverKryo = server.getKryo();
        serverKryo.register(CommunicationObject.class);
        log.info("Registered communication class");
    }


    private void initializeInterpreter() {
        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof CommunicationObject) {
                    CommunicationObject response = (CommunicationObject) object;
                    log.info("received {} from localhost:{}", response.getText(), response.getSenderPort());
                    delegateToInterpretMessage(connection, response);
                }
            }
        });
    }

    private void startServer() throws WalletException, IOException {
        new Thread(server).start();
        serverPort = discoverPort();
        server.bind(serverPort);
        log.info("Server started at port: {}", serverPort);
        initializeInterpreter();
    }


    private void delegateToInterpretMessage(Connection connection, CommunicationObject object) {

        String hostAddress = connection.getRemoteAddressTCP().getAddress().getHostAddress();
        String port = String.valueOf(connection.getRemoteAddressTCP().getPort());
        String text = object.getText();
        log.info("Interpreting: {} from {}:{}", text, hostAddress, port);
        interpreter.interpretMessage(text);
    }

    private int discoverPort() throws WalletException {
        Random random = new Random();
        int tryNumber = 0;
        do {
            int port = random.ints(1, 49152, 65535)
                    .sum();

            if (IpUtils.isPortAvailable(port)) {
                return port;
            }

            tryNumber++;
        } while (tryNumber > 10);
        throw new WalletException("Failed to Create server socket");
    }
}
