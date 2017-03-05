/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.ai.test;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import tfe.core.ai.Ai;
import tfe.core.cards.Card;
import tfe.core.game.HandComparator;

/**
 *
 * @author Ilari
 */
public class AiTest {

    Ai ai;

    public AiTest() {
    }

    @Before
    public void setUp() {
        this.ai = new Ai();
    }

    @Test
    public void testGetHand() {
        List<Card> list = new ArrayList<>();
        List<Card> list2 = new ArrayList<>();
        Card c = new Card("Spade", 2);
        Card c2 = new Card("Spade", 3);
        list.add(c);
        list2.add(c2);
        ai.drawPocketCards(list);
        assertTrue(ai.getHand(list2).size() == 2);
    }

    @Test
    public void testNotPremium() {
        List<Card> list = new ArrayList<>();
        Card c = new Card("Spade", 2);
        Card c2 = new Card("Spade", 3);
        list.add(c);
        list.add(c2);
        ai.drawPocketCards(list);
        assertTrue(!ai.premium());
    }

    @Test
    public void testPremiumWithPair() {
        List<Card> list = new ArrayList<>();
        Card c = new Card("Spade", 13);
        Card c2 = new Card("Heart", 13);
        list.add(c);
        list.add(c2);
        ai.drawPocketCards(list);
        assertTrue(ai.premium());
    }

    @Test
    public void testPremiumWithOutPair() {
        List<Card> list = new ArrayList<>();
        Card c = new Card("Spade", 14);
        Card c2 = new Card("Heart", 13);
        list.add(c);
        list.add(c2);
        ai.drawPocketCards(list);
        assertTrue(ai.premium());
    }

    @Test
    public void testNotGood() {
        List<Card> list = new ArrayList<>();
        Card c = new Card("Spade", 2);
        Card c2 = new Card("Spade", 3);
        list.add(c);
        list.add(c2);
        ai.drawPocketCards(list);
        assertTrue(!ai.premium());
    }

    @Test
    public void testGoodWithPair() {
        List<Card> list = new ArrayList<>();
        Card c = new Card("Spade", 9);
        Card c2 = new Card("Heart", 9);
        list.add(c);
        list.add(c2);
        ai.drawPocketCards(list);
        assertTrue(ai.good());
    }

    @Test
    public void testGoodWithOutPair() {
        List<Card> list = new ArrayList<>();
        Card c = new Card("Spade", 14);
        Card c2 = new Card("Heart", 10);
        list.add(c);
        list.add(c2);
        ai.drawPocketCards(list);
        assertTrue(ai.good());
    }

    @Test
    public void testNotMedium() {
        List<Card> list = new ArrayList<>();
        Card c = new Card("Spade", 2);
        Card c2 = new Card("Spade", 3);
        list.add(c);
        list.add(c2);
        ai.drawPocketCards(list);
        assertTrue(!ai.medium());
    }

    @Test
    public void testMediumWithPair() {
        List<Card> list = new ArrayList<>();
        Card c = new Card("Spade", 7);
        Card c2 = new Card("Heart", 7);
        list.add(c);
        list.add(c2);
        ai.drawPocketCards(list);
        assertTrue(ai.medium());
    }

    @Test
    public void testMediumWithOutPair() {
        List<Card> list = new ArrayList<>();
        Card c = new Card("Spade", 7);
        Card c2 = new Card("Heart", 8);
        list.add(c);
        list.add(c2);
        ai.drawPocketCards(list);
        assertTrue(ai.medium());
    }

    @Test
    public void testBetDoesNotGoBelowZero() {
        double Bet = 10.0;
        ai.setChips(8);
        ai.bet(Bet);
        assertEquals(ai.getChips(), 0, 0.1);
    }

    @Test
    public void testBetWorks() {
        double bet = 10.0;
        ai.setChips(20);
        ai.bet(bet);
        assertEquals(ai.getChips(), 10, 0.1);
    }

    @Test
    public void testAiWinChips() {
        double chips = 50.0;
        ai.winChips(chips);
        assertEquals(ai.getChips(), 50, 0.1);
    }

    @Test
    public void testAllIn() {
        double chips = ai.getChips();
        String x = ai.allIn();
        assertEquals(x, "AI is all-in");
    }

