/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.gui;

import java.util.Scanner;
import tfe.core.ai.Ai;
import tfe.core.cards.TableCards;
import tfe.core.player.Player;
import tfe.core.game.Dealer;
import tfe.core.game.Game;
import tfe.core.support.ScannerClass;

/**
 *
 * @author ilarilai
 */
public class UserInterface {

    private Game game;
    private Player player;
    private Ai ai;
    private ScannerClass scanner;

    public UserInterface() {
        this.game = new Game();
        this.player = new Player();
        this.ai = new Ai();
        scanner = new ScannerClass();
    }

    public void greet() {
        System.out.println("Welcome to Texas Fold'em!");
        go();
    }

    public void go() {
        setGameSpeed(askGameSpeed());
        game.preparePack();
        game.setPlayerChips();
        game.setAiChips();    
        newRound();
    }

    public void bettingInstructions() {
        System.out.println("type 'c' for call/check, 'r' for raise 'f' for fold");
    }

    public void chipSituation() {
        System.out.println("AI has " + game.aiChipsLeft() + "chips left");
        System.out.println("You have " + game.playerChipsLeft() + "chips left");
    }
    
    public void prepareForNewRound() {
        game.prepareForNewRound();
    }

    public void newRound() {
        prepareForNewRound();
        shuffle();
        blinds();
        pocketCards();
        chipSituation();
        bettingRound();
        flop();
        chipSituation();
        bettingRound();
        turn();
        chipSituation();
        bettingRound();
        river();
        chipSituation();
        bettingRound();
    }

    public void flop() {
        System.out.println("Flop is:");
        System.out.println(game.flop());
    }

    public void turn() {
        System.out.println("Turn is:");
        System.out.println(game.turn());
    }

    public void river() {
        System.out.println("River is:");
        System.out.println(game.river());
    }

    public void playerAction() {
        String action = scanner.use();
        if (action.equals("c")) {
            System.out.println(game.checkOrCall());
        } else if (action.equals("r")) {
            System.out.println("How much?");
            double amount = Integer.parseInt(scanner.use());
            System.out.println(game.raise(amount));
        } else if (action.equals("f")) {
            aiWinsRound();
            newRound();
        } else {
            bettingRound();
        }
    }

    public void aiWinsRound() {
        System.out.println(game.aiWinsRound());
    }

    public void playerWinsRound() {
        System.out.println(game.playerWinsRound());
    }

    public void aiAction() {
        String action = game.aiAction();
        System.out.println(action);
        if (action.equals("AI folds")) {
            playerWinsRound();
            newRound();
        } else if (action.equals("AI calls")) {
            game.aiCalls(); 
        } else if (action.contains("bet")) {
            game.aiBets(action);
            // JATKA TÄSTÄ. MITEN AI:n BET AMOUNT NUMEROINA GAME LUOKALLE?
        }
    }

    public void bettingRound() {
        if (!game.bettingOrder()) {
            bettingInstructions();
            playerAction();
            aiAction();
        } else {
            aiAction();
            bettingInstructions();
            playerAction();
        }
    }

    public String askGameSpeed() {
        System.out.println("Type 'fast', 'normal' or 'slow' to determine game speed");
        String answer = scanner.use();
        return answer;
    }

    public void setGameSpeed(String answer) {
        if (answer.contains("fast")) {
            game.setBigBlind(30);
            game.setStackSize(500);
        } else if (answer.contains("normal")) {
            game.setBigBlind(30);
            game.setStackSize(1000);
        } else if (answer.contains("slow")) {
            game.setBigBlind(30);
            game.setStackSize(2000);
        } else {
            System.out.println("I didn't understand. Let's try again.");
            go();
        }
    }

    public void shuffle() {
        System.out.println(game.shuffle());
    }

    public void blinds() {
        System.out.println(game.blinds());
    }

    public void pocketCards() {
        System.out.println("Dealing pocket cards...");
        System.out.println(game.pocketCards());
    }

}
