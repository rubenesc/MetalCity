/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.main.stuff;

import java.lang.Integer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Ruben
 */
public class ArrayProblems {

    public ArrayProblems() {
    }

    public static void main(String[] args) {
        ArrayProblems ap = new ArrayProblems();
        ap.test01();
        ap.mergeSortTest();
        ap.test02();
        ap.isMajorityTest();
        ap.findOcurrencesOfElementTest();
        //********************************************//
        ap.findTheIntersectionOfTwoSortedArraysTest();
        ap.findThe_kth_SmallestElementInTheUnionOfTwoSortedArraysTest();
        ap.printArrayInSpiralTest();
        ap.rotateArrayTest();
        ap.arrayMultiplicationTest();
        ap.sum3Test(); //Finding all unique triplets that sums to zero

    }

    //Finding all unique triplets that sums to zero
    //http://www.leetcode.com/2010/04/finding-all-unique-triplets-that-sums.html
    private void sum3Test() {
        System.out.println("");
        System.out.println("*** sum3Test ***");

        int[] a = {-1, 0, 1, 2, -1, -4};
        int n = a.length;
        //Given a set S of n integers, find all pairs of integers of a and b in S such that a + b = k?

        mergeSort(a);
        printArray(a);

        System.out.println("-->");
        for(int i = 0; i<a.length; i++){
            System.out.print(a[i]+", ");
        }
        System.out.println("");
        
        List<Integer[]> b = new ArrayList<Integer[]>();
        for (int i = 0; i < n; i++) {

            int j = i + i;
            int k = n - 1;

            while (j < k) {
                int twoSum = a[i] + a[j];
                if (twoSum + a[k] < 0) {
                    j++;
                } else if (twoSum + a[k] > 0) {
                    k--;
                } else  {
                    Integer[] o = new Integer[3];
                    o[0] = a[i];
                    o[1] = a[j];
                    o[2] = a[k];
                    b.add(o);
                    j++;
                    k--;
                }
            }
        }

        Integer[] o = new Integer[3];
        for (int i = 0; i < b.size(); i++) {
            o = b.get(i);
            System.out.println(a[0] + " - " + a[1] + " - " + a[2]);
        }
        System.out.println("");

    }

    //http://www.leetcode.com/2010/04/multiplication-of-numbers.html
    /**
     * Example:
     *        A: {4, 3, 2, 1, 2}
     *   OUTPUT: {12, 16, 24, 48, 24}
     */
    private void arrayMultiplicationTest() {
        System.out.println("");
        System.out.println("*** arrayMultiplicationTest ***");

        int[] a = {4, 3, 2, 1, 2};
        int n = a.length;
        int[] o = new int[n];

        for (int i = 0; i < n; i++) {
            o[i] = 1;
        }


        int left = 1;
        int right = 1;
        for (int i = 0; i < n; i++) {
            o[i] = o[i] * left;
            o[n - 1 - i] = o[n - 1 - i] * right;
            left = left * a[i];
            right = right * a[n - 1 - i];
        }

        printArray(o);

    }

    private void rotateArrayTest() {
        System.out.println("");
        System.out.println("*** rotateArrayTest ***");

        String[] arr = {"a", "b", "c", "d", "e", "f", "g"};
        //ring[] arr = {"e", "f", "g"  "a", "b", "c", "d", };

        printArray(arr);

        int n = arr.length;
        int k = 5;

        String[] arrRotate = rotate1(arr, n, k, 0);//brute force
        //check http://www.leetcode.com/2010/04/rotating-array-in-place.html

        System.out.println("");
        printArray(arrRotate);

    }

    private void rotate2(String[] a, int k) {
        int n = a.length;
        rotate2(a, 0, n - 1);
        rotate2(a, 0, k - 1);
        rotate2(a, k, n - 1);
    }

    private void rotate2(String[] a, int left, int right) {
        //not working
        int n = a.length;
        for (int i = 0; i < n; i++) {
            String temp = a[i];
            int ir = (i + 1);
            if (ir >= n) {
                ir = n - ir;
            }
            a[i] = a[ir];
            a[ir] = temp;
        }
    }