    @Test
    public void testButtonChange() {
        if (ai.getButton() == false) {
            ai.buttonChange();
            assertTrue(ai.getButton());
        }
        if (ai.getButton() == true) {
            ai.buttonChange();
            assertTrue(!ai.getButton());
        }
    }

    @Test
    public void testGetChips() {
        ai.setChips(50);
        assertEquals(50, ai.getChips(), 0.1);
    }

    @Test
    public void testSetChips() {
        ai.setChips(50);
        assertEquals(ai.getChips(), 50, 0.1);
    }

    @Test
    public void testBetBigBlindNotBelow() {
        double bb = 50;
        ai.setChips(40);
        ai.betBigBlind(bb);
        assertEquals(ai.getChips(), 0, 0.1);
    }

    @Test
    public void testBetBigBlind() {
        double bb = 50;
        ai.setChips(60);
        ai.betBigBlind(bb);
        assertEquals(ai.getChips(), 10, 0.1);
    }

    @Test
    public void testBetSmallBlindNotBelowZero() {
        double bb = 50;
        ai.setChips(40);
        ai.betBigBlind(bb);
        assertEquals(ai.getChips(), 0, 0.1);
    }

    @Test
    public void testBetSmallBlind() {
        double bb = 50;
        ai.setChips(60);
        ai.betBigBlind(bb);
        assertEquals(ai.getChips(), 10, 0.1);
    }

    @Test
    public void testDrawPocketCards() {
        List<Card> list = new ArrayList<>();
        Card card = new Card("Spade", 3);
        Card card2 = new Card("Spade", 4);
        list.add(card);
        list.add(card2);
        ai.drawPocketCards(list);
        assertEquals(ai.getPocketCards(), list);
    }

    @Test
    public void testLastBetReturnZeroIfListEmpty() {
        List<Double> bettingHistory = new ArrayList<>();
        assertEquals(ai.lastBet(bettingHistory), 0.0, 0.0);
    }

    @Test
    public void testLastBet() {
        List<Double> bettingHistory = new ArrayList<>();
        double bet = 1.0;
        double bet2 = 3.0;
        bettingHistory.add(bet);
        bettingHistory.add(bet2);
        assertEquals(ai.lastBet(bettingHistory), 3.0, 0.1);
    }

    @Test
    public void testPreFlop() {
        List<Card> tableCards = new ArrayList<>();
        assertTrue(ai.preFlop(tableCards));
    }

    @Test
    public void testHealthyStack() {
        ai.setChips(50.0);
        double bigBlind = 2.0;
        assertTrue(ai.healthyStack(bigBlind));
    }

    @Test
    public void testNotHealthyStack() {
        ai.setChips(1000.0);
        double bigBlind = 200;
        assertTrue(!ai.healthyStack(bigBlind));
    }

    @Test
    public void testDangeredStack() {
        ai.setChips(1000.0);
        double bigBlind = 60;
        assertTrue(ai.dangeredStack(bigBlind));
    }

    @Test
    public void testNotDangeredStack() {
        ai.setChips(1000.0);
        double bigBlind = 30;
        assertTrue(!ai.dangeredStack(bigBlind));
        bigBlind = 900;
        assertTrue(!ai.dangeredStack(bigBlind));
    }

    @Test
    public void testVeryLowStack() {
        ai.setChips(1000);
        double bigBlind = 120;
        assertTrue(ai.veryLowStack(bigBlind));
    }

    @Test
    public void testNotVeryLowStack() {
        ai.setChips(1000);
        double bigBlind = 10;
        assertTrue(!ai.veryLowStack(bigBlind));
    }

    @Test
    public void testNotPlayableCards() {
        List<Card> pocketCards = new ArrayList<>();
        Card card = new Card("Spade", 2);
        Card card2 = new Card("Heart", 3);
        pocketCards.add(card);
        pocketCards.add(card2);
        ai.drawPocketCards(pocketCards);
        assertTrue(!(ai.premium() || ai.good() || ai.medium()));
    }

    @Test
    public void testPlayableCards() {
        List<Card> pocketCards = new ArrayList<>();
        Card card = new Card("Spade", 14);
        Card card2 = new Card("Heart", 14);
        pocketCards.add(card);
        pocketCards.add(card2);
        ai.drawPocketCards(pocketCards);
        assertTrue(ai.premium() || ai.good() || ai.medium());
    }

