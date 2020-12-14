package mvc;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import mvc.Entities.Box;
import mvc.Entities.Ship;
import mvc.Panels.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bot {

    private Model model;
    Random random = new Random();
    Board board;
    Board myBoard;
    boolean turn = false;
    //память бота
    private int shipsCounter = 10;
    private int shipCounterPrev = 10;
    private Boolean vertical = null;
    private Box lastSuccessMove;
    private List<Box> successMoves = new ArrayList<>();

    //
    View view;

    public Bot(Board myBoard, View view){
        this.view = view;
        board =  new Board(true, click -> {
            Box box = (Box) click.getSource();

            if (box.wasShot)
                return;

            turn = !box.shoot();

            if (view.model.checkAllEnemyShipsAreDead()) {
                View.callInformationWindow("Вы выиграли!");
                view.exit.setVisible(true);
            }

            if (turn)
                enemyMove();
        });
        this.myBoard = myBoard;
    }

    public boolean botPlacingShips() {
        int fourDeck = 1;
        int threeDeck = 2;
        int twoDeck = 3;
        int oneDeck = 4;
        List<Ship> res = new ArrayList<>();
        while (fourDeck + threeDeck + twoDeck + oneDeck > 0) {
            if (fourDeck != 0) {
                int x = random.nextInt(10);
                int y = random.nextInt(10);
                Ship sh = new Ship(4, Math.random() < 0.5);
                if (board.placeShip(sh, x, y)) {
                    fourDeck--;
                    res.add(sh);
                }
            }
            if (threeDeck != 0) {
                int x = random.nextInt(10);
                int y = random.nextInt(10);
                Ship sh = new Ship(3, Math.random() < 0.5);
                if (board.placeShip(sh, x, y)) {
                    threeDeck--;
                    res.add(sh);
                }
            }
            if (twoDeck != 0) {
                int x = random.nextInt(10);
                int y = random.nextInt(10);
                Ship sh = new Ship(2, Math.random() < 0.5);
                if (board.placeShip(sh, x, y)) {
                    twoDeck--;
                    res.add(sh);
                }
            }
            if (oneDeck != 0) {
                int x = random.nextInt(10);
                int y = random.nextInt(10);
                Ship sh = new Ship(1, Math.random() < 0.5);
                if (board.placeShip(sh, x, y)) {
                    oneDeck--;
                    res.add(sh);
                }
            }
        }
        view.model.setAllShipsOfEnemy(res);
        return true;
    }

    private void enemyMove() {
        while (turn) {
            Box cell;
            int x, y;
            x = random.nextInt(10);
            y = random.nextInt(10);
            cell = myBoard.getBox(x, y);
            if (lastSuccessMove != null) {
                x = lastSuccessMove.x;
                y = lastSuccessMove.y;
                if (vertical == null) {
                    if (lastSuccessMove.x < 9 && lastSuccessMove.y < 9 && lastSuccessMove.y > 0 && lastSuccessMove.x > 0) {
                        if (!myBoard.getBox(lastSuccessMove.x + 1, lastSuccessMove.y).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x + 1, lastSuccessMove.y);
                        } else if (!myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y + 1).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y + 1);
                        } else if (!myBoard.getBox(lastSuccessMove.x - 1, lastSuccessMove.y).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x - 1, lastSuccessMove.y);
                        } else if (!myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y - 1).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y - 1);
                        }
                    } else if (lastSuccessMove.y < 9 && lastSuccessMove.x < 9 && lastSuccessMove.x > 0) {
                        if (!myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y + 1).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y + 1);
                        } else if (!myBoard.getBox(lastSuccessMove.x - 1, lastSuccessMove.y).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x - 1, lastSuccessMove.y);
                        } else if (!myBoard.getBox(lastSuccessMove.x + 1, lastSuccessMove.y).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x + 1, lastSuccessMove.y);
                        }
                    } else if (lastSuccessMove.y < 9 && lastSuccessMove.x < 9 && lastSuccessMove.y > 0) {
                        if (!myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y + 1).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y + 1);
                        } else if (!myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y - 1).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y - 1);
                        } else if (!myBoard.getBox(lastSuccessMove.x + 1, lastSuccessMove.y).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x + 1, lastSuccessMove.y);
                        }
                    } else if (lastSuccessMove.y < 9 && lastSuccessMove.x > 0 && lastSuccessMove.y > 0) {
                        if (!myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y + 1).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y + 1);
                        } else if (!myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y - 1).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y - 1);
                        } else if (!myBoard.getBox(lastSuccessMove.x - 1, lastSuccessMove.y).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x - 1, lastSuccessMove.y);
                        }
                    } else if (lastSuccessMove.x < 9 && lastSuccessMove.x > 0 && lastSuccessMove.y > 0) {
                        if (!myBoard.getBox(lastSuccessMove.x + 1, lastSuccessMove.y).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x + 1, lastSuccessMove.y);
                        } else if (!myBoard.getBox(lastSuccessMove.x - 1, lastSuccessMove.y).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x - 1, lastSuccessMove.y);
                        } else if (!myBoard.getBox(lastSuccessMove.x - 1, lastSuccessMove.y).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x - 1, lastSuccessMove.y);
                        }
                    } else if (lastSuccessMove.x < 9 && lastSuccessMove.y > 0) {
                        if (!myBoard.getBox(lastSuccessMove.x + 1, lastSuccessMove.y).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x + 1, lastSuccessMove.y);
                        } else if (!myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y - 1).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y - 1);
                        }
                    } else if (lastSuccessMove.y < 9 && lastSuccessMove.x > 0) {
                        if (!myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y + 1).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y + 1);
                        } else if (!myBoard.getBox(lastSuccessMove.x - 1, lastSuccessMove.y).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x - 1, lastSuccessMove.y);
                        }
                    }
                } else {
                    if (vertical) {
                        if (y > 0 && myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y - 1) != null &
                                !myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y - 1).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y - 1);
                        } else if (y < 9 && myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y + 1) != null
                                & !myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y + 1).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x, lastSuccessMove.y + 1);
                        } else if (successMoves.size() != 0) {
                            lastSuccessMove = successMoves.get(0);
                            continue;
                        }
                    }
                    if (!vertical) {
                        if (x > 0 && myBoard.getBox(lastSuccessMove.x - 1, lastSuccessMove.y) != null &
                                !myBoard.getBox(lastSuccessMove.x - 1, lastSuccessMove.y).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x - 1, lastSuccessMove.y);
                        } else if (x < 9 && myBoard.getBox(lastSuccessMove.x + 1, lastSuccessMove.y) != null
                                & !myBoard.getBox(lastSuccessMove.x + 1, lastSuccessMove.y).wasShot) {
                            cell = myBoard.getBox(lastSuccessMove.x + 1, lastSuccessMove.y);
                        } else if (successMoves.size() != 0) {
                            lastSuccessMove = successMoves.get(0);
                            continue;
                        }
                    }
                }
            }


            if (cell.wasShot) {
                continue;
            }


            turn = cell.shoot();

            if(cell.ship != null && !cell.ship.isAlive()){
                shipsCounter--;
            }

            if (turn) {
                if (lastSuccessMove != null & vertical == null) {
                    int y1 = lastSuccessMove.y;
                    int y2 = cell.y;
                    vertical = y1 != y2;
                }

                successMoves.add(cell);
                lastSuccessMove = cell;
                if (shipsCounter < shipCounterPrev) {
                    for (Box successMove : successMoves) {
                        if (successMove.x > 0) {
                            myBoard.getBox(successMove.x - 1, successMove.y).wasShot = true;
                        }
                        if (successMove.x < 9) {
                            myBoard.getBox(successMove.x + 1, successMove.y).wasShot = true;
                        }
                        if (successMove.y > 0) {
                            myBoard.getBox(successMove.x, successMove.y - 1).wasShot = true;
                        }
                        if (successMove.y < 9) {
                            myBoard.getBox(successMove.x, successMove.y + 1).wasShot = true;
                        }
                        if (successMove.y > 0 & successMove.x > 0) {
                            myBoard.getBox(successMove.x - 1, successMove.y - 1).wasShot = true;
                        }
                        if (successMove.y < 9 & successMove.x < 9) {
                            myBoard.getBox(successMove.x + 1, successMove.y + 1).wasShot = true;
                        }
                        if (successMove.y < 9 & successMove.x > 0) {
                            myBoard.getBox(successMove.x - 1, successMove.y + 1).wasShot = true;
                        }
                        if (successMove.y > 0 & successMove.x < 9) {
                            myBoard.getBox(successMove.x + 1, successMove.y - 1).wasShot = true;
                        }
                    }
                    shipCounterPrev--;
                    lastSuccessMove = null;
                    successMoves.clear();
                    vertical = null;
                }
            }

            if (view.model.checkAllPlayerShipsAreDead()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Вы проиграли");
                alert.setHeaderText(null);
                alert.setContentText("Вас выиграл наш бот");
                alert.showAndWait();
                view.exit.setVisible(true);
                board.deleteHandlers();
                break;
            }
        }
    }
}
