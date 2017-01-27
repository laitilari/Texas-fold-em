/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.game;

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
    private int stackSize;
    private int potSize;

    public Game() {
        this.player = new Player();
        this.scanner = new Scanner(System.in);
        this.ai = new Ai();
        this.dealer = new Dealer();
        this.bigBlind = 0;
        this.stackSize = 0;
        this.potSize = 0;
    }

    public void startGame() {
        System.out.println("Hello, my name is " + dealer.getDealerName()
                + ". I am your dealer.");
        askGameSpeed();
        preparePack();
        player.setChips(stackSize);
        ai.setChips(stackSize);
        newRound();
    }

    private void newRound() {
        shuffle();
        pocketCards();
        flop();
        turn();
        river();
        clearCards();
    }

    public void clearCards() {
        player.getPocketCards().clear();
        ai.getPocketCards().clear();
        dealer.clearTable();
        dealer.reAssemblePack();
    }

    public boolean end() {
        //Jos pelimerkit loppu, palauta false
        return true;
    }

    public void preparePack() {
        dealer.assemblePack();
    }

    public void shuffle() {
        System.out.println("*Shuffling*");
        dealer.shufflePack();
    }

    public void pocketCards() {
        System.out.println("*Dealing pocket cards*");
        dealer.dealPocketCards(this.player, this.ai);
        System.out.println("Your pocket cards:");
        System.out.println(player.getPocketCards().toString());
        System.out.println("Ai:");
        System.out.println(ai.getPocketCards().toString());
        bettingRound();
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

    public void bettingRound() {
        System.out.println("type 'c' for call, 'r' for raise 'f' for fold, ' ' for check.");
        String action = scanner.nextLine();
        if (!player.isButton()) {
            player.betSmallBlind(bigBlind / 2);
            ai.betBigBlind(bigBlind);
            setPotSize(bigBlind / 2 + bigBlind);
            if (action.contains("c")) {
                player.bet(this.bigBlind);
            } else if (action.isEmpty()) {
                ai.action();
            } else if (action.contains("f")) {
                ai.winChips(potSize);
                newRound();
            } else if (action.contains("r")) {
                System.out.println("How much?");
                Integer howMuch = Integer.parseInt(scanner.nextLine());
                potSize += howMuch;
            }
            ai.action();
        }
        if (player.isButton()) {
            ai.betSmallBlind(bigBlind / 2);
            player.betBigBlind(bigBlind);
            setPotSize(bigBlind / 2 + bigBlind);
            ai.action();

            //KESKEN
        }
    }

    public int getPotSize() {
        return potSize;
    }

    public void setPotSize(int potSize) {
        this.potSize = potSize;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }

    public int getStackSize() {
        return stackSize;
    }

    public void setStackSize(int stackSize) {
        this.stackSize = stackSize;
    }

    public void askGameSpeed() {
        System.out.println("Type 'fast', 'normal' or 'slow' to determine game speed");
        String answer = scanner.nextLine();
        setGameSpeed(answer);
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

}
