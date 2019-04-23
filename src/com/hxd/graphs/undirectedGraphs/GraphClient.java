package com.hxd.graphs.undirectedGraphs;

import java.io.File;

import com.hxd.introcs.stdlib.In;
import com.hxd.introcs.stdlib.StdOut;

/**
 * 包含典型的图形处理代码。
 * @author 候旭东 20170216
 * */

public class GraphClient {

    // maximum degree 
    public static int maxDegree(Graph G) {
        int max = 0;
        for (int v = 0; v < G.V(); v++)
            if (G.degree(v) > max)
                max = G.degree(v);
        return max;
    }

    // average degree
    public static int avgDegree(Graph G) {
        // each edge incident on two vertices
        return 2 * G.E() / G.V();
    }

    // number of self-loops
    public static int numberOfSelfLoops(Graph G) {
        int count = 0;
        for (int v = 0; v < G.V(); v++)
            for (int w : G.adj(v))
                if (v == w) count++;
        return count/2;   // self loop appears in adjacency list twice
    } 

    public static void main(String[] args) {
    	File file1 = new File(".\\algs4-data\\tinyG.txt");
        In in = new In(file1);
        Graph G = new Graph(in);
        StdOut.println(G);


        StdOut.println("vertex of maximum degree = " + maxDegree(G));
        StdOut.println("average degree           = " + avgDegree(G));
        StdOut.println("number of self loops     = " + numberOfSelfLoops(G));

    }
}
