package sample;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.Random;

 public  class Controller {

     private final static int RADIUS = 25;

     private static Ball clickedBall;
     private static boolean isClicked;
     private static int size = 9;
     private static boolean isLosed = false;
     private static Ball cells[][] = new Ball[size][size];
     private static Ball cellsToDelete[] = new Ball[size+1];
     public static int score =  0;

    public static GridPane initMap() {
        GridPane grid = new GridPane();
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
        generateBalls(4);
        return grid;
    }

    public static int getScore() {
        return score;
    }

    private static void setEvents () {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size;j++) {
                final int finalI = i;
                final int finalJ = j;
                cells[i][j].getShape().setOnMouseClicked((MouseEvent event) -> {

                    move(cells[finalI][finalJ]);
                });
            }
        }
    }

    private static void move(Ball ball) {
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

    private static  void generateBall() {
        Random random = new Random(System.currentTimeMillis());
        Ball ball = cells[random.nextInt(size)][random.nextInt(size)];
        int attempt = 0;
        while(!ball.isEmpty()) {
            ball = cells[random.nextInt(size)][random.nextInt(size)];
            attempt++;
            if(attempt > size*size-2) {
                isLosed = true;
                return;
            }
        }
        System.out.println("new" + attempt);
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

     public static boolean isLosed() {
         for(int i=0; i < 9; i++) {
             for(int j=0; j < 9; j++) {
                 if(cells[i][j].getColor() == Color.WHITE) {
                     return false;
                 }
             }
         }
         Alert a = new Alert(Alert.AlertType.CONFIRMATION);
         a.show();
         return true;
     }




    private static void generateBalls(int quentity) {
        for(int i=0; i < quentity; i++)
            generateBall();
    }
//---------------------------------check
    private static boolean isSameColor(Ball ball1, Ball ball2) {
        if(ball1.getColor()!= Color.WHITE && ball2.getColor() != Color.WHITE) {
            if (ball1.getColor() == ball2.getColor())
                return true;
        }
        return false;
    }

     static void  deleteBalls() {
         int i=0;
         while(cellsToDelete[i] != null) {
             cellsToDelete[i].setColor(Color.WHITE);
             i++;
         }
     }


     static void erase() {
         for(int i=0; i < 10; i++)
             cellsToDelete[i] = null;
     }

     private static boolean isEmpty() {
         for(int i=0; i < 10; i++) {
             if(cellsToDelete[i] != null)
                 return false;
         }
         return true;
     }

     public static void checkHorizontal() {
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
                     if(count >=3) {                           //balls to dissapear
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

     public static void checkVertical() {
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
                     if (count >= 3) {                           //balls to dissapear
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

     private static boolean findPath(Ball cell) {
         int arr[][] = new int[size][size];
         for(int i=0; i < 9; i++)  {
             for(int j=0;j < 9; j++) {
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
             for(int i=0; i < 9; i++) {
                 for(int j=0; j < 9; j++) {
                     if(arr[i][j] == n) {
                         if(j-1 >= 0 && arr[i][j-1] == -1 ) {
                             arr[i][j-1] = n+1;
                         }
                         if(j+1 < 9 && arr[i][j+1] == -1 ) {
                             arr[i][j+1] = n+1;
                         }
                         if(i-1 >= 0 && arr[i-1][j] == -1 ) {
                             arr[i-1][j] = n+1;
                         }
                         if(i+1 < 9 && arr[i+1][j] == -1 ) {
                             arr[i+1][j] = n+1;
                         }
                     }
                 }
             }
             n++;
             if(arr[cell.getY()][cell.getX()] != -1){
                 return true;
             }
             if(n > 81) {
                 System.out.println("No way,punk");
                 return false;
             }
         }
         return true;
     }

     public static void printArr() {
         System.out.print("Arr To delete :");
         for(Ball b : cellsToDelete)
             System.out.println(b);
     }

 }
