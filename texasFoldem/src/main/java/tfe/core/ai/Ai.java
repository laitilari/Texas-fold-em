package tfe.core.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import tfe.core.cards.Card;

/**
 *
 * @author ilarilai
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

    public double betRandomized() {
        return random.nextDouble() + 0.5;
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

    public Double lastBet(List<Double> bettingHistory) {
        double lastBet = 0.0;
        if (!bettingHistory.isEmpty()) {
            lastBet = bettingHistory.get(bettingHistory.size() - 1);
            return lastBet;
        }
        return null;
    }

    public Double secondLastBet(List<Double> bettingHistory) {
        double secondLastBet = 0.0;
        if (!bettingHistory.isEmpty() || bettingHistory.size() >= 2) {
            secondLastBet = bettingHistory.get(bettingHistory.size() - 2);
            return secondLastBet;
        }
        return null;
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

    public String betNormalBet(double bigBlind) {
        bet(bigBlind * 2.5 * betRandomized());
        return "AI bets:" + bigBlind * 2.5 * betRandomized();
    }

    public String aiFolds() {
        return "AI folds";
    }

    public String aiRaises(double lastBet) {
        return "AI bets:" + lastBet * 3 * betRandomized();
    }

    public boolean hasEnoughChips(double bet) {
        if (getChips() - bet >= 0) {
            return true;
        }
        return false;
    }

    public String aiCalls(List<Double> bettingHistory) {
        double amount = secondLastBet(bettingHistory);
        if (hasEnoughChips(amount)) {
            bet(amount);
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

    public String actionToNormalEnemyBetPreFlop(double lastBet, List<Double> bettingHistory) {
        if (goodOrPremium()) {
            return aiRaises(lastBet);
        } else if (medium()) {
            return aiCalls(bettingHistory);
        }
        return aiFolds();
    }

    public String actionToNotNormalEnemyBetPreFlop(double lastBet, List<Double> bettingHistory) {
        if (goodOrPremium()) {
            return aiCalls(bettingHistory);
        }
        return aiFolds();
    }

    public String actionToEmptyBettingHistoryPreFlop(double bigBlind) {
        if (playablePocketCards()) {
            return betNormalBet(bigBlind);
        }
        return aiFolds();
    }

    public String outOfPositionPreFlopAction(List<Card> tableCards, List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (noRaises(bettingHistory)) {
            return actionToEmptyBettingHistoryPreFlop(bigBlind);
        }
        return aiCalls(bettingHistory);
    }

    public String inPositionPreFlopAction(List<Card> tableCards, List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (normalEnemyBet(bettingHistory, lastBet, bigBlind)) {
            return actionToNormalEnemyBetPreFlop(lastBet, bettingHistory);
        }
        return actionToNotNormalEnemyBetPreFlop(lastBet, bettingHistory);
    }

    public String healthyStackPreFlopAction(List<Card> tableCards, List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (!getButton()) {                         // if out of position
            return outOfPositionPreFlopAction(tableCards, bettingHistory,
                    pot, bigBlind, playerChips, lastBet);
        }
        return inPositionPreFlopAction(tableCards, bettingHistory,
                pot, bigBlind, playerChips, lastBet);
    }

    public String preFlopActionDecider(List<Card> tableCards, List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips, double lastBet) {
        if (healthyStack(bigBlind)) {
            healthyStackPreFlopAction(tableCards, bettingHistory, pot, bigBlind,
                    playerChips, lastBet);
        } else if (dangeredStack(bigBlind)) {

        }
        return "Something went wrong with preflopactiondecider";
    }

    public String action(List<Card> tableCards, List<Double> bettingHistory,
            double pot, double bigBlind, double playerChips) {
        Card first = pocketCards.get(0);
        Card second = pocketCards.get(1);
        double lastBet = lastBet(bettingHistory);
        //Preflop
        if (preFlop(tableCards)) {                              //Preflop action
            return preFlopActionDecider(tableCards, bettingHistory,
                    pot, bigBlind, playerChips, lastBet);

        } else if (lastBet == 0 || bettingHistory.isEmpty()) {
            if (premium() || good()) {
                return "AI bets:" + 0.5 * pot * betRandomized();
            } else {
                bet(0.0);
                return "AI calls";
            }
        } else if (bettingHistory.size() >= 2) {
            if (premium() || good()) {
                return allIn();
            }
        } else if (bettingHistory.size() > 0 && bettingHistory.size() < 2
                || lastBet < 0.75 * pot) {
            if (premium() || good()) {
                return allIn();
            } else {
                bet(lastBet);
                return "AI calls";
            }
        }
        return "Something went wrong";
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
