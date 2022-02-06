package gui;

import civ.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
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
        Scene scene = new Scene(mainVBox, 1600, 1000, new Color(0.5, 0.1, 0.1, 1));

        HBox canvasHBox = new HBox();
        this.canvas = new GridCanvas(1600, 800);
        canvasHBox.getChildren().add(this.canvas);
        scene.setOnKeyPressed(keyEvent -> this.canvas.requestFocus());


        mainVBox.getChildren().add(canvasHBox);

        HBox optionsBox = new HBox();
        mainVBox.getChildren().add(optionsBox);

        VBox opt1 = new VBox();
        VBox opt2 = new VBox();
        VBox opt3 = new VBox();
        optionsBox.getChildren().addAll(opt1, opt2, opt3);

        MenuButton actionButton = new MenuButton("Execute Action");
        actionButton.setOnAction(actionEvent -> {
            MapPosition position = this.canvas.map.getSelectedPosition();
            Unit unit = this.canvas.map.getField(position).getUnit();
            if (unit != null) {
                unit.action();
            }
            this.canvas.render();
        });
        opt1.getChildren().add(actionButton);

        MenuButton newGameButton = new MenuButton("New Game");
        newGameButton.setOnAction(actionEvent -> {
            this.canvas.map = MapGenerator.generateRandomMap(30, 30);
            this.canvas.render();
            this.canvas.ctx.drawImage(new Image("file:.\\src\\main\\resources\\instructions.png"), 0, 0);
        });

        opt2.getChildren().add(newGameButton);

        MenuButton endTurnButton = new MenuButton("End Turn");
        endTurnButton.setOnAction(actionEvent -> {
            Player p = this.canvas.map.nextTurn();
            System.out.println("Player " + p.getColor() + "'s turn");
            this.canvas.render();
            endTurnButton.setStyle(String.format("-fx-background-color: %s;", p.getColor()));
        });
        endTurnButton.setStyle(String.format("-fx-background-color: %s;", this.canvas.map.playerAtTurn.getColor()));
        opt3.getChildren().add(endTurnButton);

        this.canvas.init();
        newGameButton.getOnAction().handle(new ActionEvent());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
