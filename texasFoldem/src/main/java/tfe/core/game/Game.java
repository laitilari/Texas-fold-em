package tfe.core.game;

import java.util.ArrayList;
import java.util.List;
import tfe.core.ai.Ai;
import tfe.core.player.Player;

/**
 * Tässä luokassa tapahtuu pelin suorittamisen keskeisin logiikka. Luokka
 * tarjoaa useita Texas Hold'em pelille ominaisia metodeja.
 */
public class Game {

    private Player player;
    private Ai ai;
    private Dealer dealer;
    private double bigBlind;
    private double stackSize;
    private double potSize;
    private List<Double> bettingHistory;

    /**
     * Luodaan pelissä tarvittavat oliot ja alustetaan tarvittavat muuttujat.
     */
    public Game() {
        this.player = new Player();
        this.ai = new Ai();
        this.dealer = new Dealer();
        this.bigBlind = 0;
        this.stackSize = 0.0;
        this.potSize = 0.0;
        this.bettingHistory = new ArrayList<>();
    }

    /**
     * Valmistelee pelin uutta kättä varten.
     */
    public void prepareForNewRound() {
        buttonChange();
        clearPot();
        clearCards();
        clearBettingHistory();
    }

    public void preparePack() {
        dealer.assemblePack();
    }

    /**
     * Tyhjentää panostushistorian, joka on apuna AI:n ratkaisujen tekemisessä.
     */
    public void clearBettingHistory() {
        bettingHistory.clear();
    }

    /**
     * Määrää pelaajat vaihtamaan pelijärjestystään. Kutsutaan jokaisen käden
     * jälkeen.
     */
    public void buttonChange() {
        player.buttonChange();
        ai.buttonChange();
    }

    public void clearPot() {
        this.potSize = 0;
    }

    /**
     * Kutsuu pelaajia ja dealeria tyhjentämään korttinsa seuraavaa kättä
     * varten.
     */
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

    /**
     * Lisää blindit panostushistoriaan.
     */
    public void addBlindsToBettingHistory() {
        bettingHistory.add(bigBlind / 2);
        bettingHistory.add(bigBlind);
    }

    /**
     * Lisää blindit pottiin.
     */
    public void addBlindsToPot() {
        addToPot(bigBlind / 2 + bigBlind);
        addBlindsToBettingHistory();
    }

    /**
     * Kutsuu pelaajia asettamaan blindinsa.
     *
     * @return Palauttaa tiedon blindien asettamisesta ja niiden koosta
     * @see #aiInPosition()
     * @see #playerInPosition()
     */
    public String blinds() {
        if (!player.isButton()) {
            return aiInPosition();
        }
        if (player.isButton()) {
            return playerInPosition();
        }
        return "Something went wrong";
    }

    public String aiInPosition() {
        player.betSmallBlind(bigBlind / 2);
        ai.betBigBlind(bigBlind);
        addBlindsToPot();
//            addBlindsToBettingHistory();
        return "AI bets big blind (" + bigBlind + ")"
                + ", you bet small blind (" + bigBlind / 2 + ")";
    }

    public String playerInPosition() {
        ai.betSmallBlind(bigBlind / 2);
        player.betBigBlind(bigBlind);
        addBlindsToPot();
//            addBlindsToBettingHistory();
        return "You bet big blind (" + bigBlind + ")"
                + ", AI bets small blind (" + bigBlind / 2 + ")";
    }

    /**
     * Kutsuu dealeria jakamaan käsikortit.
     *
     * @return Tieto pelaajan käsikorteista UI:luokalle printattavaksi
     */
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

    /**
     * Pelaajan checkaamisesta tai callaamisesta aiheutuvat toiminnot peliin.
     *
     * @return Tieto pelaajan valinnasta
     */
    public String checkOrCall() {
        if (bettingHistory.isEmpty()) {
            return "Player checked";
        }
        if (bettingHistory.get(bettingHistory.size() - 1) == 0.0
                || subtractLastTwoBets() == 0.0) {
            return "Player checked";
        } else {
            player.bet(subtractLastTwoBets());
            addToPot(subtractLastTwoBets());
            return "Player called " + subtractLastTwoBets();
        }
    }

