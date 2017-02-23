package tfe.core.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import tfe.core.cards.Card;
import tfe.core.game.HandComparator;

/**
 * Tämä luokka vastaa pelin tekoälystä. Luokka suorittaa metodeissa
 * määriteltyjen sääntöjen ja metodien parametreina tulevien tietojen
 * perusteella tekoälyn valinnat pelissä.
 */
public class Ai {

    private List<Card> pocketCards;
    private double chips;
    private boolean button;
    private Random random;

    /**
     * Constructor.
     */
    public Ai() {
        this.pocketCards = new ArrayList<>();
        this.button = true;
        this.random = new Random();
    }

    /**
     * Jos kortit ovat erittäin hyvät.
     *
     * @return totuusarvo
     */
    public boolean premium() {
        Card first = pocketCards.get(0);
        Card second = pocketCards.get(1);
        if (first.getValue() >= 11 && first.getValue() == second.getValue()) {
            return true;
        }
        return first.getValue() + second.getValue() >= 26;
    }

    /**
     * Jos kortit ovat hyvät.
     *
     * @return totuusarvo
     */
    public boolean good() {
        Card first = pocketCards.get(0);
        Card second = pocketCards.get(1);
        if (first.getValue() >= 9 && first.getValue() <= 10
                && first.getValue() == second.getValue()) {
            return true;
        }
        return first.getValue() + second.getValue() >= 22
                && first.getValue() + second.getValue() <= 25;
    }

    /**
     * Jos kortit ovat menettelevät.
     *
     * @return totuusarvo
     */
    public boolean medium() {
        Card first = pocketCards.get(0);
        Card second = pocketCards.get(1);
        if (first.getValue() >= 5 && first.getValue() <= 8
                && first.getValue() == second.getValue()) {
            return true;
        }
        if (first.getValue() >= 13 || second.getValue() >= 13) {
            return true;
        }
        return first.getValue() + second.getValue() >= 15;
    }

    /**
     * Onko preflop? Eli onko pöytäkortit tyhjä.
     *
     * @param tableCards pöytäkortit
     * @return totuusarvo.
     */
    public boolean preFlop(List<Card> tableCards) {
        return tableCards.isEmpty();
    }

    /**
     * Onko iso stack?
     *
     * @param bigBlind bb
     * @return totuusarvo
     */
    public boolean healthyStack(double bigBlind) {
        return getChips() >= 20 * bigBlind;
    }

    /**
     * Onko pieni stack?
     *
     * @param bigBlind bb
     * @return totuusarvo
     */
    public boolean dangeredStack(double bigBlind) {
        return getChips() >= 10 * bigBlind && healthyStack(bigBlind) == false;
    }

    /**
     * Onko todella pieni stack?
     *
     * @param bigBlind bb
     * @return totuusarvo
     */
    public boolean veryLowStack(double bigBlind) {
        return getChips() < 10 * bigBlind;
    }

    /**
     * Ovatko kortit pelattavissa, eli eivät ole aivan huonot.
     *
     * @return totuusarvo
     */
    public boolean playablePocketCards() {
        return premium() || good() || medium();
    }

    /**
     * Hyvät tai erityisen hyvät.
     *
     * @return totuusarvo
     */
    public boolean goodOrPremium() {
        return premium() || good();
    }

    /**
     * Onko vastustajan panostus normaalin kokoinen.
     *
     * @param bettingHistory bh
     * @param lastBet lastBet
     * @param bigBlind bb
     * @return totuusarvo
     */
    public boolean normalEnemyBet(List<Double> bettingHistory, double lastBet,
            double bigBlind) {
        return lastBet <= 3 * bigBlind;
    }

    /**
     * Panostaa normaalin panostuksen koon muutoksella höystettynä.
     *
     * @param bigBlind bb
     * @return String
     */
    public String betNormalBet(double bigBlind) {
        return "AI bets:" + bigBlind * 2.5;
    }

    /**
     * Panostaa normaalin jatkobetin verran.
     *
     * @param pot pot
     * @return String
     */
    public String continuationBet(double pot) {
        return "AI bets:" + 0.5 * pot;
    }

    /**
     * check.
     *
     * @return String
     */
    public String check() {
        return "AI bets:" + 0.0;
    }

    /**
     * fold.
     *
     * @return String
     */
    public String aiFolds() {
        return "AI folds";
    }

