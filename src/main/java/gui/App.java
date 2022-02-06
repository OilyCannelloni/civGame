package gui;

import civ.Player;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {
    private GridCanvas canvas;

    @Override
    public void start(Stage primaryStage) {
        System.out.println("start");

        VBox mainVBox = new VBox();
        Scene scene = new Scene(mainVBox, 1500, 1200, new Color(0.5, 0.1, 0.1, 1));

        HBox canvasHBox = new HBox();
        this.canvas = new GridCanvas(1500, 1000);
        canvasHBox.getChildren().add(this.canvas);

        mainVBox.getChildren().add(canvasHBox);

        HBox optionsBox = new HBox();
        mainVBox.getChildren().add(optionsBox);

        VBox opt1 = new VBox();
        VBox opt2 = new VBox();
        VBox opt3 = new VBox();
        optionsBox.getChildren().addAll(opt1, opt2, opt3);

        MenuButton actionButton = new MenuButton("Execute Action");
        opt1.getChildren().add(actionButton);

        MenuButton newGameButton = new MenuButton("New Game");
        opt2.getChildren().add(newGameButton);

        MenuButton endTurnButton = new MenuButton("End Turn");
        endTurnButton.setOnAction(actionEvent -> {
            Player p = this.canvas.map.nextTurn();
            System.out.println("Player " + p.getColor() + "'s turn");
            this.canvas.render();
        });
        opt3.getChildren().add(endTurnButton);

        this.canvas.init();
        this.canvas.render();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
