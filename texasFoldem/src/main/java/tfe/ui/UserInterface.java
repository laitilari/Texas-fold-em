package tfe.ui;

import java.util.Scanner;
import tfe.core.ai.Ai;
import tfe.core.cards.TableCards;
import tfe.core.player.Player;
import tfe.core.game.Dealer;
import tfe.core.game.Game;
import tfe.core.support.ScannerClass;

/**
 * Käyttöliittymäluokka, joka tarjoaa tekstikäyttöliittymän pelin pelaamiseen.
 * Luokka hoitaa kaiken tulostamisen ja kutsuu Game-luokkaa sekä ScannerClass-
 * luokkaa toimintojensa toteuttamiseen.
 */
public class UserInterface {

    private Game game;
    private ScannerClass scanner;

    public UserInterface() {
        this.game = new Game();
        scanner = new ScannerClass();
    }

    public void greet() {
        System.out.println("Welcome to Texas Fold'em!");
        go();
    }

    /**
     * Metodi kutsuu pelin valmisteluun liittyviä metodeja.
     */
    public void go() {
        setGameSpeed(askGameSpeed());
        game.prepareGame();
        newRound();
    }

    /**
     * Kutsuu uuden kierroksen valmisteluun liittyviä metodeja.
     */
    public void prepareForNewRound() {
        game.prepareForNewRound();
        shuffle();
        blinds();
        pocketCards();
    }

    public void buttonChange() {
        game.buttonChange();
    }

    /**
     * Starts the betting round for current street.
     */
    public void streetActions() {
        prepareForNewStreet();
        bettingRound();
    }

    /**
     * Prepares the game for current street and betting round.
     */
    public void prepareForNewStreet() {
        game.clearBettingHistory();
        chipSituation();
    }

    /**
     * Kutsuu käden suoritukseen liittyviä metodeja.
     */
    public void newRound() {
        while (!game.end()) {
            prepareForNewRound();
            streetActions();
            flop();
            streetActions();
            game.showDown();
        }
    }

    /**
     * Suorittaa Flopin.
     *
     * @see #turn()
     * @see #river()
     */
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

    /**
     * Kun pelaaja valitsee raise-vaihtoehdon. Kysyy uudestaan, jos annettu
     * pelimerkkien määrä on liian pieni.
     */
    public void playerRaise() {
        System.out.println("How much?");
        while (true) {
            double amount = Integer.parseInt(scanner.use());
            if (amount >= game.getBigBlind()) {
                System.out.println(game.raise(amount));
                break;
            } else if (amount >= game.playerChipsLeft()) {
                System.out.println(game.playerAllIn());
                break;
            } else {
                System.out.println("You must raise atleast big blind");
            }
        }
    }

    /**
     * Kysyy pelaajan pelivalinnan ja kutsuu sen mukaista metodia.
     */
    public void playerAction() {
        bettingInstructions();
        String action = scanner.use();
        if (action.equals("c")) {
            System.out.println(game.checkOrCall());
        } else if (action.equals("r")) {
            playerRaise();
            //uusi aiaction, jos tämä on rundin päättävä action
        } else if (action.equals("f")) {
            aiWinsRound();
            newRound();
        } else {
            playerAction();
        }
    }

    public void aiWinsRound() {
        System.out.println(game.aiWinsRound());
    }

    public void playerWinsRound() {
        System.out.println(game.playerWinsRound());
    }

    public void aiCalls() {
        System.out.println(game.aiCalls());
    }

    public void aiBets(String action) {
        System.out.println(game.aiBetsOrRaises(action));
    }

    public void aiAllIn(String action) {
        System.out.println(game.aiAllIn(action));
    }

    /**
     * AI:n valintoja seuraava metodi, joka kutsuu valinnan mukaisia metodeja.
     */
    public void aiAction() {
        String action = game.aiAction();
        if (action.equals("AI folds")) {
            System.out.println(action);
            playerWinsRound();
            newRound();
        } else if (action.equals("AI calls")) {
            aiCalls();
        } else if (action.contains("bet")) {
            aiBets(action);
        } else if (action.contains("all-in")) {
            aiAllIn(action);
        }
    }

    /**
     * Määrittelee pelijärjestyksen Game-luokan bettingOrder metodin avulla.
     */
    public void bettingRound() {
        if (!game.bettingOrder()) {
            playerAction();
            aiAction();
        } else {
            aiAction();
            playerAction();
        }
    }

    /**
     * Kysyy pelaajalta pelinopeuden.
     *
     * @return pelaajan valinta pelin nopeudesta
     */
    public String askGameSpeed() {
        System.out.println("Type 'fast', 'normal' or 'slow' to determine game speed");
        String answer = scanner.use();
        return answer;
    }

    /**
     * Asettaa pelin nopeuden.
     *
     * @param answer Pelaajan valitsema nopeus
     */
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

    /**
     * Ohjeet pelaajalle valintojen tekemiseen.
     */
    public void bettingInstructions() {
        System.out.println("type 'c' for call/check, 'r' for raise 'f' for fold");
    }

    /**
     * Tulostaa pelin osapuolten pelimerkkien määrän, jotta pelaajan on helpompi
     * seurata pelin kulkua ja tehdä valintoja pelimerkkien määrän perusteella.
     */
    public void chipSituation() {
        System.out.println("There is " + game.getPotSize() + " chips in the pot");
        System.out.println("AI has " + game.aiChipsLeft() + "chips left");
        System.out.println("You have " + game.playerChipsLeft() + "chips left");
    }

    public void shuffle() {
        System.out.println(game.shuffle());
    }

    public void blinds() {
        System.out.println(game.blinds());
    }

    /**
     * Kertoo pelaajalle, että uudet käskikortit jaetaan ja kutsuu Game-luokan
     * metodia, joka vie pyynnön Dealer-luokalle.
     */
    public void pocketCards() {
        System.out.println("Dealing pocket cards...");
        System.out.println(game.pocketCards());
    }

}
