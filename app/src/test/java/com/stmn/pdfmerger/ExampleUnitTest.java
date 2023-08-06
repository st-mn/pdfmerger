/*
JUnit test class named ExampleUnitTest. JUnit is a popular testing framework for Java 
that allows developers to write and execute unit tests to verify the correctness of their code at the method or function level. 
Unit tests are designed to test individual units of code in isolation to ensure they work as expected.

The package and import statements include the necessary JUnit and testing-related classes.
The class contains a single test method named addition_isCorrect(), annotated with @Test. 
This method will be executed when running the unit test.

Inside the addition_isCorrect() method:
A simple addition operation is performed: 2 + 2.
The result of the addition (4) is compared to the expected value (also 4) using assertEquals(). 
If the two values match, the test passes; otherwise, it fails.
The assertEquals() method is a static method from the org.junit.Assert class. 
It is used to compare two values and check whether they are equal. 
If the values are not equal, the test will fail, and an assertion error will be thrown, 
indicating that the code being tested doesn't behave as expected.

*/

package com.stmn.pdfmerger;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}
