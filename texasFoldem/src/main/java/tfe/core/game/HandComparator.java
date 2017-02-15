package tfe.core.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import tfe.core.cards.Card;

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

    public boolean pair(List<Card> hand) {
        if (sameConsecutiveValues(hand) == 2) {
            return true;
        }
        return false;
    }

    public boolean trips(List<Card> hand) {
        if (sameConsecutiveValues(hand) == 3) {
            return true;
        }
        return false;
    }

    public boolean quads(List<Card> hand) {
        if (sameConsecutiveValues(hand) == 4) {
            return true;
        }
        return false;
    }

    public boolean fullHouse(List<Card> hand) {
        if (trips(hand)) {
            Collections.reverse(hand);
            if (pair(hand)) {
            return true;
            }
        }
        return false;
    }

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
    
    public boolean straightFlush(List<Card> hand) {
        if (straight(hand, 0, 4) && flush(hand) || straight(hand, 1, 5) && flush(hand)
        ||straight(hand, 2,6)) {
            return true;
        }
        return false;
    }

    public void compare() {

    }

    public int[] cardsToIntArray(List<Card> hand) {
        int[] values = new int[hand.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = hand.get(i).getValue();
        }
        Arrays.sort(values);
        return values;
    }

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
