/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.cards.test;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tfe.core.cards.Card;
import tfe.core.cards.PackOfCards;

/**
 *
 * @author ilarilai
 */
public class PackOfCardsTest {

    PackOfCards pack;

    public PackOfCardsTest() {

    }

    @Before
    public void setUp() {
        pack = new PackOfCards();
        pack.assemblePack();
    }

    @Test
    public void testAssemblesPack() {
        pack.assemblePack();
        assertEquals(pack.getCards().size(), 104);
    }

    @Test
    public void testTakeOne() {
        pack.takeOne();
        assertEquals(pack.getCards().size(), 51);
    }

    @Test
    public void testTakeMany() {
        pack.takeMany(12);
        assertEquals(pack.getCards().size(), 40);
    }

    @Test
    public void testTakenCardGoesToRemoved() {
        assertEquals(pack.getRemovedCards().size(), 0);
        pack.takeOne();
        assertEquals(pack.getRemovedCards().size(), 1);
    }

    @Test
    public void testReAssembles() {
        pack.takeOne();
        pack.reAssemblePack();
        assertEquals(pack.getCards().size(), 52);
    }

    @Test
    public void testRemovedCleared() {
        pack.takeMany(5);
        pack.reAssemblePack();
        assertTrue(pack.getRemovedCards().isEmpty());
    }

    @Test
    public void testShuffle() {
        Card card = pack.getCards().get(0);
        pack.shuffle();
        Card card2 = pack.getCards().get(0);
        assertNotEquals(card, card2);
    }

}
