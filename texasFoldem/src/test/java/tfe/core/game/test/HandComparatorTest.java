/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.game.test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import tfe.core.cards.Card;
import tfe.core.game.HandComparator;

/**
 *
 * @author Ilari
 */
public class HandComparatorTest {

    private List<Card> playerPocketCards;
    private List<Card> aiPocketCards;
    private List<Card> tableCards;
    private HandComparator hc;

    public HandComparatorTest() {
        this.playerPocketCards = new ArrayList<>();
        this.aiPocketCards = new ArrayList<>();
        this.tableCards = new ArrayList<>();
        this.hc = new HandComparator();
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testStraightFlush() {
        tableCards.add(new Card("Hearts", 5));
        tableCards.add(new Card("Hearts", 6));
        tableCards.add(new Card("Hearts", 7));
        tableCards.add(new Card("Spades", 14));
        tableCards.add(new Card("Spades", 13));
        playerPocketCards.add(new Card("Hearts", 4));
        playerPocketCards.add(new Card("Hearts", 3));
        tableCards.addAll(playerPocketCards);
        assertTrue(hc.straightFlush(tableCards));
    }

    @Test
    public void testFlush() {
        tableCards.add(new Card("Clubs", 2));
        tableCards.add(new Card("Hearts", 4));
        tableCards.add(new Card("Diamonds", 6));
        tableCards.add(new Card("Diamonds", 8));
        tableCards.add(new Card("Diamonds", 10));
        playerPocketCards.add(new Card("Diamonds", 12));
        playerPocketCards.add(new Card("Diamonds", 14));
        tableCards.addAll(playerPocketCards);
        assertTrue(hc.flush(tableCards));
    }

    @Test
    public void testStraightMiddle() {
        tableCards.add(new Card("Hearts", 2));
        tableCards.add(new Card("Hearts", 6));
        tableCards.add(new Card("Hearts", 7));
        tableCards.add(new Card("Spades", 8));
        tableCards.add(new Card("Spades", 13));
        playerPocketCards.add(new Card("Hearts", 4));
        playerPocketCards.add(new Card("Diamonds", 5));
        tableCards.addAll(playerPocketCards);
        assertTrue(hc.straight(tableCards, 1, 5));
    }

    @Test
    public void testStraightFront() {
        tableCards.add(new Card("Hearts", 2));
        tableCards.add(new Card("Hearts", 3));
        tableCards.add(new Card("Hearts", 6));
        tableCards.add(new Card("Spades", 8));
        tableCards.add(new Card("Spades", 13));
        playerPocketCards.add(new Card("Hearts", 4));
        playerPocketCards.add(new Card("Diamonds", 5));
        tableCards.addAll(playerPocketCards);
        assertTrue(hc.straight(tableCards, 0, 4));
    }

    @Test
    public void testStraightLate() {
        tableCards.add(new Card("Hearts", 1));
        tableCards.add(new Card("Hearts", 6));
        tableCards.add(new Card("Hearts", 10));
        tableCards.add(new Card("Spades", 11));
        tableCards.add(new Card("Spades", 12));
        playerPocketCards.add(new Card("Hearts", 13));
        playerPocketCards.add(new Card("Diamonds", 14));
        tableCards.addAll(playerPocketCards);
        assertTrue(hc.straight(tableCards, 2, 6));
    }

    @Test
    public void testStraightWith6Cards() {
        tableCards.add(new Card("Hearts", 2));
        tableCards.add(new Card("Hearts", 3));
        tableCards.add(new Card("Hearts", 4));
        tableCards.add(new Card("Spades", 11));
        playerPocketCards.add(new Card("Hearts", 5));
        playerPocketCards.add(new Card("Diamonds", 6));
        tableCards.addAll(playerPocketCards);
        assertTrue(hc.straight(tableCards, 0, 5));
    }

    @Test
    public void testQuads() {
        tableCards.add(new Card("Hearts", 2));
        tableCards.add(new Card("Diamonds", 2));
        tableCards.add(new Card("Clubs", 2));
        tableCards.add(new Card("Spades", 10));
        tableCards.add(new Card("Diamonds", 4));
        playerPocketCards.add(new Card("Spades", 2));
        playerPocketCards.add(new Card("Diamonds", 14));
        tableCards.addAll(playerPocketCards);
        assertTrue(hc.quads(tableCards));
    }

    @Test
    public void testFullHouse() {
        tableCards.add(new Card("Hearts", 14));
        tableCards.add(new Card("Spades", 14));
        tableCards.add(new Card("Hearts", 7));
        tableCards.add(new Card("Spades", 8));
        tableCards.add(new Card("Spades", 13));
        playerPocketCards.add(new Card("Clubs", 8));
        playerPocketCards.add(new Card("Diamonds", 8));
        tableCards.addAll(playerPocketCards);
        assertTrue(hc.fullHouse(tableCards));
    }
    
    @Test
    public void testNotFullHouse() {
        tableCards.add(new Card("Hearts", 5));
        tableCards.add(new Card("Spades", 5));
        tableCards.add(new Card("Hearts", 9));
        tableCards.add(new Card("Spades", 8));
        tableCards.add(new Card("Spades", 13));
        playerPocketCards.add(new Card("Clubs", 2));
        playerPocketCards.add(new Card("Diamonds", 12));
        tableCards.addAll(playerPocketCards);
        assertTrue(hc.fullHouse(tableCards));
    }

    @Test
    public void testTrips() {
        tableCards.add(new Card("Hearts", 7));
        tableCards.add(new Card("Spades", 5));
        tableCards.add(new Card("Hearts", 7));
        tableCards.add(new Card("Spades", 8));
        tableCards.add(new Card("Spades", 13));
        playerPocketCards.add(new Card("Clubs", 7));
        playerPocketCards.add(new Card("Diamonds", 9));
        tableCards.addAll(playerPocketCards);
        assertTrue(hc.trips(tableCards));
    }

    @Test
    public void testPair() {
        tableCards.add(new Card("Hearts", 5));
        tableCards.add(new Card("Hearts", 9));
        tableCards.add(new Card("Hearts", 8));
        tableCards.add(new Card("Spades", 14));
        tableCards.add(new Card("Spades", 13));
        playerPocketCards.add(new Card("Clubs", 7));
        playerPocketCards.add(new Card("Hearts", 7));
        tableCards.addAll(playerPocketCards);
        assertTrue(hc.pair(tableCards));
    }

    @Test
    public void testSameConsecutiveValuesWithNone() {
        tableCards.add(new Card("Hearts", 2));
        tableCards.add(new Card("Hearts", 3));
        tableCards.add(new Card("Hearts", 4));
        tableCards.add(new Card("Spades", 5));
        tableCards.add(new Card("Spades", 6));
        playerPocketCards.add(new Card("Hearts", 7));
        playerPocketCards.add(new Card("Hearts", 8));
        tableCards.addAll(playerPocketCards);
        int x = hc.sameConsecutiveValues(tableCards);
        assertEquals(x, 1);
    }

    @Test
    public void testSameConsecutiveValuesWithPair() {
        tableCards.add(new Card("Hearts", 2));
        tableCards.add(new Card("Hearts", 3));
        tableCards.add(new Card("Hearts", 4));
        tableCards.add(new Card("Spades", 5));
        tableCards.add(new Card("Spades", 6));
        playerPocketCards.add(new Card("Hearts", 2));
        playerPocketCards.add(new Card("Hearts", 8));
        tableCards.addAll(playerPocketCards);
        int x = hc.sameConsecutiveValues(tableCards);
        assertEquals(x, 2);
    }

    @Test
    public void testSameConsecutiveValuesWithThree() {
        tableCards.add(new Card("Hearts", 2));
        tableCards.add(new Card("Hearts", 2));
        tableCards.add(new Card("Hearts", 2));
        tableCards.add(new Card("Spades", 8));
        tableCards.add(new Card("Spades", 6));
        playerPocketCards.add(new Card("Hearts", 9));
        playerPocketCards.add(new Card("Hearts", 10));
        tableCards.addAll(playerPocketCards);
        int x = hc.sameConsecutiveValues(tableCards);
        assertEquals(x, 3);
    }

    @Test
    public void testCardsToIntArray() {
        tableCards.add(new Card("Hearts", 2));
        tableCards.add(new Card("Hearts", 3));
        tableCards.add(new Card("Hearts", 4));
        tableCards.add(new Card("Spades", 5));
        tableCards.add(new Card("Spades", 6));
        playerPocketCards.add(new Card("Hearts", 7));
        playerPocketCards.add(new Card("Hearts", 8));
        tableCards.addAll(playerPocketCards);
        int[] values = hc.cardsToIntArray(tableCards);
        int[] values2 = new int[]{2, 3, 4, 5, 6, 7, 8};
        assertEquals(values[4], values2[4]);
    }

    @Test
    public void testNoPairNorBetterWith7Cards() {
        tableCards.add(new Card("Clubs", 2));
        tableCards.add(new Card("Hearts", 4));
        tableCards.add(new Card("Clubs", 6));
        tableCards.add(new Card("Spades", 8));
        tableCards.add(new Card("Spades", 10));
        playerPocketCards.add(new Card("Diamonds", 12));
        playerPocketCards.add(new Card("Diamonds", 14));
        tableCards.addAll(playerPocketCards);
        assertTrue(!hc.pairOrBetter(tableCards));
    }

    @Test
    public void testPairOrBetterWith7Cards() {
        tableCards.add(new Card("Clubs", 2));
        tableCards.add(new Card("Hearts", 4));
        tableCards.add(new Card("Clubs", 12));
        tableCards.add(new Card("Spades", 12));
        tableCards.add(new Card("Spades", 8));
        playerPocketCards.add(new Card("Diamonds", 12));
        playerPocketCards.add(new Card("Diamonds", 14));
        tableCards.addAll(playerPocketCards);
        assertTrue(hc.pairOrBetter(tableCards));
    }

    @Test
    public void testNotPairNorBetterWith6() {
        tableCards.add(new Card("Hearts", 2));
        tableCards.add(new Card("Hearts", 3));
        tableCards.add(new Card("Hearts", 4));
        tableCards.add(new Card("Spades", 8));
        playerPocketCards.add(new Card("Diamonds", 5));
        playerPocketCards.add(new Card("Clubs", 9));
        tableCards.addAll(playerPocketCards);
        assertTrue(!hc.pairOrBetter(tableCards));
    }

    @Test
    public void testPairOrBetterWith6() {
        tableCards.add(new Card("Hearts", 2));
        tableCards.add(new Card("Hearts", 3));
        tableCards.add(new Card("Hearts", 4));
        tableCards.add(new Card("Spades", 8));
        playerPocketCards.add(new Card("Clubs", 4));
        playerPocketCards.add(new Card("Hearts", 9));
        tableCards.addAll(playerPocketCards);
        assertTrue(hc.pairOrBetter(tableCards));
    }

    @Test
    public void testNotPairNorBetterWith5() {
        tableCards.add(new Card("Hearts", 2));
        tableCards.add(new Card("Hearts", 3));
        tableCards.add(new Card("Hearts", 4));
        playerPocketCards.add(new Card("Diamonds", 5));
        playerPocketCards.add(new Card("Clubs", 9));
        tableCards.addAll(playerPocketCards);
        assertTrue(!hc.pairOrBetter(tableCards));
    }

    @Test
    public void testPairOrBetterWith5() {
        tableCards.add(new Card("Hearts", 2));
        tableCards.add(new Card("Hearts", 3));
        tableCards.add(new Card("Hearts", 4));
        playerPocketCards.add(new Card("Diamonds", 2));
        playerPocketCards.add(new Card("Clubs", 9));
        tableCards.addAll(playerPocketCards);
        assertTrue(hc.pairOrBetter(tableCards));
    }

    @Test
    public void testFlushOrBetter() {
        tableCards.add(new Card("Hearts", 2));
        tableCards.add(new Card("Hearts", 3));
        tableCards.add(new Card("Hearts", 4));
        tableCards.add(new Card("Spades", 4));
        tableCards.add(new Card("Diamonds", 4));
        playerPocketCards.add(new Card("Hearts", 2));
        playerPocketCards.add(new Card("Hearts", 9));
        tableCards.addAll(playerPocketCards);
        assertTrue(hc.flushOrBetter(tableCards));
    }
}
