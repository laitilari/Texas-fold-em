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
        return random.nextDouble();
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

    public String action(List<Card> tableCards, List<Double> bettingHistory,
            double pot, double bb, double playerChips) {
        Card first = pocketCards.get(0);
        Card second = pocketCards.get(1);
        double lastBet = 0.0;
        if (!bettingHistory.isEmpty()) {
            lastBet = bettingHistory.get(bettingHistory.size() - 1);
        }
        //Preflop
        if (tableCards.isEmpty()) {                                             //Preflop action
            if (getChips() >= 12 * bb) {
                //If out of position
                if (bettingHistory.size() < 3) {
                    if (premium() || good() || medium()) {
                        bet(bb * 2.5);
                        return "AI bets " + bb * 2.5 * betRandomized();
                    }
                    return "AI folds";
                } else if (bettingHistory.size() > 2) {
                    if (playerChips == lastBet && lastBet > getChips() / 2) {   //If player is all in with large stack
                        if (premium() || good()) {
                            bet(playerChips);
                            return "AI bets " + playerChips;
                        } else if (getChips() > 10 * bb) {
                            return "AI folds";
                        } else {
                            return allIn();
                        }
                    } else if (playerChips == lastBet && lastBet < getChips() / 2) { //Jos pelaaja all in with small stack
                        if (premium() || good() || medium()) {
                            bet(playerChips);
                            System.out.println("AI bets " + playerChips);
                        } else {
                            return "AI folds";
                        }
                    }
                    if (lastBet <= 3 * bb) {
                        if (premium() || good()) {
                            bet(lastBet * 3 * betRandomized());
                            return "AI bets " + bb * 3 * betRandomized();
                        } else if (medium()) {
                            bet(bettingHistory.get(bettingHistory.size() - 1)
                                    - bettingHistory.get(bettingHistory.size() - 2));
                            System.out.println("AI calls");
                        } else {
                            return "AI folds";
                        }
                    } else if (lastBet > 3 * bb && lastBet < 6 * bb) {
                        if (premium()) {
                            if (getChips() < 12 * bb) {
                                return allIn();
                            } else {
                                bet(lastBet * 3 * betRandomized());
                                return "AI bets " + bb * 3 * betRandomized();
                            }
                        } else if (good()) {
                            if (getChips() < 10 * bb) {
                                return allIn();
                            } else {
                                bet(lastBet * 3 * betRandomized());
                                return "AI bets " + bb * 3 * betRandomized();
                            }
                        } else if (medium()) {
                            bet(bettingHistory.get(bettingHistory.size() - 1)
                                    - bettingHistory.get(bettingHistory.size() - 2));
                            return "AI calls";
                        }
                    } else {
                        if (premium() || good()) {
                            return allIn();
                        }
                        return "AI folds";
                    }
                }
            } else if (getChips() <= 11 * bb) {
                if (medium() || good() || premium()) {
                    return allIn();
                } else {
                    return "AI folds";
                }
            }
        }
        return "";
    }

    public String allIn() {
        bet(getChips());
        return "AI is goes all-in with " + getChips() + " chips!!!";
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
