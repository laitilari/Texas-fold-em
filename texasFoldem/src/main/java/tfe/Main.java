package tfe;

import tfe.core.cards.Card;
import tfe.core.cards.PackOfCards;

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
        Card card = pack.takeOne();
        System.out.println(card);
        pack.shuffle();
        Card card2 = pack.takeOne();
        System.out.println(card2);
        Card card3 = pack.takeOne();
        System.out.println(card3);
        
    }
    
}