    /**
     * Tekoälyn callaamisesta aiheutuvat toiminnot peliin.
     *
     * @return Tieto tekoälyn valinnasta
     */
    public String aiCalls() {
        if (bettingHistory.isEmpty()) {
            return "AI checks";
        } else if (bettingHistory.get(bettingHistory.size() - 1) == 0.0) {
            return "AI checks";
        }
        addToPot(subtractLastTwoBets());
        bettingHistory.add(subtractLastTwoBets());
        return "AI calls " + subtractLastTwoBets() * -1;
    }

    /**
     * Tekoälyn panostuksesta aiheutuva toiminta.
     *
     * @param action tekoälyn valinta ja tieto, paljonko merkkejä panostaa
     * @return tekoälyn toiminnan tekstimuotoinen esitys
     */
    public String aiBetsOrRaises(String action) {
        String[] parts = action.split(":");
        double amount = Double.parseDouble(parts[1]);
        bettingHistory.add(amount);
        addToPot(amount);
        return action;
    }

    /**
     * Pelaaja all-in, siitä peliin aiheutuvat vaikutukset.
     *
     * @return tekstiesitys
     */
    public String playerAllIn() {
        addToPot(playerChipsLeft());
        bettingHistory.add(playerChipsLeft());
        return "Player is all-in with " + playerChipsLeft();
    }

    /**
     * AI all-in.
     *
     * @see #playerAllIn()
     * @param action AI:n valinta
     * @return tekstiesitys AI:n valinnasta
     */
    public String aiAllIn(String action) {
        addToPot(ai.getChips());
        bettingHistory.add(ai.getChips());
        return action;
    }

    /**
     * Pelaaja nostaa.
     *
     * @param amount paljonko?
     * @return tesktiesitys pelaajan nostosta.
     */
    public String raise(double amount) {
        player.bet(amount);
        bettingHistory.add(amount);
        addToPot(amount);
        return "Player raised " + amount;
    }

    /**
     * AI voittaa kierroksen
     *
     * @return tekstiesitys
     */
    public String aiWinsRound() {
        ai.winChips(potSize);
        return "AI wins the pot";
    }

    /**
     * Pelaaja voittaa kierroksen.
     *
     * @see #aiWinsRound()
     * @return tekstiesitys
     */
    public String playerWinsRound() {
        player.winChips(potSize);
        return "Player wins the pot";
    }

    /**
     * Vähentää panostushistorian kaksi viimeisintä panosta toisistaan. Tätä
     * toiminnallisuutta tarvitaan, jotta pelaajan jo panostamia pelimerkkejä ei
     * unohdettaisi, kun pelaaja callaa tai re-reissaa vastapuolen panostuksen.
     *
     * @return erotuksen tulos
     */
    public double subtractLastTwoBets() {
        if (bettingHistory.size() >= 2) {
            return bettingHistory.get(bettingHistory.size() - 1)
                    - bettingHistory.get(bettingHistory.size() - 2);
        } else if (bettingHistory.size() == 1) {
            return bettingHistory.get(bettingHistory.size() - 1);
        }
        return 0.0;
    }

    /**
     * Ottaa selvää peliä varten pelijärjestyksestä.
     *
     * @return tieto pelijärjestyksestä
     */
    public boolean bettingOrder() {
        if (!player.isButton()) {
            return false;
        }
        return true;
    }

    /**
     * Lähettää AI:lle tiedot pelin sen hetkisestä tilanteesta, jotta AI voi
     * toimia tilanteen mukaan.
     *
     * @return tekstiesitys AI:n toiminnasta.
     */
    public String aiAction() {
        return ai.action(dealer.getTableCards(), getBettingHistory(),
                potSize, bigBlind, player.getChips());
    }

    /**
     * Pelin lopettava metodi.
     *
     * @return totuusarvo, onko peli loppu (on, jos pelimerkit loppu)
     */
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

    public Player getPlayer() {
        return player;
    }

    public Ai getAi() {
        return ai;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setPlayerChips() {
        player.setChips(getStackSize());
    }

    public void setAiChips() {
        ai.setChips(getStackSize());
    }

    public void setPotSize(double potSize) {
        this.potSize = potSize;
    }
}
