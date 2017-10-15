package sample;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * Created by elektroniker on 10/13/17.
 */
public class Ball {
    private int x;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private int y;
    private Circle shape;

    public Ball(int x, int y, Circle shape) {
        this.x = x;
        this.y = y;
        this.shape = shape;

    }

    public Circle getShape() {
        return shape;
    }

    public void setShape(Circle shape) {
        this.shape = shape;
    }

    public boolean isEmpty() {
        return shape.getFill() == Color.WHITE;
    }

    public void setColor(Paint fill) {
        shape.setFill(fill);
    }
    public Paint getColor() {
        return shape.getFill();
    }

    public void setOpacity(double opacity) {
        shape.setOpacity(opacity);
    }

    @Override
    public String toString() {
        return "Ball{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