    /**
     * raise.
     *
     * @param lastBet lastBet
     * @return String
     */
    public String aiRaises(double lastBet) {
        return "AI bets:" + lastBet * 3;
    }

    /**
     * Onko tarpeeksi pelimerkkejä panostukseen.
     *
     * @param bet panostuksen määrä
     * @return totuusarvo
     */
    public boolean hasEnoughChips(double bet) {
        return getChips() - bet > 0;
    }

    /**
     * Calls.
     *
     * @param bettingHistory bh
     * @param lastBet lastbet
     * @return String
     */
    public String aiCalls(List<Double> bettingHistory, double lastBet) {
        return "AI calls";
    }

    /**
     * Onko aikaisemmin ollut nostoja.
     *
     * @param bettingHistory panostushistoria
     * @return totuusarvo
     */
    public boolean raises(List<Double> bettingHistory) {
        return bettingHistory.size() > 2;
    }

    /**
     * Vastaus normaalinkokoiseen panostukseen.
     *
     * @param lastBet lastbet
     * @param bettingHistory bh
     * @return String
     */
    public String actionToEnemyBetPreFlopHealthyStack(double lastBet, List<Double> bettingHistory) {
        if (bettingHistory.size() < 4) {
            if (goodOrPremium()) {
                return aiRaises(lastBet);
            } else if (medium()) {
                return aiCalls(bettingHistory, lastBet);
            }
        } else {
            if (goodOrPremium()) {
                return allIn();
            }
            return aiFolds();
        }
        return aiFolds();
    }

    /**
     * Vastaus panostuksista tyhjään pöytään isolla stackilla.
     *
     * @param bigBlind bb
     * @return String
     */
    public String actionToEmptyBettingHistoryPreFlopHealthyStack(double bigBlind) {
        if (playablePocketCards()) {
            return betNormalBet(bigBlind);
        }
        return aiFolds();
    }

    /**
     * Vastaus panostuksista tyhjään pöytään pienellä stackilla.
     *
     * @param bigBlind bb
     * @return String
     */
    public String actionToEmptyBettingHistoryPreFlopDangeredStack(double bigBlind) {
        if (goodOrPremium()) {
            return allIn();
        } else if (playablePocketCards()) {
            return betNormalBet(bigBlind);
        }
        return aiFolds();
    }

    /**
     * Valitsee vastauksen, jos ei ole positiossa isolla stackilla.
     *
     * @param bigBlind bb
     * @param bettingHistory bh
     * @param lastBet lb
     * @param pot pot
     * @param playerChips pc
     * @return String
     */
    public String outOfPositionPreFlopActionHealthyStack(List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (raises(bettingHistory)) {
            if (goodOrPremium()) {
                allIn();
            } else if (playablePocketCards()) {
                return aiCalls(bettingHistory, lastBet);
            }
        }
        return actionToEmptyBettingHistoryPreFlopHealthyStack(bigBlind);
    }

    /**
     * Action to call or normal bet size.
     *
     * @param lastBet last bet
     * @param bettingHistory betting history
     * @return action
     */
    public String actionToPlayerCallOrNormalBetPreFlopInPosition(double lastBet, List<Double> bettingHistory) {
        if (goodOrPremium()) {
            return aiRaises(lastBet);
        } else if (playablePocketCards()) {
            return aiCalls(bettingHistory, lastBet);
        }
        return aiFolds();
    }

    /**
     * Action to bigger preflop bet
     *
     * @param lastBet last bet
     * @param bettingHistory betting history
     * @return action
     */
    public String actionToNotNormalEnemyBetPreFlopHealthyStack(double lastBet, List<Double> bettingHistory) {
        if (goodOrPremium()) {
            return allIn();
        }
        return aiFolds();
    }

    /**
     * Valitsee vastauksen, jos on positiossa isolla stackilla.
     *
     * @param bigBlind bb
     * @param bettingHistory bh
     * @param lastBet lb
     * @param pot pot
     * @param playerChips pc
     * @return String
     */
    public String inPositionPreFlopActionHealthyStack(List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (normalEnemyBet(bettingHistory, lastBet, bigBlind)) {
            return actionToPlayerCallOrNormalBetPreFlopInPosition(lastBet, bettingHistory);
        }
        return actionToNotNormalEnemyBetPreFlopHealthyStack(lastBet, bettingHistory);
    }

