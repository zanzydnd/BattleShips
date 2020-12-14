package mvc;

import mvc.Entities.Box;
import mvc.Entities.Ship;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    @Test
    void checkAllPlayerShipsAreDead() {
        Model model = new Model();
        model.shipsOneDeck.add(new Ship(1,true));
        model.shipsThreeDeck.add(new Ship(3,true));
        model.shipsTwoDeck.add(new Ship(2,true));
        assertFalse(model.checkAllPlayerShipsAreDead());

        for(Ship ship : model.getAllShips()){
            while (ship.isAlive()){
                ship.hit();
            }
        }

        assertTrue(model.checkAllPlayerShipsAreDead());
    }

    @Test
    void checkAllEnemyShipsAreDead() {
        Model model = new Model();
        List<Ship> list = new ArrayList<>();
        list.add(new Ship(2,true));
        list.add(new Ship(1,false));
        list.add(new Ship(3,true));
        model.setAllShipsOfEnemy(list);

        assertFalse(model.checkAllEnemyShipsAreDead());

        for(Ship ship : model.allShipsOfEnemy){
            while(ship.isAlive()){
                ship.hit();
            }
        }

        assertTrue(model.checkAllEnemyShipsAreDead());
    }

    @Test
    void removeShip() {
        Model model = new Model();
        Ship sh1 = new Ship(1,true);
        List<Box> boxes = new ArrayList<>();
        boxes.add(new Box(0,0));
        sh1.setBoxes(boxes);
        Ship sh2 = new Ship(2,true);
        boxes = new ArrayList<>();
        boxes.add(new Box(1,1));
        boxes.add(new Box(1,2));
        sh2.setBoxes(boxes);
        Ship sh3 = new Ship(3,true);
        boxes.add(new Box(2,1));
        boxes.add(new Box(2,2));
        boxes.add(new Box(2,3));
        sh3.setBoxes(boxes);
        model.shipsOneDeck.add(sh1);
        model.shipsThreeDeck.add(sh2);
        model.shipsTwoDeck.add(sh3);

        Ship sh4 = new Ship(1,true);
        boxes = new ArrayList<>();
        boxes.add(new Box(5,5));
        sh4.setBoxes(boxes);

        assertFalse(model.removeShip(sh4));

        assertTrue(model.removeShip(sh1));
        assertTrue(model.removeShip(sh2));
        assertTrue(model.removeShip(sh3));
    }

    @Test
    void checkIsThereEnoughShips() {
        Model model = new Model();
        assertTrue(model.checkIsThereEnoughShips(new Ship(1,true)));
        assertTrue(model.checkIsThereEnoughShips(new Ship(1,true)));
        assertTrue(model.checkIsThereEnoughShips(new Ship(1,true)));
        assertTrue(model.checkIsThereEnoughShips(new Ship(1,true)));
    }
}