/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfe.core.support.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static java.lang.System.in;
import java.util.Scanner;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ilari
 */
public class SupportClassTest {

    public SupportClassTest() {
    }

    @Before
    public void setUp() {

    }

    @Test
    public void testUse() {
        String data = "Hello, World!\r\n";
        InputStream stdin = System.in;
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        Scanner scanner = new Scanner(System.in);
        System.setIn(stdin);
        String x = scanner.nextLine();
        assertEquals(stdin, stdin);
    }

}
