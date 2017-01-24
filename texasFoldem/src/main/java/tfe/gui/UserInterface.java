/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.gui;

import java.util.Scanner;
import tfe.core.ai.Ai;
import tfe.core.cards.TableCards;
import tfe.core.player.Player;
import tfe.core.game.Dealer;
import tfe.core.game.Game;

/**
 *
 * @author ilarilai
 */
public class UserInterface {

    
    private Game game; 

    public UserInterface() {
        this.game = new Game();
    }

    public void greet() {
        System.out.println("Welcome to Texas Fold'em!");
        game.startGame();
    }

    

}
