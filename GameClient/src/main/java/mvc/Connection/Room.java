package mvc.Connection;

import mvc.Entities.Ship;
import mvc.Panels.Board;
import mvc.View;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Room extends Thread {
    private List<Connection> listConnection = new ArrayList<>();
    private volatile boolean allPlayersConnected = false;
    private List<Ship> allShipsPlayer1;
    private List<Ship> allShipsPlayer2;
    private ServerSocket serverSocket;
    private Board boardPlayer1;
    private Board boardPlayer2;

    public Room(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }



    @Override
    public void run() {
        startServer();
    }


    public void startServer() {
        try {
            while (!allPlayersConnected) {
                Socket socket = serverSocket.accept();
                if (listConnection.size() == 0) {
                    System.out.println("Первый игрок подключился");
                    Connection connection = new Connection(socket);
                    listConnection.add(connection);
                    connection.send(new Message(MessageType.ACCEPTED));
                    Message message = connection.receive();
                    if (message.getMessageType() == MessageType.FIELD) {
                        boardPlayer1 = message.getGameField();
                        allShipsPlayer1 = message.getListOfAllShips();
                    }
                    new ThreadConnection(connection).start();
                } else if (listConnection.size() == 1) {
                    Connection connection = new Connection(socket);
                    listConnection.add(connection);
                    System.out.println("Второй игрок подключился");
                    connection.send(new Message(MessageType.ACCEPTED));
                    Message message = connection.receive();
                    if (message.getMessageType() == MessageType.FIELD) {
                        boardPlayer2 = message.getGameField();
                        allShipsPlayer2 = message.getListOfAllShips();
                        connection.send(new Message(MessageType.FIELD, boardPlayer1, allShipsPlayer1));
                        listConnection.get(0).send(new Message(MessageType.FIELD, boardPlayer2, allShipsPlayer2));
                    }
                    new ThreadConnection(connection).start();
                    allPlayersConnected = true;
                }
            }
            serverSocket.close();
        } catch (Exception e) {
            View.callInformationWindow("Ошибка при запуске сервера");
        }
    }


    // Класс  исполнения для  основного цикла общения клиента и сервера
    private class ThreadConnection extends Thread {
        private Connection connection;
        private volatile boolean stopCicle = false;


        public ThreadConnection(Connection connection) {
            this.connection = connection;
        }

        public void mainCicle(Connection connection) {
            try {
                while (!stopCicle) {
                    Message message = connection.receive();
                    if (message.getMessageType() == MessageType.DISCONNECT || message.getMessageType() == MessageType.DEFEAT) {
                        sendMessageEnemy(message);
                        stopCicle = true;
                    } else if (message.getMessageType() == MessageType.MY_DISCONNECT) {
                        stopCicle = true;
                    } else {
                        sendMessageEnemy(message);
                    }
                }
                connection.close();
            } catch (Exception e) {
                View.callInformationWindow("Обмен выстрелами. Связь потеряна");
            }
        }

        private void sendMessageEnemy(Message message) throws IOException {
            for (Connection con : listConnection) {
                if (!connection.equals(con)) {
                    con.send(message);
                }
            }
        }

        @Override
        public void run() {
            mainCicle(connection);
        }
    }
}
