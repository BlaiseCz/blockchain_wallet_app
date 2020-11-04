package com.wallet.blockchain_wallet.client.wallet;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.wallet.blockchain_wallet.client.protocol.ProtocolInterpreter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Random;


@Slf4j
@Data
public class WalletClient implements WalletService {
    private Client client;
    private Random random;

    private HostInfo coreConnectionParams;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private static final int CONNECTION_TIMEOUT = 5000;


    private ProtocolInterpreter interpreter;

    public WalletClient(HostInfo hostInfo) throws WalletException {
        try {
            this.coreConnectionParams = hostInfo;
            this.client = new Client();
            registerCommunicationClass();
        } catch (Exception e) {
            throw new WalletException("Something wrong with WalletClient constructor...");
        }
    }

    @Override
    public void sendMessage(String msg) throws WalletException {
        if (coreConnectionParams == null) {
            throw new WalletException("HostInfo is null!");
        }

        try {
            client.start();
            client.connect(CONNECTION_TIMEOUT, coreConnectionParams.getAddress(), coreConnectionParams.getPort());
            CommunicationObject msgToSend = new CommunicationObject(msg);

            log.info("Sending to localhost:{} msg{}", coreConnectionParams.getPort(), msgToSend.getText());
            client.sendTCP(msgToSend.getText());
            initializeInterpreter();
        } catch (Exception e) {
            e.printStackTrace();
            throw new WalletException("Could not send TCP packet to " + coreConnectionParams.getAddress() + ":" + coreConnectionParams.getPort());
        }
    }


    @Override
    public void getData() {

    }

    private void registerCommunicationClass() {

        Kryo clientKryo = client.getKryo();
        clientKryo.register(CommunicationObject.class);
        log.info("Registered communication class");
    }


    private void initializeInterpreter() throws IOException {
        client.connect(CONNECTION_TIMEOUT, coreConnectionParams.getAddress(), coreConnectionParams.getPort());

        client.addListener(new Listener() {
            @Override
            public void received (Connection connection, Object object) {
                if (object instanceof CommunicationObject) {
                    CommunicationObject response = (CommunicationObject)object;
                    log.info("received {} from localhost:{}", response.getText());
                }
            }
        });
    }



    private void delegateToInterpretMessage(Connection connection, CommunicationObject object) {

        String hostAddress = connection.getRemoteAddressTCP().getAddress().getHostAddress();
        String port = String.valueOf(connection.getRemoteAddressTCP().getPort());
        String text = object.getText();
        log.info("Interpreting: {} from {}:{}", text, hostAddress, port);
        interpreter.interpretMessage(text);
    }

    public void closeClient() {
        client.close();
    }
}