    @Test
    public void testNormalAiBet() {
        double bigBlind = 30.0;
        ai.setChips(2000);
        assertEquals(ai.betNormalBet(bigBlind), "AI bets:75.0");
    }

    @Test
    public void testAiFolds() {
        assertEquals(ai.aiFolds(), "AI folds");
    }

    @Test
    public void testNormalEnemyBet() {
        List<Double> bettingHistory = new ArrayList<>();
        double bet = 75.0;
        double bigBlind = 30.0;
        bettingHistory.add(bet);
        assertTrue(ai.normalEnemyBet(bettingHistory, bet, bigBlind));
    }

    @Test
    public void testNotNormalEnemyBet() {
        List<Double> bettingHistory = new ArrayList<>();
        double bet = 529.0;
        double bigBlind = 30.0;
        bettingHistory.add(bet);
        assertTrue(!ai.normalEnemyBet(bettingHistory, bet, bigBlind));
    }

    @Test
    public void testGoodOrPremium() {
        List<Card> pocketCards = new ArrayList<>();
        Card card = new Card("Spade", 14);
        Card card2 = new Card("Heart", 14);
        pocketCards.add(card);
        pocketCards.add(card2);
        ai.drawPocketCards(pocketCards);
        assertTrue(ai.goodOrPremium());
    }

    @Test
    public void testNotGoodOrPremium() {
        List<Card> pocketCards = new ArrayList<>();
        Card card = new Card("Spade", 3);
        Card card2 = new Card("Heart", 2);
        pocketCards.add(card);
        pocketCards.add(card2);
        ai.drawPocketCards(pocketCards);
        assertTrue(!ai.goodOrPremium());
    }

    @Test
    public void testAiRaises() {
        double y = 50.0;
        String x = ai.aiRaises(y);
        assertEquals(x, "AI bets:150.0");
    }

    @Test
    public void testHasEnoughChips() {
        double x = 50;
        ai.setChips(100);
        assertTrue(ai.hasEnoughChips(x));
    }

    @Test
    public void testHasNotEnoughChips() {
        double x = 50;
        ai.setChips(49);
        assertTrue(!ai.hasEnoughChips(x));
    }

    @Test
    public void testAiCalls() {
        ai.setChips(400);
        List<Double> bettingHistory = new ArrayList<>();
        bettingHistory.add(80.0);
        assertEquals(ai.aiCalls(bettingHistory, 80), "AI calls");
    }

    @Test
    public void testAiCallsAndGoesAllIn() {
        ai.setChips(40);
        List<Double> bettingHistory = new ArrayList<>();
        bettingHistory.add(80.0);
        assertEquals(ai.aiCalls(bettingHistory, 80), "AI calls");
    }

    @Test
    public void testNoRaises() {
        List<Double> bettingHistory = new ArrayList<>();
        bettingHistory.add(15.0);
        bettingHistory.add(30.0);
        assertTrue(!ai.raises(bettingHistory));
    }

    @Test
    public void testYesRaises() {
        List<Double> bettingHistory = new ArrayList<>();
        bettingHistory.add(40.0);
        assertTrue(!ai.raises(bettingHistory));
    }

    @Test
    public void testActionEnemyBetPreFlopWithPremiumOrGoodCards() {
        List<Double> bettingHistory = new ArrayList<>();
        bettingHistory.add(40.0);
        List<Card> pocketCards = new ArrayList<>();
        Card card = new Card("Spade", 14);
        Card card2 = new Card("Heart", 14);
        pocketCards.add(card);
        pocketCards.add(card2);
        ai.drawPocketCards(pocketCards);
        assertTrue(ai.actionToEnemyBetPreFlopHealthyStack(40, bettingHistory)
                .contains("AI bets"));
    }

    @Test
    public void testActionToNormalEnemyBetPreFlopWithMedimumCards() {
        List<Double> bettingHistory = new ArrayList<>();
        ai.setChips(2000);
        bettingHistory.add(40.0);
        List<Card> pocketCards = new ArrayList<>();
        Card card = new Card("Spade", 8);
        Card card2 = new Card("Heart", 8);
        pocketCards.add(card);
        pocketCards.add(card2);
        ai.drawPocketCards(pocketCards);
        assertTrue(ai.actionToEnemyBetPreFlopHealthyStack(40, bettingHistory)
                .equals("AI calls"));
    }

