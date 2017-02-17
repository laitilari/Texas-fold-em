package tfe.core.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Korttipakka.
 */
public class PackOfCards {

    private List<Card> cards;
    private List<Card> removedCards;

    public PackOfCards() {
        this.cards = new ArrayList<>();
        this.removedCards = new ArrayList<>();
    }

    /**
     * Ottaa yhden kortin pakasta.
     */
    public Card takeOne() {
        Card card = this.cards.get(0);
        this.cards.remove(0);
        remove(card);
        return card;
    }

    /**
     * Ottaa monta korttia.
     * @param howMany montako
     * @return otetut kortit.
     */
    public List<Card> takeMany(int howMany) {
        List<Card> manyCards = new ArrayList<>();
        for (int i = 0; i < howMany; i++) {
            manyCards.add(takeOne());
        }
        return manyCards;
    }

    /**
     * Lisää pakasta nostetut kortit takaisin pakkaan.
     */
    public void reAssemblePack() {
        cards.addAll(removedCards);
        removedCards.clear();
    }

    public void remove(Card card) {
        removedCards.add(card);
    }

    /**
     * Muodostaa korttipakan.
     */
    public void assemblePack() {
        ArrayList<String> suits = new ArrayList<>();
        suits.add("Spades");
        suits.add("Clubs");
        suits.add("Hearts");
        suits.add("Diamonds");
        int suitNumber = 0;
        for (int i = 0; i < 4; i++) {
            String suit = suits.get(suitNumber);
            for (int j = 2; j < 15; j++) {
                Card card = new Card(suit, j);
                cards.add(card);
            }
            suitNumber++;
        }
    }

    /**
     * Sekoittaa pakan.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    public List<Card> getCards() {
        return cards;
    }

    public List<Card> getRemovedCards() {
        return removedCards;
    }
}
