package tfe.core.game;

import java.util.ArrayList;
import java.util.List;
import tfe.core.ai.Ai;
import tfe.core.player.Player;
import tfe.ui.UserInterface;

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
    private HandComparator handComparator;

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
     * Valmistelee korttipakan ja asettaa pelimerkit.
     */
    public void prepareGame() {
        preparePack();
        setPlayerChips();
        setAiChips();
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

    /**
     * Käsien vertailu.
     *
     * @return string
     */
    public String showDown() {
        return "";
    }

    /**
     * Valmistelee pakan.
     */
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

    /**
     * Tyhjentää potin.
     */
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

    /**
     * Sekoittaa.
     *
     * @return string
     */
    public String shuffle() {
        dealer.shufflePack();
        return "Shuffling...";
    }

    /**
     * Lisää blindit pottiin.
     */
    public void addBlindsToPot() {
        addToPot(bigBlind / 2 + bigBlind);
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

    /**
     * If ai is in position, blinds.
     *
     * @return string of blinds.
     */
    public String aiInPosition() {
        player.betSmallBlind(bigBlind / 2);
        ai.betBigBlind(bigBlind);
        getBettingHistory().add(bigBlind / 2);
        getBettingHistory().add(bigBlind);
        addBlindsToPot();
        return "AI bets big blind (" + bigBlind + ")"
                + ", you bet small blind (" + bigBlind / 2 + ")";
    }

    /**
     * If player is in position blinds.
     *
     * @return blinds
     */
    public String playerInPosition() {
        ai.betSmallBlind(bigBlind / 2);
        player.betBigBlind(bigBlind);
        getBettingHistory().add(bigBlind / 2);
        getBettingHistory().add(bigBlind);
        addBlindsToPot();
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

    /**
     * Lisää pottiin chippejä.
     *
     * @param amount double.
     */
    public void addToPot(double amount) {
        this.potSize += amount;
    }

    /**
     * Kertoo paljonko chippejä jäljellä.
     *
     * @return chips.
     */
    public double playerChipsLeft() {
        return player.getChips();
    }

    /**
     * Paljonko AI:lla chippejä jäljellä.
     *
     * @return chips.
     */
    public double aiChipsLeft() {
        return ai.getChips();
    }

    /**
     * Viimeisin panostus.
     *
     * @return määrä.
     */
    public double lastBet() {
        if (this.bettingHistory.isEmpty()) {
            return 0.0;
        }
        return this.bettingHistory.get(bettingHistory.size() - 1);
    }

    /**
     * Pelaajan checkaamisesta tai callaamisesta aiheutuvat toiminnot peliin.
     *
     * @return Tieto pelaajan valinnasta
     */
    public String checkOrCall() {
        if (lastBet() == 0.0) {
            return "Player checks";
        } else if (bettingHistory.size() == 1) {
            double amount = player.bet(lastBet());
            addToPot(amount);
            bettingHistory.add(amount);
            if (player.getChips() == 0) {
                return "Player calls " + amount + " and is all in";
            }
            return "Player calls " + lastBet();
        } else if (lastBet() == 15) {
            return "Player checks";
        } else {
            double amount = subtractLastTwoBets();
            bettingHistory.add(amount);
            player.bet(amount);
            addToPot(amount);
            return "Player calls " + amount;
        }
    }

    /**
     * Tekoälyn callaamisesta aiheutuvat toiminnot peliin.
     *
     * @return Tieto tekoälyn valinnasta
     */
    public String aiCalls() {
        if (lastBet() == 0.0 || lastBet() == 15.0) {
            return "AI checks";
        }
        if (bettingHistory.size() == 1) {
            double amount = lastBet();
            if (ai.getChips() <= amount) {
                return aiAllIn(amount);
            } else {
                amount = ai.bet(lastBet());
                addToPot(amount);
                bettingHistory.add(amount);
                return "AI calls " + lastBet();
            }
        } else if (bettingHistory.isEmpty()) {
            return "AI checks";
        } else {
            double amount = lastBet();
            if (ai.getChips() <= amount) {
                return aiAllIn(amount);
            }
            amount = subtractLastTwoBets();
            bettingHistory.add(amount);
            ai.bet(amount);
            addToPot(amount);
            return "AI calls " + amount;
        }
    }

    /**
     * Tekoälyn panostuksesta aiheutuva toiminta.
     *
     * @param action tekoälyn valinta ja tieto, paljonko merkkejä panostaa
     * @return tekoälyn toiminnan tekstimuotoinen esitys
     */
    public String aiBetsOrRaises(String action) {
        if (action.contains("all-in")) {
            return action;
        }
        String[] parts = action.split(":");
        double amount = Double.parseDouble(parts[1]);
        if (amount >= ai.getChips()) {
            aiAllIn(amount);
        }
        if (amount > 0.0) {
            bettingHistory.add(amount);
            ai.bet(amount);
            addToPot(amount);
            return "Ai raises " + amount;
        }
        return "Ai checks.";
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
    public String aiAllIn(double amount) {
        amount = ai.getChips();
        ai.bet(amount);
        addToPot(amount);
        bettingHistory.add(amount);
        return "AI calls " + amount + " and is all in";
    }

    /**
     * Pelaaja nostaa.
     *
     * @param amount paljonko?
     * @return tesktiesitys pelaajan nostosta.
     */
    public String raise(double amount) {
        if (amount >= player.getChips()) {
            amount = player.getChips();
            bettingHistory.add(amount);
            player.bet(amount);
            addToPot(amount);
            return "Player goes all in with " + amount;
        }
        player.bet(amount);
        bettingHistory.add(amount);
        addToPot(amount);
        return "Player raises " + amount;
    }

    /**
     * AI voittaa kierroksen.
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
            return (lastBet())
                    - (bettingHistory.get(bettingHistory.size() - 2));
        } else if (bettingHistory.size() == 1) {
            return lastBet();
        }
        return 0.0;
    }

    /**
     * Ottaa selvää peliä varten pelijärjestyksestä.
     *
     * @return tieto pelijärjestyksestä
     */
    public boolean bettingOrder() {
        return player.isButton();
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
        return player.getChips() == stackSize * 2 || ai.getChips() == stackSize * 2;
    }

    public List<Double> getBettingHistory() {
        return bettingHistory;
    }

    /**
     * flop.
     *
     * @return tablecards
     */
    public String flop() {
        dealer.dealFlop();
        return showTableCards();
    }

    /**
     * turn.
     *
     * @return tablecards
     */
    public String turn() {
        dealer.dealTurn();
        return showTableCards();
    }

    /**
     * river.
     *
     * @return tablecards
     */
    public String river() {
        dealer.dealRiver();
        return showTableCards();
    }

    /**
     * Näyttää pöytäkortit.
     *
     * @return tablecards
     */
    public String showTableCards() {
        return dealer.tellTableCards();
    }

    /**
     * Palauttaa potin koon.
     *
     * @return double pot size
     */
    public double getPotSize() {
        return potSize;
    }

    /**
     * Palauttaa big blindin.
     *
     * @return bigblind double
     */
    public double getBigBlind() {
        return bigBlind;
    }

    /**
     * Asettaa big blindin.
     *
     * @param bigBlind double
     */
    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }

    /**
     * Palauttaa aloitustackin.
     *
     * @return double amount
     */
    public double getStackSize() {
        return stackSize;
    }

    /**
     * Asettaa aloitusstackin.
     *
     * @param stackSize amount double
     */
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

    /**
     * Antaa pelaajalle chippejä.
     */
    public void setPlayerChips() {
        player.setChips(getStackSize());
    }

    /**
     * Antaa AI:lle chippejä.
     */
    public void setAiChips() {
        ai.setChips(getStackSize());
    }

    /**
     * Asettaa pottikoon.
     *
     * @param potSize amound double.
     */
    public void setPotSize(double potSize) {
        this.potSize = potSize;
    }
}
