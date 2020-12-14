package mvc.Panels;

import javafx.scene.layout.HBox;
import mvc.Entities.Box;
import mvc.Entities.Ship;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    static Board board;
    static Box[][] boxesArr = new Box[10][10];

    @BeforeAll
    static void create(){
        board = new Board();
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Box box = new Box(x, y);
                boxesArr[x][y] = box;
            }
        }
        Ship ship = new Ship(2,true);
        boxesArr[0][0].ship = ship;
        ship.setBoxes(new ArrayList<>());
        ship.getBoxesOfShip().add(boxesArr[0][0]);
        boxesArr[0][1].ship = ship;
        ship.getBoxesOfShip().add(boxesArr[0][1]);
        board.field = boxesArr;
    }

    @Test
    void canPlaceShip() {
        assertFalse(board.canPlaceShip(new Ship(2,true),0,0));
    }

    @Test
    void placeShip() {
            assertTrue(board.placeShip(new Ship(1, true), 5, 5));
    }
}