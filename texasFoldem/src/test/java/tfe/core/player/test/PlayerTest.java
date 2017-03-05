/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.player.test;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import tfe.core.cards.Card;
import tfe.core.player.Player;

/**
 *
 * @author Ilari
 */
public class PlayerTest {

    Player player;
    double chips;

    public PlayerTest() {
    }

    @Before
    public void setUp() {
        player = new Player();
        player.setChips(100.0);
    }

    @Test
    public void testBetTakesChips() {
        double bet = 10.0;
        player.bet(bet);
        assertEquals(player.getChips(), 90, 0.1);
    }

    @Test
    public void testCantBetTooMuch() {
        double bet = 110.0;
        player.bet(bet);
        assertEquals(player.getChips(), 0, 1);
    }

    @Test
    public void testBetSmallBlind() {
        player.betSmallBlind(15);
        assertEquals(player.getChips(), 85, 0.1);
    }
    
    @Test
    public void testSmallBlindDoesntGoUnderZero() {
        player.betSmallBlind(150.0);
        assertEquals(player.getChips(), 0.0, 0);
    }
    
    @Test
    public void testBigBlindDoesntGoUnderZero() {
        player.betBigBlind(150.0);
        assertEquals(player.getChips(), 0.0, 0);
    }

    @Test
    public void testBetBigBlind() {
        player.betSmallBlind(30);
        assertEquals(player.getChips(), 70, 0.1);
    }

    @Test
    public void testWinChips() {
        player.winChips(50);
        assertEquals(player.getChips(), 150, 0.1);
    }

    @Test
    public void testDrawPocketCards() {
        List<Card> list = new ArrayList<>();
        list.add(new Card("Spade", 14));
        list.add(new Card("Heart", 14));
        player.drawPocketCards(list);
        assertEquals(player.getPocketCards(), list);
    }

    @Test
    public void testButtonChange() {
        assertTrue(!player.isButton());
        player.buttonChange();
        assertTrue(player.isButton());
    }
    
    @Test
    public void testGetChips() {
        assertEquals(player.getChips(), 100.0, 0);
    }
    
    @Test
    public void testSetChips() {
        player.setChips(50.0);
        assertEquals(player.getChips(), 50.0, 0);
    }
    
    @Test
    public void testGetHand() {
        List<Card> list = new ArrayList<>();
        List<Card> list2 = new ArrayList<>();
        Card c = new Card("Spade", 2);
        Card c2 = new Card("Spade", 3);
        list.add(c);
        list2.add(c2);
        player.drawPocketCards(list);
        assertTrue(player.getHand(list2).size() == 2);
    }

}
