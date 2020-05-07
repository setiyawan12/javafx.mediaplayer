/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafx_mediaplayer;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;

/**
 *
 * @author Asus
 */
public class FXMLDocumentController implements Initializable {

    private MediaPlayer mediaPlayer;

    @FXML
    private MediaView mediaView;

    @FXML
    private StackPane sPane;

    @FXML
    private Button playPause;

    @FXML
    private Slider volume;

    @FXML
    Slider seek;

    @FXML
    BorderPane bPane;

    List<String> playlist = new ArrayList<>();
    List<String> sourceName = new ArrayList<>();
    static int INDEX, PLAY = 0;

    @FXML
    private void OpenFiles(ActionEvent event) {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(""
                + "Media File", "*.mp3", "*.mp4");
        fc.getExtensionFilters().add(filter);
        List<File> f = fc.showOpenMultipleDialog(null);
        if (!f.isEmpty()) {
            if (!playlist.isEmpty()) {
                playlist.clear();
                sourceName.clear();
            }
            for (int i = 0; i < f.size(); i++) {
                playlist.add(f.get(i).toURI().toString());
                sourceName.add(f.get(i).getName());
            }
            INDEX = 0;
            playMedia(INDEX);
        }
    }

    private void playMedia(int index) {
        String source = playlist.get(INDEX);
        Media media = new Media(source);
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.autosize();

        DoubleProperty width = mediaView.fitWidthProperty();
        DoubleProperty height = mediaView.fitHeightProperty();
        width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "widht"));
        height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
        mediaView.setPreserveRatio(true);

        volume.setValue(50);
        volume.valueProperty().addListener((Observable observable) -> {
            mediaPlayer.setVolume(volume.getValue() / 100);
        });
        mediaPlayer.currentTimeProperty().addListener((ObservableValue<? extends Duration> Observable, Duration oldValue, Duration newValue) -> {
            seek.setValue(newValue.toSeconds());
        });
        seek.setOnMouseClicked((MouseEvent event1) -> {
            mediaPlayer.seek(Duration.seconds(seek.getValue()));
        });
        seek.setOnMouseDragExited((MouseEvent event1) -> {
            mediaPlayer.seek(Duration.seconds(seek.getValue()));
        });

        mediaPlayer.play();
        PLAY = 1;
        Image imagePause = new Image(getClass().getResourceAsStream("/images/00.png"));
    }

    @FXML
    private void seekBackward(ActionEvent event) {
        mediaPlayer.setRate(0.5);
    }

    @FXML
    private void backward(ActionEvent event) {
        if (INDEX > 0) {
            INDEX--;
            playMedia(INDEX);
        }
    }

    @FXML
    public void stop(ActionEvent event) {
        mediaPlayer.stop();
        Image imagePlay = new Image(getClass().getResourceAsStream("images/5.png"));
        playPause.setGraphic(new ImageView(imagePlay));
        PLAY = 0;
    }

    @FXML
    public void pausePlay(ActionEvent event) {
        if (!playlist.isEmpty()) {
            if (PLAY == 1) {
                mediaPlayer.pause();
                Image imagePlay = new Image(getClass().getResourceAsStream("images/5.png"));
                playPause.setGraphic(new ImageView(imagePlay));
                PLAY = 0;
            } else {
                mediaPlayer.play();
                Image imagePlay = new Image(getClass().getResourceAsStream("images/00.png"));
                playPause.setGraphic(new ImageView(imagePlay));
                PLAY = 1;
            }
        } else {
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Message");
            dialog.setContentText("Plis open media!");
            dialog.setOnCloseRequest((DialogEvent event1) -> {
                dialog.close();
            });
            dialog.initStyle(StageStyle.DECORATED);
            dialog.initModality(Modality.NONE);
            dialog.show();
        }
    }

    @FXML
    public void forward(ActionEvent event) {
        if (INDEX >= 0 && INDEX < playlist.size()) {
            INDEX++;
            playMedia(INDEX);
        }
    }
    @FXML
    public void seekForward(ActionEvent event) {
        mediaPlayer.setRate(1.5);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
}
