/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.ai;

import java.util.ArrayList;
import java.util.List;
import tfe.core.cards.Card;

/**
 *
 * @author ilarilai
 */
public class Ai {

    private List<Card> pocketCards;
    private List<Card> hand;

    public Ai() {
        this.hand = new ArrayList<>();
    }

    public void drawPocketCards(List<Card> pocketCards) {
        this.pocketCards = pocketCards;
    }

    public List<Card> getPocketCards() {
        return pocketCards;
    }
       
}
