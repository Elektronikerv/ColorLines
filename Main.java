
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    private SimpleIntegerProperty scoreProperty = new SimpleIntegerProperty(0);
    @Override
    public void start(Stage stage) {

        Game game = new Game();
        initStage(game, stage, game.getSize());
        stage.setOnCloseRequest(event -> Platform.exit());


        Thread checkingThread = new Thread(()-> {
            final boolean[] b = {false};
            while(!b[0]) {
                game.checkHorizontal();
                game.checkVertical();

                Platform.runLater(() -> {
                    scoreProperty.set(game.getScore());
                    b[0] = game.isLosed();
                    if (b[0])
                        showLoseMessage();
                });

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        checkingThread.start();
    }

    private void initStage(Game game, Stage stage, int size) {
        GridPane grid = game.initMap();

        Label title = new Label("Score: ");
        Label scoreLabel = new Label("0000");

        Font font = new Font("Times New Roman", 20);
        title.setFont(font);
        scoreLabel.setFont(font);
        title.setLabelFor(scoreLabel);
        scoreLabel.textProperty().bind(scoreProperty.asString());

        HBox hBox = new HBox();
        hBox.getChildren().addAll(title, scoreLabel);
        hBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(hBox, grid);

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.setTitle("Color Lines");
        stage.setResizable(false);
        stage.show();
    }

    private void showLoseMessage() {
        Alert loseMessage = new Alert(Alert.AlertType.INFORMATION);
        loseMessage.setTitle("You lose!");
        loseMessage.setHeaderText("You lose! Your score: " + scoreProperty.get());
        loseMessage.showAndWait();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
