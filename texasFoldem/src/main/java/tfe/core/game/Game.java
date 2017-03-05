package tfe.core.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import tfe.core.ai.Ai;
import tfe.core.cards.Card;
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
    private HandComparator hc;
    private boolean allIn;

    /**
     * Luodaan pelissä tarvittavat oliot ja alustetaan tarvittavat muuttujat.
     */
    public Game() {
        this.player = new Player();
        this.ai = new Ai();
        this.dealer = new Dealer();
        this.bigBlind = 30;
        this.stackSize = 0.0;
        this.potSize = 0.0;
        this.bettingHistory = new ArrayList<>();
        this.hc = new HandComparator();
        this.allIn = false;
    }

    /**
     * Valmistelee korttipakan ja asettaa pelimerkit.
     */
    public void prepareGame() {
        this.allIn = false;
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
     * @return winner
     */
    public String showDown() {
        List<Card> playerCards = player.getHand(dealer.getTableCards());
        List<Card> aiCards = ai.getHand(dealer.getTableCards());
        return compareHands(playerCards, aiCards);
    }

    /**
     * Highest value of arrays.
     *
     * @param playerValues player cards
     * @param aiValues ai cards
     * @param hand hand
     * @return string
     */
    public String highestCard(int[] playerValues, int[] aiValues, String hand) {
        hand = "higher kicker";
        if (playerValues[playerValues.length - 1] > aiValues[aiValues.length - 1]) {
            return playerWinsRoundAtShowDown();
        } else if (aiValues[aiValues.length - 1] > playerValues[playerValues.length - 1]) {
            return aiWinsRoundAtShowDown();
        } else {
            return secondHighCard(playerValues, aiValues, hand);
        }
    }

    /**
     * Second highest value of arrays.
     *
     * @param playerValues player cards
     * @param aiValues ai cards
     * @param hand hand
     * @return string
     */
    public String secondHighCard(int[] playerValues, int[] aiValues, String hand) {
        if (playerValues[playerValues.length - 2] > aiValues[aiValues.length - 2]) {
            return playerWinsRoundAtShowDown();
        } else if (aiValues[aiValues.length - 2] > playerValues[playerValues.length - 2]) {
            return aiWinsRoundAtShowDown();
        } else {
            return thirdHighCard(playerValues, aiValues, hand);
        }
    }

    /**
     * Third highest value of arrays.
     *
     * @param playerValues player cards
     * @param aiValues ai cards
     * @param hand hand
     * @return string
     */
    public String thirdHighCard(int[] playerValues, int[] aiValues, String hand) {
        if (playerValues[playerValues.length - 3] > aiValues[aiValues.length - 3]) {
            return playerWinsRoundAtShowDown();
        } else if (aiValues[aiValues.length - 3] > playerValues[playerValues.length - 3]) {
            return aiWinsRoundAtShowDown();
        } else {
            return fourthHighCard(playerValues, aiValues, hand);
        }
    }

    /**
     * Fourth highest value of arrays.
     *
     * @param playerValues player cards
     * @param aiValues ai cards
     * @param hand hand
     * @return string
     */
    public String fourthHighCard(int[] playerValues, int[] aiValues, String hand) {
        if (playerValues[playerValues.length - 4] > aiValues[aiValues.length - 4]) {
            return playerWinsRoundAtShowDown();
        } else if (aiValues[aiValues.length - 4] > playerValues[playerValues.length - 4]) {
            return aiWinsRoundAtShowDown();
        } else {
            return fifthHighCard(playerValues, aiValues, hand);
        }
    }

    /**
     * Fifth highest value of arrays.
     *
     * @param playerValues player cards
     * @param aiValues ai cards
     * @param hand hand
     * @return string
     */
    public String fifthHighCard(int[] playerValues, int[] aiValues, String hand) {
        if (playerValues[playerValues.length - 5] > aiValues[aiValues.length - 5]) {
            return playerWinsRoundAtShowDown();
        } else if (aiValues[aiValues.length - 5] > playerValues[playerValues.length - 5]) {
            return aiWinsRoundAtShowDown();
        } else {
            return splitPot();
        }
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
        addToPot(bigBlind);
        addToPot(bigBlind / 2);
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
        return "Your pocket cards: " + player.getPocketCards().toString();
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
        if (lastBet() == 0.0 || lastBet() == 15.0) {
            return "Player checks";
        }
        if (bettingHistory.size() == 1) {
            double amount = lastBet();
            if (player.getChips() <= amount) {
                return playerAllIn(amount);
            } else {
                amount = player.bet(lastBet());
                addToPot(amount);
                bettingHistory.add(amount);
                return "Player calls " + lastBet();
            }
        } else if (bettingHistory.isEmpty()) {
            return "Player checks";
        } else {
            double amount = lastBet();
            if (player.getChips() <= amount) {
                return playerAllIn(amount);
            }
            amount = subtractLastTwoBets();
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
            return aiAllIn(ai.getChips());
        }
        String[] parts = action.split(":");
        double amount = Double.parseDouble(parts[1]);
        if (amount >= ai.getChips()) {
            double x = ai.getChips();
            ai.bet(x);
            addToPot(x);
            bettingHistory.add(x);
            return aiAllIn(x);
        }
        if (amount > 0.0) {
            if (amount < lastBet()) {
                amount = 2 * lastBet();
            }
            if (amount >= ai.getChips()) {
                double x = ai.getChips();
                ai.bet(x);
                addToPot(x);
                bettingHistory.add(x);
                return aiAllIn(x);
            }
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
     * @param amount amount
     * @return tekstiesitys AI:n valinnasta
     */
    public String aiAllIn(double amount) {
        amount = ai.getChips();
        ai.bet(amount);
        addToPot(amount);
        bettingHistory.add(amount);
        return "AI is all in with " + amount;
    }

    /**
     * Player all-in.
     *
     * @param amount double
     * @return string
     */
    public String playerAllIn(double amount) {
        amount = player.getChips();
        player.bet(amount);
        addToPot(amount);
        bettingHistory.add(amount);
        return "Player is all in with " + amount;
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
     * AI voittaa kierroksen showdownilla.
     *
     * @return tekstiesitys
     */
    public String aiWinsRoundAtShowDown() {
        ai.winChips(potSize);
        return "AI wins the pot";
    }

    /**
     * Pelaaja voittaa kierroksen.
     *
     * @see #aiWinsRound()
     * @return tekstiesitys
     */
    public String playerWinsRoundAtShowDown() {
        player.winChips(potSize);
        return "Player wins the pot";
    }

    /**
     * Split pot.
     * @return string
     */
    public String splitPot() {
        player.winChips(potSize / 2);
        ai.winChips(potSize / 2);
        return "Player and AI split the pot";
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

    /**
     * Sets that both are all in.
     */
    public void allIn() {
        this.allIn = true;
    }

    /**
     * Returns both all in status.
     *
     * @return boolean
     */
    public boolean getAllIn() {
        return this.allIn;
    }

    /**
     * Determines who won the game.
     *
     * @return winner
     */
    public String whoWon() {
        if (player.getChips() == stackSize * 2) {
            return "You win!";
        }
        return "Ai wins the game.";
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

    /**
     * Comapre higher pair.
     *
     * @param playerCards cards
     * @param aiCards cards
     * @return highest value
     */
    public String higherPair(List<Card> playerCards, List<Card> aiCards) {
        ArrayList<Integer> aiValues = new ArrayList<>();
        ArrayList<Integer> playerValues = new ArrayList<>();
        for (Card c : playerCards) {
            playerValues.add(c.getValue());
        }
        for (Card c : aiCards) {
            aiValues.add(c.getValue());
        }
        int helper = 0;
        int playerHighest = 0;
        Collections.sort(aiValues);
        Collections.sort(playerValues);
        for (int i : playerValues) {
            if (i == helper) {
                playerHighest = helper;
            }
            helper = i;
        }
        helper = 0;
        int aiHighest = 0;
        for (int i : aiValues) {
            if (i == helper) {
                aiHighest = helper;
            }
            helper = i;
        }
        if (playerHighest > aiHighest) {
            return playerWinsRoundAtShowDown();
        } else if (playerHighest < aiHighest) {
            return aiWinsRoundAtShowDown();
        } else {
            return compareHighCards(playerCards, aiCards);
        }
    }

    /**
     * Compare the hands.
     *
     * @param playerCards cards
     * @param aiCards cards
     * @return string
     */
    public String compareHands(List<Card> playerCards, List<Card> aiCards) {
        String hand = "";
        if (hc.straightFlush(aiCards) && hc.straightFlush(playerCards)) {
            return compareHighCards(playerCards, aiCards);
        } else if (!hc.straightFlush(aiCards) && hc.straightFlush(playerCards)) {
            hand = "straight flush";
            return playerWinsRoundAtShowDown();
        } else if (hc.straightFlush(aiCards) && !hc.straightFlush(playerCards)) {
            hand = "straight flush";
            return aiWinsRoundAtShowDown();

        } else if (hc.quads(aiCards) && hc.quads(playerCards)) {
            return compareHighCards(playerCards, aiCards);
        } else if (!hc.quadsOrBetter(aiCards) && hc.quadsOrBetter(playerCards)) {
            hand = "quads";
            return playerWinsRoundAtShowDown();
        } else if (hc.quadsOrBetter(aiCards) && !hc.quadsOrBetter(playerCards)) {
            hand = "quads";
            return aiWinsRoundAtShowDown();

        } else if (hc.fullHouse(aiCards) && hc.fullHouse(playerCards)) {
            return compareHighCards(playerCards, aiCards);
        } else if (!hc.fullHouseOrBetter(aiCards) && hc.fullHouseOrBetter(playerCards)) {
            hand = "full house";
            return playerWinsRoundAtShowDown();
        } else if (hc.fullHouseOrBetter(aiCards) && !hc.fullHouseOrBetter(playerCards)) {
            hand = "full house";
            return aiWinsRoundAtShowDown();

        } else if (hc.flush(aiCards) && hc.flush(playerCards)) {
            return compareHighCards(playerCards, aiCards);
        } else if (!hc.flushOrBetter(aiCards) && hc.flushOrBetter(playerCards)) {
            hand = "flush";
            return playerWinsRoundAtShowDown();
        } else if (hc.flushOrBetter(aiCards) && !hc.flushOrBetter(playerCards)) {
            hand = "flush";
            return aiWinsRoundAtShowDown();

        } else if (hc.straightOrBetter(aiCards) && hc.straightOrBetter(playerCards)) {
            if (!hc.flushOrBetter(aiCards) && !hc.flushOrBetter(playerCards)) {
                return compareHighCards(playerCards, aiCards);
            }
        } else if (!hc.straightOrBetter(aiCards) && hc.straightOrBetter(playerCards)) {
            hand = "straight";
            return playerWinsRoundAtShowDown();
        } else if (hc.straightOrBetter(aiCards) && !hc.straightOrBetter(playerCards)) {
            hand = "straight";
            return aiWinsRoundAtShowDown();

        } else if (hc.trips(aiCards) && hc.trips(playerCards)) {
            return higherPair(playerCards, aiCards);
        } else if (!hc.trips(aiCards) && hc.tripsOrBetter(playerCards)) {
            hand = "trips";
            return playerWinsRoundAtShowDown();
        } else if (hc.trips(playerCards) && !hc.tripsOrBetter(aiCards)) {
            hand = "trips";
            return aiWinsRoundAtShowDown();

        } else if (hc.pair(playerCards) && hc.pair(aiCards)) {
            return higherPair(playerCards, aiCards);
        } else if (!hc.pairOrBetter(aiCards) && hc.pairOrBetter(playerCards)) {
            hand = "pair";
            return playerWinsRoundAtShowDown();
        } else if (hc.pairOrBetter(aiCards) && !hc.pairOrBetter(playerCards)) {
            hand = "pair";
            return aiWinsRoundAtShowDown();
        }
        return compareHighCards(playerCards, aiCards);
    }

    /**
     * Compare high cards.
     *
     * @param playerCards cards
     * @param aiCards cards
     * @return string
     */
    public String compareHighCards(List<Card> playerCards, List<Card> aiCards) {
        String hand = "";
        int[] playerValues = hc.cardsToIntArray(playerCards);
        int[] aiValues = hc.cardsToIntArray(aiCards);
        return highestCard(playerValues, aiValues, hand);
    }

}
