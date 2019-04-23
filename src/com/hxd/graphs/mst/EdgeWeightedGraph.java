package com.hxd.graphs.mst;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Bag;
import com.hxd.introcs.Stack;
import com.hxd.introcs.stdlib.In;
import com.hxd.introcs.stdlib.StdRandom;

/**
 * {@code EdgeWeightedGraph}类表示名为0到V-1的顶点的边缘加权图，其中每个无向边为{@link Edge}类型，
 * 并具有实值权重。 它支持以下两个主要操作：向图中添加边，对入射到顶点的所有边进行迭代。 它还提供用于返回顶点数目V和
 * 边数目E的方法。允许平行边缘和自循环。 此实现使用邻接列表表示，它是@link {Bag}对象的顶点索引数组。 
 * 所有操作都需要恒定的时间（在最坏的情况下），除了在入射到给定顶点的边上迭代，这需要与这样的边的数量成比例的时间。
 * @author houxu_000
 * */

public class EdgeWeightedGraph {
	
	private static final String NEWLINE = System.getProperty("line.separator");
	
	private final int V;
	private int E;
	private Bag<Edge>[] adj;
	
	/**
     * Initializes an empty edge-weighted graph with {@code V} vertices and 0 edges.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
	@SuppressWarnings("unchecked")
	public EdgeWeightedGraph(int V) {
		if (V < 0)	throw new IllegalArgumentException("Number of vertices must be nonnegative");
		this.V = V;
		this.E = 0;
		adj = (Bag<Edge>[]) new Bag[V];
		for (int v = 0; v < V; v++)
			adj[v] = new Bag<Edge>();
	}
	
	 /**
     * Initializes a random edge-weighted graph with {@code V} vertices and <em>E</em> edges.
     *
     * @param  V the number of vertices
     * @param  E the number of edges
     * @throws IllegalArgumentException if {@code V < 0}
     * @throws IllegalArgumentException if {@code E < 0}
     */
	public EdgeWeightedGraph(int V, int E) {
		this(V);
		if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = StdRandom.uniform(V);
			int w = StdRandom.uniform(V);
			double weight = Math.round(100 * StdRandom.uniform()) / 100.0;
			Edge edge = new Edge(v, w, weight);
			addEdge(edge);
		}
	}

	/**  
     * Initializes an edge-weighted graph from an input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices and edge weights,
     * with each entry separated by whitespace.
     *
     * @param  in the input stream
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
	public EdgeWeightedGraph(In in) {
		this(in.readInt());
		int E = in.readInt();
		if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = in.readInt();
			int w = in.readInt();
			validateVertex(v);
			validateVertex(w);
			double weight = in.readDouble();
			Edge edge = new Edge(v, w, weight);
			addEdge(edge);
		}
	}
	
	/**
     * Initializes a new edge-weighted graph that is a deep copy of {@code G}.
     *
     * @param  G the edge-weighted graph to copy
     */
	public EdgeWeightedGraph(EdgeWeightedGraph G) {
		this(G.V());
		this.E = G.E();
		for (int v = 0; v < G.V(); v++) {
			Stack<Edge> reverse = new Stack<Edge>();
			for (Edge e : G.adj(v))
				reverse.push(e);
			for (Edge e : reverse )
				adj[v].add(e);
		}
	}
	
	/**
     * Returns the number of vertices in this edge-weighted graph.
     *
     * @return the number of vertices in this edge-weighted graph
     */
	public int V() {
		return V;
	}

	/**
	 * Returns the number of edges in this edge-weighted graph.
	 *
	 * @return the number of edges in this edge-weighted graph
	 */
	public int E() {
		return E;
	}

	/**
     * Adds the undirected edge {@code e} to this edge-weighted graph.
     *
     * @param  e the edge
     * @throws IllegalArgumentException unless both endpoints are between {@code 0} and {@code V-1}
     */
	public void addEdge(Edge edge) {
		int v = edge.either();
		int w = edge.other(v);
		validateVertex(v);
		validateVertex(w);
		adj[v].add(edge);
		adj[w].add(edge);
		E++;
	}
	
	/**
	 * Returns the edges incident on vertex {@code v}.
	 *
	 * @param  v the vertex
	 * @return the edges incident on vertex {@code v} as an Iterable
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public Iterable<Edge> adj(int v) {
		validateVertex(v);
		return adj[v];
	}

	/**
     * Returns the degree of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the degree of vertex {@code v}               
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public int degree(int v) {
		validateVertex(v);
		return adj[v].size();
	}
	
	/**
     * Returns all edges in this edge-weighted graph.
     * To iterate over the edges in this edge-weighted graph, use foreach notation:
     * {@code for (Edge e : G.edges())}.
     * @return all edges in this edge-weighted graph, as an iterable
     */
	public Iterable<Edge> edges() {
		Bag<Edge> list = new Bag<Edge>();
		for (int v = 0; v < V; v++) {
			int selfLoops = 0;
			for (Edge e : adj(v)) {
				if (e.other(v) > v)
					list.add(e);
				else if (e.other(v) == v){
						if(selfLoops % 2 == 0)	list.add(e);
						selfLoops ++;
					}
			}
		}
		return list;
	}
	
	private void validateVertex(int v) {
		if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V + " "+E + NEWLINE);
		for (int v = 0; v < V; v++) {
			s.append(v + ": ");
			for (Edge e : adj(v))
				s.append(e + " ");
			s.append(NEWLINE);
		}
		return s.toString();
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{
					new File(".\\algs4-data\\tinyEWG.txt"),
					new File(".\\algs4-data\\mediumEWG.txt"),
					};
			for(File file : files) {
				In in = new In(file);
				EdgeWeightedGraph G = new EdgeWeightedGraph(in);
				System.out.println(G);
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
