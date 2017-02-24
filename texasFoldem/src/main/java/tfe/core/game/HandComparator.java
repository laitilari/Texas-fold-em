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

    /**
     * Konstruktori.
     *
     */
    public HandComparator() {
    }

    /**
     * Pari.
     *
     * @param hand käsi
     * @return totuusarvo
     */
    public boolean pair(List<Card> hand) {
        return sameConsecutiveValues(hand) == 2;
    }

    /**
     * Kolmoset.
     *
     * @param hand käsi
     * @return totuusarvo
     */
    public boolean trips(List<Card> hand) {
        return sameConsecutiveValues(hand) == 3;
    }

    /**
     * Neloset.
     *
     * @param hand käsi
     * @return totuusarvo
     */
    public boolean quads(List<Card> hand) {
        return sameConsecutiveValues(hand) == 4;
    }

    /**
     * Suora.
     *
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
     *
     * @param hand käsi
     * @return totuusarvo
     */
    public boolean flush(List<Card> hand) {
        int hearts = 0;
        int spades = 0;
        int diamonds = 0;
        int clubs = 0;
        for (Card card : hand) {
            switch (card.getSuit()) {
                case "Hearts":
                    hearts++;
                case "Spades":
                    spades++;
                case "Diamonds":
                    diamonds++;
                case "Clubs":
                    clubs++;
            }
        }
        return hearts == 5 || spades == 5 || diamonds == 5 || clubs == 5;
    }

    /**
     * Värisuora.
     *
     * @param hand käsi
     * @return totuusarvo
     */
    public boolean straightFlush(List<Card> hand) {
        return straight(hand, 0, 4) && flush(hand) || straight(hand, 1, 5) && flush(hand)
                || straight(hand, 2, 6);
    }

    /**
     * Täyskäsi.
     *
     * @param hand käsi
     * @return totuusarvo
     */
    public boolean fullHouse(List<Card> hand) {
        int[] ints = cardsToIntArray(hand);
        System.out.println(Arrays.toString(ints));
        if (sameConsecutiveValues(hand) == 4) {
            int toBeRemoved = fullHouseHelperMethod(ints);
            cardRemover(hand, toBeRemoved);
        }
        return sameConsecutiveValues(hand) == 2;
    }

    /**
     * Tarkistaa, onko kädessä kolmoset.
     *
     * @param ints array of values
     * @return value to be removed
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
     * sameConseceutiveValues metodille, joka tarkistaa, onko kädessä vielä
     * pari.
     *
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
     *
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
     *
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
    
    /**
     * Checks if the hand has pair or better values.
     * @param hand list of cards to be checked
     * @return true or false
     */
    public boolean pairOrBetter(List<Card> hand) {
        if (hand.size() == 7) {
            if (pair(hand) || trips(hand) || quads(hand) || flush(hand)
                    || straight(hand, 0, 4) || straight(hand, 1, 5) || straight(hand, 2, 6)
                    || straightFlush(hand)) {
                return true;
            }
        } else if (hand.size() == 6) {
            if (pair(hand) || trips(hand) || quads(hand) || flush(hand)
                    || straight(hand, 0, 4) || straight(hand, 1, 5) || straight(hand, 2, 6)
                    || straightFlush(hand)) {
                return true;
            }
        }
        return pair(hand) || trips(hand) || quads(hand) || flush(hand)
                || straight(hand, 0, 4) || straightFlush(hand);
    }
}
