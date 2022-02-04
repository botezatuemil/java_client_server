import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

       FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
       Parent root = (Parent) fxmlLoader.load();
       Scene scene = new Scene(root);
       stage.setScene(scene);
       stage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
