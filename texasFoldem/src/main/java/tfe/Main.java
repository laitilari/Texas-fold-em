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
     * KOODIKATSELMOIJA HUOM. peli vielä kesken, ei voi pelata loppuun saakka.
     * @param args 
     */
    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        ui.greet();
        ui.go();
    }

}
