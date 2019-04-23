package com.hxd.graphs.directedGraphs;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Queue;
import com.hxd.introcs.stdlib.In;

/**
 * 强力强组件算法。 BruteSCC.java通过首先计算传递闭包来计算强组件。它需要O（EV）时间和O（V ^ 2）空间。
 * @author houxu_000 
 * */

public class BruteSCC {
	private int count;	// number of strongly connected components
	private int[] id;	// id[v] = id of strong component containing v
	
	public BruteSCC(Digraph G) {
		
		id = new int[G.V()];
		for (int v = 0; v < G.V(); v++)
			id[v] = v;
		
		TransitiveClosure tc = new TransitiveClosure(G);
		
		for (int v = 0; v < G.V(); v++)
			for (int w = 0; w < v; w++)
				if (tc.reachable(v, w) && tc.reachable(w, v))
					id[v] = id[w];
		
		for (int v = 0; v < G.V(); v++)
			if (id[v] == v)
				count++;
	}
	
	// return the number of strongly connected components
    public int count() { return count; }

    // are v and w strongly connected?
    public boolean stronglyConnected(int v, int w) {
        return id[v] == id[w];
    }

    // in which strongly connected component is vertex v?
    public int id(int v) { return id[v]; }
    
    @SuppressWarnings("unchecked")
	public static void main(String[] args) {
    	try {
			File[] files = new File[]{
					new File(".\\algs4-data\\tinyDG.txt"),
//					new File(".\\algs4-data\\mediumDG.txt"),
					};
			for(File file : files) {
				In in = new In(file);
				Digraph G = new Digraph(in);
				System.out.println(G);
				BruteSCC scc = new BruteSCC(G);
				
				int m = scc.count();
				System.out.println(m + " strong components");
				Queue<Integer>[] components = (Queue<Integer>[]) new Queue[m];
				for (int i = 0; i < m; i++)
					components[i] = new Queue<Integer>();
				for (int v = 0; v < G.V(); v++)
					components[scc.id(v)].enqueue(v);
				for (int i = 0; i < m; i++){
					for (int v : components[i])
						System.out.print(v+"  ");
					System.out.println();
				}
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
