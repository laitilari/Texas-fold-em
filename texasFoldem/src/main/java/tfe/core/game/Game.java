package tfe.core.game;

import java.util.ArrayList;
import java.util.List;
import tfe.core.ai.Ai;
import tfe.core.player.Player;

/**
 *
 * @author ilarilai
 */
public class Game {

    private Player player;
    private Ai ai;
    private Dealer dealer;
    private double bigBlind;
    private double stackSize;
    private double potSize;
    private List<Double> bettingHistory;

    public Game() {
        this.player = new Player();
        this.ai = new Ai();
        this.dealer = new Dealer();
        this.bigBlind = 0;
        this.stackSize = 0.0;
        this.potSize = 0.0;
        this.bettingHistory = new ArrayList<>();
    }

    public void setPlayerChips() {
        player.setChips(getStackSize());
    }

    public void setAiChips() {
        ai.setChips(getStackSize());
    }

    public void preparePack() {
        dealer.assemblePack();
    }

    public void prepareForNewRound() {
        buttonChange();
        clearPot();
        clearCards();
        clearBettingHistory();
    }

    public void clearBettingHistory() {
        bettingHistory.clear();
    }

    public void buttonChange() {
        player.buttonChange();
        ai.buttonChange();
    }

    public void clearPot() {
        this.potSize = 0;
    }

    public void clearCards() {
        player.getPocketCards().clear();
        ai.getPocketCards().clear();
        dealer.clearTable();
        dealer.reAssemblePack();
    }

    public String shuffle() {
        dealer.shufflePack();
        return "Shuffling...";
    }

    public void addBlindsToBettingHistory() {
        bettingHistory.add(bigBlind / 2);
        bettingHistory.add(bigBlind);
    }

    public void addBlindsToPot() {
        addToPot(bigBlind / 2 + bigBlind);
        addBlindsToBettingHistory();
    }

    public String blinds() {
        if (!player.isButton()) {
            player.betSmallBlind(bigBlind / 2);
            ai.betBigBlind(bigBlind);
            addBlindsToPot();
            addBlindsToBettingHistory();
            return "AI bets big blind (" + bigBlind + ")"
                    + ", you bet small blind (" + bigBlind / 2 + ")";

        }
        if (player.isButton()) {
            ai.betSmallBlind(bigBlind / 2);
            player.betBigBlind(bigBlind);
            addBlindsToPot();
            addBlindsToBettingHistory();
            return "You bet big blind (" + bigBlind + ")"
                    + ", AI bets small blind (" + bigBlind / 2 + ")";
        }
        return "Something went wrong";
    }

    public String pocketCards() {
        dealer.dealPocketCards(this.player, this.ai);
        return "Your pocket cards: " + player.getPocketCards().toString()
                + "(DEVELOPMENT AI pocket cards: " + ai.getPocketCards().toString();
    }

    public void addToPot(double amount) {
        this.potSize += amount;
    }

    public double playerChipsLeft() {
        return player.getChips();
    }

    public double aiChipsLeft() {
        return ai.getChips();
    }

    public String checkOrCall() {
        if (bettingHistory.get(bettingHistory.size() - 1) == 0) {
            return "Player checked";
        } else {
            player.bet(subtractLastTwoBets());
            addToPot(subtractLastTwoBets());
            return "Player called";
        }
    }
    
    public String aiCalls() {
        addToPot(subtractLastTwoBets());
        bettingHistory.add(subtractLastTwoBets());
        if (subtractLastTwoBets() == 0) {
            return "AI checks";
        }
        return "AI calls " + subtractLastTwoBets() * -1;  
    }
    
    public String aiBets(String action) {
        String[] parts = action.split(":");
        double amount = Double.parseDouble(parts[1]);
        bettingHistory.add(amount);
        addToPot(amount);
        return action;
    }
    
    public String aiAllIn(String action) {
        addToPot(ai.getChips());
        bettingHistory.add(ai.getChips());
        return action;
    }

    public String raise(double amount) {
        player.bet(amount);
        bettingHistory.add(amount);
        addToPot(amount);
        return "Player raised " + amount;
    }

    public String aiWinsRound() {
        ai.winChips(potSize);
        return "AI wins the pot";
    }
    
    public String playerWinsRound() {
        player.winChips(potSize);
        return "Player wins the pot";
    }

    public double subtractLastTwoBets() {
        return bettingHistory.get(bettingHistory.size() - 1)
                - bettingHistory.get(bettingHistory.size() - 2);
    }

    public boolean bettingOrder() {
        if (!player.isButton()) {
            return false;
        }
        return true;
    }

    public String aiAction() {
        return ai.action(dealer.getTableCards(), getBettingHistory(), potSize, bigBlind, player.getChips());
    }

    public boolean end() {
        //Jos pelimerkit loppu, palauta false
        return true;
    }

    public List<Double> getBettingHistory() {
        return bettingHistory;
    }

    public String flop() {
        dealer.dealFlop();
        return showTableCards();
    }

    public String turn() {
        dealer.dealTurn();
        return showTableCards();
    }

    public String river() {
        dealer.dealRiver();
        return showTableCards();
    }

    public String showTableCards() {
        return dealer.tellTableCards();
    }

    public double getPotSize() {
        return potSize;
    }

    public double getBigBlind() {
        return bigBlind;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }

    public double getStackSize() {
        return stackSize;
    }

    public void setStackSize(int stackSize) {
        this.stackSize = stackSize;
    }
}
