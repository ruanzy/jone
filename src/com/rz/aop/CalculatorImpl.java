package com.rz.aop;

public class CalculatorImpl implements Calculator {


    public int calculate(int a, int b) {
    	System.out.println("**********Actual Method Execution**********");
        return a/b;
    }

}
