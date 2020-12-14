package Connection;
import mvc.Entities.*;
import mvc.Panels.*;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {

    private int x;
    private int y;
    private Board board;
    private MessageType type;
    private List<Ship> listOfAllShips;
    private boolean yourTurn;
    private boolean endgame;
    private boolean win;
    private String code;

    public Message(MessageType messageType,int x, int y) {
        this.x = x;
        this.y = y;
        this.type = messageType;
    }

    public String getCode(){
        return code;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public Message(MessageType msType,String code){
        this.type = msType;
        this.code = code;
    }

    public Message(MessageType messageType, Board board, List<Ship> list) {
        this.x = x;
        this.y = y;
        this.board = board;
        this.listOfAllShips = list;
        this.type = messageType;
    }


    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
    }

    public void setEndgame(boolean endgame) {
        this.endgame = endgame;
    }

    public Message(MessageType type){
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean getYourTurn(){
        return yourTurn;
    }

    public boolean isEndgame() {
        return endgame;
    }

    public MessageType getMessageType() {
        return type;
    }

    public Board getGameField() {
        return board;
    }

    public List<Ship> getListOfAllShips() {
        return listOfAllShips;
    }
}
