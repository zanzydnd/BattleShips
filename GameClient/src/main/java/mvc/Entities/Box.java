package mvc.Entities;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import mvc.Panels.Board;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.List;

public class Box extends Rectangle implements Serializable {
    public boolean wasShot = false;
    public int x , y;
    //public Color color = Color.BLUE;
    public Ship ship = null;
    public Board board;

    public Box(int x, int y){
        super(41,41);
        this.x = x;
        this.y = y;
    }

    public Box(int x, int y,Board board){
        super(41,41);
        this.board = board;
        this.x = x;
        this.y = y;
        //setFill(color);
        //setStroke(Color.BLACK);
        if(board.isEnemy()){
            try {
                Image closed = new Image("/closed.png");
                setFill(new ImagePattern(closed));
            } catch (Exception e){
            }
        }
        else {
            try{
            Image closed = new Image("/empty.png");
            setFill(new ImagePattern(closed));}
            catch (Exception e){

            }
        }//setFill(Color.BLUE);
        //setStroke(Color.BLACK);
    }

    public void setWasShot(boolean wasShot){
        this.wasShot = wasShot;
    }

    public boolean getWasShot(){
        return wasShot;
    }
    /*
        public void setColor(Color c){
            color = c;
            setFill(color);
            setStroke(Color.BLACK);
        }
    */
    public boolean shoot(){
        wasShot = true;
        if(ship!=null){
            ship.hit();
            Image img = new Image("/hitted.png");
            setFill(new ImagePattern(img));
            //setFill(Color.ORANGE);
            if(!ship.isAlive()){
                List<Box> boxes = ship.getBoxesOfShip();
                Image dead = new Image("/destroy_ship.png");
                boxes.forEach(box -> {
                    box.setFill(new ImagePattern(dead));
                    //box.setFill(Color.RED);
                });
            }
            return true;
        }
        else{
            Image miss = new Image("/point.png");
            //setFill(Color.BLACK);
            setFill(new ImagePattern(miss));
            //setStroke(Color.BLACK);
            return false;
        }
    }

    public void fillColour(){
        setHeight(41);
        setWidth(41);
        Image closed = new Image("/closed.png");
        setFill(new ImagePattern(closed));
        //setFill(Color.LIGHTBLUE);
        //setStroke(Color.BLACK);
    }

    @Override
    public boolean equals(Object obj) {
        Box check = (Box) obj;
        boolean flag = false;

        if (check.x == x && check.y == y){
            flag = true;
        }

        return flag;
    }
}

