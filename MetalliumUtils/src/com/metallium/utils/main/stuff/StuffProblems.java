/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.main.stuff;

/**
 *
 * @author Ruben
 */
public class StuffProblems {

    private static void painterTest() {
        System.out.println("*** painterTest ***");
    }

    public StuffProblems() {
    }

    public static void main(String[] args) {

        StuffProblems sp = new StuffProblems();
        sp.mathPow();
        String s = "-150";
        System.out.println(s + ": atoi --> " + atoi(s));

        //Given US denomination coins, what is the algorithm to make change for any amount?
        getChangeTest();
        System.out.println("");
        System.out.println("--> " + 346 % 25);
        System.out.println("--> " + 346 / 25);
        System.out.println("--> " + 13 * 25);
        System.out.println("--> " + (325+21));
        painterTest();
        
        
        
        

    }

    private static void getChangeTest() {
        System.out.println("*** getChangeTest ***");

        double money = 3.46;
        int[] coins = {25,10,5,1};
        int[] change = getNumber(coins, money);
        System.out.println("money: " + money);
        System.out.print("coins: ");
        printArray(change);

    }

    private static int[] getNumber(int[] coinDenominations, double totalAmountInDollars) {
        int numberOfEachCoin[] = new int[coinDenominations.length];
        int totalAmountInCents = (int) (totalAmountInDollars * 100);
        for (int i = 0; i < coinDenominations.length; i++) {
            int denomination = coinDenominations[i];
            numberOfEachCoin[i] = totalAmountInCents / denomination;
            totalAmountInCents = totalAmountInCents % denomination;
        }
        return numberOfEachCoin;
    }

    public static int atoi(String s) {
        if (s == null) {
            return 0;
        }

        int anwser = 0;
        int sign = 1;
        int i = 0;
        int tens = 0;
        char base = '0';
        int length = s.length();
        if (s.charAt(0) == '-') {
            sign = -1;
            i = 1;
        }

        for (; i < length; i++) {

            char c = s.charAt(length - i);
            anwser += (c - base) * tens;

            if (tens == 0) {
                tens = 1;
            }
            tens = tens * 10;
        }

        return anwser * sign;

    }

    private void mathPow() {
        //a^b
        long x = 3;
        int b = 10;
        for (int i = 1; i <= b; i++) {
            System.out.println(x + "^" + i + " --> x: " + Math.pow(x, i));
        }
    }

    private static void printArray(int[] data) {
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + ",");
        }
        System.out.println("");

    }
}
