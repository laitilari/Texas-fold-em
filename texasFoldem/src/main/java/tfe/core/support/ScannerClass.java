package tfe.core.support;

import java.util.Scanner;

/**
 * Scanner luokka käyttäjän syötteen lukemiseen.
 */
public class ScannerClass {

    private Scanner scanner;

    /**
     * Konstruktori tekee uuden scannerin.
     */
    public ScannerClass() {
        scanner = new Scanner(System.in);
    }

    /**
     * Käyttää scanneria.
     *
     * @return scanner nextLine
     */
    public String use() {
        String returned = scanner.nextLine();
        return returned;
    }

}
