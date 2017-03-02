package tfe.core.player;

import java.util.ArrayList;
import java.util.List;
import tfe.core.cards.Card;

/**
 * Luokasta tehty olio esittää pelin pelaajaa, jolla on muun muassa käsikortit
 * ja pelimerkit.
 */
public class Player {

    private List<Card> pocketCards;
    private List<Card> hand;
    private double chips;
    private boolean button;

    /**
     * Valmistelee ja luo pelaaja-olion. Alustaa käden ja taskukortit sekä
     * pelimerkit ja asettaa pelaajan early positioon olion luonnin yhteydessä.
     *
     */
    public Player() {
        this.hand = new ArrayList<>();
        this.pocketCards = new ArrayList<>();
        this.button = false;
        this.chips = 0.0;
    }

    /**
     * Peliposition vaihtava metodi.
     */
    public void buttonChange() {
        if (this.button == false) {
            this.button = true;
        } else {
            this.button = false;
        }
    }

    /**
     * Panostusmetodi. Pelimerkit eivät voi mennä alle nollan.
     *
     * @param bet panostuksen määrä
     * @return bet amount
     */
    public double bet(double bet) {
        if (this.chips - bet >= 0) {
            this.chips -= bet;
            return bet;
        } else {
            this.chips = 0;
            return bet - this.chips;
        }
    }

    /**
     * Panostaa pienen blindin.
     *
     * @param smallBlind puolet isosta blindista
     */
    public void betSmallBlind(double smallBlind) {
        if (this.chips - smallBlind >= 0) {
            this.chips -= smallBlind;
        } else {
            this.chips = 0;
        }
    }

    /**
     * Panostaa ison blindin.
     *
     * @param bigBlind ennaltamääritetty 30 pelimerkkiä
     */
    public void betBigBlind(double bigBlind) {
        if (this.chips - bigBlind >= 0) {
            this.chips -= bigBlind;
        } else {
            this.chips = 0;
        }
    }

    /**
     * Voittaa pelimerkkejä itselleen.
     *
     * @param howMuch määrä
     */
    public void winChips(double howMuch) {
        this.chips += howMuch;
    }

    /**
     * Lisää pelaajalle jaetut taskukortit omaan listaan.
     *
     * @param pocketCards dealerin jakamat satunnaiset kaksi korttia
     */
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

    /**
     * Is button.
     * @return boolean
     */
    public boolean isButton() {
        return button;
    }
    
    public List<Card> getHand(List<Card> tableCards) {
        this.pocketCards.addAll(tableCards);
        return this.pocketCards;
    }

}
