package com.sonar.sonarqube;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    private Calculator calculator = new Calculator();

    @Test
    void sumTwoNumbers() {
       int result =  calculator.sum(5, 5);

       assertEquals(10, result);
    }

    @Test
    void subtractTwoNumbers() {
        int result =  calculator.subtract(12, 5);

        assertEquals(7, result);
    }

    @Test
    void multiplyTwoNumbers() {
        int result =  calculator.multiply(3, 5);

        assertEquals(15, result);
    }

    @Test
    void divideTwoNumbers() {
        float result =  calculator.divide(20, 5);

        assertEquals(4, result);
    }
}