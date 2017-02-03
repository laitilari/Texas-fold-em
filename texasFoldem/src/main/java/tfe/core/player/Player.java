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
    private double chips;
    private boolean button;

    public Player() {
        this.hand = new ArrayList<>();
        this.pocketCards = new ArrayList<>();
        this.button = false;
        this.chips = 0.0;
    }

    public void bet(double bet) {
        if (this.chips - bet >= 0) {
            this.chips -= bet;
        } else {
            this.chips = 0;
        }
    }

    public void betSmallBlind(double smallBlind) {
        if (this.chips - smallBlind >= 0) {
            this.chips -= smallBlind;
        } else {
            this.chips = 0;
        }
    }

    public void betBigBlind(double bigBlind) {
        if (this.chips - bigBlind >= 0) {
            this.chips -= bigBlind;
        } else {
            double allInWith = this.chips;
            System.out.println("You are all-in with " + allInWith + " chips");
            this.chips = 0;
        }
    }

    public void winChips(double howMuch) {
        this.chips += howMuch;
    }

    public void drawPocketCards(List<Card> pocketCards) {
        this.pocketCards.addAll(pocketCards);
    }

    public List<Card> getPocketCards() {
        return this.pocketCards;
    }

    public double getChips() {
        return chips;
    }

    public void setChips(double chips) {
        this.chips = chips;
    }

    public boolean isButton() {
        return button;
    }

    public void buttonChange() {
        if (this.button == false) {
            this.button = true;
        } else {
            this.button = false;
        }
    }

}
