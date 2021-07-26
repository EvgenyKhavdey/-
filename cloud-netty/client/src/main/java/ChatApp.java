import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("windowAuthorization.fxml"));
        Parent parent = loader.load();
        primaryStage.setScene(new Scene(parent));
        primaryStage.setOnCloseRequest(event -> {
            ((ChatController) loader.getController()).closeConnection();
            primaryStage.close();
        });
        primaryStage.show();
    }
}
