package com.epam.practice.arkanoid.controller;

import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


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
    private Button startButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button startGameButton;
    @FXML
    private Pane beforeStart;

    private Utils game;
    private Boolean stop;
    private Boolean pause;
    private int countLife;
    private boolean die;
    private boolean changedBalls;

    {
        changedBalls = false;
        die = false;
        stop = false;
        pause = false;
        countLife = 1;
    }

    @FXML
    public void initialize() {
        game = new Utils(board);
        time.setDisable(true);
        score.setDisable(true);
        setDisablePauseStopButtons(true);
    }

    @FXML
    private void handleStartGameButtonClick(MouseEvent event) {
        beforeStart.setVisible(false);
        startGameButton.setVisible(false);
        startGameButton.setDisable(false);
        makeStop();
        checkProperties();
    }


    @FXML
    private void handleStopButtonClick(MouseEvent event) {
        beforeStart.setVisible(true);
        startGameButton.setVisible(true);
    }

    public void makeStop() {
        setDisablePauseStopButtons(true);
        startButton.setDisable(false);
        time.setText("");
        score.setText("");
        game.makeNewGame();
        game.run();
        game.makeBall();
        makeTime();
        stop = false;
        die = false;
        countLife=3;
        count.setText("3");
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
        game.makeBall();
        makeTime();
        setDisablePauseStopButtons(false);
        stop = false;
        die = false;
        try {
            Integer i = Integer.parseInt(speed.getText());
            if (i > 5 || i < 0)
                throw new RuntimeException();
            game.setBallSpeed(i);
            i = Integer.parseInt(count.getText());
            if (i > 3 || i < 0)
                throw new RuntimeException();
            countLife = i;
        } catch (RuntimeException e) {
            speed.setText("3");
            game.setBallSpeed(3);
            countLife = 1;
            count.setText("1");
        }
        game.setStart(true);
        changedBalls = false;
    }

    private void makeTime() {
        Date date = new Date();
        new AnimationTimer() {
            public void handle(long currentTime) {
                {
                    if (game.isStart()) {
                        Calendar calendar = Calendar.getInstance();
                        long seconds = calendar.getTime().getTime() - date.getTime();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
                        time.setText("Time: " + simpleDateFormat.format(seconds));
                        if (seconds / 100000 < 1) {
                            game.changeBallSpeed(seconds / 100000);
                        }
                        score.setText("Score: " + (int) (seconds / 10000 * game.getBallSpeed()));

                    }
                }
            }
        }.start();
    }

    private void checkProperties() {
        new Thread(() -> {
            while (!die) {
                if (game.isStart()) {
                    setDisableStartFieldButtons(true);
                } else {
                    setDisableStartFieldButtons(false);
                }
                if (stop) {
                    setDisablePauseStopButtons(true);
                } else {
                    setDisablePauseStopButtons(false);
                }
                if (!game.isBallAlive() && !changedBalls) {
                    countLife--;
                    changedBalls = true;
                    if (countLife < 0) {
                        die = true;
                        stop = true;
                        countLife = -1;
                        beforeStart.setVisible(true);
                        startGameButton.setVisible(true);
                    } else {
                        count.setText(Integer.toString(countLife));
                        // ("дебильная бага из-за того ято fx иногда не видет текст филд");
                        startButton.setDisable(false);
                    }
                }
            }
        }).start();
    }

    public void setDisablePauseStopButtons(boolean flag) {
        stopButton.setDisable(flag);
        pauseButton.setDisable(flag);
    }

    public void setDisableStartFieldButtons(boolean flag) {
        startButton.setDisable(flag);
        speed.setDisable(flag);
        score.setDisable(flag);
        count.setDisable(flag);
    }
}
