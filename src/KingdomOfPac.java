import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class KingdomOfPac extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/resources/fxmlfiles/menuscreens/WelcomeScreen.fxml"));

        primaryStage.setTitle("Kingdom Of Pac");

        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}
