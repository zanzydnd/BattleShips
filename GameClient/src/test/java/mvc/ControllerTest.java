package mvc;

import mvc.Connection.Connection;
import mvc.Connection.Message;
import mvc.Connection.MessageType;
import mvc.Entities.Box;
import mvc.Entities.Ship;
import mvc.Panels.Board;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.configuration.IMockitoConfiguration;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControllerTest {

    @Test
    void checkIfAllShipsArePlaced() {
        Model model = new Model();
        model.shipsOneDeck.add(new Ship(1,true));
        model.shipsOneDeck.add(new Ship(1,false));
        model.shipsOneDeck.add(new Ship(1,true));
        model.shipsOneDeck.add(new Ship(1,false));
        model.shipsTwoDeck.add(new Ship(2,true));
        model.shipsTwoDeck.add(new Ship(2,true));
        model.shipsTwoDeck.add(new Ship(2,true));
        model.shipsThreeDeck.add(new Ship(3,true));
        model.shipsThreeDeck.add(new Ship(3,true));
        model.shipsFourDeck.add(new Ship(4,true));
        View view = mock(View.class);
        Controller controller = new Controller(view,model);
        assertTrue(controller.checkIfAllShipsArePlaced());
    }

    @Test
    void deleteShip() {
        Ship ship = new Ship(2, true);
        ship.setBoxes(new ArrayList<>());
        ship.getBoxesOfShip().add(new Box(1,1));
        ship.getBoxesOfShip().add(new Box(1,0));
        Model model = mock(Model.class);
        View view = mock(View.class);
        when(model.removeShip(any(Ship.class))).thenReturn(true);
        Controller controller = new Controller(view,model);
        assertTrue(controller.deleteShip(ship));
    }

    @Test
    void sendMessage() throws IOException {
        View view = mock(View.class);
        Model model = mock(Model.class);
        Board board = mock(Board.class);
        int x = 1;
        int y = 1;
        Box box = new Box(x , y);
        box.wasShot =  true;
        when(board.getBox(x,y)).thenReturn(box);
        when(model.getEnemyBoard()).thenReturn(board);
        Controller controller = new Controller(view , model);

        assertFalse(controller.sendMessage(x , y));

    }

    @Test
    void receiveMessage() throws IOException, ClassNotFoundException {
        View view = mock(View.class);
        Model model = new Model();
        Controller controller = new Controller(view , model);
        Connection con = mock(Connection.class);

        when(con.receive()).thenReturn(new Message(MessageType.DEFEAT));
        controller.setConnection(con);
        Message message = controller.receiveMessage();
        assertEquals(message.getMessageType(),MessageType.MY_DISCONNECT);
        assertTrue(message.isEndgame());
        assertTrue(message.isWin());

        when(con.receive()).thenReturn(new Message(MessageType.DISCONNECT));
        controller.setConnection(con);
        message = controller.receiveMessage();
        assertEquals(message.getMessageType(),MessageType.MY_DISCONNECT);
        assertTrue(message.isEndgame());
        assertTrue(message.isWin());
    }
}