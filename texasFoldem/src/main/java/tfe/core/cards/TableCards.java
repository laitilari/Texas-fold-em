package tfe.core.cards;

import java.util.ArrayList;
import java.util.List;

/**
 * Pöytäkortit.
 */
public class TableCards {

    private List<Card> cards;

    public TableCards() {
        this.cards = new ArrayList<>();
    }

    /**
     * Lisää itseensä flopin verran kortteja (3).
     * @param flop floppikortit
     */
    public void drawFlop(List<Card> flop) {
        this.cards.addAll(flop);
    }

    /**
     * Lisää itseensä yhden kortin. Käytetään turnilla ja riverillä.
     * @param card 
     */
    public void drawCard(Card card) {
        this.cards.add(card);
    }

    public List<Card> getCards() {
        return this.cards;
    }

}
