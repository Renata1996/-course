package com.epam.practice.arkanoid.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Board extends Rectangle {

    private static final double SPEED = 5;
    private static final double WIDTH = 60;
    private static final double HEIGHT = 10;

    public Board(double positionX, double positionY) {
        super(positionX - WIDTH / 2, positionY - HEIGHT, WIDTH, HEIGHT);
        this.setFill(Color.BLACK);
        this.getX();
    }

    public void move(double x) {
        this.setX(x - SPEED);
    }


    public void setXY(double x, double y) {
        this.setX(x);
        this.setY(y - HEIGHT);
    }
}
