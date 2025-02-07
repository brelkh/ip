package mike;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI to initialise Mike with FXML.
 */
public class Main extends Application {


    private Mike mike = new Mike();

    /**
     * Sets up the JavaFX stage.
     *
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("MIKE");
            fxmlLoader.<MainWindow>getController().setMike(mike);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
