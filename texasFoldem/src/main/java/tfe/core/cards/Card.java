package tfe.core.cards;

/**
 * Korttiluokka.
 */
public class Card {

    private String suit;
    private int value;

    /**
     * Konstruktori.
     *
     * @param suit suit
     * @param value value
     */
    public Card(String suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    /**
     * Palauttaa kortin tiedot. Huomioi erityistapauksina kuvakortit.
     *
     * @return tekstiesitys kortin arvosta ja maasta.
     */
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
