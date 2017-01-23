package tfe;

import tfe.core.ai.Ai;
import tfe.core.cards.Card;
import tfe.core.cards.PackOfCards;
import tfe.core.cards.TableCards;
import tfe.core.player.Player;
import tfe.dealer.Dealer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ilarilai
 */
public class Main {

    public static void main(String[] args) {

        PackOfCards pack = new PackOfCards();
        pack.assemblePack();
        TableCards tableCards = new TableCards();
        Player p = new Player("pelaaja");
        Ai ai = new Ai();
        Dealer d = new Dealer(p);
        
    }

}
