/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.main.stuff;

/**
 *
 * @author Ruben
 */
public class RecursionProblems {

    public RecursionProblems() {
    }

    public static void main(String args[]) {
        RecursionProblems rp = new RecursionProblems();
        rp.factTest();
        rp.permuteTest();  //o(n!)
        rp.combineTest();
        //       rp.telephoneWordsTest(); //o(3^n)
        rp.fibonacciTest();

    }

    private void factTest() {
        System.out.println("*** factTest ***");
        int num = 4;
        System.out.println(num + " --1--> " + fact(num));
        System.out.println(num + " --2--> " + factRec(num));

        for (num = 0; num < 10; num++) {
            System.out.println(num + " ---> " + factRec(num));
        }
    }

    private void permuteTest() {
        System.out.println("*** permuteTest ***");
        String word = "abcd";
        System.out.println("permute: " + word);
        permute(word);

    }

    private void combineTest() {
        System.out.println("*** combineTest ***");
        String word = "wxyz";
        System.out.println("combine: " + word);
        combine(word);

    }

    public void combine(String str) {
        int length = str.length();
        char[] instr = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        doCombine(instr, sb, length, 0, 0);
    }

    private void doCombine(char[] instr, StringBuilder sb, int length, int level, int start) {

        for (int i = start; i < length; i++) {
            sb.append(instr[i]);
            System.out.println("--> " + sb.toString());
            if (i < length - 1) {
                doCombine(instr, sb, length, level + 1, i + 1);
            }

            sb.setLength(sb.length() - 1);
        }

    }

    public void permute(String word) {
        int length = word.length();
        boolean[] usedLetter = new boolean[length];
        StringBuilder sb = new StringBuilder();
        char[] in = word.toCharArray();
        doPermute(in, sb, usedLetter, length, 0);
    }

    private void doPermute(char[] in, StringBuilder sb, boolean[] usedLetter, int length, int level) {
        if (level == length) {
            System.out.println(level + "--> " + sb.toString());
            return;
        }

        for (int i = 0; i < length; ++i) {

            if (usedLetter[i]) {
                continue;
            }
            sb.append(in[i]);
            usedLetter[i] = true;
            doPermute(in, sb, usedLetter, length, level + 1);
            usedLetter[i] = false;
            sb.setLength(sb.length() - 1);

        }

    }

    private int fact(int num) {
        int anwser = 1;
        while (num > 0) {
            anwser = anwser * num;
            num--;
        }
        return anwser;
    }

    private int factRec(int num) {

        if (num > 0) {
            return factRec(num - 1) * num;
        } else {
            return 1;
        }

    }
    public static final int PHONE_NUMBER_LENGTH = 7;

    public void telephoneWordsTest() {
        System.out.println("*** telephoneWordsTest ***");

        char[] result = new char[PHONE_NUMBER_LENGTH];
        int[] phone = {4, 9, 7, 1, 9, 2, 7};
        doPrintTelephoneWords(phone, 0, result);

    }

    private void doPrintTelephoneWords(int[] phone, int currentDigit, char[] result) {

        if (currentDigit == PHONE_NUMBER_LENGTH) {
            System.out.println("--> " + new String(result));
            return;
        }


        for (int i = 1; i <= 3; i++) {
            result[currentDigit] = getCharKey(phone[currentDigit], i);
            doPrintTelephoneWords(phone, currentDigit + 1, result);
            if (phone[currentDigit] == 0 || phone[currentDigit] == 1) {
                return;
            }
        }
    }

    private char getCharKey(int telKey, int place) {
        int auxCont = 1;
        int auxCont2 = 0;

        if (telKey == auxCont2++) {
            return '0';
        } else if (telKey == auxCont2++) {
            return '1';
        } else if (telKey == auxCont2++) {
            auxCont = 1;
            if (place == auxCont++) {
                return 'A';
            } else if (place == auxCont++) {
                return 'B';
            } else if (place == auxCont++) {
                return 'C';
            }
        } else if (telKey == auxCont2++) {
            auxCont = 1;
            if (place == auxCont++) {
                return 'D';
            } else if (place == auxCont++) {
                return 'E';
            } else if (place == auxCont++) {
                return 'F';
            }
        } else if (telKey == auxCont2++) {
            auxCont = 1;
            if (place == auxCont++) {
                return 'G';
            } else if (place == auxCont++) {
                return 'H';
            } else if (place == auxCont++) {
                return 'I';
            }
        } else if (telKey == auxCont2++) {
            auxCont = 1;
            if (place == auxCont++) {
                return 'J';
            } else if (place == auxCont++) {
                return 'K';
            } else if (place == auxCont++) {
                return 'L';
            }
        } else if (telKey == auxCont2++) {
            auxCont = 1;
            if (place == auxCont++) {
                return 'M';
            } else if (place == auxCont++) {
                return 'N';
            } else if (place == auxCont++) {
                return 'O';
            }
        } else if (telKey == auxCont2++) {
            auxCont = 1;
            if (place == auxCont++) {
                return 'P';
            } else if (place == auxCont++) {
                return 'R';
            } else if (place == auxCont++) {
                return 'S';
            }
        } else if (telKey == auxCont2++) {
            auxCont = 1;
            if (place == auxCont++) {
                return 'T';
            } else if (place == auxCont++) {
                return 'U';
            } else if (place == auxCont++) {
                return 'V';
            }
        } else if (telKey == auxCont2++) {
            auxCont = 1;
            if (place == auxCont++) {
                return 'W';
            } else if (place == auxCont++) {
                return 'X';
            } else if (place == auxCont++) {
                return 'Y';
            }
        }

        return 'z';
    }

    private static void fibonacciTest() {
        System.out.println("");
        System.out.println("*** Fibonacci Test ***");

        int n = 8;
        System.out.println(n + " --> ");
        fibonacci(n);

        int[] in = new int[n];
        fibonacciRec(in, n, 0);

        for (int i = 0; i < n; i++) {
            System.out.print(in[i] + ", ");
        }
        System.out.println("");
        System.out.println(n+": " + fibonacciRec(n-1));
        
        // 4
//        f0 + f1 + f2 + f3 + f4
//        0  + 1  + 1  + 2  +  3        
    }
    
    public static int fibonacciRec(int n) {
        
        if (n == 0){
            return 0;
        }
        
        if (n == 1) {
            return 1;
        }
        

        
        return fibonacciRec(n-1)+fibonacciRec(n-2);
        
    }
    

    public static void fibonacciRec(int[] in, int n, int level) {

        if (level < n) {

            if (level == 0) {
                in[level] = 0;
            } else if (level == 1) {
                in[level] = 1;
            } else {
                in[level] = in[level - 1] + in[level - 2];
            }

            fibonacciRec(in, n, level + 1);
        }

    }

    public static void fibonacci(int n) {
        int a = 0, b = 1;

        for (int i = 0; i < n; i++) {
            System.out.print(a + ", ");
            a = a + b;
            b = a - b;
        }
        System.out.println("");
    }
}
