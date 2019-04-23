package com.hxd.graphs.undirectedGraphs;
/******************************************************************************
 *  Compilation:  javac AdjMatrixGraph.java
 *  Execution:    java AdjMatrixGraph V E
 *  Dependencies: StdOut.java
 *
 *  A graph, implemented using an adjacency matrix.
 *  Parallel edges are disallowed; self-loops are allowd.
 *  
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.hxd.introcs.stdlib.StdRandom;

/**
 *	{@code}使用邻接矩阵表示实现相同的API
 * 	@author 候旭东 20170216
 * */

public class AdjMatrixGraph {
	private static final String NEWLINE = System.getProperty("line.separator");
	private int V;
	private int E;
	private boolean[][] adj;
	
	public AdjMatrixGraph(int V) {
		if (V < 0) throw new RuntimeException("Number of vertices must be nonnegative");
		this.V = V;
		this.E = 0;
		this.adj = new boolean[V][V];
	}
	
	public AdjMatrixGraph(int V, int E) {
		this(V);
		if (E < 0) throw new RuntimeException("Number of edges must be nonnegative");
        if (E > V*(V-1) + V) throw new RuntimeException("Too many edges");
        
        while (this.E != E) {
        	int v = StdRandom.uniform(V);
        	int w =  StdRandom.uniform(V);
        	addEdge(v, w);
        }
	}
	
	public int V() {return V;}
	
	public int E() {return E;}
	
	public void addEdge(int v, int w) {
		if (!adj[v][w])	E++;
		adj[v][w] = true;
		adj[w][v] = true;
	}
	
	public boolean contains(int v, int w) {
		return adj[v][w];
	}
	
	public Iterable<Integer> adj(int v) {
		return new AdjIterator(v);
	}
	
	private class AdjIterator implements Iterator<Integer>,Iterable<Integer> {

		private int v;
		private int w = 0;
		
		AdjIterator(int v) {
			this.v = v;
		}
		
		@Override
		public Iterator<Integer> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			while (w < V) {
				if (adj[v][w])
					return true;
				w++;
			}
			return false;
		}

		@Override
		public Integer next() {
			if (!hasNext())
				 throw new NoSuchElementException();
			return w++;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V +" "+E+NEWLINE);
		for (int v = 0; v < V ; v++) {
			s.append(v + ": ");
			for (int w : adj(v)) 
				s.append(w + " ");
			s.append(NEWLINE);
		}
		return s.toString();
	}
	
	public static void main(String[] args) {
		int V = 13;
		int E = 13;
		AdjMatrixGraph G = new AdjMatrixGraph(V, E);
		System.out.println(G);
	}
}
