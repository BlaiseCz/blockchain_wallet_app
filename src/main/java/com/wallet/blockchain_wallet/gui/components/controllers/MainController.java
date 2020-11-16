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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final WalletClient walletClient;

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

    /**
     * REQUESTS BEGIN
     */
    @FXML
    void nodesRequest() {
        Optional<HostInfo> hostInfo = getHostInfoFromAppContext();
        hostInfo.ifPresent(this::joinToWeb);
    }

    private void joinToWeb(HostInfo hostInfo) {
        try {
            walletClient.joinToWeb(hostInfo);
            walletClient.sendMessage("NS");
            enableButtons();
        } catch (Exception e) {
            ControllerAlert.showAlert(Alert.AlertType.ERROR, "ERROR", "NODES REQUEST ERROR");
        } catch (WalletException we) {
            ControllerAlert.showAlert(Alert.AlertType.WARNING, "WARNING", we.getMessage());
        }
    }

    @FXML
    void notifyWallet() {
        try {
            String walletJsonData = walletDataTextField.getText();
            walletClient.setWalletData(GsonBuilders.jsonToWalletData(walletJsonData));
            String pk = walletClient.getWalletData()
                                    .getPublicKey();
            String walletAddress = walletClient.getWalletData()
                                               .getWalletAddress();
            walletClient.sendMessage("NW" + pk + "HASH:" + walletAddress);
        } catch (WalletException e) {
            ControllerAlert.showAlert(Alert.AlertType.ERROR, "ERROR", e.getMessage());
        }
    }

    @FXML
    public void getWalletBalance() {
        try {
            walletClient.sendMessage("WD" + walletClient.getWalletData()
                                                        .getWalletAddress());
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

            if (walletClient.getWalletData()
                            .getWalletAddress()
                            .equals(recipientAddress)) {
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
            walletClient.sendMessage("WS");
        } catch (WalletException e) {
            e.printStackTrace();
            ControllerAlert.showAlert(Alert.AlertType.ERROR, "ERROR", e.getMessage());
        }
    }

    @FXML
    void initWallet() {
        String walletJsonData = walletDataTextField.getText();
        walletClient.setWalletData(GsonBuilders.jsonToWalletData(walletJsonData));
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
        String pickedAddress = walletsListView.getSelectionModel()
                                              .getSelectedItem();
        recipientWalletAddressTextField.setText(pickedAddress);
    }

    /**
     * VIEW UPDATES BEGIN
     */
    private void updateHostInfoListView(List<HostInfo> hostInfoList) {
        hostInfoListView.getItems()
                        .clear();
        hostInfoListView.getItems()
                        .addAll(hostInfoList);
        hostInfoListView.getSelectionModel()
                        .setSelectionMode(SelectionMode.SINGLE);
    }

    private void updateWalletsListView(List<String> walletsList) {
        walletsListView.getItems()
                       .clear();
        walletsListView.getItems()
                       .addAll(walletsList);
        walletsListView.getSelectionModel()
                       .setSelectionMode(SelectionMode.SINGLE);

    }

    private void updateOwnedCoins(double coins) {
        ownedCoinsText.setText(String.valueOf(coins));
    }

    private String generateTransactionMessage(double transactionCoins, String recipientAddress) {

        String msg = "TS" + walletClient.getWalletData()
                                        .getWalletAddress()
                + "|" + recipientAddress
                + "|" + transactionCoins
                + "|" + System.currentTimeMillis()
                + "|";

        return msg + SignatureApplier
                .applySignature(walletClient.getWalletData()
                                            .getPrivateKey(), msg);
    }

    private Optional<HostInfo> getHostInfoFromAppContext() {
        try {
            int port = Integer.parseInt(portNumberField.getText());
            String host = hostTextField.getText();
            return Optional.of(new HostInfo(host, port));
        } catch (Exception e) {
            ControllerAlert.showAlert(Alert.AlertType.ERROR, "ERROR", "WRONG PORT/ADDRESS");
        }
        return Optional.empty();
    }

    private void enableButtons() {
        notifyWalletButton.setDisable(false);
        balanceButton.setDisable(false);
        sendCoinsButton.setDisable(false);
        requestWalletsButton.setDisable(false);
    }


    public void updateHostInfo(List<HostInfo> hostInfoListFromData) {
        Platform.runLater(() -> updateHostInfoListView(hostInfoListFromData));
    }

    public void updateCoins(double receiveOwnedCoins) {
        Platform.runLater(() -> updateOwnedCoins(receiveOwnedCoins));
    }

    public void updateWallets(List<String> walletsResponseList) {
        Platform.runLater(() -> updateWalletsListView(walletsResponseList));
    }
}
