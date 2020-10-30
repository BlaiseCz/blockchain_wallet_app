package com.wallet.blockchain_wallet.gui.components;

import com.wallet.blockchain_wallet.client.wallet.HostInfo;
import com.wallet.blockchain_wallet.client.wallet.WalletClient;
import com.wallet.blockchain_wallet.client.wallet.WalletException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {


    private WalletClient walletClient;
    private HostInfo hostInfo;

    @FXML
    private NumberTextField portTextField;

    @FXML
    private TextField hostTextField;

    @FXML
    private Button nodeRequestButton;

    @FXML
    private Button notifyWalletButton;

    @FXML
    private Button transactionButton;

    @FXML
    private Button ballanceButton;

    @FXML
    private Text coinsBalanceText;

    @FXML
    void closeApp() {
        System.exit(0);
    }

    @FXML
    private ListView<HostInfo> hostInfoListView;


    @FXML
    void nodesRequest() throws WalletException {
        try {
            int port = Integer.parseInt(portTextField.getText());
            String host = hostTextField.getText();
            hostInfo = new HostInfo(host, port);
        } catch (Exception e) {
            ControllerAllert.showAlert(Alert.AlertType.ERROR, "ERROR", "WRONG PORT/IP ADDRESS");
        }

        if(hostInfo != null) {
            walletClient = new WalletClient(hostInfo);
//        walletClient.sendMessage("NS");
            List<HostInfo> hostInfoList = new ArrayList<>();
            hostInfoList.add(new HostInfo("123", 1));
            hostInfoList.add(new HostInfo("567", 2));

            viewHostInfoList(hostInfoList);
        }
    }

    @FXML
    void notifyWallet() {
        try {
            walletClient = new WalletClient(hostInfo);
            walletClient.sendMessage("NWas89nhf293823jf2bh827fg2ebhHASH:f893fu7293h5i3v532987djh");
        } catch (WalletException e){
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
        System.out.println("get ballance");
        coinsBalanceText.setText("124");
    }

    @FXML
    public void performTransaction() {
        System.out.println("perform transaction");
    }
}
