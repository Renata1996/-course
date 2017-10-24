package com.epam.practice.arkanoid.entities;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Board extends Rectangle {

    private static final double SPEED = 5;
    private static double width = 60;
    private static double height = 10;

    public Board(double positionX, double positionY) {
        super(positionX - width / 2, positionY - height, width, height);
        this.setFill(Color.BLACK);
        this.getX();
    }

    public void move(double x) {
        this.setX(x - SPEED);
    }


    public void setXY(double x, double y) {
        this.setX(x);
        this.setY(y-height);
    }
}