    @Test
    public void testActionToNormalEnemyBetPreFlopWithBadCards() {
        List<Double> bettingHistory = new ArrayList<>();
        ai.setChips(2000);
        bettingHistory.add(40.0);
        List<Card> pocketCards = new ArrayList<>();
        Card card = new Card("Spade", 2);
        Card card2 = new Card("Heart", 3);
        pocketCards.add(card);
        pocketCards.add(card2);
        ai.drawPocketCards(pocketCards);
        assertTrue(ai.actionToEnemyBetPreFlopHealthyStack(40, bettingHistory)
                .equals("AI folds"));
    }

    @Test
    public void testPlayerHasBetWithPremium() {
        List<Double> bettingHistory = new ArrayList<>();
        ai.setChips(2000);
        bettingHistory.add(40.0);
        List<Card> pocketCards = new ArrayList<>();
        Card card = new Card("Spade", 13);
        Card card2 = new Card("Heart", 13);
        pocketCards.add(card);
        pocketCards.add(card2);
        ai.drawPocketCards(pocketCards);
        HandComparator hc = new HandComparator();
        assertEquals(ai.playerHasBet(bettingHistory, pocketCards, 20.0, hc), "AI calls");
    }

    @Test
    public void testPlayerHasBetWithNothing() {
        List<Double> bettingHistory = new ArrayList<>();
        ai.setChips(2000);
        bettingHistory.add(40.0);
        List<Card> pocketCards = new ArrayList<>();
        Card card = new Card("Spade", 2);
        Card card2 = new Card("Heart", 3);
        pocketCards.add(card);
        pocketCards.add(card2);
        ai.drawPocketCards(pocketCards);
        HandComparator hc = new HandComparator();
        assertEquals(ai.playerHasBet(bettingHistory, pocketCards, 20.0, hc), "AI folds");
    }

    @Test
    public void testActionToNotNormalEnemyBetPreFlopWithGoodOrPremium() {
        List<Double> bettingHistory = new ArrayList<>();
        ai.setChips(2000);
        bettingHistory.add(40.0);
        List<Card> pocketCards = new ArrayList<>();
        Card card = new Card("Spade", 13);
        Card card2 = new Card("Heart", 13);
        pocketCards.add(card);
        pocketCards.add(card2);
        ai.drawPocketCards(pocketCards);
        HandComparator hc = new HandComparator();
        assertEquals(ai.actionToNotNormalEnemyBetPreFlopHealthyStack(40.0, bettingHistory), "AI is all-in");
    }

    @Test
    public void testActionToNotNormalEnemyBetPreFlopWithMedium() {
        List<Double> bettingHistory = new ArrayList<>();
        ai.setChips(2000);
        bettingHistory.add(40.0);
        List<Card> pocketCards = new ArrayList<>();
        Card card = new Card("Spade", 7);
        Card card2 = new Card("Heart", 7);
        pocketCards.add(card);
        pocketCards.add(card2);
        ai.drawPocketCards(pocketCards);
        assertTrue(ai.actionToNotNormalEnemyBetPreFlopHealthyStack(40, bettingHistory)
                .equals("AI folds"));
    }

    @Test
    public void testEmptyOrCheckedWithEmptyBettingHistory() {
        List<Double> bettingHistory = new ArrayList<>();
        double lastBet = 50.0;
        assertTrue(ai.emptyOrChecked(bettingHistory, lastBet));
    }

    @Test
    public void testEmptyOrCheckedWithEmptyLastBet() {
        List<Double> bettingHistory = new ArrayList<>();
        bettingHistory.add(215.0);
        double lastBet = 0.0;
        assertTrue(ai.emptyOrChecked(bettingHistory, lastBet));
    }

