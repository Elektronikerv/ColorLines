
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import java.util.Random;

 public  class Game {

     private final static  int RADIUS = 25;
     private final static int PADDING = 1;
     private final static int MIN_BALLS_TO_DISAPPEAR = 5;
  
     private Ball clickedBall;
     private boolean isClicked;
     private int size;
     private Ball cells[][];
     private Ball cellsToDelete[];
     private int score =  0;

     public Game() {
        this(9);
    }

    public Game(int size) {
        this.size = size;
        cells = new Ball[size][size];
        cellsToDelete = new Ball[size+1];
    }

    public  GridPane initMap() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(PADDING));
        grid.setHgap(PADDING);
        grid.setVgap(PADDING);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Circle circle = new Circle(RADIUS);
                circle.setFill(Color.WHITE);
                circle.setStroke(Color.BLACK);
                grid.add(circle, j, i);
                cells[i][j] = new Ball(j, i, circle);
            }
        }
        setEvents();
        generateBalls(3);
        return grid;
    }

    public  int getScore() {
        return score;
    }

    private  void setEvents () {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size;j++) {
                final int finalI = i;
                final int finalJ = j;
                cells[i][j].getShape().setOnMouseClicked((MouseEvent event) -> move(cells[finalI][finalJ]));
            }
        }
    }

    private  void move(Ball ball) {
        if(isClicked && ball.isEmpty() && findPath(ball)) {

            clickedBall.setOpacity(1);
            ball.setColor(clickedBall.getColor());

            clickedBall.setColor(Color.WHITE);
            clickedBall = null;
            isClicked = false;
            generateBalls(3);
        }
        else if(!ball.isEmpty() && isClicked) {
            clickedBall.setOpacity(1);
            clickedBall = ball;
            clickedBall.setOpacity(0.7);
        }
        else if(!ball.isEmpty()) {
            clickedBall = ball;
            clickedBall.setOpacity(0.7);
            isClicked = true;
        }
    }

    private   void generateBall() {
        Random random = new Random(System.currentTimeMillis());
        Ball ball = cells[random.nextInt(size)][random.nextInt(size)];
        int attempt = 0;
        while(!ball.isEmpty()) {
            ball = cells[random.nextInt(size)][random.nextInt(size)];
            if(attempt++ > size*size*5)
                return;
        }

        int randCol = random.nextInt(5);
        Paint color = null;
        switch (randCol) {
            case 0: color = Color.BLUE;
                break;
            case 1: color = Color.RED;
                break;
            case 2: color = Color.GREEN;
                break;
            case 3: color = Color.AQUA;
                break;
            case 4: color = Color.YELLOW;
                break;
        }
        ball.setColor(color);
    }

     public  boolean isLosed() {
         for(int i=0; i < size; i++) {
             for(int j=0; j < size; j++) {
                 if(cells[i][j].getColor() == Color.WHITE)
                     return false;
             }
         }
         return true;
     }




    private  void generateBalls(int quentity) {
        for(int i=0; i < quentity; i++)
            generateBall();
    }

    private  boolean isSameColor(Ball ball1, Ball ball2) {
        if(ball1.getColor()!= Color.WHITE && ball2.getColor() != Color.WHITE) {
            if (ball1.getColor() == ball2.getColor())
                return true;
        }
        return false;
    }

      void  deleteBalls() {
         int i=0;
         while(cellsToDelete[i] != null) {
             cellsToDelete[i].setColor(Color.WHITE);
             i++;
         }
     }

     private void erase() {
         for(int i=0; i < cellsToDelete.length; i++)
             cellsToDelete[i] = null;
     }

     private  boolean isEmpty() {
         for(int i=0; i < cellsToDelete.length; i++) {
             if(cellsToDelete[i] != null)
                 return false;
         }
         return true;
     }

     public  void checkHorizontal() {
         int count=0;
         for(int i=0; i < size; i++) {
             for(int j=0; j < size; j++) {

                 if(((j+1) < size) && isSameColor(cells[i][j], cells[i][j+1]) && isEmpty()) {
                     cellsToDelete[count++] = cells[i][j];
                     cellsToDelete[count++] = cells[i][j+1];
                     continue;
                 }
                 else if(((j+1) < size) && isSameColor(cells[i][j], cells[i][j+1])) {
                     cellsToDelete[count++] = cells[i][j+1];
                 }
                 else {
                     if(count >= MIN_BALLS_TO_DISAPPEAR) {                           //balls to dissapear
                         deleteBalls();
                         score+=count;
                     }
                     count = 0;
                     erase();
                 }
             }
             count = 0;
             erase();
         }
     }

     public  void checkVertical() {
         int count = 0;
         for (int i = 0; i < size; i++) {
             for (int j = 0; j < size; j++) {
                 if (((j + 1) < size) && isSameColor(cells[j][i], cells[j + 1][i]) && isEmpty()) {
                     cellsToDelete[count++] = cells[j][i];
                     cellsToDelete[count++] = cells[j + 1][i];
                     continue;
                 } else if (((j + 1) < size) && isSameColor(cells[j][i], cells[j + 1][i])) {
                     cellsToDelete[count++] = cells[j + 1][i];
                 } else {
                     if (count >= MIN_BALLS_TO_DISAPPEAR) {                           //balls to dissapear
                         deleteBalls();
                         score += count;
                     }
                     count = 0;
                     erase();
                 }
             }
             count = 0;
             erase();
         }
     }

     private  boolean findPath(Ball cell) {
         int arr[][] = new int[size][size];
         for(int i=0; i < size; i++)  {
             for(int j=0;j < size; j++) {
                 if(cells[i][j].getColor() == Color.WHITE) {       	 //copy array
                     arr[i][j] = -1;                              //-1 empty cell
                 }
                 else
                 arr[i][j] = -2;                              //-2 block
             }
         }
         arr[clickedBall.getY()][clickedBall.getX()] = 0;
         int n = 0;
         boolean isFound = false;

         while(!isFound) {
             for(int i=0; i < size; i++) {
                 for(int j=0; j < size; j++) {
                     if(arr[i][j] == n) {
                         if(j-1 >= 0 && arr[i][j-1] == -1 ) {
                             arr[i][j-1] = n+1;
                         }
                         if(j+1 < size && arr[i][j+1] == -1 ) {
                             arr[i][j+1] = n+1;
                         }
                         if(i-1 >= 0 && arr[i-1][j] == -1 ) {
                             arr[i-1][j] = n+1;
                         }
                         if(i+1 < size && arr[i+1][j] == -1 ) {
                             arr[i+1][j] = n+1;
                         }
                     }
                 }
             }
             n++;
             if(arr[cell.getY()][cell.getX()] != -1){
                 return true;
             }
             if(n > size*size) {
                 return false;
             }
         }
         return true;
     }
  
     public int getSize() {
         return size;
     }
 }
