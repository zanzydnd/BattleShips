package mvc.Panels;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import mvc.Entities.Box;
import mvc.Entities.Ship;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board extends Parent implements Serializable {
    private boolean enemy = false;
    Random random = new Random();
    public Box[][] field = new Box[10][10];


    public Board(){}

    public Board(boolean enemy, EventHandler<? super MouseEvent> handler) {
        this.enemy = enemy;
        VBox rows = new VBox();
        for (int y = 0; y < 10; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 10; x++) {
                Box box = new Box(x, y,this);
                field[x][y] = box;
                box.setOnMouseClicked(handler);
                row.getChildren().addAll(box);
            }

            rows.getChildren().addAll(row);
        }
        getChildren().addAll(rows);
    }

    public boolean isEnemy(){
        return enemy;
    }

    private boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    private boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }

    public Box getBox(int x, int y) {
        try {
            return field[x][y];
        }
        catch (ArrayIndexOutOfBoundsException e ){
            return null;
        }
        //(Box) ((HBox) rows.getChildren().get(y)).getChildren().get(x);
    }

    public Box[] getNeighbours(int x, int y) {
        Point2D[] points = new Point2D[]{
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x + 1, y - 1),
                new Point2D(x + 1, y + 1),
                new Point2D(x - 1, y - 1),
                new Point2D(x - 1, y + 1),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1)
        };

        List<Box> neighbours = new ArrayList<Box>();
        for (Point2D p : points) {
            if (isValidPoint(p)) {
                neighbours.add(getBox((int) p.getX(), (int) (p.getY())));
            }
        }

        return neighbours.toArray(new Box[0]);

    }

    public boolean canPlaceShip(Ship ship, int x, int y) {
        int legnth = ship.getCountDeck();
        if (ship.vertical) {
            for (int i = y; i < y + legnth; i++) {
                if (!isValidPoint(x, i))
                    return false;

                Box box = getBox(x, i);
                if (box.ship != null)
                    return false;

                for (Box neighbour : getNeighbours(x, i)) {
                    if (!isValidPoint(x, i))
                        return false;

                    if (neighbour.ship != null)
                        return false;
                }
            }
        } else {
            for (int i = x; i < x + legnth; i++) {
                if (!isValidPoint(i, y))
                    return false;

                Box box = getBox(i, y);
                if (box.ship != null)
                    return false;

                for (Box neighbour : getNeighbours(i, y)) {
                    if (!isValidPoint(i, y))
                        return false;

                    if (neighbour.ship != null)
                        return false;
                }
            }
        }
        return true;
    }

    public boolean placeShip(Ship ship, int x , int y){
        List<Box> boxes = new ArrayList<>();
        Image image = null;
        try {
            image = new Image("/ship.png");
        }
        catch (Exception e){
        }
        if(canPlaceShip(ship,x,y)){
            int length = ship.getCountDeck();
            if(ship.vertical){
                for (int i = y; i < y + length; i++) {
                    Box cell = getBox(x, i);
                    boxes.add(cell);
                    cell.ship = ship;
                    if (!enemy) {
                        //cell.setColor(Color.WHITE);
                        try {
                            cell.setFill(new ImagePattern(image));
                        }
                        catch (Exception e){}//cell.setFill(Color.WHITE);
                        //cell.setStroke(Color.GREEN);
                    }
                }
                ship.setBoxes(boxes);
                System.out.println(boxes);
            }
            else{
                for (int i = x; i < x + length; i++) {
                    Box cell = getBox(i, y);
                    boxes.add(cell);
                    cell.ship = ship;
                    if (!enemy) {
                        //cell.setColor(Color.WHITE);
                        cell.setFill(new ImagePattern(image));
                        //cell.setFill(Color.WHITE);
                        //cell.setStroke(Color.GREEN);
                    }
                }
                ship.setBoxes(boxes);
            }

            return true;
        }
        return false;
    }

    public void deleteHandlers(){
        for(Box boxes[] : field){
            for(Box box: boxes){
                box.setOnMouseClicked(null);
            }
        }
    }

    public void paintBoard(EventHandler<? super MouseEvent> handler){
        VBox rows = new VBox();
        Image image = new Image("/closed.png");
        for(int y = 0; y < 10;y++){
            HBox row = new HBox();
            for(int x = 0; x < 10;x ++){
                field[x][y].setOnMouseClicked(handler);
                field[x][y].fillColour();
                row.getChildren().addAll(field[x][y]);
            }
            rows.getChildren().addAll(row);
        }
        getChildren().addAll(rows);
    }


    public void setHandler(EventHandler<? super MouseEvent> handler) {
        for(int y = 0; y < 10;y++){
            for(int x = 0; x < 10;x ++){
                field[x][y].setOnMouseClicked(handler);
            }
        }
    }
}
