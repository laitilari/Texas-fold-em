/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.cards.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tfe.core.cards.Card;

/**
 *
 * @author ilarilai
 */
public class CardTest {

    public CardTest() {
    }

    @Before
    public void setUp() {

    }

    @Test
    public void testValue() {
        Card card = new Card("Spade", 14);
        assertEquals(card.getValue(), 14);
    }

    @Test
    public void testSuit() {
        Card card = new Card("Spade", 14);
        assertEquals(card.getSuit(), "Spade");
    }

    @Test
    public void testAce() {
        Card card = new Card("Spade", 14);
        String aString = card.toString();
        assertTrue(aString.contains("Ace"));
    }

    @Test
    public void testJack() {
        Card card = new Card("Spade", 11);
        String aString = card.toString();
        assertTrue(aString.contains("Jack"));
    }

    @Test
    public void testQueen() {
        Card card = new Card("Spade", 12);
        String aString = card.toString();
        assertTrue(aString.contains("Queen"));
    }

    @Test
    public void testKing() {
        Card card = new Card("Spade", 13);
        String aString = card.toString();
        assertTrue(aString.contains("King"));
    }

}
