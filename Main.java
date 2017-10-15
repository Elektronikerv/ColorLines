package sample;

import com.sun.javafx.tk.Toolkit;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    final static int PADDING = 1;


    @Override
    public void start(Stage stage) throws InterruptedException {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(PADDING));
        grid.setHgap(PADDING);
        grid.setVgap(PADDING);

        grid = Controller.initMap();
        Label title = new Label("Score: ");
        Label l = new Label();
        title.setLabelFor(l);
        SimpleIntegerProperty i = new SimpleIntegerProperty(0);
        l.textProperty().bind(i.asString());
        grid.add(title, 9, 0);
        grid.add(l, 10, 0);

        Scene scene = new Scene(grid, 600, 600);
        stage.setScene(scene);
        stage.setTitle("Color Lines");
        stage.setResizable(false);
        stage.show();

        Timeline t = new Timeline((new KeyFrame(Duration.seconds(0.1),
                (event) -> {
                        i.set(Controller.getScore());
                    }

        )));
        t.setCycleCount(Animation.INDEFINITE);
        t.play();


        Thread checkThread = new Thread(()-> {
            while(true) {
                Controller.checkHorizontal();
                Controller.checkVertical();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        stage.setOnCloseRequest(event ->
                        System.exit(0)
        );

        checkThread.start();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
