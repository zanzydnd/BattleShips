package Entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


import java.io.Serializable;
import java.util.List;

public class Box extends Rectangle implements Serializable {
    public boolean wasShot = false;
    public int x , y;
    //public Color color = Color.BLUE;
    public Ship ship = null;


    public Box(int x, int y){
        super(30,30);
        this.x = x;
        this.y = y;
        //setFill(color);
        //setStroke(Color.BLACK);
        setFill(Color.BLUE);
        setStroke(Color.BLACK);
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
            setFill(Color.ORANGE);
            if(!ship.isAlive()){
                List<Box> boxes = ship.getBoxesOfShip();
                boxes.forEach(box -> {
                    box.setFill(Color.RED);
                });
            }
            return true;
        }
        else{
            setFill(Color.BLACK);
            setStroke(Color.BLACK);
            return false;
        }
    }

    public void fillColour(){
        setHeight(30);
        setWidth(30);
        setFill(Color.LIGHTBLUE);
        setStroke(Color.BLACK);
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

