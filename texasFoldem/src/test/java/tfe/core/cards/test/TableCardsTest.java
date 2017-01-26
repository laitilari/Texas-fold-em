/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.cards.test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import tfe.core.cards.Card;
import tfe.core.cards.TableCards;

/**
 *
 * @author ilarilai
 */
public class TableCardsTest {

    TableCards tableCards;

    public TableCardsTest() {
    }

    @Before
    public void setUp() {
        tableCards = new TableCards();
    }

    @Test
    public void testDrawCard() {
        Card card = new Card("Spade", 14);
        tableCards.drawCard(card);
        assertEquals(tableCards.getCards().size(), 1);
    }

    @Test
    public void testFlop() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Card card = new Card("Spade", 14);
            cards.add(card);
        }
        tableCards.drawFlop(cards);
        assertEquals(tableCards.getCards().size(), 3);
    }

}
