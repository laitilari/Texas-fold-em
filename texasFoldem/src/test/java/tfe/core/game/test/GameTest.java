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
    }

    @Test
    public void testBettingHistory() {
        game.addBlindsToBettingHistory();
        assertEquals(game.getBettingHistory().get(0), 15, 0.1);
        assertEquals(game.getBettingHistory().get(1), 30, 0.1);
    }

    @Test
    public void testClearBettingHistory() {
        game.addBlindsToBettingHistory();
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
    public void testAddBlindsToBettingHistory() {
        game.addBlindsToBettingHistory();
        assertEquals(game.getBettingHistory()
                .get(game.getBettingHistory().size() - 1), 30, 0.1);
        assertEquals(game.getBettingHistory().size(), 2);
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
    public void testCheckOrCall() {
        game.getBettingHistory().add(0.0);
        String x = game.checkOrCall();
        assertEquals(x, "Player checked");

        game.getBettingHistory().add(0.0);
        game.getBettingHistory().add(25.0);
        x = game.checkOrCall();
        assertEquals(x, "Player called " + game.subtractLastTwoBets());
    }

    @Test
    public void testSubstractLastTwoBets() {
        game.getBettingHistory().add(0.5);
        game.getBettingHistory().add(1.0);
        game.getBettingHistory().add(2.0);
        game.getBettingHistory().add(5.0);
        double x = game.subtractLastTwoBets();
        assertEquals(x, 3.0, 0.1);
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
    public void testEnd() {
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
        game.aiBetsOrRaises(given);
        assertEquals(game.getPotSize(), 42.0, 0.1);
        assertEquals(game.getBettingHistory().size(), 1);
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
        String x = "AI is all-in";
        game.aiAllIn(x);
        assertEquals(game.getPotSize(), game.aiChipsLeft(), 0.1);
        assertEquals(game.getBettingHistory()
                .get(game.getBettingHistory().size() - 1),
                game.aiChipsLeft(), 0.1);
        assertTrue(game.aiAllIn(x).contains("AI is all"));
    }

    @Test
    public void testRaise() {
        double amount = 50.0;
        game.raise(amount);
        assertEquals(game.getPotSize(), amount, 0.1);
        assertEquals(game.getBettingHistory()
                .get(game.getBettingHistory().size() - 1),
                amount, 0.1);
        assertEquals(game.raise(amount), "Player raised " + amount);
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
    public void testAiCalls() {
        game.getBettingHistory().add(0.5);
        game.getBettingHistory().add(1.0);
        game.getBettingHistory().add(0.5);
        game.getBettingHistory().add(0.0);
        String x = game.aiCalls();
        assertEquals(x, "AI checks");
    }

    @Test
    public void testBettingOrder() {
        if (game.getPlayer().isButton()) {
            assertEquals(game.bettingOrder(), true);
        }
        if (!(game.getPlayer().isButton())) {
            assertEquals(game.bettingOrder(), false);
        }
    }
}
