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
import tfe.core.cards.PackOfCards;
import tfe.core.game.Game;

/**
 *
 * @author ilarilai
 */
public class GameTest {

    Game game;

    public GameTest() {
    }

    @Before
    public void setUp() {
        game = new Game();
        game.setBigBlind(30);
        game.setStackSize(1000);
    }
    
    @Test
    public void testAllIN() {
        assertTrue(!game.getAllIn());
    }
    
    @Test
    public void testPrepareGame() {
        game.prepareGame();
        assertTrue(!game.getAllIn());
        assertEquals(game.getPlayer().getChips(), 1000.0, 0);
        assertEquals(game.getAi().getChips(), 1000.0, 0);
    }

    @Test
    public void testBettingHistory() {
        game.getBettingHistory().add(15.0);
        assertEquals(game.getBettingHistory().get(0), 15, 0.1);
    }

    @Test
    public void testClearBettingHistory() {
        game.getBettingHistory().add(15.0);
        game.clearBettingHistory();
        assertTrue(game.getBettingHistory().isEmpty());
    }

    @Test
    public void testAddToPot() {
        game.addToPot(50);
        assertEquals(game.getPotSize(), 50, 0.1);
    }

    @Test
    public void testClearPot() {
        game.addToPot(50);
        game.clearPot();
        assertTrue(game.getPotSize() == 0);
    }

    @Test
    public void testAddBlindsToPot() {
        game.addBlindsToPot();
        assertEquals(game.getPotSize(), 45, 0.1);
    }

    @Test
    public void testBlinds() {
        String x = game.blinds();
        if (game.bettingOrder()) {
            assertEquals(x, "AI bets big blind (" + 30 + ")"
                    + ", you bet small blind (" + 15 + ")");
        }
        game.buttonChange();
        if (!game.bettingOrder()) {
            assertEquals(x, "You bet big blind (" + 30 + ")"
                    + ", AI bets small blind (" + 15 + ")");
        }
    }

    @Test
    public void testCheckOrCallLastBet0() {
        game.getBettingHistory().add(0.0);
        String x = game.checkOrCall();
        assertEquals(x, "Player checks");
    }

    @Test
    public void testCheckOrCallOneBetBefore() {
        game.getPlayer().setChips(100);
        game.getBettingHistory().add(50.0);
        assertEquals(game.checkOrCall(), "Player calls 50.0");
    }

    @Test
    public void testCheckOrCallIfPreFlop() {
        game.getPlayer().setChips(100);
        game.getBettingHistory().add(15.0);
        game.getBettingHistory().add(30.0);
        game.getBettingHistory().add(15.0);
        assertEquals(game.checkOrCall(), "Player checks");
    }

    @Test
    public void testCheckOrCallPreFlopWithRaise() {
        game.getBettingHistory().add(15.0);
        game.getBettingHistory().add(30.0);
        game.getBettingHistory().add(100.0);
        game.getPlayer().setChips(500);
        assertEquals(game.checkOrCall(), "Player calls 70.0");
    }

    @Test
    public void testCheckOrCallMultipleBetsAfterFlop() {
        Card card = new Card("Spades", 5);
        game.getDealer().getTableCards().add(card);
        game.getPlayer().setChips(500);
        game.getBettingHistory().add(15.0);
        game.getBettingHistory().add(30.0);
        game.getBettingHistory().add(100.0);
        assertEquals(game.checkOrCall(), "Player calls 70.0");
    }

    @Test
    public void testPlayerCallsAndGoesAllIn() {
        game.getPlayer().setChips(50.0);
        game.getBettingHistory().add(50.0);
        assertEquals(game.checkOrCall(), "Player is all in with 50.0");
    }

    @Test
    public void testSubstractLastTwoBets() {
        game.getBettingHistory().add(0.5);
        game.getBettingHistory().add(1.0);
        game.getBettingHistory().add(2.0);
        game.getBettingHistory().add(5.0);
        double x = game.subtractLastTwoBets();
        assertEquals(x, 3.0, 0.0);
    }

    @Test
    public void testSubstractLastTwoBetsReturnsLastBetIfOnlyOneBet() {
        game.getBettingHistory().add(0.5);
        assertEquals(game.subtractLastTwoBets(), 0.5, 0.1);
    }

    @Test
    public void testSubstractLastTwoBetsReturnsZeroIfHistoryEmpty() {
        assertEquals(game.subtractLastTwoBets(), 0.0, 0.0);
    }

    @Test
    public void testEndNotTrue() {
        assertTrue(!game.end());
        game.end();
        game.getPlayer().setChips(2000.0);
        assertTrue(game.end());
    }
    
        @Test
    public void testEndTrue() {
        assertTrue(!game.end());
        game.end();
        game.getPlayer().setChips(2000.0);
        assertTrue(game.end());
    }

    @Test
    public void testGetBettingHistory() {
        List<Double> bettingHistory2 = new ArrayList<>();
        bettingHistory2 = game.getBettingHistory();
        assertEquals(game.getBettingHistory(), bettingHistory2);
    }

    @Test
    public void testSetPotSize() {
        game.setPotSize(100);
        assertEquals(game.getPotSize(), 100, 0.1);
    }

    @Test
    public void testAiBetsOrRaises() {
        String given = "Ai bets:42.0";
        game.getAi().setChips(50.0);
        assertEquals(game.aiBetsOrRaises(given), "Ai raises 42.0");    
    }
    
    @Test
    public void testPlayerInPosition() {
        assertEquals(game.playerInPosition(), "You bet big blind (" + 30.0 + ")"
                + ", AI bets small blind (" + 30.0 / 2 + ")");
    }
    
    @Test
    public void testShuffle() {
        assertEquals(game.shuffle(), "Shuffling...");
    }
    
