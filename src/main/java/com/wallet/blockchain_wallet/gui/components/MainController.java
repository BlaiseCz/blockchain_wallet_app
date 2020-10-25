package com.wallet.blockchain_wallet.gui.components;

import com.wallet.blockchain_wallet.client.wallet.HostInfo;
import com.wallet.blockchain_wallet.client.wallet.WalletClient;
import com.wallet.blockchain_wallet.client.wallet.WalletException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    void closeApp() {
        System.exit(0);
    }

    @FXML
    private ListView<HostInfo> hostInfoListView;

//    @FXML
//    private static Alert alert;


    @FXML
    void nodesRequest() throws WalletException {
        int port = Integer.parseInt(portTextField.getText());
        String host = hostTextField.getText();
        hostInfo = new HostInfo(host, port);

        walletClient = new WalletClient(hostInfo);
//        walletClient.sendMessage("NS");
        List<HostInfo> hostInfoList = new ArrayList<>();
        hostInfoList.add(new HostInfo("123", 1));
        hostInfoList.add(new HostInfo("567", 2));

        viewHostInfoList(hostInfoList);
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

}
