package com.epam.practice.arkanoid.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Brick {

    private static int MARGIN = 5;
    private int width = 21;
    private int height = 15;
    private int firstX;
    private int firstY;
    private int countLines;
    private double paneWidth;
    private List<Rectangle> rectangles;

    public Brick(int firstX, int firstY, int countLines, double paneWidth) {
        this.firstX = firstX;
        this.firstY = firstY;
        this.countLines = countLines;
        this.paneWidth = paneWidth;
    }

    public List<Rectangle> makeRectangle() {
        rectangles = new ArrayList<>();
        for (int j = 0; j < countLines; j++) {
            firstX = MARGIN;
            for (int i = 0; firstX < paneWidth - width; i++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setX(firstX);
                rectangle.setY(firstY);
                rectangle.setWidth(width);
                rectangle.setHeight(height);
                rectangle.setFill(new Color(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 1));
                rectangles.add(rectangle);
                firstX += width + MARGIN;
            }
            firstY += MARGIN + height;
        }
        return rectangles;
    }

    public List<Rectangle> getRectangles() {
        return rectangles;
    }

    public void setRectangles(List<Rectangle> rectangles) {
        this.rectangles = rectangles;
    }

    public void makeNewBricks() {
        rectangles.clear();
        makeRectangle();
    }
}
