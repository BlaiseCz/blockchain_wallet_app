package com.wallet.blockchain_wallet.gui.components;

import com.wallet.blockchain_wallet.client.wallet.HostInfo;
import com.wallet.blockchain_wallet.client.wallet.WalletClient;
import com.wallet.blockchain_wallet.client.wallet.WalletException;
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
    private NumberTextField portTextField;

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
    private Button notifyWalletButton;

    @FXML
    private Button ballanceButton;

    @FXML
    private Button sendCoinsButton;

    @FXML
    void closeApp() {
        walletClient.closeClient();
        System.exit(0);
    }

    @FXML
    void nodesRequest() {
        try {
            int port = Integer.parseInt(portTextField.getText());
            String host = hostTextField.getText();
            hostInfo = new HostInfo(host, port);
        } catch (Exception e) {
            ControllerAllert.showAlert(Alert.AlertType.ERROR, "ERROR", "WRONG PORT/ADDRESS");
        }

        if (hostInfo != null) {
            try {
                walletClient = new WalletClient(hostInfo);
                walletClient.sendMessage("NS");

//                List<HostInfo> hostInfoList = new ArrayList<>();
//                hostInfoList.add(new HostInfo("12ASFASF312A", 49123));
//                hostInfoList.add(new HostInfo("56ASVASF7XAA", 51002));
//
//                viewHostInfoList(hostInfoList);
                buttonAccess(true);
            } catch (Exception e) {
                ControllerAllert.showAlert(Alert.AlertType.ERROR, "ERROR", "NODES REQUEST ERROR");
            } catch (WalletException we) {
                ControllerAllert.showAlert(Alert.AlertType.WARNING, "WARNING", we.getMessage());
            }
        }
    }

    @FXML
    void notifyWallet() {
        try {
            walletClient.sendMessage("NWas89nhf293823jf2bh827fg2ebhHASH:f893fu7293h5i3v532987djh");
        } catch (WalletException e) {
            ControllerAllert.showAlert(Alert.AlertType.ERROR, "ERROR", e.getMessage());
        }
    }

    @FXML
    public void viewHostInfoList(List<HostInfo> hostInfoList) {
        hostInfoListView.getItems().clear();
        hostInfoListView.getItems().addAll(hostInfoList);
        hostInfoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
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
                try {
                    log.info(hostInfo.getAddress() + ":" + hostInfo.getPort() + recipientInfo + " amount: " + transactionCoins);
                    walletClient.sendMessage("TSgjfd98g2h39ghn8e | fjh329f8h9e8yf290837fgyh |13.372137|128579683948| jfv78fdhv873b495bv7865987c632874");
                } catch (WalletException e) {
                    ControllerAllert.showAlert(Alert.AlertType.ERROR, "ERROR", e.getMessage());
                }
            } else {
                ControllerAllert.showAlert(Alert.AlertType.WARNING, "WARNING", "not enough coins");
            }
        } catch (Exception e) {
            ControllerAllert.showAlert(Alert.AlertType.ERROR, "ERROR", e.getMessage());
        }
    }

    @FXML
    public void handleRecipientClick() {
        HostInfo pickedValue = hostInfoListView.getSelectionModel().getSelectedItem();
        log.info("clicked on " + pickedValue.getAddress() + ":" + pickedValue.getPort());
        coinsRecipient.setText(pickedValue.getAddress() + ":" + pickedValue.getPort());
    }

    private void buttonAccess(boolean toggle) {
        notifyWalletButton.setDisable(!toggle);
        ballanceButton.setDisable(!toggle);
        sendCoinsButton.setDisable(!toggle);

    }
}
