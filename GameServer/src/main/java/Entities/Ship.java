package Entities;

import javafx.scene.Parent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Ship extends Parent implements Serializable {

    public boolean vertical = true;
    public List<Box> boxes;
    private int countDeck;
    private int health;

    public List<Box> getBoxesOfShip() {
        return boxes;
    }

    public int getHealth() {
        return health;
    }

    public int getCountDeck() {
        return countDeck;
    }

    public Ship(int count, boolean vertical) {
        this.countDeck = count;
        this.vertical = vertical;
        this.health = count;
    }

    public void setBoxes(List<Box> boxes) {
        if(boxes == null){
            this.boxes = null;
        }
        else {
            this.boxes = new ArrayList<>(boxes);
        }
    }


    public void hit() {
        health--;
        System.out.println(health);
    }

    public boolean isAlive() {
        return health > 0;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        Ship ship = (Ship) o;
        if(!(ship.countDeck == this.countDeck))
            return false;

        AtomicBoolean flag = new AtomicBoolean(true);
        System.out.println(ship.getBoxesOfShip());
        ship.getBoxesOfShip().forEach(e->{
            if(!this.getBoxesOfShip().contains(e))
                flag.set(false);
        });
        return flag.get();
    }
}
