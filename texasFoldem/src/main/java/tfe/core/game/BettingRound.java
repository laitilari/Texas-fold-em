/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.game;

/**
 *
 * @author ilarilai
 */
public class BettingRound {
    
    private int bettingRoundCount;

    public BettingRound() {
        this.bettingRoundCount = 0;
    }
    
    public void increaseBettingRound() {
        this.bettingRoundCount++;
    }

    public int getBettingRoundCounr() {
        return bettingRoundCount;
    }
    
}
