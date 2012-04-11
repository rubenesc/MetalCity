/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.main.stuff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Ruben
 */
public class StringProblems {

    public StringProblems() {
    }

    public static void main(String[] args) {
        StringProblems sp = new StringProblems();
        sp.findFirstNonRepeatedCharacterTest();
        sp.removeCharactersTest();
        sp.reverseStringTest();
        sp.stringToIntTest();
        sp.integerToStringTest();
        sp.moduleTest();
        sp.isCompleteTest();
    }

    private void findFirstNonRepeatedCharacterTest() {
        System.out.println("*** findFirstNonRepeatedCharacterTest ***");
        String word = "Ruben Escudero";
        System.out.println(word + " --> " + findFirstNonRepeatedCharacter(word));
    }

    private void removeCharactersTest() {
        System.out.println("*** removeCharactersTest ***");
        String word = "Battle of the Vowels: Hawaii vs. Grozny";
        String remove = "aeiou";
        System.out.println(word + " --> " + removeCharacters(word, remove));
    }

    private void reverseStringTest() {
        System.out.println("*** reverseStringTest ***");
        String word = "Battle of the Vowels: Hawaii vs. Grozny";
        System.out.println(word + " --1--> " + reverseString1(word));
        System.out.println(word + " --2--> " + reverseString2(word));
        System.out.println(word + " --3--> " + reverseString3(word));
        System.out.println(word + " --4--> " + reverseString4(word));
    }

    private String reverseString4(String phrase) {

        if (phrase == null) {
            return phrase;
        }

        phrase = reverseString2(phrase);

        Character c;
        int startIndex = 0;
        char[] x = phrase.toCharArray();
        for (int i = 0; i < x.length; i++) {
            c = x[i];
            if (c.equals(' ') && startIndex != i) {
                flipString(x, startIndex, i - 1);
                startIndex = i + 1;
            }

            if ((i == (x.length - 1)) && (startIndex != i)) {
                flipString(x, startIndex, i);
            }

        }


        return new String(x);
    }

    private void stringToIntTest() {
        System.out.println("*** stringToIntTest ***");
        String str = "123";
        System.out.println(str + " --1--> " + stringToInt1(str));
        str = "-123";
        System.out.println(str + " --1--> " + stringToInt1(str));
        str = "123456789";
        System.out.println(str + " --1--> " + stringToInt1(str));
        str = "-123456789";
        System.out.println(str + " --1--> " + stringToInt1(str));
        str = "0";
        System.out.println(str + " --1--> " + stringToInt1(str));
        str = "-0";
        System.out.println(str + " --1--> " + stringToInt1(str));
        str = "1";
        System.out.println(str + " --1--> " + stringToInt1(str));
        str = "-1";
        System.out.println(str + " --1--> " + stringToInt1(str));
    }

    private void integerToStringTest() {
        System.out.println("*** integerToStringTest ***");
        int num = 123;
        System.out.println(num + " --1--> " + integerToString(num));
        num = -123;
        System.out.println(num + " --1--> " + integerToString(num));
        num = 123456789;
        System.out.println(num + " --1--> " + integerToString(num));
        num = -123456789;
        System.out.println(num + " --1--> " + integerToString(num));
        num = 0;
        System.out.println(num + " --1--> " + integerToString(num));
        num = -0;
        System.out.println(num + " --1--> " + integerToString(num));
        num = 1;
        System.out.println(num + " --1--> " + integerToString(num));
        num = -1;
        System.out.println(num + " --1--> " + integerToString(num));

    }

    private String integerToString(int num) {

        int sign = 1;
        if (num < 0) {
            sign = -1;
            num = num * sign;
        }


        Character asciiBase = '0';
        char[] arr = new char[10];
        int cont = 0;
        while (num > 0) {
            System.out.println("num: " + num + " - " + num % 10);
            arr[cont++] = (char) (num % 10 + asciiBase);
            num = num / 10;
        }

        StringBuilder sb = new StringBuilder();
        while (cont >= 0) {
            sb.append(arr[cont--]);
        }

        if (sign < 0) {
            sb.insert(0, "-");
        }

        return sb.toString();
    }

    private int stringToInt1(String str) {

        int answer = 0;

        if (str == null) {
            return answer;
        }

        int sign = 1;
        Character cSign = str.charAt(0);
        int end = 0;
        if (cSign.equals('-')) {
            sign = -1;
            end = 1;
        }


        int baseAscii = Character.valueOf('0');
        int tens = 1;
        boolean isFirst = true;
        for (int i = str.length() - 1; i >= end; i--) {

            int number = str.charAt(i);
            number = number - baseAscii;

            if (isFirst) {
                isFirst = false;
            } else {
                tens = tens * 10;
            }

            answer = answer + number * tens;
        }

        return answer * sign;
    }

    private int stringToInt2(String str) {

        if (str == null) {
            return 0;
        }

        int anwser = 0;
        int sign = 1;
        Character sNum = str.charAt(0);

        int end = 0;
        if (sNum.equals('-')) {
            sign = -1;
            end = 1;
        }

        int tens = 1;
        boolean isFirst = true;

        for (int i = str.length() - 1; i >= end; i--) {

            if (isFirst) {
                isFirst = false;
            } else {
                tens = tens * 10;
            }
            sNum = str.charAt(i);
            int x = sNum;
            int y = '0';
            anwser += Integer.parseInt(sNum.toString()) * tens;
            System.out.println(i + " - " + x + " - " + y + " - " + sNum);
        }


        return anwser * sign;
    }

