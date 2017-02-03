package tfe.core.game.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import tfe.core.cards.Card;
import tfe.core.cards.PackOfCards;
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
}
