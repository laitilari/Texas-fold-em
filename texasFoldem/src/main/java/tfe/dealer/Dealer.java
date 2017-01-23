/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.dealer;

import java.util.List;
import tfe.core.ai.Ai;
import tfe.core.cards.Card;
import tfe.core.cards.PackOfCards;
import tfe.core.cards.TableCards;
import tfe.core.player.Player;

/**
 *
 * @author ilarilai
 */
public class Dealer {
    
    private PackOfCards pack;
    private TableCards table;
    private Player player;
    private Ai ai;
    private String dealerName;
    
    public Dealer(Player player) {
        this.pack = new PackOfCards();
        this.table = new TableCards();
        this.player = player;
        this.ai = new Ai();
        this.dealerName = "James";
    }  
    
    public void shufflePack() {
        pack.shuffle();
    }
    
    public void dealPocketCards() {
        List<Card> pocketCards = pack.takeMany(4);
        player.drawPocketCards(pocketCards.subList(0, 2));
        ai.drawPocketCards(pocketCards.subList(2, 4));
    }
    
    public void dealFlop() {
        List<Card> flop = pack.takeMany(3);
        table.drawFlop(flop);
    }
    
    public void dealTurn() {
        table.drawCard(dealCard());
    }
    
    public void dealRiver() {
        table.drawCard(dealCard());
    }
    
        public Card dealCard() {
        Card card = pack.takeOne();
        return card;
    }
    
    public String getDealerName() {
        return this.dealerName;
    }
}