    private void flipString(char[] str, int start, int end) {
        //ruben
        //01234
        //04, 13, 22

        //     todays
        //     012345
        //05,  sodayt
        //14,  sydaot
        //23,  syadot

        while (start < end) {
            char temp = str[start];
            str[start] = str[end];
            str[end] = temp;

            start++;
            end--;
        }
    }

    private String reverseString3(String phrase) {

        if (phrase == null) {
            return phrase;
        }
        // 012345678901234
        //"today is sunday"
        //sunday is today
        int b = phrase.length();
        Character letter;
        StringBuilder sb = new StringBuilder();
        for (int i = phrase.length() - 1; i >= 0; i--) {
            letter = phrase.charAt(i);
            if (letter.equals(' ')) {
                if (i != b) {
                    String word = phrase.substring(i + 1, b);
                    sb.append(word);
                    b = i;
                }
                sb.append(letter);
            }

            if ((i == 0) && (i != b)) {
                String d = phrase.substring(0, b);
                sb.append(d);
            }
        }


        return sb.toString();
    }

    private String reverseString2(String word) {

        if (word == null) {
            return word;
        }

        int length = word.length();
        char[] auxArray = new char[length];
        for (int i = 0; i < length; i++) {
            auxArray[length - 1 - i] = word.charAt(i);
        }

        return new String(auxArray);
    }

    private String reverseString1(String word) {

        if (word == null) {
            return word;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = word.length() - 1; i >= 0; i--) {
            sb.append(word.charAt(i));
        }

        return sb.toString();
    }

    private String removeCharacters(String word, String remove) {
        HashMap<Character, Boolean> h = new HashMap<Character, Boolean>();
        StringBuilder sb = new StringBuilder();
        Character auxChar;

        for (int i = 0; i < remove.length(); i++) {
            h.put(remove.toLowerCase().charAt(i), Boolean.TRUE);
        }

        for (int i = 0; i < word.length(); i++) {
            auxChar = word.charAt(i);
            Boolean get = h.get(Character.toLowerCase(auxChar));
            if (get == null) {
                sb.append(auxChar);
            }
        }

        return sb.toString();
    }

    private Character findFirstNonRepeatedCharacter(String word) {

        HashMap<Character, Integer> hash = new HashMap<Character, Integer>();

        Character anwser = null;
        Character auxChar = null;
        Integer auxCount;
        if (word != null) {
            char[] cArray = word.toLowerCase().toCharArray();
            for (int i = 0; i < cArray.length; i++) {
                auxChar = cArray[i];
                auxCount = hash.get(auxChar);
                if (auxCount == null) {
                    auxCount = 0;
                }

                hash.put(auxChar, ++auxCount);
            }

            for (int i = 0; i < cArray.length; i++) {
                auxChar = cArray[i];
                auxCount = hash.get(auxChar);
                if (auxCount == 1) {
                    return auxChar;
                }
            }
        }

        return anwser;
    }

    private void moduleTest() {
        System.out.println("*** moduleTest ***");
        int number = 123456789;
        System.out.println("number: " + number);
        Character asciiBase = '0';
        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            int mod = number % 10;
            char cNum = (char) (mod + asciiBase);
            sb.append(cNum);
//            System.out.println("mod: " + mod + ", char: " + cNum);
            number /= 10;
        }

        System.out.println("answer: " + sb.reverse().toString());

    }

    private void isCompleteTest() {
        System.out.println("*** isComplete Test ***");
        String s = "()";
        System.out.println(s + " --> " + isParaenthesisComplete(s));
        s = "(()";
        System.out.println(s + " --> " + isParaenthesisComplete(s));
        s = "(()(";
        System.out.println(s + " --> " + isParaenthesisComplete(s));
        s = "(())";
        System.out.println(s + " --> " + isParaenthesisComplete(s));
        s = "((())()())";
        System.out.println(s + " --> " + isParaenthesisComplete(s));
        s = ")()()()())))(()()()((";
        System.out.println(s + " --> " + isParaenthesisComplete(s));
        s = ")((())";
        System.out.println(s + " --> " + isParaenthesisComplete(s));
        s = "((((()())())))";
        System.out.println(s + " --> " + isParaenthesisComplete(s));
        s = "()()()))";
        System.out.println(s + " --> " + isParaenthesisComplete(s));




    }

    private boolean isParaenthesisComplete(String s) {

        if (s == null) {
            return false;
        }

        int l = s.length();
        if (l % 2 != 0) {
            return false;
        }

        Queue<Character> queue = new LinkedList<Character>();
        Character last = null;
        boolean add = true;
        for (int i = 0; i < l; i++) {
            Character now = s.charAt(i);
            if (last == null) {
                last = now;
            }

            add = true;
            if (!last.equals(now)) {
                if (now.equals(')')) {
                    queue.poll();
                    last = queue.peek();
                    add = false;
                }
            }

            if (add) {
                queue.add(now);
            }
        }

        if (queue.size() > 0) {
            return false;
        }

        return true;
    }
}
