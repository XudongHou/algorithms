package com.hxd.graphs.undirectedGraphs;

/******************************************************************************
 *  Compilation:  javac DepthFirstSearch.java
 *  Execution:    java DepthFirstSearch filename.txt s
 *  Dependencies: Graph.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/41graph/tinyG.txt
 *                http://algs4.cs.princeton.edu/41graph/mediumG.txt
 *
 *  Run depth first search on an undirected graph.
 *  Runs in O(E + V) time.
 *
 *  % java DepthFirstSearch tinyG.txt 0
 *  0
 *  0 1 2 3 4 5 6 
 *  7
 *  [true, true, true, true, true, true, true, false, false, false, false, false, false]
 *  NOT connected
 *  8
 *  7 8 
 *  2
 *  [false, false, false, false, false, false, false, true, true, false, false, false, false]
 *  NOT connected
 *  
 *  10
 *  9 10 11 12 
 *  4
 *  [false, false, false, false, false, false, false, false, false, true, true, true, true]
 *  NOT connected
 *
 ******************************************************************************/

import java.io.File;
import java.nio.file.FileSystemNotFoundException;
import java.util.Arrays;

import com.hxd.introcs.stdlib.In;

/**
 *  The {@code DepthFirstSearch} class represents a data type for 
 *  determining the vertices connected to a given source vertex <em>s</em>
 *  in an undirected graph. For versions that find the paths, see
 *  {@link DepthFirstPaths} and {@link BreadthFirstPaths}.
 *  <p>
 *  This implementation uses depth-first search.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>
 *  (in the worst case),
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  It uses extra space (not including the graph) proportional to <em>V</em>.
 *  <p>
 *  探索迷宫中的所有通道需要:
 *  选择一条没有标记过的通道,在走过的路上铺一条绳子;
 *  标记所有的第一次路过的路口和通道;
 *  当来到一个标记过的路口时(用绳子)回退到上个路口;
 *  当退回到的路口已没有可走的通道时继续回退;
 *  <p>
 *  深度优先搜索解决连通性问题;路径检测;单点路径问题
 *  <p>
 * 	@author 候旭东 20170216
 * */

public class DepthFirstSearch {
	private boolean[] marked;		// marked [v] =有没有s-v路径？
	private int count;				//连接到s的顶点数
	
	/**
     * Computes the vertices in graph {@code G} that are
     * connected to the source vertex {@code s}.
     * @param G the graph
     * @param s the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
	public DepthFirstSearch(Graph G,int s) {
		marked = new boolean[G.V()];
		validateVertex(s);
		dfs(G,s);
	}

	/**
	 * 代码中的调用和返回机制对应迷宫中绳子的作用当已经处理过依附于一个顶点的所有边时(搜索了路口连接的所用通道),
	 * 只能返回.这将构成一个单向通道,无向图的深度优先搜索中,在碰到边v-w时,要么进行递归调用(w没有被标记过),要
	 * 么跳过这条边(w已经被标记过).第二次从另一个方向w-v遇到这条边时,总会被忽略它,因为它的另一端v肯定已经被访
	 * 问过
	 * */
	// depth first search from v
	private void dfs(Graph G, int v) {
		count++;
		marked[v] = true;			//自身标记
		//递归地访问它的所有没有标记过的邻居顶点
		for (int w : G.adj(v)) {
			if (!marked[w])
				dfs(G, w);
		}
	}
	
	/**
	 * 在源顶点s和定点v之间是否有路径
     * Is there a path between the source vertex {@code s} and vertex {@code v}?
     * @param v the vertex
     * @return {@code true} if there is a path, {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public boolean marked(int v) {
		validateVertex(v);
		return marked[v];
	}
	
	/**
	 * 返回连接到源顶点{@code s}的顶点数。
     * Returns the number of vertices connected to the source vertex {@code s}.
     * @return the number of vertices connected to the source vertex {@code s}
     */
	public int count() {
		return count;
	}
	
	private void validateVertex(int v) {
		int V = marked.length;
		if ( v<0 || v>V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}
	
	/**
     * Unit tests the {@code DepthFirstSearch} data type.
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
				DepthFirstSearch search = new DepthFirstSearch(G, j);
				for (int v = 0; v < G.V(); v++) {
					if (search.marked(v))
						System.out.print(v + " ");
				}
				System.out.println();
				System.out.println(search.count());
				System.out.println(Arrays.toString(search.marked));
				if (search.count() != G.V())	System.out.println("NOT connected");
				else                         	System.out.println("connected");
				System.out.println();
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
