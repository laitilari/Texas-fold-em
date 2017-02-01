/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.support;

import java.util.Scanner;

/**
 *
 * @author Ilari
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
