package com.hxd.graphs.directedGraphs;

/******************************************************************************
 *  Compilation:  javac DirectedDFS.java
 *  Execution:    java DirectedDFS digraph.txt s
 *  Dependencies: Digraph.java Bag.java In.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *                http://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 *                http://algs4.cs.princeton.edu/42digraph/largeDG.txt
 *
 *  Determine single-source or multiple-source reachability in a digraph
 *  using depth first search.
 *  Runs in O(E + V) time.
 *
 *  % java DirectedDFS
 *  0 to 2: true
 *	0 connects 6 points
 *	2 to 12: false
 *	2 connects 6 points
 *	4 to 12: false
 *	4 connects 6 points
 *	6 to 8: true
 *	6 connects 12 points
 *
 ******************************************************************************/

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.introcs.stdlib.In;
import com.hxd.introcs.stdlib.StdRandom;

/**
 * 	有向图的可达性,在有向图中,深度优先搜索标记由一个集合的顶点可达的所有顶点所需的时间与被标记的所有顶点的出度只和成正比
 *	{@code DirectedDFS}类表示用于确定从有向图中的给定源顶点（或源顶点集合）可达到的顶点的数据类型。
 *	有关找到路径的版本，请参阅{@link DepthFirstDirectedPaths}和{@link BreadthFirstDirectedPaths}。 
 *	此实现使用深度优先搜索。 构造函数需要与V + E成正比的时间（在最坏的情况下），其中V是顶点的数量，E是边的数量。
 *	The {@code DirectedDFS} class represents a data type for 
 *  determining the vertices reachable from a given source vertex <em>s</em>
 *  (or set of source vertices) in a digraph. For versions that find the paths,
 *  see {@link DepthFirstDirectedPaths} and {@link BreadthFirstDirectedPaths}.
 *  <p>
 *  This implementation uses depth-first search.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>
 *  (in the worst case),
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  <p>
 *	@author 候旭东 20170219
 * */

public class DirectedDFS {
	private boolean[] marked;
	private int count;			//可从s到达的顶点数

    /**
     * Computes the vertices in digraph {@code G} that are
     * reachable from the source vertex {@code s}.
     * @param G the digraph
     * @param s the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
	public DirectedDFS(Digraph G, int s) {
		marked = new boolean[G.V()];
		validateVertex(s);
		dfs(G, s);
	}

	/**
     * Computes the vertices in digraph {@code G} that are
     * connected to any of the source vertices {@code sources}.
     * @param G the graph
     * @param sources the source vertices
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     *         for each vertex {@code s} in {@code sources}
     */
	public DirectedDFS(Digraph G, Iterable<Integer> sources) {
		marked = new boolean[G.V()];
		validateVertices(sources);
		for (int v : sources) {
			if (!marked[v])
				dfs(G, v);
		}
	}
	
	private void dfs(Digraph G, int v) {
		count++;
		marked[v] = true;
		for (int w : G.adj(v))
			if (!marked[w])
				dfs(G, w);
	}
	
	/**
     * Is there a directed path from the source vertex (or any
     * of the source vertices) and vertex {@code v}?
     * @param  v the vertex
     * @return {@code true} if there is a directed path, {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public boolean marked(int v) {
		validateVertex(v);
		return  marked[v];
	}
	
	/**
     * Returns the number of vertices reachable from the source vertex
     * (or source vertices).
     * @return the number of vertices reachable from the source vertex
     *   (or source vertices)
     */
	public int count() {
		return count;
	}
	
	private void validateVertices(Iterable<Integer> vertices) {
		if (vertices == null) {
	           throw new IllegalArgumentException("argument is null");
	       }
	       int V = marked.length;
	       for (int v : vertices) {
	    	   if (v < 0 || v >= V) {
	    		   throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	    		   }
	       }
	}
	
	private void validateVertex(int v) {
		int V = marked.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
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
				for(int s = 0 ; s < G.V(); s+=2){
					DirectedDFS dfs = new DirectedDFS(G, s);
					int v = StdRandom.uniform(G.V());
					System.out.println(s + " to "+ v +": " + dfs.marked(v));
					System.out.println(s + " connects " + dfs.count() + " points");
				}
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
