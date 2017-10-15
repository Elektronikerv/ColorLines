package sample;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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


        Thread checkThread = new Thread(()-> {
            final boolean[] b = {false};
            while(!b[0]) {
                Controller.checkHorizontal();
                Controller.checkVertical();

                Platform.runLater(() -> {
                    i.set(Controller.getScore());
                    b[0] = Controller.isLosed();
                    if(b[0]) {
                        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                        a.show();
                    }
                });

                try {
                    Thread.sleep(300);
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
