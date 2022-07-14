package MusicPlayer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Controller<label> implements Initializable{
//FXML Definitionen
    @FXML
    private Pane pane;
    @FXML
    private Label songLabel;
    @FXML
    private Label songLabel2;
    @FXML
    private Label playNow;
    @FXML
    private Button playButton, pauseButton, resetButton, previousButton, nextButton, songButton;
    @FXML
    private ComboBox<String> speedBox;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ProgressBar songProgressBar;

    @FXML
    private Button switchToScene1, switchToScene2;

    @FXML private ListView<String> myListView;

    @FXML
    private label myLabel;

    String currentSong;

    private Media media;
    private MediaPlayer mediaPlayer;

    private File directory;
    private File[] files;

    private ArrayList<File> songs;

    private int songNumber;
    private int[] speeds = {25, 50, 75, 100, 125, 150, 175, 200};

    private Timer timer;
    private TimerTask task;

    private boolean running;

    private Stage stage;
    private Scene scene;
    private Parent root;

    List<String> playlist = new ArrayList<>();


    //Initialisierung
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        songs = new ArrayList<File>();

        //Musicfiles verlinken
        directory = new File(getClass().getResource("/music").getFile());

        files = directory.listFiles();


        if(files != null) {

            for(File file : files) {

                songs.add(file);
            }
        }

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        //aktualisierter Text anzeigen
        songLabel.setText(songs.get(songNumber).getName());

        if (songLabel2 != null){
        songLabel2.setText("Eminem_TheWayIAm.mp3");}

        for(int i = 0; i < speeds.length; i++) {

            speedBox.getItems().add(Integer.toString(speeds[i])+"%");
        }

        speedBox.setOnAction(this::changeSpeed);

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {

                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });

        songProgressBar.setStyle("-fx-accent: #00FF00;");
    }

    //Songs abspielen
    public void playMedia() {

        beginTimer();
        changeSpeed(null);
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        mediaPlayer.play();

        nowPlaying();
    }

    //Songs pausieren
    public void pauseMedia() {

        cancelTimer();
        mediaPlayer.pause();

        playNow.setVisible(false);
    }

    //Song zurücksetzen auf den Start
    public void resetMedia() {

        songProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
    }

    //Vorher abgspielter Song abspielen
    public void previousMedia() {

        if(songNumber > 0) {

            songNumber--;

            mediaPlayer.stop();

            if(running) {

                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(songs.get(songNumber).getName());

            playMedia();
        }
        else {

            songNumber = songs.size() - 1;

            mediaPlayer.stop();

            if(running) {

                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(songs.get(songNumber).getName());

            playMedia();
        }
    }

    //nächster Song in der Liste abspielen
    public void nextMedia() {

        if(songNumber < songs.size() - 1) {

            songNumber++;

            mediaPlayer.stop();

            if(running) {

                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(songs.get(songNumber).getName());

            playMedia();
        }
        else {

            songNumber = 0;

            mediaPlayer.stop();

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(songs.get(songNumber).getName());

            playMedia();
        }
    }

    //Geschwindigkeit des laufenden Songs verändern
    public void changeSpeed(ActionEvent event) {

        if(speedBox.getValue() == null) {

            mediaPlayer.setRate(1);
        }
        else {

            //mediaPlayer.setRate(Integer.parseInt(speedBox.getValue()) * 0.01);
            mediaPlayer.setRate(Integer.parseInt(speedBox.getValue().substring(0, speedBox.getValue().length() - 1)) * 0.01);
        }
    }

    public void beginTimer() {

        timer = new Timer();

        task = new TimerTask() {

            public void run() {

                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                songProgressBar.setProgress(current/end);

                if(current/end == 1) {

                    cancelTimer();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public void cancelTimer() {

        running = false;
        timer.cancel();
    }

    //von 2. Seite auf die Startseite wechseln
    public void switchToScene1(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(Main.class.getResource("design_neu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        scene.getStylesheets().add(getClass().getResource("/css/css_musicplayer.css").toExternalForm());


    }

    //von der 1. Seite auf die zweite Seite wechseln
    public void switchToScene2(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("design_neu5.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        scene.getStylesheets().add(getClass().getResource("/css/css_musicplayer.css").toExternalForm());
    }

    public void nowPlaying(){

        playNow.setVisible(true);
    }
}
