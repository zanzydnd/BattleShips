package mvc;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import mvc.Connection.Connection;
import mvc.Connection.Message;
import mvc.Connection.MessageType;
import mvc.Connection.Room;
import mvc.Entities.Box;
import mvc.Entities.Ship;


import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class Controller {
    private View view;
    private Model model;
    private Connection connection;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
    }


    public boolean checkIfAllShipsArePlaced() {
        if (model.getShipsOneDeck().size() == 4 &&
                model.getShipsTwoDeck().size() == 3 &&
                model.getShipsThreeDeck().size() == 2 &&
                model.getShipsFourDeck().size() == 1) return true;
        else return false;
    }

    public boolean deleteShip(Ship ship) {

        if (ship == null)
            return true;

        if (!ship.isAlive() || ship.getHealth() < ship.getCountDeck())
            return false;

        if (model.removeShip(ship)) {
            List<Box> clearBoxes = ship.getBoxesOfShip();
            for (int i = 0; i < clearBoxes.size(); i++) {
                Box bx = clearBoxes.get(i);
                bx.ship = null;
                Image img = null;
                try {
                    img = new Image("/empty.png");
                    bx.setFill(new ImagePattern(img));
                }catch (Exception e){
                }
            }
            ship.setBoxes(null);
            return true;
        }
        return false;
    }

    // методы коннекта к серверу и обмен данными


    public void createRoom() throws IOException, ClassNotFoundException {
        connection = new Connection(new Socket("localhost", 8080));
        Message message = connection.receive(); //принимае от сервера сообщение
        //если тип сообщения ACCEPTED то отправляем на сервер наше поле с кораблями и список всех кораблей
        if (message.getMessageType() == MessageType.ACCEPTED) {
            connection.send(new Message(MessageType.CREATE_ROOM));
            Message message1 = connection.receive();
            View.callInformationWindow("Код для вашего друга: " + message1.getCode());
            connection.send(new Message(MessageType.FIELD, model.getPlayerBoard(), model.getAllShips()));
            Message messageField = connection.receive(); //ждем ответа от сервера с полем и списком кораблей соперника
            if (messageField.getMessageType() == MessageType.FIELD) {
                model.setEnemyBoard(messageField.getGameField());//сохраняем в в модель поле и список кораблей противника
                model.setAllShipsOfEnemy(messageField.getListOfAllShips());
            }
        }
    }

    public void connectToRoom(String code) throws IOException, ClassNotFoundException {
        connection = new Connection(new Socket("localhost", 8080));
        Message message = connection.receive(); //принимае от сервера сообщение
        //если тип сообщения ACCEPTED то отправляем на сервер наше поле с кораблями и список всех кораблей
        if (message.getMessageType() == MessageType.ACCEPTED) {
            connection.send(new Message(MessageType.CONNECT_TO_ROOM, code));
            connection.send(new Message(MessageType.FIELD, model.getPlayerBoard(), model.getAllShips()));
            Message messageField = connection.receive(); //ждем ответа от сервера с полем и списком кораблей соперника
            System.out.println("Второй отправил поле");
            System.out.println("Второй получил поле");
            if (messageField.getMessageType() == MessageType.FIELD) {
                System.out.println(messageField.getGameField());
                model.setEnemyBoard(messageField.getGameField());//сохраняем в в модель поле и список кораблей противника
                System.out.println("!!!!!!!" + model.getEnemyBoard() + "!!!!!!!!");
                model.setAllShipsOfEnemy(messageField.getListOfAllShips());
            }
        }
    }

    public void disconnectGameRoom() throws IOException {
        connection.send(new Message(MessageType.DISCONNECT));
    }

    public boolean sendMessage(int x, int y) throws IOException {
        Box box = model.getEnemyBoard().getBox(x, y);
        if (!box.wasShot) {
            box.shoot(); //открываем бокс выстрела
            //openBoxesAround(box); //открываем соседние пустые клетки (боксы)
            connection.send(new Message(MessageType.SHOT, x, y)); //отправляем координаты выстрела на сервер
            return true;
        } else return false;
    }

    public Message receiveMessage() throws IOException, ClassNotFoundException {
        Message message = connection.receive();
        if (message.getMessageType() == MessageType.SHOT) {
            int x = message.getX();
            int y = message.getY();
            Box box = model.playerBoard.getBox(x, y);
            Message sendingMessage;
            if (box.shoot() && !model.checkAllPlayerShipsAreDead()) {
                sendingMessage = new Message(MessageType.WAITING_TURN);
                sendingMessage.setEndgame(false);
                sendingMessage.setYourTurn(false);
                connection.send(sendingMessage);
            } else {
                sendingMessage = new Message(MessageType.SHOT);
                sendingMessage.setEndgame(false);
                sendingMessage.setYourTurn(true);
            }
            if (model.checkAllPlayerShipsAreDead()) {//проверка на конец игры, если да послыем соответствующее сообщение
                sendingMessage = new Message(MessageType.DEFEAT);
                sendingMessage.setEndgame(true);
                sendingMessage.setWin(false);
                connection.send(sendingMessage);
                //View.callInformationWindow("Вы проиграли! Все Ваши корабли уничтожены.");
            }
            return sendingMessage;
        } else if (message.getMessageType() == MessageType.WAITING_TURN) {
            Message sendingMessage = new Message(MessageType.WAITING_TURN);
            sendingMessage.setYourTurn(true);
            sendingMessage.setEndgame(false);
            return sendingMessage;
        } else if (message.getMessageType() == MessageType.DISCONNECT) {
            Message sendingMessage = new Message(MessageType.MY_DISCONNECT);
            sendingMessage.setEndgame(true);
            sendingMessage.setWin(true);
            connection.send(sendingMessage);
            //View.callInformationWindow("Ваш соперник покинул игру. Вы одержали техническую победу!");
            return sendingMessage;
            //если тип сообщения DEFEAT отправялем, что мы тоже отключаемся
        } else if (message.getMessageType() == MessageType.DEFEAT) {
            Message sendingMessage = new Message(MessageType.MY_DISCONNECT);
            sendingMessage.setEndgame(true);
            sendingMessage.setWin(true);
            connection.send(sendingMessage);
            //View.callInformationWindow("Все корабли противника уничтожены. Вы одержали победу!");
            return sendingMessage;
        } else {
            Message over = new Message(MessageType.DISCONNECT);
            over.setEndgame(true);
            over.setWin(false);
            return over;
        }
    }

    public void setConnection(Connection connection){
        this.connection = connection;
    }
}