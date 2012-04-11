/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.main.stuff;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Ruben
 */
public class Scramble {

    String[][] grid = {
        {"c", "a", "t", "b"},
        {"m", "n", "o", "m"},
        {"n", "m", "m", "a"},
        {"a", "s", "n", "v"},};
    private static int M = 4;
    
    private Set<String> dictonary = new HashSet<String>();
    
    public Scramble() {
        dictonary.add("cat");
        dictonary.add("can");
        dictonary.add("tom");
    }

    public static void main(String args[]) {
        Scramble s = new Scramble();
        s.process();
    }

    private void process() {



        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                findWords(i, j);
            }
            System.out.println("");
        }

    }

    private void findWords(int i, int j) {

        StringBuilder sb = new StringBuilder();
        System.out.println("l: " + grid[i][j] + "[" + i + "][" + j + "]");
        
        System.out.println("Up");
        sb = new StringBuilder();
        for (int a = i; a >= 0; a--) {
            sb.append(grid[a][j]);
            verifyWord(sb);
        }
        
        System.out.println("Right");
        sb = new StringBuilder();
        for (int a = j; a < M; a++) {
            sb.append(grid[i][a]);
            verifyWord(sb);
        }
        System.out.println("Left");
        sb = new StringBuilder();
        for (int a = j; a >= 0; a--) {
            sb.append(grid[i][a]);
            verifyWord(sb);
        }
        System.out.println("Down");
        sb = new StringBuilder();
        for (int a = i; a < M; a++) {
            sb.append(grid[a][j]);
            verifyWord(sb);
        }
        
    }

    private void verifyWord(StringBuilder sb) {
//        System.out.println(sb.toString());
        if (dictonary.contains(sb.toString())){
            System.out.println("--word-->" + sb.toString());
        }
    }
}
