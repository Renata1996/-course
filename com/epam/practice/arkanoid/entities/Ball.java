package com.epam.practice.arkanoid.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball extends Circle {
    private double angle;
    private double speed;
    private boolean alive;

    public Ball(double x, double y, double radius, double speed) {
        this.setFill(Color.BLUE);
        this.setCenterX(x);
        this.setCenterY(y - radius);
        this.setRadius(radius);
        this.speed = speed;
        this.alive = true;
        this.angle = -(Math.random() * Math.PI);
    }

    public void update() {
        setCenterX(getCenterX() + speed * Math.cos(angle));
        setCenterY(getCenterY() + speed * Math.sin(angle));
    }

    public void move(double x, double y) {
        this.setCenterX(x);
        this.setCenterY(y - getRadius());
    }

    public void changeAngle() {
        this.angle = -(Math.random() * Math.PI);
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }


}
