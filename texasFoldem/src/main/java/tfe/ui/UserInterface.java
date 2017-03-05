package tfe.ui;

import java.util.concurrent.TimeUnit;
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
    public void greet() throws InterruptedException {
        System.out.println("Welcome to Texas Fold'em!");
        go();
    }

    /**
     * Metodi kutsuu pelin valmisteluun liittyviä metodeja.
     */
    public void go() throws InterruptedException {
        setGameSpeed(askGameSpeed());
        game.prepareGame();
        newRound();
    }

    /**
     * Kutsuu uuden kierroksen valmisteluun liittyviä metodeja.
     */
    public void prepareForNewRound() throws InterruptedException {
        game.prepareForNewRound();
        shuffle();
        TimeUnit.SECONDS.sleep(2);
        chipSituation();
        TimeUnit.SECONDS.sleep(4);
        blinds();
        TimeUnit.SECONDS.sleep(4);
        pocketCards();
        TimeUnit.SECONDS.sleep(2);
    }

    public void buttonChange() {
        game.buttonChange();
    }

    /**
     * Starts the betting round for current street.
     */
    public void streetActions() throws InterruptedException {
        if (!game.getAllIn()) {
            prepareForNewStreet();
            TimeUnit.SECONDS.sleep(4);
            bettingRound();
        }
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
    public void newRound() throws InterruptedException {
        while (!game.end()) {
            prepareForNewRound();
            bettingRound();
            prepareForNewStreet();
            flop();
            TimeUnit.SECONDS.sleep(4);
            streetActions();
            turn();
            TimeUnit.SECONDS.sleep(4);
            streetActions();
            river();
            TimeUnit.SECONDS.sleep(4);
            streetActions();
            System.out.println(game.showDown());
            if (game.end()) {
                break;
            }
        }
        gameEndMessages();
    }

    /**
     * Choises regarding the end of the game.
     */
    public void gameEndMessages() throws InterruptedException {
        System.out.println(game.whoWon());
        System.out.println("Type 'y' to play again, type anything else to leave the game.");
        String answer = scanner.use();
        if (answer.equals("y")) {
            go();
        }
        System.exit(0);
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
        System.out.println(game.aiBetsOrRaises(action));
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
    public void bettingRound() throws InterruptedException {
        boolean playerAllIn = false;
        boolean aiAllIn = false;
        while (true) {
            if (!game.bettingOrder()) {
                double playerChips = game.getPlayer().getChips();
                String playerAction = playerAction();
                if (game.getPlayer().getChips() == 0
                        || game.lastBet() >= game.getAi().getChips()) {
                    playerAllIn = true;
                    if (aiAllIn) {
                        game.allIn();
                    }
                }
                if (bettingRoundCounter > 0 && playerAction.equals("c")) {
                    if (aiAllIn || game.getBettingHistory().get(game.getBettingHistory().size() - 2)
                            >= playerChips) {
                        game.allIn();
                    }
                    bettingRoundCounter = 0;
                    break;
                }
                if (playerAction.equals("f")) {
                    aiWinsRound();
                    newRound();
                    bettingRoundCounter = 0;
                    break;
                }
                String ai = aiAction();
                if (game.getAi().getChips() == 0) {
                    aiAllIn = true;
                    if (playerAllIn) {
                        bettingRoundCounter = 0;
                        game.allIn();
                        break;
                    }
                }
                if (ai.equals("f")) {
                    playerWinsRound();
                    newRound();
                    bettingRoundCounter = 0;
                    break;
                } else if (ai.equals("c")) {
                    if (playerAllIn) {
                        game.allIn();
                    }
                    bettingRoundCounter = 0;
                    break;
                } else {
                    bettingRoundCounter++;
                    continue;
                }
            } else {
                double aiChips = game.getAi().getChips();
                String ai = aiAction();
                TimeUnit.SECONDS.sleep(2);
                if (game.getAi().getChips() == 0
                        || game.lastBet() >= game.getPlayer().getChips()) {
                    aiAllIn = true;
                    if (playerAllIn) {
                        bettingRoundCounter = 0;
                        game.allIn();
                    }
                }
                if (bettingRoundCounter > 0 && ai.equals("c")) {
                    if (playerAllIn || game.getBettingHistory().get(game.getBettingHistory().size() - 2)
                            >= aiChips) {
                        game.allIn();
                    }
                    bettingRoundCounter = 0;
                    break;
                }
                if (bettingRoundCounter > 0 && ai.equals("bet")) {
                    continue;
                }
                if (ai.equals("f")) {
                    playerWinsRound();
                    newRound();
                    bettingRoundCounter = 0;
                    break;
                }
                String playerAction = playerAction();
                TimeUnit.SECONDS.sleep(2);
                if (game.getPlayer().getChips() == 0) {
                    playerAllIn = true;
                    if (aiAllIn) {
                        bettingRoundCounter = 0;
                        game.allIn();
                    }
                }
                if (playerAction.equals("f")) {
                    aiWinsRound();
                    newRound();
                    bettingRoundCounter = 0;
                    break;
                } else if (playerAction.equals("c")) {
                    if (aiAllIn) {
                        game.allIn();
                    }
                    bettingRoundCounter = 0;
                    break;
                } else {
                    bettingRoundCounter++;
                    continue;
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
    public void setGameSpeed(String answer) throws InterruptedException {
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
        System.out.println("There are " + game.getPotSize() + " chips in the pot");
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
