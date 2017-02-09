package tfe.core.game;

import java.util.List;
import tfe.core.ai.Ai;
import tfe.core.cards.Card;
import tfe.core.cards.PackOfCards;
import tfe.core.cards.TableCards;
import tfe.core.player.Player;

/**
 * Luokka on apuluokka, joka on luotu siitä syystä, että haluttiin luoda 
 * korttipakalle joku käyttäjä. Näin vältytään tilanteelta,
 * jossa luokissa kutsuttaisiin korttipakkaa jakamaan 
 * kortteja itsestään. Luokka myös lisää koodin luettavuutta. Vrt. esim: "dealer.
 * dealFlop() vs. pack.takeFlopCardsAndPutOnTable().
 */
public class Dealer {

    private PackOfCards pack;
    private TableCards table;

    public Dealer() {
        this.pack = new PackOfCards();
        this.table = new TableCards();
    }

    public void assemblePack() {
        pack.assemblePack();
    }

    public void shufflePack() {
        pack.shuffle();
    }

    public void reAssemblePack() {
        pack.reAssemblePack();
    }

    public void clearTable() {
        table.getCards().clear();
    }

    /**
     * Jakaa käsikortit pelaajalle ja AI:lle.
     * @param player pelaaja jolle jaetaan
     * @param ai AI jolle jaetaan
     */
    public void dealPocketCards(Player player, Ai ai) {
        List<Card> playerCards = pack.takeMany(2);
        List<Card> aiCards = pack.takeMany(2);
        player.drawPocketCards(playerCards);
        ai.drawPocketCards(aiCards);
    }

    /**
     * Jakaa flopin kortit pöydälle.
     * @see #dealTurn()
     * @see #dealRiver()
     */
    public void dealFlop() {
        List<Card> flop = pack.takeMany(3);
        table.drawFlop(flop);
    }

    public void dealTurn() {
        table.drawCard(dealCard());
    }

    public void dealRiver() {
        table.drawCard(dealCard());
    }

    /**
     * Nostaa ja jakaa pakasta yhden kortin.
     * @return kortti
     */
    public Card dealCard() {
        Card card = pack.takeOne();
        return card;
    }

    /**
     * Lukee pelaajalle, mitkä kortit pöydällä on.
     * @return tekstiesitys pöytäkorteista
     */
    public String tellTableCards() {
        return table.getCards().toString();
    }

    public List<Card> getTableCards() {
        return table.getCards();
    }
}
