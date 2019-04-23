package com.hxd.search.balancedBinarySearchTrees;

import com.hxd.introcs.stdlib.StdRandom;

/******************************************************************************
 *  Compilation:  javac TestRedBlackBST.java
 *  Execution:    java -ea TestRedBlackBST 10
 *  Dependencies: RedBlackBST.java
 *  
 *  Test client for RedBlackBST.java.
 *
 ******************************************************************************/

public class TestRedBlackBST {

    public static void main(String[] args) {

        String test = "S E A R C H E X A M P L E"; 
        String[] keys = test.split(" "); 
        RedBlackBST<String, Integer> st = new RedBlackBST<String, Integer>();
        for (int i = 0; i < keys.length; i++) 
            st.put(keys[i], i); 

        System.out.println("size = " + st.size());
        System.out.println("min  = " + st.min());
        System.out.println("max  = " + st.max());
        System.out.println();


        // print keys in order using allKeys()
        System.out.println("Testing keys()");
        System.out.println("--------------------------------");
        for (String s : st.keys()) 
            System.out.println(s + " " + st.get(s)); 
        System.out.println();

        // print keys in order using select
        System.out.println("Testing select");
        System.out.println("--------------------------------");
        for (int i = 0; i < st.size(); i++)
            System.out.println(i + " " + st.select(i)); 
        System.out.println();

        // test rank, floor, ceiling
        System.out.println("key rank floor ceil");
        System.out.println("-------------------");
        for (char i = 'A'; i <= 'Z'; i++) {
            String s = i + "";
            System.out.printf("%2s %4d %4s %4s\n", s, st.rank(s), st.floor(s), st.celling(s));
        }
        System.out.println();

        // test range search and range count
        String[] from = { "A", "Z", "X", "0", "B", "C" };
        String[] to   = { "Z", "A", "X", "Z", "G", "L" };
        System.out.println("range search");
        System.out.println("-------------------");
        for (int i = 0; i < from.length; i++) {
            System.out.printf("%s-%s (%2d) : ", from[i], to[i], st.size(from[i], to[i]));
            for (String s : st.keys(from[i], to[i]))
                System.out.print(s + " ");
            System.out.println();
        }
        System.out.println();

        // delete the smallest keys
        for (int i = 0; i < st.size() / 2; i++) {
            st.deleteMin();
        }
        System.out.println("After deleting the smallest " + st.size() / 2 + " keys");
        System.out.println("--------------------------------");
        for (String s : st.keys()) 
            System.out.println(s + " " + st.get(s)); 
        System.out.println();

        // delete all the remaining keys
        while (!st.isEmpty()) {
            st.delete(st.select(st.size() / 2));
        }
        System.out.println("After deleting the remaining keys");
        System.out.println("--------------------------------");
        for (String s : st.keys()) 
            System.out.println(s + " " + st.get(s)); 
        System.out.println();

        System.out.println("After adding back N keys");
        System.out.println("--------------------------------");
        for (int i = 0; i < keys.length; i++) 
            st.put(keys[i], i); 
        for (String s : st.keys()) 
            System.out.println(s + " " + st.get(s)); 
        System.out.println();

        System.out.println();




        // insert N elements in order if one command-line argument supplied
        if (args.length == 0) return;
        int n = Integer.parseInt(args[0]);
        RedBlackBST<Integer, Integer> st2 = new RedBlackBST<Integer, Integer>();
        for (int i = 0; i < n; i++) {
            st2.put(i, i);
            int h = st2.height();
            System.out.println("i = " + i + ", height = " + h + ", size = " + st2.size());
        }

        // delete keys in random order
        System.out.println("Deleting keys in random order");
        while (st2.size() > 0) {
            int i = StdRandom.uniform(n);
            if (st2.contains(i)) {
                st2.delete(i);
                int h = st2.height();
                System.out.println("i = " + i + ", height = " + h + ", size = " + st2.size());
            }
        }

        System.out.println("size = " + st2.size());
    }
}
