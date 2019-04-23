package com.hxd.graphs.directedGraphs;

/******************************************************************************
 *  Compilation:  javac Topoological.java
 *  Execution:    java  Topological filename.txt delimiter
 *  Dependencies: Digraph.java DepthFirstOrder.java DirectedCycle.java
 *                EdgeWeightedDigraph.java EdgeWeightedDirectedCycle.java
 *                SymbolDigraph.java
 *  Data files:   http://algs4.cs.princeton.edu/42digraph/jobs.txt
 *
 *  Compute topological ordering of a DAG or edge-weighted DAG.
 *  Runs in O(E + V) time.
 *
 *  % java Topological jobs.txt "/"
 *  Calculus
 *  Linear Algebra
 *  Introduction to CS
 *  Programming Systems
 *  Algorithms
 *  Theoretical CS
 *  Artificial Intelligence
 *  Machine Learning
 *  Neural Networks
 *  Robotics
 *  Scientific Computing
 *  Computational Biology
 *  Databases
 *
 *
 ******************************************************************************/

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.graphs.sp.EdgeWeightedDigraph;
import com.hxd.graphs.sp.EdgeWeightedDirectedCycle;

/**
 *	拓扑排序:给定一幅有向图,将所有顶点排序,使得所有的有向边均从排在前面的元素指向排在后面的元素
 *	(或者说明无法做到这一点).一幅有向无环图的拓扑排序顺序即为所有顶点的逆后序排列.使用深度优先
 *	搜索对有向无环图进行拓扑排序所需的时间和V+E成正比.一般来说,如果一个有优先限制的问题中存在有
 *	向环,那么这个问题基本是无解的
 *	<p>
 *	@author 候旭东 20170221
 * */

public class Topological {
	private Iterable<Integer> order;		//顶点的拓扑排序
	private int[] rank;						// rank [v] =顶点v的拓扑顺序的位置
	
	/**
     * Determines whether the digraph {@code G} has a topological order and, if so,
     * finds such a topological order.
     * @param G the digraph
     */
	public Topological(Digraph G) {
		DirectedCycle finder = new DirectedCycle(G);
		if (!finder.hasCycle()) {
			DepthFirstOrder dfs = new DepthFirstOrder(G);
			order = dfs.reversePost();
			rank = new int[G.V()];
			int i = 0;
			for (int v : order)
				rank[v] = i++;
		}
	}
	
	/**
     * Determines whether the edge-weighted digraph {@code G} has a topological
     * order and, if so, finds such an order.
     * @param G the edge-weighted digraph
     */
    public Topological(EdgeWeightedDigraph G) {
        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(G);
        if (!finder.hasCycle()) {
            DepthFirstOrder dfs = new DepthFirstOrder(G);
            order = dfs.reversePost();
        }
    }
	
	/**
     * Returns a topological order if the digraph has a topologial order,
     * and {@code null} otherwise.
     * @return a topological order of the vertices (as an interable) if the
     *    digraph has a topological order (or equivalently, if the digraph is a DAG),
     *    and {@code null} otherwise
     */
	public Iterable<Integer> order() {
		return order;
	}
	
	/**
     * Does the digraph have a topological order?
     * @return {@code true} if the digraph has a topological order (or equivalently,
     *    if the digraph is a DAG), and {@code false} otherwise
     */
	public boolean hasOrder() {
		return order != null;
	}
	
	/**
     * The the rank of vertex {@code v} in the topological order;
     * -1 if the digraph is not a DAG
     *
     * @param v the vertex
     * @return the position of vertex {@code v} in a topological order
     *    of the digraph; -1 if the digraph is not a DAG
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public int rank(int v) {
		validateVertex(v);
		if (hasOrder()) return rank[v];
		else			return -1;
	}

	private void validateVertex(int v) {
		 int V = rank.length;
	        if (v < 0 || v >= V)
	            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{
					new File(".\\algs4-data\\jobs.txt"),
					};
			for(File file : files) {
				String delimiter = "/";
				SymbolDigraph sg = new SymbolDigraph(file, delimiter);
				Topological topological = new Topological(sg.digraph());
				for (int v : topological.order())
					System.out.println(sg.nameOf(v));
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
