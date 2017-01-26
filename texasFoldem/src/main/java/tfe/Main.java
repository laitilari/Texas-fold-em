package tfe;

import java.util.Scanner;
import tfe.gui.UserInterface;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ilarilai
 */
public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        UserInterface ui = new UserInterface();
        ui.greet();

    }

}