    @Test
    public void testAiInPosition() {
        assertEquals(game.aiInPosition(), "AI bets big blind (" + 30.0 + ")"
                + ", you bet small blind (" + 30.0 / 2 + ")");
    }

    @Test
    public void testPlayerAllIn() {
        game.playerAllIn();
        assertEquals(game.getPotSize(), game.playerChipsLeft(), 0.1);
        assertEquals(game.getBettingHistory()
                .get(game.getBettingHistory().size() - 1),
                game.playerChipsLeft(), 0.1);
    }

    @Test
    public void testAiAllIn() {
        double amount = 50.0;
        game.getAi().setChips(49.0);
        assertEquals(game.aiAllIn(amount), "AI is all in with " + 49.0);
    }

    @Test
    public void testRaise() {
        game.getPlayer().setChips(2000);
        double amount = 600.0;
        assertEquals(game.raise(amount), "Player raises " + amount);
    }

    @Test
    public void testRaiseWithAllIn() {
        game.getPlayer().setChips(500);
        double amount = 600.0;
        assertEquals(game.raise(amount), "Player goes all in with " + 500.0);
    }

    @Test
    public void testAiWinsRound() {
        assertEquals(game.aiWinsRound(), "AI wins the pot");
    }

    @Test
    public void testPlayerWinsRound() {
        assertEquals(game.playerWinsRound(), "Player wins the pot");
    }

    @Test
    public void testAiChecks() {
        game.getBettingHistory().add(0.0);
        assertEquals(game.aiCalls(), "AI checks");
    }

    @Test
    public void testAiChecksPreFlop() {
        game.getBettingHistory().add(15.0);
        game.getBettingHistory().add(30.0);
        game.getBettingHistory().add(15.0);
        assertEquals(game.aiCalls(), "AI checks");
    }

    @Test
    public void testAiCallsIfOnlyOneBetBefore() {
        game.getAi().setChips(500);
        game.getBettingHistory().add(59.0);
        assertEquals(game.aiCalls(), "AI calls 59.0");
    }

    @Test
    public void testAiCallsMultipleBets() {
        game.getAi().setChips(500);
        game.getBettingHistory().add(50.0);
        game.getBettingHistory().add(100.0);
        game.getBettingHistory().add(200.0);
        assertEquals(game.aiCalls(), "AI calls 100.0");
    }

    @Test
    public void testAiCallsAndGoesAllIn() {
        game.getAi().setChips(50.0);
        game.getBettingHistory().add(50.0);
        assertEquals(game.aiCalls(), "AI is all in with 50.0");
    }

    @Test
    public void testBettingOrder() {
        if (game.getPlayer().isButton()) {
            assertTrue(game.bettingOrder());
        }
        if (!(game.getPlayer().isButton())) {
            assertTrue(!game.bettingOrder());
        }
    }
    
    @Test
    public void testHigherPair() {
        ArrayList<Card> tableCards = new ArrayList<>();
        ArrayList<Card> playerPocketCards = new ArrayList<>();
        tableCards.add(new Card("Hearts", 7));
        tableCards.add(new Card("Hearts", 10));
        tableCards.add(new Card("Spades", 2));
        tableCards.add(new Card("Spades", 9));
        tableCards.add(new Card("Diamonds", 5));
        
        playerPocketCards.add(new Card("Diamonds", 7));
        playerPocketCards.add(new Card("Clubs", 6));
        playerPocketCards.addAll(tableCards);
        
        ArrayList<Card> aiPocketCards = new ArrayList<>();
        aiPocketCards.add(new Card("Diamonds", 14));
        aiPocketCards.add(new Card("Clubs", 5));
        aiPocketCards.addAll(tableCards);
        assertEquals(game.higherPair(playerPocketCards, aiPocketCards),
                "Player wins the pot");
    }
    
    @Test
    public void testSecondHighestCard() {
        ArrayList<Card> tableCards = new ArrayList<>();
        ArrayList<Card> playerPocketCards = new ArrayList<>();
        tableCards.add(new Card("Hearts", 7));
        tableCards.add(new Card("Hearts", 10));
        tableCards.add(new Card("Spades", 2));
        tableCards.add(new Card("Spades", 9));
        tableCards.add(new Card("Diamonds", 14));
        
        playerPocketCards.add(new Card("Diamonds", 14));
        playerPocketCards.add(new Card("Clubs", 13));
        playerPocketCards.addAll(tableCards);
        
        ArrayList<Card> aiPocketCards = new ArrayList<>();
        aiPocketCards.add(new Card("Diamonds", 14));
        aiPocketCards.add(new Card("Clubs", 5));
        aiPocketCards.addAll(tableCards);
        assertEquals(game.higherPair(playerPocketCards, aiPocketCards),
                "Player wins the pot");
    }
    
    @Test
    public void testCompareHands() {
        ArrayList<Card> tableCards = new ArrayList<>();
        
        tableCards.add(new Card("Hearts", 5));
        tableCards.add(new Card("Hearts", 2));
        tableCards.add(new Card("Hearts", 7));
        tableCards.add(new Card("Spades", 14));
        tableCards.add(new Card("Diamonds", 13));
        
        ArrayList<Card> playerPocketCards = new ArrayList<>();
        playerPocketCards.add(new Card("Diamonds", 12));
        playerPocketCards.add(new Card("Clubs", 3));
        playerPocketCards.addAll(tableCards);
        
        ArrayList<Card> aiPocketCards = new ArrayList<>();
        aiPocketCards.add(new Card("Hearts", 12));
        aiPocketCards.add(new Card("Hearts", 8));
        aiPocketCards.addAll(tableCards);
        
        assertEquals(game.compareHands(playerPocketCards, aiPocketCards), "AI wins the pot");
    }
}
