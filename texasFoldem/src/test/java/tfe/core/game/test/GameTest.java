/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.game.test;

import java.io.ByteArrayInputStream;
import static java.lang.System.in;
import java.util.Scanner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tfe.core.ai.Ai;
import tfe.core.cards.TableCards;
import tfe.core.game.Dealer;
import tfe.core.game.Game;
import tfe.core.player.Player;

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

//    @Test
//    public void testGameSpeedFast() {
//        Scanner keyboard = new Scanner(in);
//        System.out.println("Type 'fast', 'normal' or 'slow' to determine game speed");
//        String input = keyboard.nextLine();
//        game.setGameSpeed(input);
//        assertEquals(game.getBigBlind(), 30);
//        assertEquals(game.getStackSize(), 500);
//    }
//    @Test
//    public void testGameSpeedNormal() {
//        game.askGameSpeed();
//        assertEquals(game.getBigBlind(), 30);
//        assertEquals(game.getStackSize(), 1000);
//    }
//    @Test
//    public void testGameSpeedSlow() {
//        game.askGameSpeed();
//        assertEquals(game.getBigBlind(), 30);
//        assertEquals(game.getStackSize(), 2000);
//    }
}
