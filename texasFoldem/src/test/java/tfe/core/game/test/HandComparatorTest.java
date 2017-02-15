/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.game.test;

import java.util.ArrayList;
import java.util.Collections;
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
        this.hc = new HandComparator(playerPocketCards,
                aiPocketCards, tableCards);
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testStraightFlush() {
        tableCards.add(new Card("Hearts", 5));
        tableCards.add(new Card("Hearts", 6));
        tableCards.add(new Card("Hearts", 7));
        playerPocketCards.add(new Card("Hearts", 4));
        playerPocketCards.add(new Card("Hearts", 3));
        tableCards.addAll(playerPocketCards);
        assertTrue(hc.straightFlush(tableCards));
    }

    @Test
    public void testStraight() {
        tableCards.add(new Card("Hearts", 5));
        tableCards.add(new Card("Hearts", 6));
        tableCards.add(new Card("Hearts", 7));
        playerPocketCards.add(new Card("Hearts", 4));
        playerPocketCards.add(new Card("Diamonds", 3));
        tableCards.addAll(playerPocketCards);
        assertTrue(hc.straight(tableCards));
    }

//    @Test
//    public void testPair() {
//        tableCards.add(new Card("Hearts", 5));
//        tableCards.add(new Card("Hearts", 6));
//        tableCards.add(new Card("Hearts", 7));
//        playerPocketCards.add(new Card("Hearts", 5));
//        playerPocketCards.add(new Card("Diamonds", 3));
//        tableCards.addAll(playerPocketCards);
//        assertTrue(hc.pair(tableCards));
//    }
    
    @Test
    public void testSameConsecutiveValues() {
        
    }
    
    @Test
    public void testCardsToIntArray() {
        
    }

}
