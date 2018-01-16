import ConnectionManagment.DeviceFinder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.MalformedURLException;

public class Main extends Application{
    //just starts the aplication
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent rootas = FXMLLoader.load(getClass().getResource("test.fxml"));
        Controller controller =  new Controller();
        primaryStage.setTitle("Smart Socket Configurator");
        Scene root = new Scene(rootas);
        primaryStage.setScene(root);
        primaryStage.show();
        controller.start();
    }
}
