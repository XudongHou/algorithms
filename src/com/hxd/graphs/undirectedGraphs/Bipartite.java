package com.hxd.graphs.undirectedGraphs;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Stack;
import com.hxd.introcs.stdlib.In;

/**
 *	{@code Bipartite}类表示一种数据类型，用于确定无向图是二分图还是具有奇长周期。isBipartite操作确定图是
 *	否是二分图。 如果是，则颜色操作确定二分区; 如果不是，则奇数循环操作确定具有奇数边的循环。
 * 	The {@code Bipartite} class represents a data type for 
 *  determining whether an undirected graph is bipartite or whether
 *  it has an odd-length cycle.
 *  The <em>isBipartite</em> operation determines whether the graph is
 *  bipartite. If so, the <em>color</em> operation determines a
 *  bipartition; if not, the <em>oddCycle</em> operation determines a
 *  cycle with an odd number of edges.
 *  <p>
 *  This implementation uses depth-first search.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>
 *  (in the worst case),
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Afterwards, the <em>isBipartite</em> and <em>color</em> operations
 *  take constant time; the <em>oddCycle</em> operation takes time proportional
 *  to the length of the cycle.
 *  See {@link BipartiteX} for a nonrecursive version that uses breadth-first
 *  search.
 *  <p>
 *	双色问题:能够用两种颜色将图的所有顶点着色,使得任意一条边的两个端点的颜色都不同?等价于这是一幅二分图吗?
 * 	@author 候旭东 20170218
 * */
public class Bipartite {
	private boolean isBipartite;		//是二分图?
	private boolean[] color;			//color [v]在二分的一边给出顶点
	private boolean[] marked;			//marked [v] = true v已在DFS中访问过
	private int[] edgeTo;				// edgeTo [v] = 到v的路径上的上一条边
	private Stack<Integer> cycle;		//奇长周期
	
	/**
     * Determines whether an undirected graph is bipartite and finds either a
     * bipartition or an odd-length cycle.
     *
     * @param  G the graph
     */
	public Bipartite(Graph G) {
		isBipartite = true;
		color = new boolean[G.V()];
		marked = new boolean[G.V()];
		edgeTo = new int[G.V()];
		
		for (int v = 0; v < G.V(); v++)
			if (!marked[v])
				dfs (G, v);
		assert check(G);
	}
	
	private void dfs(Graph G, int v) {
		marked[v] = true;
		for (int w : G.adj(v)) {
			//如果发现奇长度循环，则退出
			if (cycle != null) return;
			//找到无同色的顶点,递归
			if (!marked[w]) {
				edgeTo[w] = v;
				color[w] = !color[v];
				dfs(G, w);
			}
			//如果v-w创建一个奇长周期,找出
			else if (color[w] == color[v]) {
				isBipartite = false;
				cycle = new Stack<Integer>();
				cycle.push(w);	//开始顶点两次
				for (int x = v; x!=w; x = edgeTo[x])
					cycle.push(x);
				cycle.push(w);
			}
		}
	}
	
	/**
	 * 如果图形是二分的，则返回true。
     * Returns true if the graph is bipartite.
     *
     * @return {@code true} if the graph is bipartite; {@code false} otherwise
     */
	public boolean isBipartite() {
		return isBipartite;
	}
	
	/**
     * Returns the side of the bipartite that vertex {@code v} is on.
     *
     * @param  v the vertex
     * @return the side of the bipartition that vertex {@code v} is on; two vertices
     *         are in the same side of the bipartition if and only if they have the
     *         same color
     * @throws IllegalArgumentException unless {@code 0 <= v < V} 
     * @throws UnsupportedOperationException if this method is called when the graph
     *         is not bipartite
     */
	public boolean color(int v) {
		validateVertex(v);
		if (!isBipartite)
			throw new UnsupportedOperationException("graph is not bipartite");
		return color[v];
	}
	
	public Iterable<Integer> oddCycle() {
		return cycle;
	}
	
	private void validateVertex(int v) {
		 int V = marked.length;
		 if (v < 0 || v >= V)
			 throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	private boolean check(Graph G) {
		// graph is bipartite
		if (isBipartite) {
			for (int v = 0; v < G.V(); v++)
				for (int w : G.adj(v)) {
					if (color[v] == color[w]) {
						System.err.printf("edge %d-%d with %d and %d in same side of bipartition\n", v, w, v, w);
						return false;
					}
				}
		}
		// graph has an odd-length cycle
		else {
			int first = -1, last = -1;
			for (int v : oddCycle()) {
				if (first == -1)	first = v;
				last = v;
			}
			if (first != last) {
				System.err.printf("cycle begins with %d and ends with %d\n", first, last);
                return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{new File(".\\algs4-data\\tinyG.txt"),
					new File(".\\algs4-data\\mediumG.txt"),
//					new File(".\\algs4-data\\largeG.txt")
					};
			for(File file : files){
				In in = new In(file);
				Graph G = new Graph(in);
				Bipartite b = new Bipartite(G);
				if (b.isBipartite()) {
					System.out.println("Graph is bipartite");
					for (int v = 0; v < G.V(); v++)
						System.out.println(v+": "+b.color(v));
				}
				else {
					System.out.print("Graph has an odd-length cycle: ");
					for (int x :b.oddCycle())
						System.out.print(x + " ");
					System.out.println();
				}
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
