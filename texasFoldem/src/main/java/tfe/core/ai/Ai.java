package tfe.core.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import tfe.core.cards.Card;

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

    public Ai() {
        this.hand = new ArrayList<>();
        this.pocketCards = new ArrayList<>();
        this.button = true;
        this.random = new Random();
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

    public boolean preFlop(List<Card> tableCards) {
        if (tableCards.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean healthyStack(double bigBlind) {
        if (getChips() >= 20 * bigBlind) {
            return true;
        }
        return false;
    }

    public boolean dangeredStack(double bigBlind) {
        if (getChips() >= 10 * bigBlind && healthyStack(bigBlind) == false) {
            return true;
        }
        return false;
    }

    public boolean veryLowStack(double bigBlind) {
        if (getChips() < 10 * bigBlind) {
            return true;
        }
        return false;
    }

    public boolean playablePocketCards() {
        if (premium() || good() || medium()) {
            return true;
        }
        return false;
    }

    public boolean goodOrPremium() {
        if (premium() || good()) {
            return true;
        }
        return false;
    }

    public boolean normalEnemyBet(List<Double> bettingHistory, double lastBet,
            double bigBlind) {
        if (lastBet <= 3 * bigBlind) {
            return true;
        }
        return false;
    }

    public double betRandomized() {
        return random.nextDouble() + 0.75;
    }

    public String betNormalBet(double bigBlind) {
        bet(bigBlind * 2.5 * betRandomized());
        return "AI bets:" + bigBlind * 2.5 * betRandomized();
    }

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

    public boolean noRaises(List<Double> bettingHistory) {
        if (bettingHistory.isEmpty()) {
            return true;
        }
        return false;
    }

    public String actionToNormalEnemyBetPreFlopHealthyStack(double lastBet, List<Double> bettingHistory) {
        if (goodOrPremium()) {
            return aiRaises(lastBet);
        } else if (medium()) {
            return aiCalls(bettingHistory, lastBet);
        }
        return aiFolds();
    }

    public String actionToNotNormalEnemyBetPreFlopHealthyStack(double lastBet, List<Double> bettingHistory) {
        if (goodOrPremium()) {
            return aiCalls(bettingHistory, lastBet);
        }
        return aiFolds();
    }

    public String actionToEmptyBettingHistoryPreFlopHealthyStack(double bigBlind) {
        if (playablePocketCards()) {
            return betNormalBet(bigBlind);
        }
        return aiFolds();
    }

    public String actionToEmptyBettingHistoryPreFlopDangeredStack(double bigBlind) {
        if (goodOrPremium()) {
            return allIn();
        } else if (playablePocketCards()) {
            return betNormalBet(bigBlind);
        }
        return aiFolds();
    }

    public String outOfPositionPreFlopActionHealthyStack(List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (noRaises(bettingHistory)) {
            return actionToEmptyBettingHistoryPreFlopHealthyStack(bigBlind);
        }
        return aiCalls(bettingHistory, lastBet);
    }

    public String inPositionPreFlopActionHealthyStack(List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (normalEnemyBet(bettingHistory, lastBet, bigBlind)) {
            return actionToNormalEnemyBetPreFlopHealthyStack(lastBet, bettingHistory);
        }
        return actionToNotNormalEnemyBetPreFlopHealthyStack(lastBet, bettingHistory);
    }

    public String outOfPositionPreFlopActionDangeredStack(List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (noRaises(bettingHistory)) {
            return actionToEmptyBettingHistoryPreFlopDangeredStack(bigBlind);
        }
        return aiCalls(bettingHistory, lastBet);
    }

    public String healthyStackPreFlopAction(List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (!getButton()) {                         // if out of position
            return outOfPositionPreFlopActionHealthyStack(bettingHistory,
                    pot, bigBlind, playerChips, lastBet);
        }
        return inPositionPreFlopActionHealthyStack(bettingHistory,
                pot, bigBlind, playerChips, lastBet);
    }

    public String actionInPositionPreFlopDangeredStack(double lastBet, List<Double> bettingHistory) {
        if (goodOrPremium()) {
            return allIn();
        } else if (medium()) {
            return aiCalls(bettingHistory, lastBet);
        }
        return aiFolds();
    }

    public String dangeredStackPreFlopAction(List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (!getButton()) {
            return outOfPositionPreFlopActionDangeredStack(bettingHistory,
                    pot, bigBlind, playerChips, lastBet);
        }
        return actionInPositionPreFlopDangeredStack(lastBet, bettingHistory);
    }

    public String lowStackPreFlopAction(List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        return allIn();
    }

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

    public String playerHasBet(List<Double> bettingHistory, List<Card> tableCards, double lastBet) {
        if (pair(tableCards) || goodOrPremium()) {
            return aiCalls(bettingHistory, lastBet);
        } else {
            return aiFolds();
        }
    }

    public String actionDecider(List<Card> tableCards, List<Double> bettingHistory,
            double pot, double playerChips, double lastBet) {
        if (emptyOrChecked(bettingHistory, lastBet)) {
            return actionToEmptyBet(tableCards, bettingHistory,
                    pot, playerChips, lastBet);
        }
        return playerHasBet(bettingHistory, tableCards, lastBet);
    }

    public String action(List<Card> tableCards, List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips) {
        double lastBet = lastBet(bettingHistory);
        if (preFlop(tableCards)) {
            return preFlopActionDecider(bettingHistory,
                    pot, bigBlind, playerChips, lastBet);
        } else {
            return actionDecider(tableCards, bettingHistory,
                    pot, playerChips, lastBet);
        }
    }

    public Double lastBet(List<Double> bettingHistory) {
        double lastBet = 0.0;
        if (!bettingHistory.isEmpty()) {
            lastBet = bettingHistory.get(bettingHistory.size() - 1);
            return lastBet;
        }
        return lastBet;
    }

    public Double secondLastBet(List<Double> bettingHistory) {
        double secondLastBet = 0.0;
        if (!bettingHistory.isEmpty() || bettingHistory.size() >= 2) {
            secondLastBet = bettingHistory.get(bettingHistory.size() - 2);
            return secondLastBet;
        }
        return null;
    }

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

    public void winChips(double howMuch) {
        this.chips += howMuch;
    }

    public void bet(double bet) {
        if (this.chips - bet >= 0) {
            this.chips -= bet;
        } else {
            this.chips = 0;
        }
    }

}
