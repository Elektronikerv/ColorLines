

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Ball {
    private int x;
    private int y;
    private Circle shape;
    
    public Ball(int x, int y, Circle shape) {
        this.x = x;
        this.y = y;
        this.shape = shape;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public Circle getShape() {
        return shape;
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

}
