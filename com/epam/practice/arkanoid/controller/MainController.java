package com.epam.practice.arkanoid.controller;

import javafx.animation.AnimationTimer;
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
    private static final String EMPTY_STRING = "";
    private static final int DEFAULT_COUNT_LIFE = 3;
    private static final int START_COUNT_LIFE = 1;
    private static final int DEFAULT_COUNT_SPEED = 3;
    private static final String PAUSE_TEXT = "Pause";
    private static final String RESUME_TEXT = "Resume";
    private static final int MAX_SPEED = 5;
    private static final int MIN_SPEED = 0;
    private static final int MIN_COUNT_LIFE = 0;
    private static final int MAX_COUNT_LIFE = 3;
    private static final int TIME = 100000;
    private static final int IMPOSSIBLE_COUT_LIFE = -1;
    private static final int CHANGE_BALL_SPEED_TIME = 1;

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
    private Boolean pause = false;
    private int countLife = START_COUNT_LIFE;
    private boolean die = false;
    private boolean changedBalls = false;


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

    private void makeStop() {
        setDisablePauseStopButtons(true);
        startButton.setDisable(false);
        time.setText(EMPTY_STRING);
        score.setText(EMPTY_STRING);
        game.makeNewGame();
        game.run();
        game.makeBall();
        makeTime();
        stop = false;
        die = false;
        countLife = DEFAULT_COUNT_LIFE;
        count.setText(String.valueOf(DEFAULT_COUNT_LIFE));
    }

    @FXML
    private void handlePauseButtonClick(MouseEvent event) {
        if (!pause) {
            pauseButton.setText(RESUME_TEXT);
            game.setStart(false);
            pause = true;
        } else {
            pause = false;
            pauseButton.setText(PAUSE_TEXT);
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
            Integer textFromFields = Integer.parseInt(speed.getText());
            if (textFromFields < MIN_SPEED || textFromFields > MAX_SPEED) {
                makeDefault();
                game.setBallSpeed(textFromFields);
                textFromFields = Integer.parseInt(count.getText());
            }
            if (textFromFields < MIN_COUNT_LIFE || textFromFields > MAX_COUNT_LIFE) {
                makeDefault();
                countLife = textFromFields;
            }
        } catch (NumberFormatException e) {
            makeDefault();
        }
        game.setStart(true);
        changedBalls = false;

    }

    private void makeDefault() {
        speed.setText(String.valueOf(DEFAULT_COUNT_SPEED));
        game.setBallSpeed(DEFAULT_COUNT_SPEED);
        countLife = START_COUNT_LIFE;
        count.setText(String.valueOf(START_COUNT_LIFE));
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
                        if (seconds / TIME < CHANGE_BALL_SPEED_TIME) {
                            game.changeBallSpeed(seconds / TIME);
                        }
                        score.setText("Score: " + (int) (seconds / TIME * game.getBallSpeed()));

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
                    if (countLife < MIN_COUNT_LIFE) {
                        die = true;
                        stop = true;
                        countLife = IMPOSSIBLE_COUT_LIFE;
                        beforeStart.setVisible(true);
                        startGameButton.setVisible(true);
                    } else {
                        count.setText(Integer.toString(countLife));
                        // ("дебильная бага из-за того, что fx иногда не видит текст филд");
                        startButton.setDisable(false);
                    }
                }
            }
        }).start();
    }

    private void setDisablePauseStopButtons(boolean flag) {
        stopButton.setDisable(flag);
        pauseButton.setDisable(flag);
    }

    private void setDisableStartFieldButtons(boolean flag) {
        startButton.setDisable(flag);
        speed.setDisable(flag);
        score.setDisable(flag);
        count.setDisable(flag);
    }
}
