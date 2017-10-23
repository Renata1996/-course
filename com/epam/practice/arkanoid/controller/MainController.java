package com.epam.practice.arkanoid.controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.*;
import javafx.scene.control.Button;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimerTask;


public class MainController {

    @FXML
    private TextField speed;
    @FXML
    private TextField score;
    @FXML
    private TextField time;
    @FXML
    private TextField count;
    @FXML
    private Pane board;
    @FXML
    Button startButton;
    @FXML
    Button pauseButton;
    @FXML
    Button stopButton;

    private Utils game;
    private Boolean stop;
    private Boolean pause;
    private int playerScore;
    private int countLife;
    private boolean die;

    {
        die = false;
        stop = false;
        pause = false;
        playerScore = 0;
        countLife = 3;
    }

    @FXML
    public void initialize() {
        game = new Utils(board);
        time.setDisable(true);
        score.setDisable(true);
        stopButton.setDisable(true);
        pauseButton.setDisable(true);
    }

    private void makeTime() {
        Date date = new Date();
        new AnimationTimer() {
            public void handle(long currentTime) {
                {
                    if (game.isStart()) {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
                        time.setText("Time: " + simpleDateFormat.format(calendar.getTime().getTime() - date.getTime()));
                        playerScore = (int) (calendar.getTime().getTime() - date.getTime()) / 1000;
                        playerScore *= game.getBallSpeed();
                        score.setText("Score: " + playerScore);
                    }


                }
            }
        }.start();
    }

    @FXML
    private void handleStopButtonClick(MouseEvent event) {

        game.setStart(false);
        stop = true;

    }

    @FXML
    private void handlePauseButtonClick(MouseEvent event) {
        if (!pause) {
            pauseButton.setText("Resume");
            game.setStart(false);
            pause = true;
        } else {
            pause = false;
            pauseButton.setText("Pause");
            game.setStart(true);

        }
    }

    @FXML
    private void handleStartButtonClick(MouseEvent event) {
        game.run();
        makeTime();
        game.makeBall();
        stopButton.setDisable(false);
        pauseButton.setDisable(false);
        stop = false;
        try {
            Integer i = Integer.parseInt(speed.getText());
            if (i > 10 || i < 0)
                throw new RuntimeException();
            game.setBallSpeed(i);
            Integer j = Integer.parseInt(count.getText());
            if (j > 5 || j < 0)
                throw new RuntimeException();
            countLife = j;


        } catch (RuntimeException e) {
            speed.setText("3");
            game.setBallSpeed(3);
            countLife = 3;
            count.setText("3");
        }
        game.setStart(true);
        checkProperties();
    }

    private void checkProperties() {
        new Thread(() -> {
            if (!die) {
                if (game.isStart()) {
                    startButton.setDisable(true);
                    speed.setDisable(true);
                    score.setDisable(true);
                    count.setDisable(true);
                } else {
                    startButton.setDisable(false);
                    speed.setDisable(false);
                    score.setDisable(false);
                    count.setDisable(false);
                }
                if (stop) {
                    stopButton.setDisable(true);
                    pauseButton.setDisable(true);
                } else {
                    stopButton.setDisable(false);
                    pauseButton.setDisable(false);
                }

            } else {
                startButton.setDisable(true);
                stopButton.setDisable(true);
                pauseButton.setDisable(true);
            }
            if (game.isBallAlive()) {
                countLife--;
                if (countLife < 0) {
                    die = true;
                    stop = true;
                    countLife = -1;
                    board.setStyle("-fx-background-color: pink;");
                    game.setStart(false);
                } else {
                    count.setText(String.valueOf(countLife));
                    startButton.setDisable(false);
                }
            }
        }).start();
    }

}
