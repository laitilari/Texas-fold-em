/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.game;

import java.util.ArrayList;
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
    private String dealerName;

    public Dealer() {
        this.dealerName = "James";
        this.pack = new PackOfCards();
    }

    public void assemblePack() {
        pack.assemblePack();
    }

    public void shufflePack() {
        pack.shuffle();
    }

    public void reAssemblePack() {
        pack.reAssemblePack();
    }

    public void dealPocketCards(Player player, Ai ai) {
        List<Card> playerCards = pack.takeMany(2);
        List<Card> aiCards = pack.takeMany(2);
        player.drawPocketCards(playerCards);
        ai.drawPocketCards(aiCards);
    }

    public void dealFlop(TableCards table) {
        List<Card> flop = pack.takeMany(3);
        table.drawFlop(flop);
    }

    public void dealTurn(TableCards table) {
        table.drawCard(dealCard());
    }

    public void dealRiver(TableCards table) {
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
