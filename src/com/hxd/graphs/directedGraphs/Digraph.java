package com.hxd.graphs.directedGraphs;
/******************************************************************************
 *  Compilation:  javac Digraph.java
 *  Execution:    java Digraph filename.txt
 *  Dependencies: Bag.java In.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/42Digraph/tinyDG.txt
 *                http://algs4.cs.princeton.edu/42Digraph/mediumDG.txt
 *                http://algs4.cs.princeton.edu/42Digraph/largeDG.txt  
 *
 *  A graph, implemented using an array of lists.
 *  Parallel edges and self-loops are permitted.
 *
 *  % java Digraph tinyDG.txt
 *  13 vertices, 22 edges
 *  0: 5 1 
 *  1: 
 *  2: 0 3 
 *  3: 5 2 
 *  4: 3 2 
 *  5: 4 
 *  6: 9 4 8 0 
 *  7: 6 9
 *  8: 6 
 *  9: 11 10 
 *  10: 12 
 *  11: 4 12 
 *  12: 9 
 *  
 ******************************************************************************/

import java.io.File;
import java.nio.file.FileSystemNotFoundException;
import java.util.NoSuchElementException;

import com.hxd.base.Bag;
import com.hxd.base.Stack;
import com.hxd.introcs.stdlib.In;

/**
 *	{@code Digraph}类表示名为0到V-1的顶点的有向图。它支持以下两个主要操作：向有向图添加边，
 *	遍历从给定顶点相邻的所有顶点。 允许平行边缘和自循环。 这个实现使用邻接列表表示，它是{@link Bag}
 *	对象的顶点索引数组。 所有操作都需要恒定时间（在最坏的情况下），除了迭代从给定顶点相邻的顶点，这需要
 *	与这样的顶点的数量成比例的时间。
 *	The {@code Digraph} class represents a directed graph of vertices
 *  named 0 through <em>V</em> - 1.
 *  It supports the following two primary operations: add an edge to the Digraph,
 *  iterate over all of the vertices adjacent from a given vertex.
 *  Parallel edges and self-loops are permitted.
 *  <p>
 *  This implementation uses an adjacency-lists representation, which 
 *  is a vertex-indexed array of {@link Bag} objects.
 *  All operations take constant time (in the worst case) except
 *  iterating over the vertices adjacent from a given vertex, which takes
 *  time proportional to the number of such vertices.
 *	@author 候旭东 20170219
 * */

public class Digraph {
	private static final String NEWLINE = System.getProperty("line.separator");
	
	private final int V;			//顶点数
	private int E;					//边数
	private Bag<Integer>[] adj;		//adj[v] =为顶点v邻接表
	private int[] indegree;			//indegeree[v] = v的深度
	
	/**
     * Initializes an empty Digraph with <em>V</em> vertices.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
	@SuppressWarnings("unchecked")
	public Digraph(int V) {
		if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
		this.V = V;
		this.E = 0;
		indegree = new int[V];
		adj = (Bag<Integer>[]) new Bag[V];
		for (int v = 0; v < V; v++)
			adj[v] = new Bag<Integer>();
	}
	
	@SuppressWarnings("unchecked")
	public Digraph(In in) {
		try {
			this.V = in.readInt();
			int E = in.readInt();
			if (V < 0 || E < 0) throw new IllegalArgumentException("number of vertices in a Digraph must be nonnegative");
			indegree = new int[V];
			adj = (Bag<Integer>[]) new Bag[V];
			for (int v = 0; v < V; v++)
				adj[v] = new Bag<Integer>();
			for (int i = 0; i < E; i++) {
				int v = in.readInt();
				int w = in.readInt();
				addEdge(v,w);
			}
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException("invalid input format in Digraph constructor", e);
		}
	}
	
	/**
	 * 初始化一个新的有向图，它是指定有向图的深度副本。
     * Initializes a new Digraph that is a deep copy of the specified Digraph.
     *
     * @param  G the Digraph to copy
     */
	public Digraph(Digraph G) {
		this(G.V());
		this.E = G.E();
		for (int v = 0; v < V; v++)
			this.indegree[v] = G.indegree(v);
		for (int v = 0; v < V; v++) {
			Stack<Integer> reserve = new Stack<Integer>();
			for (int w : G.adj(v))
				reserve.push(w);
			for (int w : reserve)
				adj[v].add(w);
		}
	}

	/**
     * Returns the number of vertices in this Digraph.
     *
     * @return the number of vertices in this Digraph
     */
    public int V() {
        return V;
    }
	
    /**
     * Returns the number of edges in this Digraph.
     *
     * @return the number of edges in this Digraph
     */
    public int E() {
        return E;
    }
    
 // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }
    
    /**
     * Adds the directed edge v→w to this Digraph.
     *
     * @param  v the tail vertex
     * @param  w the head vertex
     * @throws IllegalArgumentException unless both {@code 0 <= v < V} and {@code 0 <= w < V}
     */
	public void addEdge(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		adj[v].add(w);
		indegree[w]++;
		E++;
	}
	
	/**
     * Returns the vertices adjacent from vertex {@code v} in this Digraph.
     *
     * @param  v the vertex
     * @return the vertices adjacent from vertex {@code v} in this Digraph, as an iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public Iterable<Integer> adj(int v) {
		validateVertex(v);
		return adj[v];
	}
	
	/**
	 * 出度为由该顶点指出的边总数;
	 * 返回从顶点{@code v}出度的有向边的数量。 这被称为顶点{@code v}的<em> outdegree </ em>。
     * Returns the number of directed edges incident from vertex {@code v}.
     * This is known as the <em>outdegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the outdegree of vertex {@code v}               
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public int outdegree(int v) {
		validateVertex(v);
		return adj[v].size();
	}
	
	/**
	 * 入度为指向该顶点的边的总数
	 * 返回入度到顶点{@code v}的有向边的数量。 这被称为顶点{@code v}的<em> indegree </ em>。
	 * Returns the number of directed edges incident to vertex {@code v}.
     * This is known as the <em>indegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the indegree of vertex {@code v}               
     * @throws IllegalArgumentException unless {@code 0 <= v < V}

	 * */
	public int indegree(int v) {
		validateVertex(v);
		return indegree[v];
	}
	
	/**
     * Returns the reverse of the digraph.
     *
     * @return the reverse of the digraph
     */
	public Digraph reverse() {
		Digraph reverse = new Digraph(V);
		for (int v = 0; v < V; v++) {
			for (int w : adj(v) )
				reverse.addEdge(w, v);
		}
		return reverse;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V + "vertices, "+ E + " edges "+ NEWLINE);
		for (int v = 0; v < V; v++) {
			s.append(String.format("%d: ", v));
			for (int w : adj[v]) 
				s.append(String.format("%d ", w));
			s.append(NEWLINE);
		}
		return s.toString();
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{
					new File(".\\algs4-data\\tinyDG.txt"),
					new File(".\\algs4-data\\mediumDG.txt"),
					};
			for(File file : files) {
				In in = new In(file);
				Digraph G = new Digraph(in); 
				System.out.println(G);
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
