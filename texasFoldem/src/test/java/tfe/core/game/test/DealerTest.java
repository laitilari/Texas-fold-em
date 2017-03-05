package tfe.core.game.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import tfe.core.cards.Card;
import tfe.core.cards.PackOfCards;
import tfe.core.cards.TableCards;
import tfe.core.game.Dealer;
import tfe.core.player.Player;

/**
 *
 * @author ilarilai
 */
public class DealerTest {

    Dealer dealer;
    PackOfCards pack;

    public DealerTest() {
    }

    @Before
    public void setUp() {
        dealer = new Dealer();
        this.pack = new PackOfCards();
        pack.assemblePack();
    }

    @Test
    public void testDealPocketCards() {
        Player player = new Player();
        List<Card> playerCards = pack.takeMany(2);
        player.drawPocketCards(playerCards);
        assertEquals(playerCards, player.getPocketCards());
    }

    @Test
    public void testAssemblePack() {
        pack.assemblePack();
        assertEquals(pack.getCards().size(), 52);
    }

    @Test
    public void testShufflePack() {
        ArrayList<Card> first = new ArrayList<>();
        ArrayList<Card> second = new ArrayList<>();
        Card card = pack.getCards().get(0);
        Card card2 = pack.getCards().get(1);
        Card card3 = pack.getCards().get(2);
        first.add(card);
        first.add(card2);
        first.add(card3);
        pack.shuffle();
        card = pack.getCards().get(0);
        card2 = pack.getCards().get(1);
        card3 = pack.getCards().get(2);
        second.add(card);
        second.add(card2);
        second.add(card3);
        assertNotEquals(first, second);
    }

    @Test
    public void testReAssemble() {
        pack.takeOne();
        pack.reAssemblePack();
        assertEquals(pack.getCards().size(), 52);
    }
    
    @Test
    public void testAddToTable() {
        TableCards tc = new TableCards();
        Card c = new Card("Spades", 14);
        tc.drawCard(c);
        assertEquals(tc.getCards().get(0), c);
    }
    
    @Test
    public void testClearTable() {
        TableCards tc = new TableCards();
        Card c = new Card("Spades", 14);
        tc.drawCard(c);
        tc.getCards().clear();
        assertEquals(tc.getCards().size(), 0);
    }
}
