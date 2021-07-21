import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.ListResponse;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthorizationController {

    private NettyNetwork network;
    @FXML
    private TextField login_field;
    @FXML
    private PasswordField password_field;
    @FXML
    private Button authorizationButton;

    public void initialize() {
        authorizationButton.setOnAction(event -> {
            String loginText = login_field.getText().trim();
            String passwordText = password_field.getText().trim();

            if(!loginText.equals("") && !passwordText.equals("")) {
                network.writeMessage(new User(loginText, passwordText));

                authorizationButton.getScene().getWindow().hide();

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("chat.fxml"));

                try {
                    loader.load();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.showAndWait();
            }
            else
                System.out.println("Login and password is empty");

        });

//        authorizationButton.setOnAction(event -> {
//            authorizationButton.getScene().getWindow().hide();
//
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("chat.fxml"));
//
//            try {
//                loader.load();
//            } catch (IOException ioException) {
//                ioException.printStackTrace();
//            }
//            Parent root = loader.getRoot();
//            Stage stage = new Stage();
//            stage.setScene(new Scene(root));
//            stage.showAndWait();
//
            network = new NettyNetwork(command -> {
                switch (command.getType()) {
                    case LIST_RESPONSE:
                        ListResponse files = (ListResponse) command;
                        Platform.runLater(() -> {

                        });

                }

            });
//        });

    }
}