    /**
     * Valitsee vastauksen, jos ei ole positiossa pienellä stackilla.
     *
     * @param bigBlind bb
     * @param bettingHistory bh
     * @param lastBet lb
     * @param pot pot
     * @param playerChips pc
     * @return String
     */
    public String outOfPositionPreFlopActionDangeredStack(List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (!raises(bettingHistory)) {
            return actionToEmptyBettingHistoryPreFlopDangeredStack(bigBlind);
        }
        if (goodOrPremium()) {
            return allIn();
        }
        return aiFolds();
    }

    /**
     * Valitsee vastauksen prefloppiin, jos on iso stack.
     *
     * @param bigBlind bb
     * @param bettingHistory bh
     * @param lastBet lb
     * @param pot pot
     * @param playerChips pc
     * @return String
     */
    public String healthyStackPreFlopAction(List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (!getButton()) {
            return outOfPositionPreFlopActionHealthyStack(bettingHistory,
                    pot, bigBlind, playerChips, lastBet);
        }
        return inPositionPreFlopActionHealthyStack(bettingHistory,
                pot, bigBlind, playerChips, lastBet);
    }

    /**
     * Valitsee vastauksen, jos on positiossa pienellä stackilla.
     *
     * @param lastBet lb
     * @param bettingHistory bh
     * @return String
     */
    public String actionInPositionPreFlopDangeredStack(double lastBet, List<Double> bettingHistory) {
        if (goodOrPremium()) {
            return allIn();
        } else if (medium()) {
            return aiCalls(bettingHistory, lastBet);
        }
        return aiFolds();
    }

    /**
     * Valitsee mitä tehdä preflop, kun on pieni stack.
     *
     * @param bigBlind bb
     * @param bettingHistory bh
     * @param lastBet lb
     * @param pot pot
     * @param playerChips pc
     * @return String
     */
    public String dangeredStackPreFlopAction(List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (!getButton()) {
            return outOfPositionPreFlopActionDangeredStack(bettingHistory,
                    pot, bigBlind, playerChips, lastBet);
        }
        return actionInPositionPreFlopDangeredStack(lastBet, bettingHistory);
    }

    /**
     * Menee all-in joka tapauksessa, kun on erittäin pieni stack.
     *
     * @param bigBlind bb
     * @param bettingHistory bh
     * @param lastBet lb
     * @param pot pot
     * @param playerChips pc
     * @return String
     */
    public String lowStackPreFlopAction(List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        return allIn();
    }

    /**
     * Päättää muuttujista riippuen, mitä tehdä preflop.
     *
     * @param bettingHistory panostushistoria
     * @param pot potin koko
     * @param bigBlind blindin koko
     * @param playerChips vastustajan pelimerkkien määrä
     * @param lastBet viimeisen panostuksen koko
     * @return AI:n päätös.
     */
    public String preFlopActionDecider(List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (healthyStack(bigBlind)) {
            return healthyStackPreFlopAction(bettingHistory, pot, bigBlind,
                    playerChips, lastBet);
        } else if (dangeredStack(bigBlind)) {
            return dangeredStackPreFlopAction(bettingHistory, pot, bigBlind,
                    playerChips, lastBet);
        } else {
            return lowStackPreFlopAction(bettingHistory, pot, bigBlind,
                    playerChips, lastBet);
        }
    }

    /**
     * Valinta tyhjään panostushistoriaan flopin jälkeen.
     *
     * @param bettingHistory bh
     * @param lastBet lb
     * @param pot pot
     * @param tableCards tc
     * @param playerChips pc
     * @param hc hc
     * @return String
     */
    public String actionToEmptyBet(List<Card> tableCards, List<Double> bettingHistory,
            double pot, double playerChips, double lastBet, HandComparator hc) {
        if (hc.pairOrBetter(getHand(tableCards)) || goodOrPremium()) {
            return continuationBet(pot);
        } else {
            return check();
        }
    }

    /**
     * Empty or checked.
     *
     * @param bettingHistory bh
     * @param lastBet lb
     * @return totuusarvo
     */
    public boolean emptyOrChecked(List<Double> bettingHistory, double lastBet) {
        return bettingHistory.isEmpty() || lastBet == 0.0;
    }

