/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.main.stuff;

/**
 *
 * @author Ruben
 */
public class NumberProblems {

    public NumberProblems() {
    }

    public static void main(String[] args) {

        NumberProblems np = new NumberProblems();
        np.isNumberPalindromeTest();
        System.out.println("");
        np.isNumberPalindromeTest();


    }

    private void isNumberPalindromeTest() {
        System.out.println("*** isNumberPalindromeTest *** ");

        isNumberPalindrome(1);
        isNumberPalindrome(22);
        isNumberPalindrome(10301);
        isNumberPalindrome(1234);
        isNumberPalindrome(123456789);

        int a = 123456789;
        int b = 100000000;
        System.out.println(a + " --> " + ((a / 1) % 10) + " - " + ((a / b)));
        System.out.println(a + " --> " + ((a / 10) % 10) + " - " + ((a % (b / 1)) / (b / 10)));
        System.out.println(a + " --> " + ((a / 100) % 10) + " - " + ((a % (b / 10)) / (b / 100)));
        System.out.println(a + " --> " + ((a / 1000) % 10) + " - " + ((a % (b / 100)) / (b / 1000)));
        System.out.println(a + " --> " + ((a / 10000) % 10) + " - " + ((a % (b / 1000)) / (b / 10000)));
        System.out.println(a + " --> " + ((a / 100000) % 10) + " - " + ((a % (b / 10000)) / (b / 100000)));

    }

    int reverse(int num) {
        assert (num >= 0);   // for non-negative integers only.
        int rev = 0;
        while (num != 0) {

            rev = rev * 10;
            rev = rev + num % 10;
            num = num / 10;
        }
        return rev;
    }

    private boolean pal(int num) {

        int div = 1;
        while (num / div >= 10) {
            div = div * 10;
        }

        while (num > 0) {
            int b = num % 10;
            int a = num / div;

            if (a != b) {
                return false;
            }

            num = num % div;
            num = num / 10;

            div = div / 100;

        }




        return true;
    }

    private boolean isNumberPalindrome(int a) {
        boolean answer = false;

        int rev = reverse(a);
        if (rev - a == 0) {
            answer = true;
        }

        if (answer) {
            System.out.println(a + " is Palindrome");
        } else {
            System.out.println(a + " is NOT Palindrome");
        }

        answer = pal(a);
        if (answer) {
            System.out.println(a + " is Palindrome");
        } else {
            System.out.println(a + " is NOT Palindrome");
        }


        return answer;
    }
}
