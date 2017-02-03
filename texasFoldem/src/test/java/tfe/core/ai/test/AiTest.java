/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.ai.test;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tfe.core.ai.Ai;
import tfe.core.cards.Card;

/**
 *
 * @author Ilari
 */
public class AiTest {

    Ai ai;

    public AiTest() {
    }

    @Before
    public void setUp() {
        this.ai = new Ai();
    }

    @Test
    public void testBetRandomized() {
        double x = ai.betRandomized();
        double y = ai.betRandomized();
        assertNotEquals(x, y);
    }

    @Test
    public void testGetHand() {
        List<Card> list = new ArrayList<>();
        List<Card> list2 = new ArrayList<>();
        Card c = new Card("Spade", 2);
        Card c2 = new Card("Spade", 3);
        list.add(c);
        list2.add(c2);
        ai.drawPocketCards(list);
        assertTrue(ai.getHand(list2).size() == 2);
    }

    @Test
    public void testNotPremium() {
        List<Card> list = new ArrayList<>();
        Card c = new Card("Spade", 2);
        Card c2 = new Card("Spade", 3);
        list.add(c);
        list.add(c2);
        ai.drawPocketCards(list);
        assertTrue(!ai.premium());
    }

    @Test
    public void testPremiumWithPair() {
        List<Card> list = new ArrayList<>();
        Card c = new Card("Spade", 13);
        Card c2 = new Card("Heart", 13);
        list.add(c);
        list.add(c2);
        ai.drawPocketCards(list);
        assertTrue(ai.premium());
    }

    @Test
    public void testPremiumWithOutPair() {
        List<Card> list = new ArrayList<>();
        Card c = new Card("Spade", 14);
        Card c2 = new Card("Heart", 13);
        list.add(c);
        list.add(c2);
        ai.drawPocketCards(list);
        assertTrue(ai.premium());
    }

    @Test
    public void testNotGood() {
        List<Card> list = new ArrayList<>();
        Card c = new Card("Spade", 2);
        Card c2 = new Card("Spade", 3);
        list.add(c);
        list.add(c2);
        ai.drawPocketCards(list);
        assertTrue(!ai.premium());
    }

    @Test
    public void testGoodWithPair() {
        List<Card> list = new ArrayList<>();
        Card c = new Card("Spade", 9);
        Card c2 = new Card("Heart", 9);
        list.add(c);
        list.add(c2);
        ai.drawPocketCards(list);
        assertTrue(ai.good());
    }

    @Test
    public void testGoodWithOutPair() {
        List<Card> list = new ArrayList<>();
        Card c = new Card("Spade", 14);
        Card c2 = new Card("Heart", 10);
        list.add(c);
        list.add(c2);
        ai.drawPocketCards(list);
        assertTrue(ai.good());
    }
}
