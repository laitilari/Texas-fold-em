/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.game.test;

import static org.junit.Assert.assertEquals;
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
    }

    @Test
    public void testAiBets() {
        String given = "Ai bets:42.0";
        game.aiBets(given);
        assertEquals(game.getPotSize(), 42.0, 0);
    }
    
}
