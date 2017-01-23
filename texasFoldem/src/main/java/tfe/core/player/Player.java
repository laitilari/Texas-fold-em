/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.player;

import java.util.ArrayList;
import java.util.List;
import tfe.core.cards.Card;

/**
 *
 * @author ilarilai
 */
public class Player {
    
    private String name;
    private List <Card> pocketCards;
    private List <Card> hand;

    public Player(String name) {
        this.name = name;
        this.pocketCards = new ArrayList<>();
        this.hand = new ArrayList<>();
    }

    public void drawPocketCards(List <Card> pocketCards) {
        this.pocketCards.addAll(pocketCards);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
