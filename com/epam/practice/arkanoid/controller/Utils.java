package com.epam.practice.arkanoid.controller;

import com.epam.practice.arkanoid.entities.Ball;
import com.epam.practice.arkanoid.entities.Board;
import com.epam.practice.arkanoid.entities.Brick;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Utils {

    private static final int MARGIN = 5;
    private static final int HALF = 2;
    private static final int BALL_RADIUS = 10;
    private static final int DEFAULT_BALL_SPEED = 3;
    private static final int DEFAULT_ANGLE_COUNT = 0;
    private static final int FIRST_X_BRICK = 0;
    private static final int FIRST_Y_BRICK = 60;
    private static final int COUNT_LINES_BRICK = 3;
    private static final int LOCAL_X = 0;
    private static final int LOCAL_Y = 0;
    private static final int LOCAL_WIDTH_HEIGHT = 1;
    private static final double BALL_COLLISION_PARAM = 0.01;
    private static final int BALL_COLLISION_COUNT = 5;
    private static final int BALL_SPEED_MAX = 7;
    private Pane pane;
    private Board board;
    private Brick brick;
    private Ball ball;
    private boolean start;
    private double ballSpeed;
    private double angle;
    private int angleCount;


    public Utils(Pane pane) {
        this.angleCount = DEFAULT_ANGLE_COUNT;
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
        ball.move(board.getX() + board.getWidth() / HALF, board.getY());
        ball.changeAngle();
    }

    private void initializeItems() {

        ballSpeed = DEFAULT_BALL_SPEED;
        board = new Board(pane.getPrefWidth() / HALF, pane.getPrefHeight());
        brick = new Brick(FIRST_X_BRICK, FIRST_Y_BRICK, COUNT_LINES_BRICK, pane.getPrefWidth());
        ball = new Ball(board.getX() + board.getWidth() / HALF, board.getY(), BALL_RADIUS, ballSpeed);
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

        if (ball.intersects(LOCAL_X, -MARGIN, pane.getPrefWidth(), LOCAL_WIDTH_HEIGHT)) {
            topIntersect();
            checkBallAngle();
        }
        if (ball.intersects(LOCAL_X, LOCAL_Y, LOCAL_WIDTH_HEIGHT, pane.getPrefHeight())) {
            leftIntersect();
            checkBallAngle();
        }
        if (ball.intersects(pane.getPrefWidth(), LOCAL_Y, LOCAL_WIDTH_HEIGHT, pane.getPrefHeight())) {
            rightIntersect();
            checkBallAngle();
        }
        if (ball.intersects(board.getLayoutBounds())) {
            bottomIntersect();
            checkBallAngle();
        } else if (ball.intersects(LOCAL_X, pane.getPrefHeight(), pane.getPrefWidth(), LOCAL_WIDTH_HEIGHT)) {
            ball.setAlive(false);
            start = false;
        }
        Rectangle rectangle = null;

        for (int i = 0; i < brick.getRectangles().size(); i++) {
            rectangle = brick.getRectangles().get(i);
            if (ball.intersects(rectangle.getX(), rectangle.getY() + rectangle.getHeight(), rectangle.getWidth(), LOCAL_WIDTH_HEIGHT)) {
                topIntersect();
                checkBallAngle();
                rectangle.setVisible(false);
                brick.getRectangles().remove(i);
            } else if (ball.intersects(rectangle.getX(), rectangle.getY(), LOCAL_WIDTH_HEIGHT, rectangle.getHeight())) {
                rightIntersect();
                checkBallAngle();
                rectangle.setVisible(false);
                brick.getRectangles().remove(i);
            } else if (ball.intersects(rectangle.getX() + rectangle.getWidth(), rectangle.getY(), LOCAL_WIDTH_HEIGHT, rectangle.getHeight())) {
                leftIntersect();
                checkBallAngle();
                rectangle.setVisible(false);
                brick.getRectangles().remove(i);
            }
        }
    }

    private void checkBallAngle() {
        if (Math.abs(angle) - Math.abs(ball.getAngle()) <= BALL_COLLISION_PARAM) {
            angleCount++;
            angle = ball.getAngle();
        }
        if (angleCount == BALL_COLLISION_COUNT) {
            ball.changeAngle();
            angleCount = DEFAULT_ANGLE_COUNT;
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
        if (ball.getAngle() > DEFAULT_ANGLE_COUNT) ball.setAngle(Math.PI - ball.getAngle());
        if (ball.getAngle() < DEFAULT_ANGLE_COUNT) ball.setAngle(-Math.PI - ball.getAngle());
    }

    private void rightIntersect() {
        if (ball.getAngle() > DEFAULT_ANGLE_COUNT) ball.setAngle(Math.PI - ball.getAngle());
        if (ball.getAngle() < DEFAULT_ANGLE_COUNT) ball.setAngle(-Math.PI - ball.getAngle());
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
        if (ballSpeed < BALL_SPEED_MAX) {
            this.ballSpeed += ballSpeed;
            ball.setSpeed(this.ballSpeed);
        }

    }

    public void makeNewGame() {

        ball.move(board.getX() + board.getWidth() / 2, board.getY());
        board.move(pane.getPrefWidth() / 2);
        brick.setFirstY(10);
        brick.makeNewBricks();
        for (Node n : pane.getChildren()) {
            if (n instanceof Group) {
                pane.getChildren().remove(n);
                break;
            }
        }
        pane.getChildren().add(makeBricksGroup());
        start = false;
        angle = ball.getAngle();

    }
}
