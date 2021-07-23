import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
<<<<<<< HEAD:cloud/client/src/main/java/ChatApp.java
        Parent parent = FXMLLoader.load(getClass().getResource("windowAuthorization.fxml"));
=======
        Parent parent = FXMLLoader.load(getClass().getResource("chat.fxml"));
>>>>>>> 2b8b36c224fbea3262cae5e8b4836ef5604d94a7:cloud3/client/src/main/java/ChatApp.java
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
    }
}


//./client/clientFiles/1.jpg
//./client/clientFiles/2.txt