    /**
     * Onko vastustaja panostanut.
     *
     * @param bettingHistory panostushistoria
     * @param tableCards pöytäkortit
     * @param lastBet viimeisin panostus
     * @param hc hc
     * @return AI:n päätös.
     */
    public String playerHasBet(List<Double> bettingHistory, List<Card> tableCards,
            double lastBet, HandComparator hc) {
        if (hc.pairOrBetter(getHand(tableCards))) {
            return aiCalls(bettingHistory, lastBet);
        }
        return aiFolds();
    }

    /**
     * Valitsee muuttujien perusteella, mikä olisi paras toiminta tilanteessa.
     *
     * @param bettingHistory bh
     * @param lastBet lb
     * @param pot pot
     * @param playerChips pc
     * @param tableCards tc
     * @param hc hc
     * @return String
     */
    public String actionDecider(List<Card> tableCards, List<Double> bettingHistory,
            double pot, double playerChips, double lastBet, HandComparator hc) {
        if (emptyOrChecked(bettingHistory, lastBet)) {
            return actionToEmptyBet(tableCards, bettingHistory,
                    pot, playerChips, lastBet, hc);
        }
        return playerHasBet(bettingHistory, tableCards, lastBet, hc);
    }

    /**
     * Onko tilanne preflop vai after flop?
     *
     * @param bigBlind bb
     * @param bettingHistory bh
     * @param pot pot
     * @param playerChips pc
     * @param tableCards tc
     * @return String
     */
    public String action(List<Card> tableCards, List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips) {
        double lastBet = lastBet(bettingHistory);
        HandComparator hc = new HandComparator();
        if (preFlop(tableCards)) {
            return preFlopActionDecider(bettingHistory,
                    pot, bigBlind, playerChips, lastBet);
        } else {
            return actionDecider(tableCards, bettingHistory,
                    pot, playerChips, lastBet, hc);
        }
    }

    /**
     * Viimeisin panostus.
     *
     * @param bettingHistory bh
     * @return double
     */
    public Double lastBet(List<Double> bettingHistory) {
        double lastBet = 0.0;
        if (!bettingHistory.isEmpty()) {
            lastBet = bettingHistory.get(bettingHistory.size() - 1);
            return lastBet;
        }
        return lastBet;
    }

    /**
     * AI panostaa kaikki merkkinsä.
     *
     * @return string
     */
    public String allIn() {
        return "AI is all-in";
    }

    /**
     * Draw pocket cards.
     *
     * @param pocketCards cards
     */
    public void drawPocketCards(List<Card> pocketCards) {
        this.pocketCards.addAll(pocketCards);
    }

    /**
     * Get pocket cards.
     *
     * @return pocketcards
     */
    public List<Card> getPocketCards() {
        return pocketCards;
    }

    /**
     * Bet small blind.
     *
     * @param smallBlind sb
     */
    public void betSmallBlind(double smallBlind) {
        if (this.chips - smallBlind >= 0) {
            this.chips -= smallBlind;
        } else {
            this.chips = 0;
        }
    }

    /**
     * Bet Big blind.
     *
     * @param bigBlind bb
     */
    public void betBigBlind(double bigBlind) {
        if (this.chips - bigBlind >= 0) {
            this.chips -= bigBlind;
        } else {
            this.chips = 0;
        }
    }

    /**
     * getchips.
     *
     * @return chips
     */
    public double getChips() {
        return chips;
    }

    /**
     * setchips.
     *
     * @param chips chips
     */
    public void setChips(double chips) {
        this.chips = chips;
    }

    /**
     * Vaihtaa AI:n positiota.
     */
    public void buttonChange() {
        if (this.button == false) {
            this.button = true;
        } else {
            this.button = false;
        }
    }

    /**
     * Returns button.
     *
     * @return boolean
     */
    public boolean getButton() {
        return this.button;
    }

    /**
     * Voittaa pelimerkkejä.
     *
     * @param howMuch double
     */
    public void winChips(double howMuch) {
        this.chips += howMuch;
    }

    /**
     * Panostaa niin, ettei voi mennä miinukselle.
     *
     * @param bet bet
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
     * Adds tablecards to pocketcards and returns.
     *
     * @return list of cards
     * @param tableCards table cards
     */
    public List<Card> getHand(List<Card> tableCards) {
        this.pocketCards.addAll(tableCards);
        return this.pocketCards;
    }

}
