package tfe.core.cards;

/**
 *
 * @author ilarilai
 */
public class Card {

    private String suit;
    private int value;

    public Card(String suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    public String toString() {
        switch (this.value) {
            case 11:
                return "Jack of " + this.suit;
            case 12:
                return "Queen of " + this.suit;
            case 13:
                return "King of " + this.suit;
            case 14:
                return "Ace of " + this.suit;
        }
        return this.value + " of " + this.suit;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
