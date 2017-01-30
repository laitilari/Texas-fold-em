/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import tfe.core.ai.Ai;
import tfe.core.cards.TableCards;
import tfe.core.player.Player;

/**
 *
 * @author ilarilai
 */
public class Game {

    private Scanner scanner;
    private Player player;
    private Ai ai;
    private Dealer dealer;
    private int bigBlind;
    private double stackSize;
    private double potSize;
    private List<Integer> bettingHistory;

    public Game() {
        this.player = new Player();
        this.scanner = new Scanner(System.in);
        this.ai = new Ai();
        this.dealer = new Dealer();
        this.bigBlind = 0;
        this.stackSize = 0.0;
        this.potSize = 0.0;
        this.bettingHistory = new ArrayList<>();
    }

    public void startGame() {
        setGameSpeed(askGameSpeed());
        preparePack();
        player.setChips(stackSize);
        ai.setChips(stackSize);
        newRound();
    }

    public String askGameSpeed() {
        System.out.println("Type 'fast', 'normal' or 'slow' to determine game speed");
        String answer = scanner.nextLine();
        return answer;
    }

    public void setGameSpeed(String answer) {
        if (answer.contains("fast")) {
            setBigBlind(30);
            setStackSize(500);
        } else if (answer.contains("normal")) {
            setBigBlind(30);
            setStackSize(1000);
        } else if (answer.contains("slow")) {
            setBigBlind(30);
            setStackSize(2000);
        } else {
            System.out.println("I didn't understand. Let's try again.");
            startGame();
        }
    }

    public void preparePack() {
        dealer.assemblePack();
    }

    public void newRound() {
        buttonChange();
        clearPot();
        clearCards();
        clearBettingHistory();
        shuffle();
        blinds();
        pocketCards();
        bettingRound();
        flop();
        bettingRound();
        turn();
        bettingRound();
        river();
        bettingRound();
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

    public void shuffle() {
        System.out.println("*Shuffling*");
        dealer.shufflePack();
    }

    public void blinds() {
        if (!player.isButton()) {
            player.betSmallBlind(bigBlind / 2);
            ai.betBigBlind(bigBlind);

        }
        if (player.isButton()) {
            ai.betSmallBlind(bigBlind / 2);
            player.betBigBlind(bigBlind);

        }
        addToPot(bigBlind / 2 + bigBlind);
        bettingHistory.add(bigBlind / 2);
        bettingHistory.add(bigBlind);
    }

    public void pocketCards() {
        System.out.println("*Dealing pocket cards*");
        dealer.dealPocketCards(this.player, this.ai);
        System.out.println("Your pocket cards:");
        System.out.println(player.getPocketCards().toString());
        System.out.println("Ai:");
        System.out.println(ai.getPocketCards().toString());
    }

    public void bettingInstructions() {
        System.out.println("type 'c' for call/check, 'r' for raise 'f' for fold");
    }

    public void addToPot(double amount) {
        this.potSize += amount;
    }

    public void playerAction(String action) {
        if (action.equals("c")) {
            if (bettingHistory.isEmpty()) {
                System.out.println("Player checked");

            } else if (bettingHistory.size() == 1) {
                player.bet(bettingHistory.get(0));
                addToPot(bettingHistory.get(0));

            } else {    
                player.bet(subtractLastTwoBets());
                addToPot(subtractLastTwoBets());
            }

        } else if (action.equals("r")) {
            System.out.println("How much?");
            int howMuch = Integer.parseInt(scanner.nextLine());
            player.bet(howMuch);
            bettingHistory.add(howMuch);
            addToPot(howMuch);

        } else if (action.equals("f")) {
            ai.winChips(potSize);
            newRound();

        } else {
            bettingInstructions();
            playerAction(action);

        }
    }
    
    public double subtractLastTwoBets() {
        return bettingHistory.get(bettingHistory.size()) 
                - bettingHistory.get(bettingHistory.size() - 1);
    }

    public void bettingRound() {
        bettingInstructions();
        if (!player.isButton()) {
            String action = scanner.nextLine();
            playerAction(action);
            ai.action(dealer.getTableCards(), getBettingHistory(), potSize, bigBlind);
        }
        if (player.isButton()) {
            ai.action(dealer.getTableCards(), getBettingHistory(), potSize, bigBlind);
            String action = scanner.nextLine();
            playerAction(action);
        }
        // potSize += ai action
        // Jos AI reissaa, add bettinghistory ja playerAction()
        // Jos foldaa, pelaaja voittaa, uusi rundi
        // Jos checkaa, jatkuu  
    }

    public boolean end() {
        //Jos pelimerkit loppu, palauta false
        return true;
    }

    public List<Integer> getBettingHistory() {
        return bettingHistory;
    }

    public void flop() {
        System.out.println("Flop is:");
        dealer.dealFlop();
        showTableCards();
    }

    public void turn() {
        System.out.println("Turn:");
        dealer.dealTurn();
        showTableCards();
    }

    public void river() {
        System.out.println("River:");
        dealer.dealRiver();
        showTableCards();
    }

    public void showTableCards() {
        dealer.tellTableCards();
    }

    public double getPotSize() {
        return potSize;
    }

    public int getBigBlind() {
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
