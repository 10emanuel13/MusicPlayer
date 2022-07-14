package MusicPlayer;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        //Image image = new Image("caitfy.jpg");
        // ImageView imgView = new ImageView();
        // imgView.setImage(image);


        Parent root = FXMLLoader.load(Main.class.getResource("design_neu.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/css_musicplayer.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent arg0) {

                Platform.exit();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}