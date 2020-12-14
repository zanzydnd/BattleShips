package mvc;


import mvc.Entities.Box;
import mvc.Entities.Ship;
import mvc.Panels.Board;

import java.util.ArrayList;
import java.util.List;

public class Model {
    protected Board enemyBoard;
    protected Board playerBoard;
    protected List<Ship> shipsOneDeck = new ArrayList<>(); //список всех наших однопалубных кораблей
    protected List<Ship> shipsTwoDeck = new ArrayList<>(); //список всех наших двухпалубных кораблей
    protected List<Ship> shipsThreeDeck = new ArrayList<>(); //список всех наших трехпалубных кораблей
    protected List<Ship> shipsFourDeck = new ArrayList<>(); //список всех наших четырехпалубных кораблей
    protected List<Ship> allShipsOfEnemy = new ArrayList<>(); //список всех кораблей соперниа


    public void setAllShipsOfEnemy(List<Ship> allShipsOfEnemy) {
        this.allShipsOfEnemy = allShipsOfEnemy;
    }


    public Board getEnemyBoard() {
        return enemyBoard;
    }


    public void setEnemyBoard(Board enemyBoard) {
        this.enemyBoard = enemyBoard;
    }

    public void setPlayerBoard(Board playerBoard) {
        this.playerBoard = playerBoard;
    }

    public Board getPlayerBoard() {
        return playerBoard;
    }

    public List<Ship> getShipsOneDeck(){
        return shipsOneDeck;
    }

    public List<Ship> getShipsFourDeck() {
        return shipsFourDeck;
    }

    public List<Ship> getShipsThreeDeck() {
        return shipsThreeDeck;
    }

    public List<Ship> getShipsTwoDeck() {
        return shipsTwoDeck;
    }

    public List<Ship> getAllShips() {
        List<Ship> allBoxesOfShips = new ArrayList<>();
        allBoxesOfShips.addAll(shipsFourDeck);
        allBoxesOfShips.addAll(shipsThreeDeck);
        allBoxesOfShips.addAll(shipsTwoDeck);
        allBoxesOfShips.addAll(shipsOneDeck);
        return allBoxesOfShips;
    }

    public List<Box> getAllBoxesOfShips() {
        List<Box> allBoxes = new ArrayList<>();
        List<Ship> allShips = getAllShips();
        for (Ship ship : allShips) {
            allBoxes.addAll(ship.getBoxesOfShip());
        }
        return allBoxes;
    }

    public boolean checkAllPlayerShipsAreDead(){
        for(Ship ship : getAllShips()){
            if(ship.isAlive())
                return false;
        }
        return true;
    }

    public boolean checkAllEnemyShipsAreDead(){
        for(Ship ship : allShipsOfEnemy){
            if(ship.isAlive())
                return false;
        }
        return true;
    }

    public boolean removeShip(Ship ship){
        if(shipsOneDeck.contains(ship)){
            shipsOneDeck.remove(ship);
            return true;
        }
        else if(shipsTwoDeck.contains(ship)){
            shipsTwoDeck.remove(ship);
            return true;
        }else if(shipsThreeDeck.contains(ship)){
            shipsThreeDeck.remove(ship);
            return true;
        }else if(shipsFourDeck.contains(ship)){
            shipsFourDeck.remove(ship);
            return true;
        }
        return false;
    }

    public boolean checkIsThereEnoughShips(Ship ship) {
        int countDeck = ship.getCountDeck();
        switch (countDeck) {
            case 1: {
                if (shipsOneDeck.size() < 4) {
                    shipsOneDeck.add(ship);
                    return true;
                } else {
                    View.callInformationWindow("Перебор однопалубных. Максимально возможно - 4.");
                    return false;
                }
            }
            case 2: {
                if (shipsTwoDeck.size() < 3) {
                    shipsTwoDeck.add(ship);
                    return true;
                } else {
                    View.callInformationWindow("Перебор двухпалубных. Максимально возможно - 3.");
                    return false;
                }
            }
            case 3: {
                if (shipsThreeDeck.size() < 2) {
                    shipsThreeDeck.add(ship);
                    return true;
                } else {
                    View.callInformationWindow("Перебор трехпалубных. Максимально возможно - 2.");
                    return false;
                }
            }
            case 4: {
                if (shipsFourDeck.size() < 1) {
                    shipsFourDeck.add(ship);
                    return true;
                } else {
                    View.callInformationWindow("Четырехпалубный уже добавлен. Максимально возможно - 1.");
                    return false;
                }
            }
        }
        return false;
    }

}
