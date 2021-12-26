package gui;

import civ.Vector2D;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {
    private GridCanvas canvas;

    @Override
    public void start(Stage primaryStage) {
        System.out.println("start");

        VBox canvasVBox = new VBox();
        Scene scene = new Scene(canvasVBox, 1600, 1000, new Color(0.1, 0.1, 0.1, 1));

        this.canvas = new GridCanvas(1600, 1000);

        canvasVBox.getChildren().add(this.canvas);

        this.canvas.init();
        this.canvas.render(new Vector2D(5, 30));

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
