/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author ilarilai
 */
public class PackOfCards {

    private List<Card> cards;
    private List<Card> removedCards;

    public PackOfCards() {
        this.cards = new ArrayList<>();
        this.removedCards = new ArrayList<>();
    }

    public Card takeOne() {
        Card card = this.cards.get(0);
        this.cards.remove(0);
        remove(card);
        return card;
    }
    
    public List<Card> takeMany(int howMany) {
        List<Card> manyCards = new ArrayList<>();
        for (int i = 0; i < howMany; i++) {
            manyCards.add(takeOne());
        }
        return manyCards;
    }
    
    public void reAssemblePack() {
        cards.addAll(removedCards);
    }
    
    public void remove(Card card) {
        removedCards.add(card);
    }

    public void assemblePack() {
        ArrayList <String> suits = new ArrayList<>();
        suits.add("spades");
        suits.add("clubs");
        suits.add("hearts");
        suits.add("diamonds");
        int suitNumber = 0;
        for (int i = 0; i < 4; i++) {
            String suit = suits.get(suitNumber);
            for (int j = 2; j < 15; j++) {               
                Card card = new Card(suit, j);
                cards.add(card);
            }
            suitNumber++;
        }
    }
    
    public void shuffle() {
        Collections.shuffle(cards);
    }
}
