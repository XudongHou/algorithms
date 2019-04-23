package com.hxd.sort;

/**
 * 候旭东 20161219 交易排序测试用例
 * */

import java.util.Arrays;

import com.hxd.base.Queue;
import com.hxd.sort.base.Transaction;

public class SortTransactions {
    public static Transaction[] readTransactions(String[] strings) {
        Queue<Transaction> queue = new Queue<Transaction>();
        for(String line: strings) {
            Transaction transaction = new Transaction(line);
            queue.enqueue(transaction);
        }
        int n = queue.size();
        Transaction[] transactions = new Transaction[n];
        for (int i = 0; i < n; i++)
            transactions[i] = queue.dequeue();

        return transactions;
    }

    public static void main(String[] args) {
    	String[] strings = new String[]{"Bellman   10/26/2007  1358.62",
    			"Turing     1/11/2002    66.10","Hoare      2/10/2005  4050.20",
        		"Knuth      6/14/1999   288.34","Dijkstra  11/18/1995   837.42",
        		"Dijkstra   9/10/2000   708.95","Turing     6/17/1990   644.08",
        		"Turing     2/11/1991  2156.86","Dijkstra   8/22/2007  2678.40",
        		"Tarjan    10/13/1993  2520.97","Turing    10/12/1993  3532.36",
        		"Tarjan     3/26/2002  4121.85","Hoare      5/10/1993  3229.27",
        		"Hoare      8/12/2003  1025.70","Knuth     11/11/2008  3284.33",
        		"Hoare      8/18/1992  4381.21","Tarjan     1/11/1999  4409.74",
        		"Thompson   2/27/2000  4747.08","Tarjan     2/12/1994  4732.35",
        		"Knuth      7/25/2008  1564.55"
        		};
        Transaction[] transactions = readTransactions(strings);
        Arrays.sort(transactions);
        for (int i = 0; i < transactions.length; i++)
            System.out.println(transactions[i]);
    }
}

