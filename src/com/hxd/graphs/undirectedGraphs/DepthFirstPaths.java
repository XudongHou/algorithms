package com.hxd.graphs.undirectedGraphs;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Stack;
import com.hxd.introcs.stdlib.In;

/**
 *	基于深度优先搜索的许找路径
 *	The {@code DepthFirstPaths} class represents a data type for finding
 *  paths from a source vertex <em>s</em> to every other vertex
 *  in an undirected graph.
 *  <p>
 *  This implementation uses depth-first search.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  It uses extra space (not including the graph) proportional to <em>V</em>.
 *  <p>
 * 	@author 候旭东 20170216
 * */

public class DepthFirstPaths {
	private boolean[] marked;	//这个顶点上调用过dfs()吗?
	private int[] edgedTo;		//从起点到一个定点的已知路径上的最后一个顶点
	private final int s;		//起点s
	
	public DepthFirstPaths(Graph G, int s) {
		marked = new boolean[G.V()];
		edgedTo = new int[G.V()];
		this.s = s;
		dfs(G,s);
	}
	// depth first search from v
	private void dfs(Graph G, int v) {
		marked[v] = true;
		for (int w : G.adj(v))
			if(!marked[w]){
				edgedTo[w] = v;
				dfs(G, w);
			}
	}
	
	/**
	 * 在源顶点{@code s}和顶点{@code v}之间是否有路径？
     * Is there a path between the source vertex {@code s} and vertex {@code v}?
     * @param v the vertex
     * @return {@code true} if there is a path, {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public boolean hasPathTo(int v) {
		validateVertex(v);
		return marked[v];
	}

	public Iterable<Integer> pathTo(int v) {
		validateVertex(v);
		if (!hasPathTo(v)) return null;
		Stack<Integer> path = new Stack<Integer>();
		for (int x = v; x != s ;x = edgedTo[x])
			path.push(x);
		path.push(s);
		return path;
	}
	
	private void validateVertex(int v) {
		int V = marked.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}
	
	/**
     * Unit tests the {@code DepthFirstPaths} data type.
     *
     * @param args the command-line arguments
     */
	public static void main(String[] args) {
		try {
			File file1 = new File(".\\algs4-data\\tinyG.txt");
			In in = new In(file1);
			Graph G = new Graph(in); 
			for(int j = 0 ; j < G.V(); j+=2) {
				System.out.println(j);
				DepthFirstPaths dfs = new DepthFirstPaths(G, j);
				
				for (int v = 0; v < G.V(); v++) {
					if (dfs.hasPathTo(v)){
						System.out.printf("%d to %d: ",j,v);
						for (int x : dfs.pathTo(v)){
							if (x == j)		System.out.print(x);
							else			System.out.print("-"+x);
						}
						System.out.println();
					}
					else {
						System.out.printf("%d to %d:  not connected\n", j, v);
					}
				}
			}	
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
