package tfe.core.game;

import java.util.ArrayList;
import java.util.List;
import tfe.core.cards.Card;

public class HandComparator {

    private List<Card> playerPocketCards;
    private List<Card> aiPocketCards;
    private List<Card> tableCards;

    public HandComparator(List<Card> playerPocketCards, List<Card> aiPocketCards, List<Card> tableCards) {
        this.playerPocketCards = playerPocketCards;
        this.aiPocketCards = aiPocketCards;
        this.tableCards = tableCards;
    }

    public boolean flush() {
        int hearts = 0;
        int spades = 0;
        int diamonds = 0;
        int clubs = 0;
        for (Card card : tableCards) {
            if (card.getSuit().equals(hearts)) {
                hearts++;
            } else if (card.getSuit().equals(spades)) {
                spades++;
            } else if (card.getSuit().equals(diamonds)) {
                diamonds++;
            } else {
                clubs++;
            }
        }
        if (hearts == 5 || spades == 5 || diamonds == 5 || clubs == 5) {
            // tee kuitenkin niin, että käyt läpi pelaajan kortit + tablecardit kokoelmaa. 
        }
        return true;
    }

    public void compare() {

    }

}