    private void rotate2a(String[] a, int left, int right) {


        while (left < right) {
            String temp = a[left];
            a[left] = a[right];
            a[right] = temp;
            left++;
            right--;
        }
    }

    private String[] rotate1(String[] a, int n, int k, int l) {

        String[] b = new String[a.length];

        for (int i = 0; i < n; i++) {
            int rotate = i + k;
            if (rotate > n) {
                rotate = rotate - n;
            }
            b[rotate - 1] = a[i];
        }

        return b;
    }

    /**
     * 
     *  Given a matrix (2D array) of m x n elements (m rows, n columns), 
     *  write a function that prints the elements in the array in a spiral manner.
     *   
     */
    private void printArrayInSpiralTest() {
        System.out.println("");
        System.out.println("*** Print Array In Spiral Test ***");

        int[][] m1 = XXX.m3x4Array();
        System.out.println("");
        printMultiArray(m1);

        int[][] mat = XXX.m5x6Array();
        System.out.println("");
        printMultiArray(mat);

        System.out.println("");
        int m = mat.length;  //5
        int n = mat[0].length; //6
        int l = 0;



        while (true) {

            if (m <= 0 || n <= 0) {
                break;
            }

            if (m == 1) {
                for (int j = 0; j < n; j++) {
                    System.out.print(mat[l][l + j] + ", ");
                }
                break;
            }

            if (n == 1) {
                for (int i = 0; i < m; i++) {
                    System.out.print(mat[l + i][l] + ", ");
                }
                break;
            }

            for (int j = 0; j < n - 1; j++) {
                System.out.print(mat[l][l + j] + ", ");
            }

            for (int i = 0; i < m - 1; i++) {
                System.out.print(mat[l + i][l + n - 1] + ", ");
            }

            for (int j = 0; j < n - 1; j++) {
                System.out.print(mat[l + m - 1][l + n - 1 - j] + ", ");
            }

            for (int i = 0; i < m - 1; i++) {
                System.out.print(mat[l + m - 1 - i][l] + ", ");
            }
            System.out.println("");

            m = m - 2;
            n = n - 2;
            l++;

        }



        m--;
        n--;

        l++;

        System.out.println("");
    }

//    private void printSpiral(int[][] x, int m, int n, int level) {
//    }
    private void printMultiArray(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.print(a[i][j] + ",");
            }
            System.out.println("");
        }
    }

    /**
     * 
     * Given two sorted arrays A, B of size m and n respectively. 
     * Find the k-th smallest element in the union of A and B. 
     * You can assume that there are no duplicate elements.
     * 
     * http://www.leetcode.com/2011/01/find-k-th-smallest-element-in-union-of.html
     * 
     */
    private void findThe_kth_SmallestElementInTheUnionOfTwoSortedArraysTest() {
        System.out.println("");
        System.out.println("*** findTheIntersectionOfTwoSortedArraysTest ***");

        int n = 20;
        int m = 30;
        int[] arr1 = generateRandomArrayNoDuplicates(n);
        int[] arr2 = generateRandomArrayNoDuplicates(m);

        mergeSort(arr1);
        mergeSort(arr2);

        printArray(arr1);
        printArray(arr2);

        int k = 20;
        kthElementUnionArray1(arr1, arr2, k); // O(n + m)
        kthElementUnionArray2(arr1, arr2, k); // O(n + m)



    }

    //To Hard
    private void kthElementUnionArray2(int[] arr1, int[] arr2, int k) {

        int n = arr1.length;
        int m = arr2.length;

        int i = 0;
        int j = 0;

        System.out.println("--> to hard <--");


        //       System.out.println("k= " + k + ", --> " + merge[k - 1]);
    }

    //// O(n + m)
    private void kthElementUnionArray1(int[] arr1, int[] arr2, int k) {

        int n = arr1.length;
        int m = arr2.length;
        int[] merge = new int[n + m];

        for (int i = 0; i < n; i++) {
            merge[i] = arr1[i];
        }

        for (int i = 0; i < m; i++) {
            merge[n + i] = arr2[i];
        }

        mergeSort(merge);
        printArray(merge);

        System.out.println("k= " + k + ", --> " + merge[k - 1]);
    }

    /**
     * 
     * http://www.leetcode.com/2010/03/here-is-phone-screening-question-from.html
     * 
     */
    private void findTheIntersectionOfTwoSortedArraysTest() {
        System.out.println("");
        System.out.println("*** findTheIntersectionOfTwoSortedArraysTest ***");


        int n = 20;
        int m = 30;
        int[] arr1 = generateRandomArray(n);
        int[] arr2 = generateRandomArray(m);

        mergeSort(arr1);
        mergeSort(arr2);

        printArray(arr1);
        printArray(arr2);

        List<Integer> inter = intersection1(arr1, arr2); //O(n*m)
        printArray(inter);
        inter = intersection2(arr1, arr2); //O(n*Log m)
        printArray(inter);
        inter = intersection3(arr1, arr2); //O(n*m)
        printArray(inter);


    }

    private List<Integer> intersection3(int[] arr1, int[] arr2) {
        int n = arr1.length;
        int m = arr2.length;
        List<Integer> inter = new ArrayList<Integer>();

        Integer lastElement = null;
        int i = 0;
        int j = 0;

        while (i < n && j < m) {

            int a = arr1[i];
            int b = arr2[j];

            if (a == b) {
                i++;
                j++;

                if (lastElement == null || lastElement != a) {
                    inter.add(a);
                    lastElement = a;
                }
            } else if (a > b) {
                j++;
            } else if (b > a) {
                i++;
            }



        }



        return inter;

    }

    /**
     * O(n*Log m)
     */
    private List<Integer> intersection2(int[] arr1, int[] arr2) {

        int n = arr1.length;
        int m = arr2.length;
        List<Integer> inter = new ArrayList<Integer>();

        Integer lastElement = null;
        for (int i = 0; i < n; i++) {
            int a = arr1[i];

            if (lastElement == null || a != lastElement) {
                int index1 = binarySearch(arr2, 0, m, a);
                if (index1 >= 0) {
                    inter.add(a);
                    lastElement = a;
                }
            }

        }

        return inter;


    }

    /**
     * O(n*m)
     */
    private static List<Integer> intersection1(int[] arr1, int[] arr2) {

        int n = arr1.length;
        int m = arr2.length;
        List<Integer> inter = new ArrayList<Integer>();

        Integer lastElement = null;
        for (int i = 0; i < n; i++) {
            int a = arr1[i];

            if (lastElement == null || lastElement != a) {
                for (int j = 0; j < n; j++) {
                    int b = arr2[j];
                    if (a == b) {
                        inter.add(a);
                        lastElement = a;
                        break;
                    }
                }
            }
        }

        return inter;
    }

    private void findOcurrencesOfElementTest() {
        System.out.println("");
        System.out.println("--- is Majority Test ---------");
        int arr[] = {1, 2, 3, 3, 3, 3, 10, 11, 11, 11, 11, 11, 11, 11, 11, 11, 12};
        printArray(arr);
        int key = 11;
        findOcurrencesOfElementTest2(arr, 11);
        findOcurrencesOfElementTest2(arr, 1);
        findOcurrencesOfElementTest2(arr, 12);
        findOcurrencesOfElementTest2(arr, 0);
        findOcurrencesOfElementTest2(arr, 10);
        findOcurrencesOfElementTest2(arr, 3);
    }

    private void findOcurrencesOfElementTest2(int[] arr, int key) {
        int o = findOcurrencesOfElement(arr, key);
        if (o > 0) {
            System.out.println(key + " appears " + o + " times in arr[]");
        } else {
            System.out.println(key + " does not appear in arr[] ");
        }
    }

    private int findOcurrencesOfElement(int arr[], int key) {

        int n = arr.length;
        int ocurrences = 0;
        int i = this.binarySearch(arr, 0, n, key);

        if (i >= 0) {
            for (; i < n; i++) {
                if (arr[i] == key) {
                    ocurrences++;
                } else {
                    break;
                }
            }
        }


        return ocurrences;
    }

    private int[] generateRandomArray(int n) {
        int[] data = new int[n];
        Random randomGenerator = new Random();
        for (int i = 0; i < n; i++) {
            data[i] = randomGenerator.nextInt(n);
        }

        return data;
    }

    private int[] generateRandomArrayNoDuplicates(int n) {

        int[] data = new int[n];

        Random randomGenerator = new Random();
        for (int i = 0; i < n; i++) {
            data[i] = randomGenerator.nextInt(n);
        }

        int i = 0;
        Set<Integer> s = new HashSet<Integer>();
        while (i < n) {

            if (s.add(randomGenerator.nextInt(n * 2))) {
                i++;
            }

        }


        Iterator<Integer> it = s.iterator();
        i = 0;
        while (it.hasNext()) {
            data[i++] = it.next();
        }

        return data;
    }

    private void isMajorityTest() {
        System.out.println("");
        System.out.println("--- is Majority Test ---------");
        int arr[] = {1, 2, 3, 3, 3, 3, 10, 11, 11, 11, 11, 11, 11, 11, 11, 11, 12};
        int n = arr.length;
        int x = 11;

        int y = n / 2;

        printArray(arr);

        if (isMajorityLinear(arr, n, x)) {
            System.out.println(x + " appears more than " + y + " times in arr[] Linar");
        } else {
            System.out.println(x + " does not appear more than " + y + " times in arr[] Linar");
        }

        if (isMajorityBinary(arr, n, x)) {
            System.out.println(x + " appears more than " + y + " times in arr[] binary");
        } else {
            System.out.println(x + " does not appear more than " + y + " times in arr[] binary");
        }



    }

    /**
     * 
     * Time Complexity: O(Log n)
     * 
     * @return 
     */
    private boolean isMajorityBinary(int arr[], int n, int key) {

        int i = binarySearch(arr, 0, arr.length, key) - 1;

        if (i >= 0) {
            if (((i + n / 2) <= (n - 1)) && (arr[i + n / 2] == key)) {
                return true;
            }
        }

        return false;
    }

    private int binarySearch(int[] a, int fromIndex, int toIndex, int key) {

        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int midVal = a[mid];

            if (midVal < key) {
                low = mid + 1;
            } else if (midVal > key) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }
        return -(low + 1);  // key not found.
    }

    /**
     * 
     * Time Complexity: O(n)
     * 
     * @return 
     */
    private boolean isMajorityLinear(int arr[], int n, int x) {

        int lastIndex = (n % 2 == 0) ? (n / 2) : (n / 2 + 1);

        for (int i = 0; i < lastIndex; i++) {

            if (arr[i] == x) {
                if (arr[i + n / 2] == x) {
                    return true;
                } else {
                    return false;
                }
            }

        }

        return false;
    }

    private void test02() {
        System.out.println("");
        System.out.println("---test02 ---------");
        int n = 500;
        int[] data = generateRandomArray(n);

        printArray(data);
        quickSort(data, 0, n - 1);
        printArray(data);


    }

    private void mergeSortTest() {
        System.out.println("");
        System.out.println("---Merge Sort Test---------");
        int[] data = {1000, 80, 10, 50, 70, 60, 90, 20, 30, 40, 1, 0, -1000};

        printArray(data);
        mergeSort(data);
        printArray(data);
    }

    private static void printArray(String[] data) {
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + ",");
        }
        System.out.println("");

    }

    private static void printArray(int[] data) {
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + ",");
        }
        System.out.println("");

    }

    private static void printArray(List<Integer> data) {
        for (Integer i : data) {
            System.out.print(i + ",");
        }
        System.out.println("");

    }

    public static void quickSort(int[] a, int l, int u) {
        if (l >= u) {
            return;
        }
        int m = l;
        for (int i = l + 1; i <= u; i++) {
            if (a[i] < a[l]) {
                swap(a, ++m, i);
            }
        }
        swap(a, l, m);
        quickSort(a, l, m - 1);
        quickSort(a, m + 1, u);
    }

    // Sorting: Supporting Algs
    private static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    /***
     * 
     *  O(n log n)
     * 
     */
    public static int[] mergeSort(int array[]) {
        // if the array has more than 1 element, we need to split it and merge the sorted halves
        if (array.length > 1) {
            // number of elements in sub-array 1
            // if odd, sub-array 1 has the smaller half of the elements
            // e.g. if 7 elements total, sub-array 1 will have 3, and sub-array 2 will have 4
            int elementsInA1 = array.length / 2;
            // since we want an even split, we initialize the length of sub-array 2 to
            // equal the length of sub-array 1
            int elementsInA2 = elementsInA1;
            // if the array has an odd number of elements, let the second half take the extra one
            // see note (1)
            if ((array.length % 2) == 1) {
                elementsInA2 += 1;
            }
            // declare and initialize the two arrays once we've determined their sizes
            int arr1[] = new int[elementsInA1];
            int arr2[] = new int[elementsInA2];
            // copy the first part of 'array' into 'arr1', causing arr1 to become full
            for (int i = 0; i < elementsInA1; i++) {
                arr1[i] = array[i];
            }
            // copy the remaining elements of 'array' into 'arr2', causing arr2 to become full
            for (int i = elementsInA1; i < elementsInA1 + elementsInA2; i++) {
                arr2[i - elementsInA1] = array[i];
            }
            // recursively call mergeSort on each of the two sub-arrays that we've just created
            // note: when mergeSort returns, arr1 and arr2 will both be sorted!
            // it's not magic, the merging is done below, that's how mergesort works :)
            arr1 = mergeSort(arr1);
            arr2 = mergeSort(arr2);

            // the three variables below are indexes that we'll need for merging
            // [i] stores the index of the main array. it will be used to let us
            // know where to place the smallest element from the two sub-arrays.
            // [j] stores the index of which element from arr1 is currently being compared
            // [k] stores the index of which element from arr2 is currently being compared
            int i = 0, j = 0, k = 0;
            // the below loop will run until one of the sub-arrays becomes empty
            // in my implementation, it means until the index equals the length of the sub-array
            while (arr1.length != j && arr2.length != k) {
                // if the current element of arr1 is less than current element of arr2
                if (arr1[j] < arr2[k]) {
                    // copy the current element of arr1 into the final array
                    array[i] = arr1[j];
                    // increase the index of the final array to avoid replacing the element
                    // which we've just added
                    i++;
                    // increase the index of arr1 to avoid comparing the element
                    // which we've just added
                    j++;
                } // if the current element of arr2 is less than current element of arr1
                else {
                    // copy the current element of arr1 into the final array
                    array[i] = arr2[k];
                    // increase the index of the final array to avoid replacing the element
                    // which we've just added
                    i++;
                    // increase the index of arr2 to avoid comparing the element
                    // which we've just added
                    k++;
                }
            }
            // at this point, one of the sub-arrays has been exhausted and there are no more
            // elements in it to compare. this means that all the elements in the remaining
            // array are the highest (and sorted), so it's safe to copy them all into the
            // final array.
            while (arr1.length != j) {
                array[i] = arr1[j];
                i++;
                j++;
            }
            while (arr2.length != k) {
                array[i] = arr2[k];
                i++;
                k++;
            }
        }
        // return the sorted array to the caller of the function
        return array;
    }

//    int[] mergeSort(int[] array) {
//        if (array.length <= 1) {
//            return array;
//        }
//        int middle = array.length / 2;
//        int firstHalf = mergeSort(array[middle -1]); //0..middle - 1
//        int secondHalf = mergeSort(array[middle
//        ..array.length - 1
//        ]);
//    return merge(firstHalf, secondHalf);
//    }
    private void test01() {

        int[][] a = new int[3][9];

        int cont = 10;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {

                a[i][j] = cont++;
            }
        }
        printArray(a, 3, 9);
    }

    private void printArray(int[][] a, int x, int y) {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println("");
        }
    }
}
