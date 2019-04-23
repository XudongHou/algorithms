package com.hxd.sort;

import com.hxd.base.Stopwatch;
import com.hxd.introcs.stdlib.StdRandom;
import com.hxd.sort.base.Insertion;
import com.hxd.sort.base.Selection;
import com.hxd.sort.base.Shell;
import com.hxd.sort.merge.Merge;
import com.hxd.sort.merge.MergeBU;
import com.hxd.sort.priorityQueue.Heap;
import com.hxd.sort.quick.Quick;

/**
 * 候旭东  20161216 比较两种排序算法的时间
 * */
public class SortCompare {
	
	 public static double time(String alg, Double[] a) { 
	        Stopwatch sw = new Stopwatch(); 
	        if      (alg.equals("Insertion"))       Insertion.sort(a); 
	        else if (alg.equals("Selection"))       Selection.sort(a); 
	        else if (alg.equals("Shell"))           Shell.sort(a); 
	        else if (alg.equals("Merge"))           Merge.sort(a); 
	        else if (alg.equals("MergeBU"))         MergeBU.sort(a); 
	        else if (alg.equals("Quick"))           Quick.sort(a); 
	        else if (alg.equals("Heap"))            Heap.sort(a); 
	        else throw new IllegalArgumentException("Invalid algorithm: " + alg);
	        return sw.elapsedTime(); 
	    } 

	    // Use alg to sort trials random arrays of length n.
	    public static double timeRandomInput(String alg, int n, int trials)  {
	        double total = 0.0;
	        Double[] a = new Double[n];
	        // Perform one experiment (generate and sort an array).
	        for (int t = 0; t < trials; t++) {
	            for (int i = 0; i < n; i++)
	                a[i] = StdRandom.uniform(0.0, 1.0);
	            total += time(alg, a);
	        } 
	        return total; 
	    } 

	    // Use alg to sort trials random arrays of length n. 
	    public static double timeSortedInput(String alg, int n, int trials) {
	        double total = 0.0;
	        Double[] a = new Double[n];
	        // Perform one experiment (generate and sort an array).
	        for (int t = 0; t < trials; t++) {
	            for (int i = 0; i < n; i++)
	                a[i] = 1.0 * i;
	            total += time(alg, a);
	        } 
	        return total; 
	    } 

	    public static void main(String[] args) { 
	        String alg1 = args[0]; 
	        String alg2 = args[1]; 
	        int n = Integer.parseInt(args[2]);
	        int trials = Integer.parseInt(args[3]);
	        double time1, time2;
	        if (args.length == 5 && args[4].equals("sorted")) {
	            time1 = timeSortedInput(alg1, n, trials);   // Total for alg1. 
	            time2 = timeSortedInput(alg2, n, trials);   // Total for alg2. 
	        }
	        else {
	            time1 = timeRandomInput(alg1, n, trials);   // Total for alg1. 
	            time2 = timeRandomInput(alg2, n, trials);   // Total for alg2. 
	        }

	        System.out.printf("For %d random Doubles\n    %s is", n, alg1); 
	        System.out.printf(" %.1f times faster than %s\n", time2/time1, alg2); 
	    } 
} 
