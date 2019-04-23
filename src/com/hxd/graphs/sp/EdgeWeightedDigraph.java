package com.hxd.graphs.sp;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;
import java.util.Stack;

import com.hxd.base.Bag;
import com.hxd.introcs.stdlib.In;
import com.hxd.introcs.stdlib.StdRandom;

/**
 * {@code EdgeWeightedDigraph}类表示名为0到V-1的顶点的加权有向图，其中每个有向边的类型为{@link DirectedEdge}，
 * 并且具有实值权重。 它支持以下两个主要操作：向有向图添加有向边并且对从给定顶点出度的所有边进行迭代。 它还提供用于返回顶点数目V和边
 * 数目E的方法。允许平行边缘和自循环。 此实现使用邻接列表表示，它是@link {Bag}对象的顶点索引数组。 所有操作都需要恒定的时间
 * （在最坏的情况下），除了迭代从给定顶点出度的边缘，这需要与这样的边的数量成比例的时间。
 * @author houxu_000 20170225
 * */

@SuppressWarnings("unchecked")
public class EdgeWeightedDigraph {
	private static final String NEWLINE = System.getProperty("line.separator");
	
	private final int V;				// number of vertices in this digraph
	private int E;						// number of edges in this digraph
	private Bag<DirectedEdge>[] adj;	// adj[v] = adjacency list for vertex v
	private int[] indegree;				// indegree[v] = indegree of vertex v

	/**
     * Initializes an empty edge-weighted digraph with {@code V} vertices and 0 edges.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
	public EdgeWeightedDigraph(int V) {
		if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
		this.V = V;
		this.E = 0;
		this.indegree = new int[V];
		adj = (Bag<DirectedEdge>[]) new Bag[V];
		for (int v = 0; v < V; v++)
			adj[v] = new Bag<DirectedEdge>();
	}
	
	/**
     * Initializes a random edge-weighted digraph with {@code V} vertices and <em>E</em> edges.
     *
     * @param  V the number of vertices
     * @param  E the number of edges
     * @throws IllegalArgumentException if {@code V < 0}
     * @throws IllegalArgumentException if {@code E < 0}
     */
	public EdgeWeightedDigraph(int V, int E) {
		this(V);
		if (E < 0) throw new IllegalArgumentException("Number of edges in a Digraph must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = StdRandom.uniform(V);
			int w = StdRandom.uniform(V);
			double weight = 0.01 * StdRandom.uniform(100);
			DirectedEdge e = new DirectedEdge(v, w, weight);
			addEdge(e);
		}
	}

	/**  
     * Initializes an edge-weighted digraph from the specified input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices and edge weights,
     * with each entry separated by whitespace.
     *
     * @param  in the input stream
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
	public EdgeWeightedDigraph(In in) {
		this(in.readInt());
		int E = in.readInt();
		if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = in.readInt();
			int w = in.readInt();
			validateVertex(v);
			validateVertex(w);
			double weight = in.readDouble();
			addEdge(new DirectedEdge(v, w, weight));
		}
	}
	
	/**
     * Initializes a new edge-weighted digraph that is a deep copy of {@code G}.
     *
     * @param  G the edge-weighted digraph to copy
     */
	public EdgeWeightedDigraph(EdgeWeightedDigraph G) {
		this(G.V());
		this.E = G.E();
		for (int v = 0; v < G.V(); v++)
			this.indegree[v] = G.indegree(v);
		for (int v = 0; v < G.V(); v++) {
			Stack<DirectedEdge> reverse = new Stack<DirectedEdge>();
			for (DirectedEdge e : G.adj(v))
				reverse.push(e);
			for (DirectedEdge e : reverse)
				adj[v].add(e);
		}
	}
	
	/**
     * Returns the number of edges in this edge-weighted digraph.
     *
     * @return the number of edges in this edge-weighted digraph
     */
	public int E() {
		return E;
	}

	/**
     * Returns the number of vertices in this edge-weighted digraph.
     *
     * @return the number of vertices in this edge-weighted digraph
     */
	public int V() {
		return V;
	}

	/**
     * Returns the directed edges incident from vertex {@code v}.
     *
     * @param  v the vertex
     * @return the directed edges incident from vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public Iterable<DirectedEdge> adj(int v) {
		validateVertex(v);
		return adj[v];
	}

	/**
     * Adds the directed edge {@code e} to this edge-weighted digraph.
     *
     * @param  e the edge
     * @throws IllegalArgumentException unless endpoints of edge are between {@code 0}
     *         and {@code V-1}
     */
	public void addEdge(DirectedEdge e) {
		int v = e.from(), w = e.to();
		validateVertex(v);
		validateVertex(w);
		adj[v].add(e);
		indegree[w]++;
		E++;
	}
	
	/**
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
     * Returns the number of directed edges incident to vertex {@code v}.
     * This is known as the <em>indegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the indegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public int indegree(int v) {
		validateVertex(v);
		return indegree[v];
	}

	/**
     * Returns all directed edges in this edge-weighted digraph.
     * To iterate over the edges in this edge-weighted digraph, use foreach notation:
     * {@code for (DirectedEdge e : G.edges())}.
     *
     * @return all edges in this edge-weighted digraph, as an iterable
     */
	public Iterable<DirectedEdge> edges() {
		Bag<DirectedEdge> list = new Bag<DirectedEdge>();
		for (int v = 0; v < V; v++)
			for (DirectedEdge e : adj(v))
				list.add(e);
		return list;
	}
	
	private void validateVertex(int v) {
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}
	
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V + " " + E + NEWLINE);
		for (int v = 0; v < V; v++) {
			s.append(v + ": ");
			for (DirectedEdge e : adj[v]) {
				s.append(e + "  ");
			}
	        s.append(NEWLINE);
		}
		return s.toString();
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{
					new File(".\\algs4-data\\tinyEWD.txt"),
//					new File(".\\algs4-data\\mediumEWD.txt"),
					};
			for(File file : files) {
				In in = new In(file);
				EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
				System.out.println(G);
				for(int v = 0; v < G.V(); v++)
					for (DirectedEdge e : G.adj(v))
						System.out.println(e);
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
