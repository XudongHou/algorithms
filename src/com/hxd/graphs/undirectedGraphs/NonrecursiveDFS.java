package com.hxd.graphs.undirectedGraphs;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;
import java.util.Iterator;

import com.hxd.base.Stack;
import com.hxd.introcs.stdlib.In;

/******************************************************************************
 *  Compilation:  javac NonrecursiveDFS.java
 *  Execution:    java NonrecursiveDFS graph.txt s
 *  Dependencies: Graph.java Queue.java Stack.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/41graph/tinyCG.txt
 *                http://algs4.cs.princeton.edu/41graph/tinyG.txt
 *                http://algs4.cs.princeton.edu/41graph/mediumG.txt
 *
 *  Run nonrecurisve depth-first search on an undirected graph.
 *  Runs in O(E + V) time.
 *
 *  Explores the vertices in exactly the same order as DepthFirstSearch.java.
 *
 *  %  java Graph tinyCG.txt
 *  6 8
 *  0: 2 1 5 
 *  1: 0 2 
 *  2: 0 1 3 4 
 *  3: 5 4 2 
 *  4: 3 2 
 *  5: 3 0 
 *
 *  %  java NonrecursiveDFS tinyCG.txt 0
 *  0 to 0 (0):  0
 *  0 to 1 (1):  0-1
 *  0 to 2 (1):  0-2
 *  0 to 3 (2):  0-2-3
 *  0 to 4 (2):  0-2-4
 *  0 to 5 (1):  0-5
 *
 ******************************************************************************/

/**
 * 	该实现使用具有显示堆栈的深度优先搜索的非递归版本,构造函数需要与V+E成时间的正比,其中V是顶点的数量,E是
 * 	边数.它使用与V成比例的额外空间(不包括图表)
 * 	The {@code NonrecursiveDFS} class represents a data type for finding
 *  the vertices connected to a source vertex <em>s</em> in the undirected
 *  graph.
 *  <p>
 *  This implementation uses a nonrecursive version of depth-first search
 *  with an explicit stack.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  It uses extra space (not including the graph) proportional to <em>V</em>.
 *  <p>
 * @author 候旭东 20170218
 * */
public class NonrecursiveDFS {
	private boolean marked[];		//// marked [v] =有没有s-v路径？
	
	@SuppressWarnings("unchecked")
	public NonrecursiveDFS(Graph G, int s) {
		marked = new boolean[G.V()];
		
		validateVertex(s);
		/**
		 * 为了能够遍历每个邻接列表，跟踪每个邻接列表中的哪个顶点需要深层搜索
		 * */
		Iterator<Integer>[] adj = (Iterator<Integer>[]) new Iterator[G.V()];
		for (int v = 0; v<G.V(); v++)
			adj[v] = (Iterator<Integer>) G.adj(v).iterator();
		/**
		 * 使用显式堆栈的深度优先搜索
		 * */
		Stack<Integer> stack = new Stack<Integer>();
		marked[s] = true;
		stack.push(s);
		while(!stack.isEmpty()) {
			int v = stack.peak();
			if (adj[v].hasNext()) {
				int w = adj[v].next();
				if (!marked[w]) {
					marked[w] = true;
					stack.push(w);
				}
			}
			else {
				stack.pop();
			}
		}
	}

	/**
	 * 顶点{@code v}是否连接到源顶点{@code s}？
     * Is vertex {@code v} connected to the source vertex {@code s}?
     * @param v the vertex
     * @return {@code true} if vertex {@code v} is connected to the source vertex {@code s},
     *    and {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public boolean marked(int v) {
		validateVertex(v);
		return marked[v];
	}
	
	private void validateVertex(int v) {
		int V = marked.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}
	
	public static void main(String[] args) {
		try {
			File file1 = new File(".\\algs4-data\\tinyG.txt");
			In in = new In(file1);
			Graph G = new Graph(in);
			int s ;
			for (s=0; s<G.V(); s++) {
				NonrecursiveDFS dfs = new NonrecursiveDFS(G, s);
				for(int v = 0; v<G.V(); v++){
					System.out.printf("%d to %d  ",s,v);
					if(dfs.marked(v))
						System.out.print("components: "+s+"-"+v);
					else
						System.out.print("not connect");
					System.out.println();
				}
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
