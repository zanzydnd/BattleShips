package mvc.Entities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoxTest {
    private static Box box;
    private static Ship ship;
    //private static Board board;

    @BeforeAll
    static void createBox() {
        //board = new Board(false,null);
        box = new Box(2, 4);
        ArrayList<Box> boxes = new ArrayList<>();
        boxes.add(box);
        ship = new Ship(1, true);
        ship.setBoxes(boxes);
    }

    @Test
    void shoot() {
        boolean shoot = true;
        try {
            shoot = box.shoot();
        } catch (RuntimeException e) {
            int shipHealth = ship.getHealth();
            if (ship != null) {
                ship.hit();
                shipHealth--;
                assertEquals(shipHealth, ship.getHealth());
                assertTrue(shoot);
            } else {
                assertTrue(!shoot);
                assertTrue(box.wasShot);
            }
        }
    }

    @Test
    void equals() {
        Box box = new Box(2, 4);
        assertEquals(BoxTest.box, box);
    }
}