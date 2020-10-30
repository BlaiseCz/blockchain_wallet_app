package com.wallet.blockchain_wallet;

import com.wallet.blockchain_wallet.gui.components.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.net.URL;

@SpringBootApplication
public class BlockchainWalletApplication extends Application {

    private MainController component;
    private ConfigurableApplicationContext springContext;
    private FXMLLoader loader;

    @Getter
    private static boolean running = true;

    public static void main(String[] args) {
//        SpringApplication.run(BlockchainWalletApplication.class, args);
        launch(args);
//        WalletClient walletClient = new WalletClient(49762, "127.0.0.1");
//        walletClient.sendMessage("NWaddressHASH:hash");
//        walletClient.sendMessage("NS");
    }

    @Override
    public void init() {
        springContext = SpringApplication.run(BlockchainWalletApplication.class);
        loader = new FXMLLoader();
        loader.setControllerFactory(springContext::getBean);
    }

    @Override
    public void stop() {
        springContext.stop();
        running = false;
    }

    @Override
    public void start(Stage primaryStage) throws
            IOException {
        URL mainFxmlFile = ResourceUtils.getURL("src/main/resources/fxml_files/main/main.fxml");

        loader.setLocation(mainFxmlFile);
        Parent root = loader.load();

        component = loader.getController();

        primaryStage.setTitle("Blockchain Wallet");
        primaryStage.setScene(new Scene(root, 750, 400));
        primaryStage.show();

    }
}
