package tfe.core.cards;

import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ilarilai
 */
public class TableCards {
    
    private List <Card> cards;
 
    public TableCards() {
        this.cards = new ArrayList<>();
    }
    
    public void drawFlop(List<Card> flop) {
       this.cards.addAll(flop);
    }
    
    public void drawCard(Card card) {
        this.cards.add(card);
    }

    
}
