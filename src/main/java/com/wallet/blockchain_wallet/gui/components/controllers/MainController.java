package com.wallet.blockchain_wallet.gui.components.controllers;

import com.wallet.blockchain_wallet.client.signature.SignatureUtils;
import com.wallet.blockchain_wallet.client.wallet.HostInfo;
import com.wallet.blockchain_wallet.client.wallet.WalletClient;
import com.wallet.blockchain_wallet.client.wallet.WalletException;
import com.wallet.blockchain_wallet.gui.components.WalletDataBuilder;
import com.wallet.blockchain_wallet.gui.components.fields.NumberTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class MainController {

    private WalletClient walletClient;
    private HostInfo hostInfo;

    @FXML
    private NumberTextField portNumberField;

    @FXML
    private TextField hostTextField;

    @FXML
    private Text ownedCoins;

    @FXML
    private ListView<HostInfo> hostInfoListView;

    @FXML
    private TextField coinsRecipient;

    @FXML
    private TextField coinsToSend;

    @FXML
    private TextField walletData;

    @FXML
    private Button notifyWalletButton;

    @FXML
    private Button balanceButton;

    @FXML
    private Button sendCoinsButton;

    public static List<HostInfo> hostInfoHolder = new ArrayList<>();
    public static boolean isHostInfoUpdated = false;

    public static int coinsBalance = 0;
    public static boolean isBalanceUpdated = false;


    @FXML
    void closeApp() {
        walletClient.closeClient();
        System.exit(0);
    }

    @FXML
    void nodesRequest() {
        initHostInfo();

        if (hostInfo != null) {
            try {
                startViewHostInfoListThread();
                setCoreParams();
                walletClient.sendMessage("NS");
                buttonAccess(true);
            } catch (Exception e) {
                ControllerAlert.showAlert(Alert.AlertType.ERROR, "ERROR", "NODES REQUEST ERROR");
            } catch (WalletException we) {
                ControllerAlert.showAlert(Alert.AlertType.WARNING, "WARNING", we.getMessage());
            }
        }
    }

    @FXML
    void notifyWallet() {
        try {
            String walletJsonData = walletData.getText();
            log.info("WalletData in JSON format {}" , walletJsonData);
            walletClient.setWalletData(WalletDataBuilder.jsonToWalletData(walletJsonData));
            String pk = SignatureUtils.getStringFromKey(walletClient.getWalletData().getPublicKey());
            String walletAddress = walletClient.getWalletData().getWalletAddress();
            walletClient.sendMessage("NW" + pk + "HASH:" + walletAddress);
        } catch (WalletException e) {
            ControllerAlert.showAlert(Alert.AlertType.ERROR, "ERROR", e.getMessage());
        }
    }

    @FXML
    public void viewHostInfoList(List<HostInfo> hostInfoList) {
        log.info("updating HostInfoList {}", hostInfoList);
        hostInfoListView.getItems().clear();
        hostInfoListView.getItems().addAll(hostInfoList);
        hostInfoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        switchIsHostInfoUpdated();
    }


    @FXML
    public void getWalletBallance() {
        log.info("get ballance");
        ownedCoins.setText("124");
    }

    @FXML
    public void performTransaction() {
        log.info("perform transaction");
        try {
            int userCoins = Integer.parseInt(ownedCoins.getText());
            int transactionCoins = Integer.parseInt(coinsToSend.getText());
            String recipientInfo = coinsRecipient.getText();
            log.info("available coins:" + userCoins + " amount: " + transactionCoins);

            if (userCoins > transactionCoins) {
                    log.info(hostInfo.getAddress() + ":" + hostInfo.getPort() + recipientInfo + " amount: " + transactionCoins);
                    walletClient.sendMessage("TSgjfd98g2h39ghn8e | fjh329f8h9e8yf290837fgyh |13.372137|128579683948| jfv78fdhv873b495bv7865987c632874");
            } else {
                ControllerAlert.showAlert(Alert.AlertType.WARNING, "WARNING", "not enough coins");
            }
        } catch (Exception | WalletException e) {
            ControllerAlert.showAlert(Alert.AlertType.ERROR, "ERROR", e.getMessage());
        }
    }

    @FXML
    public void handleRecipientClick() {
        HostInfo pickedValue = hostInfoListView.getSelectionModel().getSelectedItem();
        log.info("clicked on " + pickedValue.getAddress() + ":" + pickedValue.getPort());
        coinsRecipient.setText(pickedValue.getAddress() + ":" + pickedValue.getPort());
    }

    private void setCoreParams() throws WalletException {
        if (walletClient == null) {
            walletClient = new WalletClient(hostInfo);
        } else {
            walletClient.setCoreConnectionParams(hostInfo);
        }
    }

    /**
     * THREADS STARTERS BEGIN
     *
     * Threads are used for asynchronous server responses and update view
     */
    private void startViewHostInfoListThread() {
        new Thread(() -> {
            while (true) {
                if (isHostInfoUpdated) {
                    Platform.runLater(() -> viewHostInfoList(hostInfoHolder));
                    break;
                }
            }
        }).start();
    }

    private void startOwnedCoinsUpdateThread() {
        new Thread(() -> {
            while (true) {
                if (isBalanceUpdated) {
                    Platform.runLater(() -> viewHostInfoList(hostInfoHolder));
                    break;
                }
            }
        }).start();
    }

    /**
     * THREADS STARTERS END
     */

    private void initHostInfo() {
        try {
            int port = Integer.parseInt(portNumberField.getText());
            String host = hostTextField.getText();
            hostInfo = new HostInfo(host, port);
        } catch (Exception e) {
            ControllerAlert.showAlert(Alert.AlertType.ERROR, "ERROR", "WRONG PORT/ADDRESS");
        }
    }

    private void buttonAccess(boolean toggle) {
        notifyWalletButton.setDisable(!toggle);
        balanceButton.setDisable(!toggle);
        sendCoinsButton.setDisable(!toggle);
    }

    private void switchIsHostInfoUpdated() {
        isHostInfoUpdated = !isHostInfoUpdated;
    }

    private void switchIsBalanceUpdated() {
        isBalanceUpdated = !isBalanceUpdated;
    }
}
