package com.wallet.blockchain_wallet.client.wallet;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.wallet.blockchain_wallet.client.protocol.ProtocolInterpreter;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
@Data
public class WalletClient implements WalletService {

    private Client client;
    private static final int timeout = 100000;
    private HostInfo hostInfo;

    private ProtocolInterpreter interpreter;

    public WalletClient(HostInfo hostInfo) throws WalletException {
        try {
            this.hostInfo = hostInfo;
            client = new Client();
            registerCommunicationClass();
        } catch (Exception e) {
            throw new WalletException("Something wrong with WalletClient constructor...");
        }
    }


    public void sendMessage(String msg) throws WalletException {
        if(hostInfo == null) {
            throw new WalletException("HostInfo is null!");
        }

        if (!client.isConnected()) {
            try {
                client.start();
                client.connect(timeout, hostInfo.getAddress(), hostInfo.getPort());
            } catch (IOException e) {
                throw new WalletException("Could not connect...");
            }
        }
        try {
            client.sendTCP(new CommunicationObject(msg));
        } catch (Exception e) {
            throw new WalletException("Could not send TCP packet to " + hostInfo.getAddress() + ":" + hostInfo.getPort());
        }
    }

    public void getData() {

    }

    private void registerCommunicationClass() {
        Kryo clientKryo = client.getKryo();
        clientKryo.register(CommunicationObject.class);
        log.info("WalletClient registered");
    }

    private void initializeInterpreter() {
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof CommunicationObject) {
                    delegateToInterpretMessage(connection, (CommunicationObject) object);
                }
            }
        });
    }

    private void delegateToInterpretMessage(Connection connection, CommunicationObject object) {
        String text = object.getText();

        log.info("Interpreting: {}", text);
        interpreter.interpretMessage(text);
    }
}
