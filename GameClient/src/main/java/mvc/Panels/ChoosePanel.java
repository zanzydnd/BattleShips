package mvc.Panels;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import mvc.*;

public class ChoosePanel extends Parent {
    private View view;

    private AnchorPane deckPane;
    private RadioButton oneDeck;
    private RadioButton fourDeck;
    private RadioButton twoDeck;
    private RadioButton threeDeck;
    private ToggleGroup deckGroup;

    private AnchorPane orientPane;
    private RadioButton vertical;
    private RadioButton horizontal;
    private ToggleGroup orientGroup;

    public ChoosePanel(View view) {
        this.view = view;
        deckPane = new AnchorPane();
        deckPane.setLayoutX(20);
        deckPane.setLayoutY(330);
        deckPane.setPrefSize(230, 130);

        deckGroup = new ToggleGroup();

        oneDeck = new RadioButton();
        oneDeck.setToggleGroup(deckGroup);
        setNameOneDeck(4);

        twoDeck = new RadioButton();
        twoDeck.setToggleGroup(deckGroup);
        setNameTwoDeck(3);

        threeDeck = new RadioButton();
        threeDeck.setToggleGroup(deckGroup);
        setNameThreeDeck(2);

        fourDeck = new RadioButton();
        fourDeck.setToggleGroup(deckGroup);
        setNameFourDeck(1);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(oneDeck, twoDeck, threeDeck, fourDeck);
        deckPane.getChildren().add(vbox);

        orientPane = new AnchorPane();
        orientPane.setLayoutX(13);
        orientPane.setLayoutY(190);
        orientPane.setPrefSize(230, 80);

        orientGroup = new ToggleGroup();

        vertical = new RadioButton("Вертикальная");
        horizontal = new RadioButton("Горизонтальная");

        vertical.setToggleGroup(orientGroup);
        horizontal.setToggleGroup(orientGroup);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(vertical, horizontal);
        orientPane.getChildren().addAll(vBox);

        getChildren().addAll(deckPane, orientPane);
    }

    public void setNameOneDeck(int count) {
        String text = "Однопалубный, осталось - " + count;
        oneDeck.setText(text);
    }

    public void setNameTwoDeck(int count) {
        String text = "Двухпалубный, осталось - " + count;
        twoDeck.setText(text);
    }

    public void setNameThreeDeck(int count) {
        String text = "Трехпалубный, осталось - " + count;
        threeDeck.setText(text);
    }

    public void setNameFourDeck(int count) {
        String text = "Четырехпалубный, осталось - " + count;
        fourDeck.setText(text);
    }

    //возвращает кол-во палуб в зависимости - какой радиоБаттон выбран
    public int getCountDeck() {
        if (oneDeck.isSelected()) return 1;
        else if (twoDeck.isSelected()) return 2;
        else if (threeDeck.isSelected()) return 3;
        else if (fourDeck.isSelected()) return 4;
        else return 0;
    }

    //возвращает число обозначающее какая ориентация корабля выбрана
    public boolean getPlacement() {
        if (vertical.isSelected()) return true;
        else if (horizontal.isSelected()) return false;
        else {
            return true;
        }
    }
}
