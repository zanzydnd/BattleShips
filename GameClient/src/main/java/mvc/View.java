package mvc;
import Main.Main;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mvc.Connection.Message;
import mvc.Entities.Box;
import mvc.Entities.Ship;
import mvc.Panels.Board;
import mvc.Panels.ChoosePanel;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class View extends Parent {
    protected Model model;
    private Controller controller;
    private Board myBoard;
    private Board enemyBoard;
    private ChoosePanel onPanel;
    private TextField turnField;
    private boolean running = false;
    private Button leave;
    public Button exit = new Button("Выйти");


    public View() {
        exit.setOnAction(event -> {
            Main main = new Main();
            try {
                main.start(Main.primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        exit.setVisible(false);
        onPanel = new ChoosePanel(this);
        myBoard = new Board(false, click -> {
            if (running)
                return;

            Box box = (Box) click.getSource();
            Ship sCheck = new Ship(onPanel.getCountDeck(), onPanel.getPlacement());
            if (click.getButton() == MouseButton.PRIMARY) {
                if (myBoard.canPlaceShip(sCheck, box.x, box.y) &&
                        model.checkIsThereEnoughShips(sCheck)
                ) {
                    myBoard.placeShip(sCheck, box.x, box.y);
                    changeCountShipOnChoosePanel(sCheck.getCountDeck());
                }
            } else if (click.getButton() == MouseButton.SECONDARY) {
                Box clickedBox = (Box) click.getSource();
                changeCountShipOnChoosePanel1(clickedBox.ship.getCountDeck());
                controller.deleteShip(clickedBox.ship);
            }
        });

        Button button = new Button("Я готов");
        button.setOnAction(a -> {
            model.setPlayerBoard(myBoard);

            Bot bot = new Bot(myBoard,this);

            if (controller.checkIfAllShipsArePlaced()) {
                bot.botPlacingShips();
                onPanel.setVisible(false);
                button.setVisible(false);
                getChildren().clear();
                exit.setVisible(true);
                VBox vb = new VBox(myBoard, bot.board,exit);
                getChildren().addAll(vb);
            } else {
                callInformationWindow("Вы не расставили все корабли");
            }
        });

        Button online = new Button("online");
        online.setOnAction(action -> {
            startOnlineGame();
        });
        HBox vb = new HBox(myBoard, onPanel, button, online);
        this.getChildren().add(vb);
    }


    public void changeCountShipOnChoosePanel(int countDeck) {
        switch (countDeck) {
            case 1: {
                onPanel.setNameOneDeck(4 - model.getShipsOneDeck().size());
                break;
            }
            case 2: {
                onPanel.setNameTwoDeck(3 - model.getShipsTwoDeck().size());
                break;
            }
            case 3: {
                onPanel.setNameThreeDeck(2 - model.getShipsThreeDeck().size());
                break;
            }
            case 4: {
                onPanel.setNameFourDeck(1 - model.getShipsFourDeck().size());
                break;
            }
        }
    }


    public void changeCountShipOnChoosePanel1(int countDeck) {
        switch (countDeck) {
            case 1: {
                onPanel.setNameOneDeck(5 - model.getShipsOneDeck().size());
                break;
            }
            case 2: {
                onPanel.setNameTwoDeck(4 - model.getShipsTwoDeck().size());
                break;
            }
            case 3: {
                onPanel.setNameThreeDeck(3 - model.getShipsThreeDeck().size());
                break;
            }
            case 4: {
                onPanel.setNameFourDeck(2 - model.getShipsFourDeck().size());
                break;
            }
        }
    }


    public static void callInformationWindow(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(s);
        alert.showAndWait();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
    }


    //метод , который создает комнату
    public void startOnlineGame() {
        if (controller.checkIfAllShipsArePlaced()) {
            StackPane pane = new StackPane();
            Button createRoomButton = new Button("Создать свою комнату");
            Button connectToRoom = new Button("Присоедениться к существуещей комнате");
            TextField portPlacement = new TextField();
            Label info = new Label("Создайте комнату, введя 4-ех значный номер комнаты,\n" +
                    "либо подключитесь к уже созданной:");
            createRoomButton.setOnAction(action -> {
                try {
                    model.setPlayerBoard(myBoard);
                    //View.callInformationWindow("Ожидаем соперника: после того как соперник подключиться к комнате, появится уведомление. Затем начнется игра. Ваш ход первый.");
                    controller.createRoom();
                    refreshGuiAfterConnect();
                    turnField.setText("ВАШ ХОД");
                    View.callInformationWindow("Второй игрок подключился! Можно начинать сражение.");
                } catch (IOException | ClassNotFoundException e) {
                    View.callInformationWindow("Произошла ошибка при создании комнаты, либо некорректный номер комнаты, попробуйте еще раз.");
                }
            });
            connectToRoom.setOnAction(action -> {
                try {
                    model.setPlayerBoard(myBoard);
                    System.out.println("Второй");
                    System.out.println(portPlacement.getText());
                    String code = portPlacement.getText();
                    controller.connectToRoom(code);
                    View.callInformationWindow("Вы успешно подключились к комнате. Ваш соперник ходит первым.");
                    refreshGuiAfterConnect();
                    enemyBoard.deleteHandlers();
                    turnField.setText("ХОД СОПЕРНИКА");
                    new ReceiveThread().start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            leave = new Button("Прервать и выйти");
            leave.setOnAction(e -> {
                try {
                    controller.disconnectGameRoom();
                    Main main = new Main();
                    main.start(Main.primaryStage);
                } catch (Exception ioException) {
                    ioException.printStackTrace();
                }
            });
            leave.setDisable(true);
            VBox gridPane = new VBox();
            gridPane.getChildren().addAll(createRoomButton, connectToRoom, portPlacement, info);
            pane.getChildren().addAll(gridPane);

            Scene scene = new Scene(pane, 400, 200);
            Stage stage = new Stage();
            stage.setTitle("Коннект");
            stage.setScene(scene);
            stage.setResizable(false);

            stage.show();
        } else {
            View.callInformationWindow("Вы добавили не все корабли на своем поле!");
        }
    }

    public void refreshGuiAfterConnect() {
        getChildren().clear();
        turnField = new TextField();
        turnField.setEditable(false);
        VBox hbox = new VBox();
        myBoard.deleteHandlers();
        enemyBoard = model.getEnemyBoard();
        enemyBoard.paintBoard(click -> {
            Box box = (Box) click.getSource();
            sendShot(box.x, box.y);
        });
        hbox.getChildren().addAll(myBoard, enemyBoard, turnField,leave,exit);
        getChildren().addAll(hbox);
    }

    //МЕТОДЫ ДЛЯ СЕРВЕРА
    public void sendShot(int x, int y) {
        try {
            boolean isSendShot = controller.sendMessage(x, y); //непосредственная отправка сообщения через контроллер
            if (isSendShot) { //если сообщение отправлено, то ...
                enemyBoard.deleteHandlers();
                turnField.setText("ХОД СОПЕРНИКА");
                leave.setDisable(true);
                new ReceiveThread().start(); //запуск нити, которая ожидает сообщение от сервера
            }
        } catch (Exception e) {
            View.callInformationWindow("Произошла ошибка при отправке выстрела.");
            e.printStackTrace();
        }
    }

    //класс поток ждущий ответа от сервера
    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            try {
                Message continueGame = controller.receiveMessage(); //контроллер принял сообщение
                if (!continueGame.isEndgame()) {
                    if (continueGame.getYourTurn()) {
                        enemyBoard.setHandler(e -> {
                            Box box = (Box) e.getSource();
                            sendShot(box.x, box.y);
                        });
                        leave.setDisable(false);
                        turnField.setText("ВАШ ХОД");
                    } else {
                        turnField.setText("ХОД СОПЕРНИКА");
                        this.run();
                    }
                    //panelButtons.getExitButton().setEnabled(true); //активация кнопки выход
                } else {
                    if(continueGame.isWin()){
                        turnField.setText("Вы одержали победу");
                        getChildren().remove(leave);
                        exit.setVisible(true);
                    }
                    else{
                        turnField.setText("Вы проиграли");
                        getChildren().remove(leave);
                        exit.setVisible(true);
                    }
                    enemyBoard.deleteHandlers();
                }
            } catch (IOException | ClassNotFoundException e) {
                View.callInformationWindow("Произошла ошибка при приеме сообщения от сервера");
                e.printStackTrace();
            }
        }
    }

}

