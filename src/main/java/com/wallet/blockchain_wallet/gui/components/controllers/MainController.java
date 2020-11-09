package com.wallet.blockchain_wallet.gui.components.controllers;

import com.wallet.blockchain_wallet.client.signature.SignatureApplier;
import com.wallet.blockchain_wallet.client.utils.GsonBuilders;
import com.wallet.blockchain_wallet.client.wallet.HostInfo;
import com.wallet.blockchain_wallet.client.wallet.WalletClient;
import com.wallet.blockchain_wallet.client.wallet.WalletException;
import com.wallet.blockchain_wallet.gui.components.fields.NumberTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import lombok.SneakyThrows;
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
    private Text ownedCoinsText;

    @FXML
    private ListView<HostInfo> hostInfoListView;

    @FXML
    private ListView<String> walletsListView;

    @FXML
    private TextField recipientWalletAddressTextField;

    @FXML
    private TextField coinsToSendTextField;

    @FXML
    private TextField walletDataTextField;

    @FXML
    private Button notifyWalletButton;

    @FXML
    private Button balanceButton;

    @FXML
    private Button sendCoinsButton;

    @FXML
    private Button requestWalletsButton;

    public static List<HostInfo> hostInfoHolder = new ArrayList<>();
    public static boolean isHostInfoUpdated = false;

    public static double coinsBalance = 0.0;
    public static boolean isBalanceUpdated = false;

    public static List<String> walletsList = new ArrayList<>();
    public static boolean isWalletListUpdated = false;


    /**
     * REQUESTS BEGIN
     */
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
            String walletJsonData = walletDataTextField.getText();
            walletClient.setWalletData(GsonBuilders.jsonToWalletData(walletJsonData));
            String pk = walletClient.getWalletData().getPublicKey();
            String walletAddress = walletClient.getWalletData().getWalletAddress();
            walletClient.sendMessage("NW" + pk + "HASH:" + walletAddress);
        } catch (WalletException e) {
            ControllerAlert.showAlert(Alert.AlertType.ERROR, "ERROR", e.getMessage());
        }
    }

    @FXML
    public void getWalletBalance() {
        try {
            startOwnedCoinsUpdateThread();
            walletClient.sendMessage("WD" + walletClient.getWalletData().getWalletAddress());
        } catch (WalletException e) {
            ControllerAlert.showAlert(Alert.AlertType.ERROR, "ERROR", e.getMessage());
        }
    }

    @FXML
    public void performTransaction() {
        try {
            double userCoins = Double.parseDouble(ownedCoinsText.getText());
            double transactionCoins = Double.parseDouble(coinsToSendTextField.getText());

            String recipientAddress = recipientWalletAddressTextField.getText();

            if (walletClient.getWalletData().getWalletAddress().equals(recipientAddress)) {
                ControllerAlert.showAlert(Alert.AlertType.WARNING,
                        "WARNING",
                        "YOU CAN NOT SEND ANY COINS TO YOURSELF MORON");
            } else {
                if (userCoins > transactionCoins) {
                    String fullMsg = generateTransactionMessage(transactionCoins, recipientAddress);
                    walletClient.sendMessage(fullMsg);
                } else {
                    ControllerAlert.showAlert(Alert.AlertType.WARNING, "WARNING", "not enough coins");
                }
            }
        } catch (Exception | WalletException e) {
            e.printStackTrace();
            ControllerAlert.showAlert(Alert.AlertType.ERROR, "ERROR", e.getMessage());
        }
    }

    @FXML
    public void walletsRequest() {
        try {
            startWalletsRequestThread();
            walletClient.sendMessage("WS");
        } catch (WalletException e) {
            e.printStackTrace();
            ControllerAlert.showAlert(Alert.AlertType.ERROR, "ERROR", e.getMessage());
        }
    }
    /**
     * REQUESTS END
     */


    /**
     * VIEW HANDLERS BEGIN
     */
    @FXML
    void closeApp() {
        walletClient.closeClient();
        System.exit(0);
    }

    @FXML
    public void handleRecipientClick() {
        String pickedAddress = walletsListView.getSelectionModel().getSelectedItem();
        recipientWalletAddressTextField.setText(pickedAddress);
    }

    /**
     * VIEW UPDATES BEGIN
     */
    private void updateHostInfoListView(List<HostInfo> hostInfoList) {
        hostInfoListView.getItems().clear();
        hostInfoListView.getItems().addAll(hostInfoList);
        hostInfoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        switchIsHostInfoUpdated();
    }

    private void updateWalletsListView(List<String> walletsList) {
        walletsListView.getItems().clear();
        walletsListView.getItems().addAll(walletsList);
        walletsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    private void updateOwnedCoins(double coins) {
        ownedCoinsText.setText(String.valueOf(coins));
        switchIsBalanceUpdated();
    }
    /**
     * VIEW UPDATES END
     */

    /**
     * VIEW HANDLERS END
     */

    private String generateTransactionMessage(double transactionCoins, String recipientAddress) {

        String msg = "TS" + walletClient.getWalletData().getWalletAddress()
                + "|" + recipientAddress
                + "|" + transactionCoins
                + "|" + System.currentTimeMillis()
                + "|";

        return msg + SignatureApplier
                .applySignature(walletClient.getWalletData().getPrivateKey(), msg);
    }


    private void setCoreParams() throws WalletException {
        if (walletClient == null) {
            walletClient = new WalletClient(hostInfo);
        } else {
            walletClient.setCoreConnectionParams(hostInfo);
        }
    }

    private void initHostInfo() {
        try {
            int port = Integer.parseInt(portNumberField.getText());
            String host = hostTextField.getText();
            hostInfo = new HostInfo(host, port);
        } catch (Exception e) {
            ControllerAlert.showAlert(Alert.AlertType.ERROR, "ERROR", "WRONG PORT/ADDRESS");
        }
    }


    /**
     * THREADS STARTERS BEGIN
     * Threads are used for asynchronous server responses and update view
     */
    @SneakyThrows
    private void startViewHostInfoListThread() {
        new Thread(() -> {
            while (true) {
                if (isHostInfoUpdated) {
                    Platform.runLater(() -> updateHostInfoListView(hostInfoHolder));
                    break;
                }
                Thread.sleep(100);
            }
        }).start();
    }

    @SneakyThrows
    private void startOwnedCoinsUpdateThread() {
        new Thread(() -> {
            while (true) {
                if (isBalanceUpdated) {
                    Platform.runLater(() -> updateOwnedCoins(coinsBalance));
                    break;
                }
                Thread.sleep(100);
            }
        }).start();
    }

    @SneakyThrows
    private void startWalletsRequestThread() {
        new Thread(() -> {
            while (true) {
                if (isWalletListUpdated) {
                    Platform.runLater(() -> updateWalletsListView(walletsList));
                    break;
                }
                Thread.sleep(100);
            }
        }).start();
    }

    /**
     * THREADS STARTERS END
     */

    private void buttonAccess(boolean toggle) {
        notifyWalletButton.setDisable(!toggle);
        balanceButton.setDisable(!toggle);
        sendCoinsButton.setDisable(!toggle);
        requestWalletsButton.setDisable(!toggle);
    }

    private void switchIsHostInfoUpdated() {
        isHostInfoUpdated = !isHostInfoUpdated;
    }

    private void switchIsBalanceUpdated() {
        isBalanceUpdated = !isBalanceUpdated;
    }

    private void switchIsWalletListUpdated() {
        isWalletListUpdated = !isWalletListUpdated;
    }
}
