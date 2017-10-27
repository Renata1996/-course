package com.epam.practice.arkanoid.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;


public class Main extends Application {

    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 400;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Pane root = FXMLLoader.load(getClass().getResource("../../resourses/mainForm.fxml"));
        primaryStage.setTitle("Arkanoid");

        primaryStage.setScene(new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT));
        primaryStage.setResizable(false);
        primaryStage.show();

    }


}