    @Test
    public void testActionToEmptyBet() {
        List<Double> bettingHistory = new ArrayList<>();
        List<Card> pocketCards = new ArrayList<>();
        bettingHistory.add(215.0);
        Card card = new Card("Spade", 7);
        Card card2 = new Card("Heart", 8);
        pocketCards.add(card);
        pocketCards.add(card2);
        HandComparator hc = new HandComparator();
        ai.drawPocketCards(pocketCards);
        assertEquals(ai.actionToEmptyBet(pocketCards, bettingHistory, 0, 0, 0, hc), "AI bets:0.0");
    }

    @Test
    public void testActionToEmptyBettingHistoryPreFlop() {
        List<Double> bettingHistory = new ArrayList<>();
        ai.setChips(2000);
        bettingHistory.add(40.0);
        List<Card> pocketCards = new ArrayList<>();
        Card card = new Card("Spade", 7);
        Card card2 = new Card("Heart", 8);
        pocketCards.add(card);
        pocketCards.add(card2);
        ai.drawPocketCards(pocketCards);
        assertTrue(ai.actionToEmptyBettingHistoryPreFlopHealthyStack(40)
                .contains("AI bets"));
    }

    @Test
    public void testHealthyStackPreFlopAction() {
        List<Double> bettingHistory = new ArrayList<>();
        double pot = 500.0;
        double bigBlind = 30.0;
        double playerChips = 2000.0;
        double lastBet = 60.0;
        List<Card> pocketCards = new ArrayList<>();
        Card card = new Card("Spade", 13);
        Card card2 = new Card("Heart", 13);
        pocketCards.add(card);
        pocketCards.add(card2);
        ai.drawPocketCards(pocketCards);
        ai.setChips(10000);
        if (!ai.getButton()) {
            assertTrue(ai.outOfPositionPreFlopActionHealthyStack(bettingHistory, pot, bigBlind, playerChips, lastBet).contains("AI bets"));
        }
        if (ai.getButton()) {
            assertTrue(ai.inPositionPreFlopActionHealthyStack(bettingHistory,
                    pot, bigBlind, playerChips, lastBet).contains("AI bets"));
        }
    }

    @Test
    public void testLowStackPreFlopAction() {
        List<Double> bettingHistory = new ArrayList<>();
        double pot = 500.0;
        double bigBlind = 30.0;
        double playerChips = 2000.0;
        double lastBet = 60.0;
        assertEquals(ai.lowStackPreFlopAction(bettingHistory, pot, bigBlind, playerChips, lastBet), ai.allIn());
    }

    @Test
    public void testDangeredStackPreFlopActionWithPremium() {
        List<Double> bettingHistory = new ArrayList<>();
        double pot = 500.0;
        double bigBlind = 30.0;
        double playerChips = 2000.0;
        double lastBet = 60.0;
        List<Card> pocketCards = new ArrayList<>();
        Card card = new Card("Spade", 13);
        Card card2 = new Card("Heart", 13);
        pocketCards.add(card);
        pocketCards.add(card2);
        ai.drawPocketCards(pocketCards);
        ai.setChips(10000);
        if (!ai.getButton()) {
            assertEquals(ai.outOfPositionPreFlopActionDangeredStack(bettingHistory, pot, bigBlind, playerChips, lastBet), ai.allIn());
        }
        if (ai.getButton()) {
            assertEquals(ai.dangeredStackPreFlopAction(bettingHistory,
                    pot, bigBlind, playerChips, lastBet), ai.allIn());
        }
    }

    @Test
    public void testDangeredStackPreFlopActionWithMedium() {
        List<Double> bettingHistory = new ArrayList<>();
        double pot = 500.0;
        double bigBlind = 30.0;
        double playerChips = 2000.0;
        double lastBet = 60.0;
        List<Card> pocketCards = new ArrayList<>();
        Card card = new Card("Spade", 7);
        Card card2 = new Card("Heart", 8);
        pocketCards.add(card);
        pocketCards.add(card2);
        ai.drawPocketCards(pocketCards);
        ai.setChips(10000);
        if (!ai.getButton()) {
            assertEquals(ai.outOfPositionPreFlopActionDangeredStack(bettingHistory, pot, bigBlind, playerChips, lastBet), ai.betNormalBet(bigBlind));
        }
        if (ai.getButton()) {
            assertEquals(ai.dangeredStackPreFlopAction(bettingHistory,
                    pot, bigBlind, playerChips, lastBet), ai.aiCalls(bettingHistory, lastBet));
        }
    }
}
