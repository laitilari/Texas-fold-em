/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.ai;

import java.util.ArrayList;
import java.util.List;
import tfe.core.cards.Card;
import tfe.core.game.Game;

/**
 *
 * @author ilarilai
 */
public class Ai {

    private List<Card> pocketCards;
    private List<Card> hand;
    private double chips;
    private boolean button;

    public Ai() {
        this.hand = new ArrayList<>();
        this.pocketCards = new ArrayList<>();
        this.button = true;
    }

    public List<Card> getHand(List<Card> tableCards) {
        this.hand.addAll(this.pocketCards);
        this.hand.addAll(tableCards);
        return this.hand;
    }

    public boolean premium() {
        Card first = pocketCards.get(0);
        Card second = pocketCards.get(1);
        if (first.getValue() >= 11 && first.getValue() == second.getValue()) {
            return true;
        }
        if (first.getValue() + second.getValue() >= 26) {
            return true;
        }
        return false;
    }
    
    public boolean medium() {
        
        return false;
    }

    public void action(List<Card> tableCards, List<Integer> bettingHistory,
            double pot, int bb) {
        Card first = pocketCards.get(0);
        Card second = pocketCards.get(1);
        int cardValue = first.getValue() + second.getValue();
        if (tableCards.isEmpty()) {
            if (getChips() >= 8 * bb) {
                if (bettingHistory.size() < 3) {
                    if (cardValue >= 16) {
                        bet(bb * 2.5);
                        System.out.println("AI bets " + bb * 2.5);
                    }
                } else if (bettingHistory.size() > 2) {
                    if (bettingHistory.get(bettingHistory.size()) < 3 * bb) {
                        if (cardValue >= 16) {
                            bet(bettingHistory.get(bettingHistory.size()
                                    - bettingHistory.get(bettingHistory.size()
                                            - 1)));
                        } else if (cardValue) {
                            
                        }
                    }
                }
            } else {
                allIn();
            }
        }
    }

    public void allIn() {
        bet(getChips());
        System.out.println("AI is goes all-in with " + getChips() + " chips!!!");
    }

    public void drawPocketCards(List<Card> pocketCards) {
        this.pocketCards.addAll(pocketCards);
    }

    public List<Card> getPocketCards() {
        return pocketCards;
    }

    public void betSmallBlind(int smallBlind) {
        if (this.chips - smallBlind >= 0) {
            this.chips -= smallBlind;
        } else {
            this.chips = 0;
        }
    }

    public void betBigBlind(int bigBlind) {
        if (this.chips - bigBlind >= 0) {
            this.chips -= bigBlind;
        } else {
            double allInWith = this.chips;
            System.out.println("You are all-in with " + allInWith + " chips");
            this.chips = 0;
        }
    }

    public double getChips() {
        return chips;
    }

    public void setChips(double chips) {
        this.chips = chips;
    }

    public void buttonChange() {
        if (this.button = false) {
            this.button = true;
        } else {
            this.button = false;
        }
    }

    public void winChips(double howMuch) {
        this.chips += howMuch;
    }

    public void bet(double bet) {
        if (this.chips - bet >= 0) {
            this.chips -= bet;
            if (this.chips > 0) {
                System.out.println("AI bets " + bet);
            } else {
                allIn();
            }
        }
    }

}
