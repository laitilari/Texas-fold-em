package tfe.core.support;

import java.util.Scanner;

/**
 * Scanner luokka käyttäjän syötteen lukemiseen.
 */
public class ScannerClass {

    private Scanner scanner;

    public ScannerClass() {
        scanner = new Scanner(System.in);
    }

    public String use() {
        String returned = scanner.nextLine();
        return returned;
    }

}
