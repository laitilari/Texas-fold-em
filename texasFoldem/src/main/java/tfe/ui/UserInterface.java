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
    private int bettingRoundCounter;

    public UserInterface() {
        this.game = new Game();
        scanner = new ScannerClass();
        this.bettingRoundCounter = 0;
    }

    /**
     * Tervehtii pelaajaa.
     */
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
            bettingRound();
            prepareForNewStreet();
            flop();
            streetActions();
            turn();
            streetActions();
            river();
            streetActions();
            game.showDown();
        }
        System.out.println("Game ended");
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
    public String playerAction() {
        bettingInstructions();
        String action = scanner.use();
        if (action.equals("c")) {
            System.out.println(game.checkOrCall());
            return ("c");
        } else if (action.equals("r")) {
            playerRaise();
            return "r";
        } else if (action.equals("f")) {
            return "f";
        } else {
            playerAction();
        }
        return "Something went wrong with player action";
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
     *
     * @return ai action
     */
    public String aiAction() {
        String action = game.aiAction();
        if (action.equals("AI folds")) {
            System.out.println(action);
            return "f";
        } else if (action.contains("AI calls")) {
            aiCalls();
            return "c";
        } else if (action.contains("bet")) {
            aiBets(action);
            return "bet";
        } else if (action.contains("all-in")) {
            aiAllIn(action);
            return "all-in";
        }
        return "Something went wrong with AI action";
    }

    /**
     * Määrittelee pelijärjestyksen Game-luokan bettingOrder metodin avulla.
     */
    public void bettingRound() {
        while (true) {
            if (!game.bettingOrder()) {
                String playerAction = playerAction();
                if (bettingRoundCounter > 0 && playerAction.equals("c")) {
                    bettingRoundCounter = 0;
                    break;
                }
                if (playerAction.equals("f")) {
                    aiWinsRound();
                    newRound();
                    bettingRoundCounter = 0;
                    break;
                }
                String aiAction = aiAction();
                if (aiAction.equals("f")) {
                    playerWinsRound();
                    newRound();
                    bettingRoundCounter = 0;
                    break;
                } else if (aiAction.equals("c")) {
                    bettingRoundCounter = 0;
                    break;
                } else {
                    bettingRoundCounter++;
                    bettingRound();
                }
            } else {
                String aiAction = aiAction();
                if (bettingRoundCounter > 0 && aiAction.equals("c")) {
                    bettingRoundCounter = 0;
                    break;
                }
                if (aiAction.equals("f")) {
                    playerWinsRound();
                    newRound();
                    bettingRoundCounter = 0;
                    break;
                }
                String playerAction = playerAction();
                if (playerAction.equals("f")) {
                    aiWinsRound();
                    newRound();
                    bettingRoundCounter = 0;
                    break;
                } else if (playerAction.equals("c")) {
                    bettingRoundCounter = 0;
                    break;
                } else {
                    bettingRoundCounter++;
                    bettingRound();
                }
            }
        }
    }

    /**
     * Jos molemmat all in, mennään kaikki streetit läpi ilman panostuksia.
     */
    public void allInStreets() {
        if (game.getDealer().getTableCards().size() == 0) {
            flop();
            turn();
            river();
        } else if (game.getDealer().getTableCards().size() == 3) {
            turn();
            river();
        } else {
            river();
        }
        game.showDown();
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
