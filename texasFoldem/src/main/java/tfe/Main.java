package tfe;

import java.util.Scanner;
import tfe.ui.UserInterface;

/**
 * Main luokka avaa ohjelman käyttöliittymän, joka aloittaa pelin.
 */
public class Main {

    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        ui.greet();
        ui.go();
    }

}
