/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.ai;

import java.util.ArrayList;
import java.util.List;
import tfe.core.cards.Card;

/**
 *
 * @author ilarilai
 */
public class Ai {

    private List<Card> pocketCards;
    private List<Card> hand;
    private int chips;
    private boolean button;

    public Ai() {
        this.hand = new ArrayList<>();
        this.pocketCards = new ArrayList<>();
    }

    public void drawPocketCards(List<Card> pocketCards) {
        this.pocketCards = pocketCards;
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
            int allInWith = this.chips;
            System.out.println("You are all-in with " + allInWith + " chips");
            this.chips = 0;
        }
    }

    public int getChips() {
        return chips;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    public void buttonChange() {
        if (this.button = false) {
            this.button = true;
        } else {
            this.button = false;
        }
    }

    public void winChips(int howMuch) {
        this.chips += howMuch;
    }

    public void bet(int bet) {
        if (this.chips - bet >= 0) {
            this.chips -= bet;
        }
    }

    public void action() {
        Card first = pocketCards.get(0);
        Card second = pocketCards.get(1);

    }

}
