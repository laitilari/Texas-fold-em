package tfe.core.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import tfe.core.cards.Card;

/**
 * Arvottaa kädet ja määrittää paremmuuden.
 */

public class HandComparator {

    private List<Card> tableCards;
    private List<Card> playerHand;
    private List<Card> aiHand;

    public HandComparator(List<Card> playerPocketCards, List<Card> aiPocketCards, List<Card> tableCards) {
        this.playerHand = new ArrayList<>();
        this.aiHand = new ArrayList<>();
        playerHand.addAll(playerPocketCards);
        playerHand.addAll(tableCards);
        aiHand.addAll(playerPocketCards);
        aiHand.addAll(tableCards);
    }

    /**
     * Pari.
     */
    public boolean pair(List<Card> hand) {
        if (sameConsecutiveValues(hand) == 2) {
            return true;
        }
        return false;
    }

    /**
     * Kolmoset.
     */
    public boolean trips(List<Card> hand) {
        if (sameConsecutiveValues(hand) == 3) {
            return true;
        }
        return false;
    }

    /**
     * Neloset.
     */
    public boolean quads(List<Card> hand) {
        if (sameConsecutiveValues(hand) == 4) {
            return true;
        }
        return false;
    }

    /**
     * Suora
     * @param hand käsi jota tarkastellaan.
     * @param x alkukohta
     * @param y päätekohta
     * @return true tai false
     */
    
    public boolean straight(List<Card> hand, int x, int y) {
        int[] ints = cardsToIntArray(hand);
        int prev = -1;
        for (int i = x; i < y; i++) {
            if (prev == -1 || (prev + 1) == ints[i]) {
                prev = ints[i];
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Laskee, onko kädessä 5 samaa väriä.
     */
    public boolean flush(List<Card> hand) {
        int hearts = 0;
        int spades = 0;
        int diamonds = 0;
        int clubs = 0;
        for (Card card : hand) {
            if (card.getSuit().equals("Hearts")) {
                hearts++;
            } else if (card.getSuit().equals("Spades")) {
                spades++;
            } else if (card.getSuit().equals("Diamonds")) {
                diamonds++;
            } else {
                clubs++;
            }
        }
        if (hearts == 5 || spades == 5 || diamonds == 5 || clubs == 5) {
            return true;
        }
        return false;
    }

    /**
     * Värisuora.
     */
    public boolean straightFlush(List<Card> hand) {
        if (straight(hand, 0, 4) && flush(hand) || straight(hand, 1, 5) && flush(hand)
                || straight(hand, 2, 6)) {
            return true;
        }
        return false;
    }

    /**
     * Täyskäsi.
     */
    public boolean fullHouse(List<Card> hand) {
        int[] ints = cardsToIntArray(hand);
        System.out.println(Arrays.toString(ints));
        if (sameConsecutiveValues(hand) == 4) {
            int toBeRemoved = fullHouseHelperMethod(ints);
            cardRemover(hand, toBeRemoved);
        }
        ints = cardsToIntArray(hand);
        if (sameConsecutiveValues(hand) == 2) {
            return true;
        }
        return false;
    }
    
    /**
     * Tarkistaa, onko kädessä kolmoset.
     */

    public int fullHouseHelperMethod(int[] ints) {
        int toBeRemoved = 0;
        if (ints[0] == ints[2]) {
            toBeRemoved = ints[0];
        } else if (ints[1] == ints[3]) {
            toBeRemoved = ints[1];
        } else if (ints[2] == ints[4]) {
            toBeRemoved = ints[2];
        } else if (ints[3] == ints[5]) {
            toBeRemoved = ints[3];

        } else if (ints[4] == ints[6]) {
            toBeRemoved = ints[4];

        } else if (ints[5] == ints[7]) {
            toBeRemoved = ints[5];
        }
        return toBeRemoved;
    }

    /**
     * Poistaa kädestä kolmoset, jotta voidaan syöttää jäljellä olevat kortit
     * sameConseceutiveValues metodille, joka tarkistaa, onko kädessä vielä pari.
     * @param hand käsi josta poistetaan.
     * @param toBeRemoved korttien arvo, jonka perusteella kortit poistetaan.
     * @return käsi, josta poistettu tietyn arvoiset kortit.
     */
    public List<Card> cardRemover(List<Card> hand, int toBeRemoved) {
        Iterator<Card> i = hand.iterator();
        while (i.hasNext()) {
            Card c = i.next();
            if (c.getValue() == toBeRemoved) {
                i.remove();
            }
        }
        return hand;
    }

    /**
     * Muuntaa List-tyyppisen käden korttien arvot Arrayksi.
     * @param hand käsi, joka muutetaan.
     * @return listan korttien arvoja.
     */
    public int[] cardsToIntArray(List<Card> hand) {
        int[] values = new int[hand.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = hand.get(i).getValue();
        }
        Arrays.sort(values);
        return values;
    }

    /**
     * Tarkistaa, kuinka monta samaa, arvoltaan peräkkäistä korttia kädessä on.
     * @param hand käsi, jota tarkastellaan.
     * @return lukumäärä samoista arvoista.
     */
    public int sameConsecutiveValues(List<Card> hand) {
        int[] ints = cardsToIntArray(hand);
        int prev = 0;
        int counter = 1;
        for (int i = 0; i < ints.length; i++) {
            if (prev == ints[i]) {
                counter++;
            }
            prev = ints[i];
        }
        return counter;
    }

}
