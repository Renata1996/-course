package com.epam.practice.arkanoid.controller;

import com.epam.practice.arkanoid.entities.Ball;
import com.epam.practice.arkanoid.entities.Board;
import com.epam.practice.arkanoid.entities.Brick;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Utils {

    private static final int MARGIN = 5;
    private Pane pane;
    private Board board;
    private Brick brick;
    private Ball ball;
    private boolean start;
    private double ballSpeed;
    private double angle;
    private int angleCount;

    public Utils(Pane pane) {
        this.angleCount = 0;
        this.start = false;
        this.pane = pane;
        initializeItems();
        addItemsOnBoard();
        makeBallAnimation();
    }

    public void run() {
        moveBoard();
    }

    public void makeBall() {
        ball.setAlive(true);
        ball.move(board.getX() + board.getWidth() / 2, board.getY());
        ball.changeAngle();
    }

    private void initializeItems() {
        //default params
        ballSpeed = 3;
        board = new Board(pane.getPrefWidth() / 2, pane.getPrefHeight());
        brick = new Brick(0, 60, 3, pane.getPrefWidth());
        ball = new Ball(board.getX() + board.getWidth() / 2, board.getY(), 10, ballSpeed);
        angle = ball.getAngle();
    }

    private void moveBoard() {
        board.setOnMouseDragged(event -> {
                    if (start) {
                        if ((event.getX() > MARGIN) && (event.getX() < pane.getWidth() - board.getWidth() + MARGIN)) {
                            board.move(event.getX());
                        }
                    }
                }
        );
    }

    private void makeBallAnimation() {
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                if (start) {
                    if (ball.isAlive()) {
                        ball.update();
                        checkBallCollision();
                    }
                }
            }
        }.start();
    }

    private void checkBallCollision() {

        if (ball.intersects(0, -MARGIN, pane.getPrefWidth(), 1)) {
            topIntersect();
            checkBallAngle();
        }
        if (ball.intersects(0, 0, 1, pane.getPrefHeight())) {
            leftIntersect();
            checkBallAngle();
        }
        if (ball.intersects(pane.getPrefWidth(), 0, 1, pane.getPrefHeight())) {
            rightIntersect();
            checkBallAngle();
        }
        if (ball.intersects(board.getLayoutBounds())) {
            bottomIntersect();
            checkBallAngle();
        } else if (ball.intersects(0, pane.getPrefHeight(), pane.getPrefWidth(), 1)) {
            ball.setAlive(false);
            start = false;
        }
        Rectangle rectangle = null;

        for (int i = 0; i < brick.getRectangles().size(); i++) {
            rectangle = brick.getRectangles().get(i);
            if (ball.intersects(rectangle.getX(), rectangle.getY() + rectangle.getHeight(), rectangle.getWidth(), 0)) {
                topIntersect();
                checkBallAngle();
                rectangle.setVisible(false);
                brick.getRectangles().remove(i);
            } else if (ball.intersects(rectangle.getX(), rectangle.getY(), 0, rectangle.getHeight())) {
                rightIntersect();
                checkBallAngle();
                rectangle.setVisible(false);
                brick.getRectangles().remove(i);
            } else if (ball.intersects(rectangle.getX() + rectangle.getWidth(), rectangle.getY(), 0, rectangle.getHeight())) {
                leftIntersect();
                checkBallAngle();
                rectangle.setVisible(false);
                brick.getRectangles().remove(i);
            }
        }

    }

    private void checkBallAngle() {
        if (Math.abs(angle) - Math.abs(ball.getAngle()) <= 0.0) {
            angleCount++;
            angle = ball.getAngle();
        }
        if (angleCount == 5) {
            ball.changeAngle();
            angleCount = 0;
        }
    }

    public boolean isBallAlive() {
        return ball.isAlive();
    }

    private void topIntersect() {
        ball.setAngle(-ball.getAngle());
    }

    private void bottomIntersect() {
        ball.setAngle(-ball.getAngle());
    }

    private void leftIntersect() {
        if (ball.getAngle() > 0) ball.setAngle(Math.PI - ball.getAngle()); //going down
        if (ball.getAngle() < 0) ball.setAngle(-Math.PI - ball.getAngle()); //going up
    }

    private void rightIntersect() {
        if (ball.getAngle() > 0) ball.setAngle(Math.PI - ball.getAngle()); //going down
        if (ball.getAngle() < 0) ball.setAngle(-Math.PI - ball.getAngle()); //going up
    }

    private void addItemsOnBoard() {
        pane.getChildren().add(board);
        pane.getChildren().add(makeBricksGroup());
        pane.getChildren().add(ball);
    }

    private Group makeBricksGroup() {
        Group group = new Group();
        for (Rectangle rectangle : brick.makeRectangle()) {
            group.getChildren().add(rectangle);
        }
        return group;
    }

    public static int getMARGIN() {
        return MARGIN;
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Brick getBrick() {
        return brick;
    }

    public void setBrick(Brick brick) {
        this.brick = brick;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }


    public double getBallSpeed() {
        return ballSpeed;
    }

    public void setBallSpeed(double ballSpeed) {
        this.ballSpeed = ballSpeed;
        ball.setSpeed(ballSpeed);
    }

    public void changeBallSpeed(double ballSpeed) {
        if (ballSpeed < 7) {
            this.ballSpeed += ballSpeed;
            ball.setSpeed(this.ballSpeed);
        }

    }


}
