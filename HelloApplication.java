package com.example.connect4;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private ModuleLayer.Controller controller;
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane rootgridpane=loader.load();

        controller=loader.getController();
        controller.createPlayGround();

        MenuBar menuBar=createmenu();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        Pane menuPane=(Pane) rootgridpane.getChildren().get(0);
        menuPane.getChildren().add(menuBar);

        Scene scene=new Scene(rootgridpane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("connectfour");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private MenuBar createmenu(){
        Menu fileMenu =new Menu("File");
        MenuItem newGame=new MenuItem("New Game");
       newGame.setOnAction(actionEvent -> resetGame1());


        MenuItem resetGame=new MenuItem("Reset Game");
        resetGame.setOnAction(actionEvent -> resetGame1());

        SeparatorMenuItem sep=new SeparatorMenuItem();
        MenuItem exit=new MenuItem("Exit Game");
        exit.setOnAction(event -> exitGame());
        fileMenu.getItems().addAll(newGame,resetGame,sep,exit);

        Menu helpMenu=new Menu("help");

        MenuItem aboutGame=new MenuItem("about connect4");
        aboutGame.setOnAction(event -> aboutConnect4());


        SeparatorMenuItem sep2=new SeparatorMenuItem();
        MenuItem aboutme=new MenuItem("about me");
        aboutme.setOnAction(event -> aboutme());

        helpMenu.getItems().addAll(aboutGame,sep2,aboutme);

        MenuBar menubar=new MenuBar();
        menubar.getMenus().addAll(fileMenu,helpMenu);  //add file menu to menu bar

        return menubar;
    }

    private void aboutme() {
        Alert alert =new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Developer");
        alert.setHeaderText("Tulasi Ram");
        alert.setContentText("I love games");
        alert.show();
    }

    private void aboutConnect4() {
        Alert alert =new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About connect four");
        alert.setHeaderText("how to play");
        alert.setContentText("CConnect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. Connect Four is a solved game. The first player can always win by playing the right moves.");
        alert.show();
    }

    private void exitGame() {
        Platfom.exit();
        System.exit(0);
    }

    private void resetGame1() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}