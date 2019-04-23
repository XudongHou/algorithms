package com.hxd.graphs.directedGraphs;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.hxd.introcs.stdlib.StdRandom;

/**
 * {@code AdjMatrixDigraph}类使用邻接矩阵表示法实现Digraph相同的API。
 * @author 候旭东 20170219
 * */

public class AdjMatrixDigraph {
	private int V;
	private int E;
	private boolean[][] adj;
	
	public AdjMatrixDigraph(int V) {
		if (V < 0) throw new RuntimeException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        this.adj = new boolean[V][V];
	}
	
	public AdjMatrixDigraph(int V,int E) {
		this(V);
        if (E < 0) throw new RuntimeException("Number of edges must be nonnegative");
        if (E > V*V) throw new RuntimeException("Too many edges");
        while (this.E != E) {
        	int v = StdRandom.uniform(V);
        	int w = StdRandom.uniform(V);
        	addEdge(v,w);
        }
	}

    // number of vertices and edges
    public int V() { return V; }
    public int E() { return E; }
	
	public void addEdge(int v, int w) {
		if (!adj[v][w]) E++;
		adj[v][w] = true;
	}
	
	public Iterable<Integer> adj(int v) {
		return new AdjIterator(v);
	}
	
	private class AdjIterator implements Iterable<Integer>,Iterator<Integer> {
		private int v;
		private int w;
		
		AdjIterator(int v) {
			this.v = v;
		}

		@Override
		public boolean hasNext() {
			while (w < V) {
				if (adj[v][w]) return true;
				w++;
			}
			return false;
		}

		@Override
		public Integer next() {
			if (hasNext())	return w++;
			else			throw new NoSuchElementException();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<Integer> iterator() {
			return this;
		}
	}
	
	@Override
	public String toString() {
		String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (int w : adj(v)) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
	}
	
	public static void main(String[] args) {
		int V = 13, E = 22;
		AdjMatrixDigraph G = new AdjMatrixDigraph(V,E);
		System.out.println(G);
	}
}
