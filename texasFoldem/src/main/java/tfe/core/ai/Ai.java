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
    private List<Card> hand;
    private double chips;
    private boolean button;
    private Random random;
    private HandComparator hc;
    private List<Card> tableCards;
    private List<Card> playerCards;

    public Ai() {
        this.hand = new ArrayList<>();
        this.pocketCards = new ArrayList<>();
        this.button = true;
        this.random = new Random();
        this.tableCards = new ArrayList<>();
        this.playerCards = new ArrayList<>();
        this.hc = new HandComparator(playerCards, pocketCards, tableCards);
    }

    /**
     * Jos kortit ovat erittäin hyvät.
     */
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

    /**
     * Jos kortit ovat hyvät.
     */
    public boolean good() {
        Card first = pocketCards.get(0);
        Card second = pocketCards.get(1);
        if (first.getValue() >= 9 && first.getValue() <= 10
                && first.getValue() == second.getValue()) {
            return true;
        }
        if (first.getValue() + second.getValue() >= 22
                && first.getValue() + second.getValue() <= 25) {
            return true;
        }
        return false;
    }

    /**
     * Jos kortit ovat menettelevät.
     */
    public boolean medium() {
        Card first = pocketCards.get(0);
        Card second = pocketCards.get(1);
        if (first.getValue() >= 7 && first.getValue() <= 8
                && first.getValue() == second.getValue()) {
            return true;
        }
        if (first.getValue() >= 13 || second.getValue() >= 13) {
            return true;
        }
        if (first.getValue() >= 12 && second.getValue() >= 7 || second.getValue() >= 12
                && first.getValue() >= 7) {
            return true;
        }
        if (first.getValue() + second.getValue() >= 15) {
            return true;
        }
        return false;
    }

    /**
     * Onko preflop? Eli onko pöytäkortit tyhjä.
     *
     * @param tableCards pöytäkortit
     * @return totuusarvo.
     */
    public boolean preFlop(List<Card> tableCards) {
        if (tableCards.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Onko iso stack?
     */
    public boolean healthyStack(double bigBlind) {
        if (getChips() >= 20 * bigBlind) {
            return true;
        }
        return false;
    }

    /**
     * Onko pieni stack?
     */
    public boolean dangeredStack(double bigBlind) {
        if (getChips() >= 10 * bigBlind && healthyStack(bigBlind) == false) {
            return true;
        }
        return false;
    }

    /**
     * Onko todella pieni stack?
     */
    public boolean veryLowStack(double bigBlind) {
        if (getChips() < 10 * bigBlind) {
            return true;
        }
        return false;
    }

    /**
     * Ovatko kortit pelattavissa, eli eivät ole aivan huonot.
     */
    public boolean playablePocketCards() {
        if (premium() || good() || medium()) {
            return true;
        }
        return false;
    }

    /**
     * Hyvät tai erityisen hyvät.
     */
    public boolean goodOrPremium() {
        if (premium() || good()) {
            return true;
        }
        return false;
    }

    /**
     * Onko vastustajan panostus normaalin kokoinen.
     */
    public boolean normalEnemyBet(List<Double> bettingHistory, double lastBet,
            double bigBlind) {
        if (lastBet <= 3 * bigBlind) {
            return true;
        }
        return false;
    }

    /**
     * Muuttaa AI:n panoksen kokoa.
     */
    public double betRandomized() {
        return random.nextDouble() + 0.75;
    }

    /**
     * Panostaa normaalin panostuksen koon muutoksella höystettynä.
     */
    public String betNormalBet(double bigBlind) {
        bet(bigBlind * 2.5 * betRandomized());
        return "AI bets:" + bigBlind * 2.5 * betRandomized();
    }

    /**
     * Panostaa normaalin jatkobetin verran.
     */
    public String continuationBet(double pot) {
        bet(0.5 * pot);
        return "AI bets:" + 1 * pot;
    }

    public String check() {
        bet(0.0);
        return "AI bets:" + 0.0;
    }

    public String aiFolds() {
        return "AI folds";
    }

    public String aiRaises(double lastBet) {
        return "AI bets:" + lastBet * 3 * betRandomized();
    }

    /**
     * Onko tarpeeksi pelimerkkejä panostukseen?
     *
     * @param bet panostuksen määrä
     * @return totuusarvo
     */
    public boolean hasEnoughChips(double bet) {
        if (getChips() - bet > 0) {
            return true;
        }
        return false;
    }

    public String aiCalls(List<Double> bettingHistory, double lastBet) {
        if (hasEnoughChips(lastBet)) {
            bet(lastBet);
            return "AI calls";
        }
        return allIn();
    }

    /**
     * Onko aikaisemmin ollut nostoja?
     *
     * @param bettingHistory panostushistoria
     * @return totuusarvo
     */
    public boolean noRaises(List<Double> bettingHistory) {
        if (bettingHistory.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Vastaus normaalinkokoiseen panostukseen.
     */
    public String actionToNormalEnemyBetPreFlopHealthyStack(double lastBet, List<Double> bettingHistory) {
        if (goodOrPremium()) {
            return aiRaises(lastBet);
        } else if (medium()) {
            return aiCalls(bettingHistory, lastBet);
        }
        return aiFolds();
    }

    /**
     * Vastaus epänormaalinkokoiseen panostukseen.
     */
    public String actionToNotNormalEnemyBetPreFlopHealthyStack(double lastBet, List<Double> bettingHistory) {
        if (goodOrPremium()) {
            return aiCalls(bettingHistory, lastBet);
        }
        return aiFolds();
    }

    /**
     * Vastaus panostuksista tyhjään pöytään isolla stackilla.
     */
    public String actionToEmptyBettingHistoryPreFlopHealthyStack(double bigBlind) {
        if (playablePocketCards()) {
            return betNormalBet(bigBlind);
        }
        return aiFolds();
    }

    /**
     * Vastaus panostuksista tyhjään pöytään pienellä stackilla.
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
     */
    public String outOfPositionPreFlopActionHealthyStack(List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (noRaises(bettingHistory)) {
            return actionToEmptyBettingHistoryPreFlopHealthyStack(bigBlind);
        }
        return aiCalls(bettingHistory, lastBet);
    }

    /**
     * Valitsee vastauksen, jos on positiossa isolla stackilla.
     */
    public String inPositionPreFlopActionHealthyStack(List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (normalEnemyBet(bettingHistory, lastBet, bigBlind)) {
            return actionToNormalEnemyBetPreFlopHealthyStack(lastBet, bettingHistory);
        }
        return actionToNotNormalEnemyBetPreFlopHealthyStack(lastBet, bettingHistory);
    }

    /**
     * Valitsee vastauksen, jos ei ole positiossa pienellä stackilla.
     */
    public String outOfPositionPreFlopActionDangeredStack(List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (noRaises(bettingHistory)) {
            return actionToEmptyBettingHistoryPreFlopDangeredStack(bigBlind);
        }
        return aiCalls(bettingHistory, lastBet);
    }

    /**
     * Valitsee vastauksen prefloppiin, jos on iso stack.
     */
    public String healthyStackPreFlopAction(List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (!getButton()) {                         // if out of position
            return outOfPositionPreFlopActionHealthyStack(bettingHistory,
                    pot, bigBlind, playerChips, lastBet);
        }
        return inPositionPreFlopActionHealthyStack(bettingHistory,
                pot, bigBlind, playerChips, lastBet);
    }

    /**
     * Valitsee vastauksen, jos on positiossa pienellä stackilla.
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
     */
    public String actionToEmptyBet(List<Card> tableCards, List<Double> bettingHistory,
            double pot, double playerChips, double lastBet) {
        if (pair(tableCards) || goodOrPremium()) {
            return continuationBet(pot);
        } else {
            return check();
        }
    }

    public boolean emptyOrChecked(List<Double> bettingHistory, double lastBet) {
        if (bettingHistory.isEmpty() || lastBet == 0.0) {
            return true;
        }
        return false;
    }

    /**
     * Onko vastustaja panostanut.
     *
     * @param bettingHistory panostushistoria
     * @param tableCards pöytäkortit
     * @param lastBet viimeisin panostus
     * @return AI:n päätös.
     */
    public String playerHasBet(List<Double> bettingHistory, List<Card> tableCards, double lastBet) {
        if (pair(tableCards) || goodOrPremium()) {
            return aiCalls(bettingHistory, lastBet);
        } else {
            return aiFolds();
        }
    }

    /**
     * Valitsee muuttujien perusteella, mikä olisi paras toiminta tilanteessa.
     */
    public String actionDecider(List<Card> tableCards, List<Double> bettingHistory,
            double pot, double playerChips, double lastBet) {
        if (emptyOrChecked(bettingHistory, lastBet)) {
            return actionToEmptyBet(tableCards, bettingHistory,
                    pot, playerChips, lastBet);
        }
        return playerHasBet(bettingHistory, tableCards, lastBet);
    }

    /**
     * Onko tilanne preflop vai after flop?
     */
    public String action(List<Card> tableCards, List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips) {
        double lastBet = lastBet(bettingHistory);
        this.tableCards = tableCards;
        if (preFlop(this.tableCards)) {
            return preFlopActionDecider(bettingHistory,
                    pot, bigBlind, playerChips, lastBet);
        } else {
            return actionDecider(this.tableCards, bettingHistory,
                    pot, playerChips, lastBet);
        }
    }

    /**
     * Viimeisin panostus.
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
     * Onko pari?
     */
    public boolean pair(List<Card> tableCards) {
        Card first = pocketCards.get(0);
        Card second = pocketCards.get(1);
        for (Card card : tableCards) {
            if (card.getValue() == first.getValue()
                    || card.getValue() == second.getValue()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Toisiksi viimeisin panostus.
     */
    public Double secondLastBet(List<Double> bettingHistory) {
        double secondLastBet = 0.0;
        if (!bettingHistory.isEmpty() || bettingHistory.size() >= 2) {
            secondLastBet = bettingHistory.get(bettingHistory.size() - 2);
            return secondLastBet;
        }
        return null;
    }

    /**
     * AI panostaa kaikki merkkinsä.
     */
    public String allIn() {
        bet(getChips());
        return "AI is all-in with " + getChips() + " chips!!!";
    }

    public void drawPocketCards(List<Card> pocketCards) {
        this.pocketCards.addAll(pocketCards);
    }

    public List<Card> getPocketCards() {
        return pocketCards;
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
            this.chips = 0;
        }
    }

    public double getChips() {
        return chips;
    }

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

    public List<Card> getHand(List<Card> tableCards) {
        this.hand.addAll(this.pocketCards);
        this.hand.addAll(tableCards);
        return this.hand;
    }

    public boolean getButton() {
        return this.button;
    }

    /**
     * Voittaa pelimerkkejä.
     *
     * @param howMuch
     */
    public void winChips(double howMuch) {
        this.chips += howMuch;
    }

    /**
     * Panostaa niin, ettei voi mennä miinukselle.
     */
    public void bet(double bet) {
        if (this.chips - bet >= 0) {
            this.chips -= bet;
        } else {
            this.chips = 0;
        }
    }

}
