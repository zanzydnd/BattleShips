package mvc.Entities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    private static List<Box> listBoxes;
    private static Ship ship;

    @BeforeAll
    static void createVariable(){
        ship = new Ship(3,true);
        listBoxes = new ArrayList<>();
        listBoxes.add(new Box(1,1));
        listBoxes.get(0).ship = ship;
        listBoxes.add(new Box(1,2));
        listBoxes.get(1).ship = ship;
        listBoxes.add(new Box(1,3));
        listBoxes.get(2).ship = ship;
        ship.setBoxes(listBoxes);
    }

    @Test
    void hit() {
        int health = ship.getHealth();
        health--;
        ship.hit();
        assertEquals(health,ship.getHealth());
    }

    @Test
    void isAlive() {
        for(Box box : ship.getBoxesOfShip()){
            ship.hit();
        }
        assertFalse(ship.isAlive());
    }

    @Test
    void testEquals() {
        Ship ship1 = new Ship(3,true);
        List<Box> listBoxes1 = new ArrayList<>();
        listBoxes1.add(new Box(1,1));
        listBoxes1.get(0).ship = ship1;
        listBoxes1.add(new Box(1,2));
        listBoxes1.get(1).ship = ship1;
        listBoxes1.add(new Box(1,3));
        listBoxes1.get(2).ship = ship1;
        ship1.setBoxes(listBoxes1);

        assertEquals(ship, ship1);
    }
}