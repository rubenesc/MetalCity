/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.main.stuff;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.WeakHashMap;

/**
 *
 * @author Ruben
 */
public class CollectionTests {

    public static void main(String[] args) {
        CollectionTests ct = new CollectionTests();
        ct.priorityQueueTest1();
        ct.priorityQueueTest2();
        ct.queueTest3();
//        ct.treeMapTest();
//
//        System.out.println("");
//        System.out.println("");
//        HashMap<String, Integer> ht = new HashMap<String, Integer>();
//        ht.put("one", 1);
//        ht.put("two", 2);
//        ht.put("three", 3);
//        ht.put("four", 4);
//        ht.put("five", 5);
//
//        Set<Entry<String, Integer>> entrySet = ht.entrySet();
//        Iterator<Entry<String, Integer>> i = entrySet.iterator();
//
//        while (i.hasNext()) {
//            Entry<String, Integer> me = i.next();
//            System.out.println(me.getKey() + ":" + me.getValue());
//        }




//        Set set = tm.entrySet();
//// Get an iterator 
//        Iterator i = set.iterator();
//// Display elements 
//        while (i.hasNext()) {
//            Map.Entry me = (Map.Entry) i.next();
//            System.out.print(me.getKey() + ": ");
//            System.out.println(me.getValue());
//        }



    }

    //==================Queue =========================================//
    public void priorityQueueTest2() {
        System.out.println("");
        System.out.println("*** priorityQueueTest2 ***");
        Comparator<String> comparator = new StringLengthComparator();
        PriorityQueue<String> queue =
                new PriorityQueue<String>(10, comparator);
        queue.add("short");
        queue.add("very long indeed");
        queue.add("medium");
        while (queue.size() != 0) {
            System.out.println(queue.remove());
        }

        System.out.println("");
        queue.add("short");
        queue.add("very long indeed");
        queue.add("medium");
        System.out.println("-->" + queue.size());
        System.out.println("-->" + queue.remove());
        System.out.println("-->" + queue.remove());
        System.out.println("-->" + queue.remove());
        System.out.println("-->" + queue.size());

    }

    public void priorityQueueTest1() {
        int[] ia = {1, 5, 3, 7, 6, 9, 8}; // unordered data

        PriorityQueue<Integer> pq1 = new PriorityQueue<Integer>(); // use natural order
        for (int x : ia) // load queue
        {
            pq1.offer(x);
        }

        for (int x : ia) // review queue        
        {
            System.out.print(pq1.poll() + " ");
        }


        System.out.println("");
        PriorityQueueSort pqs = new PriorityQueueSort(); // get a Comparator
        PriorityQueue<Integer> pq2 = new PriorityQueue<Integer>(10, pqs); // use Comparator
        for (int x : ia) // load queue
        {
            pq2.offer(x);
        }

        System.out.println("size " + pq2.size());
        System.out.println("peek " + pq2.peek());
        System.out.println("size " + pq2.size());
        System.out.println("poll " + pq2.poll());
        System.out.println("size " + pq2.size());
        for (int x : ia) // review queue
        {
            System.out.print(pq2.poll() + " ");
        }



    }

    private void treeMapTest() {
        System.out.println("");
        System.out.println("*** treeMapTest ***");
        // Create a tree map 
        TreeMap tm = new TreeMap();
        // Put elements to the map 
        tm.put("John Doe", new Double(3434.34));
        tm.put("Tom Smith", new Double(123.22));
        tm.put("Jane Baker", new Double(1378.00));
        tm.put("Todd Hall", new Double(99.22));
        tm.put("Ralph Smith", new Double(-19.08));
// Get a set of the entries 
        Set set = tm.entrySet();
// Get an iterator 
        Iterator i = set.iterator();
// Display elements 
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
        }
        System.out.println();
// Deposit 1000 into John Doe's account 
        double balance = ((Double) tm.get("John Doe")).doubleValue();
        tm.put("John Doe", new Double(balance + 1000));
        System.out.println("John Doe's new balance: "
                + tm.get("John Doe"));

    }

    private void queueTest3() {
        System.out.println("");
        System.out.println("*** queue Test 3 ***");

        Queue<String> qe = new LinkedList<String>();

        qe.add("b");
        qe.add("a");
        qe.add("c");
        qe.add("e");
        qe.add("d");

        Iterator it = qe.iterator();

        System.out.println("Initial Size of Queue :" + qe.size());

        while (it.hasNext()) {
            String iteratorValue = (String) it.next();
            System.out.println("Queue Next Value :" + iteratorValue);
        }

        // get value and does not remove element from queue
        System.out.println("Queue peek :" + qe.peek());
        // get first value and remove that object from queue
        System.out.println("Queue poll :" + qe.poll());
        System.out.println("Final Size of Queue :" + qe.size());
        System.out.println("Queue poll :" + qe.poll());
        System.out.println("Final Size of Queue :" + qe.size());
        System.out.println("Queue poll :" + qe.poll());
        System.out.println("Final Size of Queue :" + qe.size());
        System.out.println("Queue poll :" + qe.poll());
        System.out.println("Final Size of Queue :" + qe.size());
        System.out.println("Queue poll :" + qe.poll());
        System.out.println("Final Size of Queue :" + qe.size());
        System.out.println("Queue poll :" + qe.poll());
        System.out.println("Final Size of Queue :" + qe.size());
        System.out.println("Queue poll :" + qe.poll());
        System.out.println("Final Size of Queue :" + qe.size());
        System.out.println("Queue poll :" + qe.poll());
        System.out.println("Final Size of Queue :" + qe.size());
    }

    static class PriorityQueueSort implements Comparator<Integer> { // inverse sort

        public int compare(Integer one, Integer two) {
            return two - one; // unboxing
        }
    }

    static class StringLengthComparator implements Comparator<String> {

        @Override
        public int compare(String x, String y) {
            // Assume neither string is null. Real code should
            // probably be more robust
            if (x.length() < y.length()) {
                return -1;
            }
            if (x.length() > y.length()) {
                return 1;
            }
            return 0;
        }
    }
//==================Queue =========================================//
}
