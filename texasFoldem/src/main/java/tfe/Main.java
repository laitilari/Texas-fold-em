package tfe;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import tfe.core.cards.Card;
import tfe.core.game.HandComparator;
import tfe.ui.UserInterface;

/**
 * Main luokka avaa ohjelman käyttöliittymän, joka aloittaa pelin.
 */
public class Main {

    /**
     * Käynnistää pelin.
     *
     * @param args args
     */
    public static void main(String[] args) throws InterruptedException {
        UserInterface ui = new UserInterface();
        ui.greet();
        ui.go();
    }
}
