/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.player;

import java.util.ArrayList;
import java.util.List;
import tfe.core.cards.Card;

/**
 *
 * @author ilarilai
 */
public class Player {

    private List<Card> pocketCards;
    private List<Card> hand;
    private int chips;
    private boolean button;

    public Player() {
        this.hand = new ArrayList<>();
        this.button = false;
    }

    public void bet(int bet) {
        if (this.chips - bet >= 0) {
            this.chips -= bet;
        } else {
            System.out.println("Dealer: you don't have that many chips!");
        }
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

    public void winChips(int howMuch) {
        this.chips += howMuch;

    }

    public void drawPocketCards(List<Card> pocketCards) {
        this.pocketCards = pocketCards;
    }

    public List<Card> getPocketCards() {
        return this.pocketCards;
    }

    public int getChips() {
        return chips;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    public boolean isButton() {
        return button;
    }

    public void buttonChange() {
        if (this.button = false) {
            this.button = true;
        } else {
            this.button = false;
        }
    }

}
