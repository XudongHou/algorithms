package com.hxd.graphs.undirectedGraphs;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Stack;
import com.hxd.introcs.stdlib.In;

/******************************************************************************
 *  Compilation:  javac Cycle.java
 *  Execution:    java  Cycle filename.txt
 *  Dependencies: Graph.java Stack.java In.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/41graph/tinyG.txt
 *                http://algs4.cs.princeton.edu/41graph/mediumG.txt
 *                http://algs4.cs.princeton.edu/41graph/largeG.txt  
 *
 *  Identifies a cycle.
 *  Runs in O(E + V) time.
 *
 *  % java Cycle tinyG.txt
 *  3 4 5 3 
 * 
 *  % java Cycle mediumG.txt 
 *  15 0 225 15 
 * 
 *  % java Cycle largeG.txt 
 *  996673 762 840164 4619 785187 194717 996673 
 *
 ******************************************************************************/

/**
 *	{@code Cycle}类表示用于确定无向图是否具有循环的数据类型。hasCycle操作确定图是否具有循环，
 *	如果是循环，循环操作返回一个。这个实现使用深度优先搜索。构造函数采用与V + E成比例的时间（在最坏
 *	的情况下），其中V是顶点的数量，E是边的数量。之后，hasCycle操作采用常数 时间; 循环操作需要与
 *	循环的长度成比例的时间。
 * 	The {@code Cycle} class represents a data type for 
 *  determining whether an undirected graph has a cycle.
 *  The <em>hasCycle</em> operation determines whether the graph has
 *  a cycle and, if so, the <em>cycle</em> operation returns one.
 *  <p>
 *  This implementation uses depth-first search.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>
 *  (in the worst case),
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Afterwards, the <em>hasCycle</em> operation takes constant time;
 *  the <em>cycle</em> operation takes time proportional
 *  to the length of the cycle.
 *  <p>
 *	@author 候旭东 20170218
 * */

public class Cycle {
	private boolean marked[];
	private int[] edgeTo;
	private Stack<Integer> cycle;
	
	/**
	 * 确定无向图{@code G}是否有循环,如果有,找到这样的循环.
	 * Determines whether the undirected graph {@code G} has a cycle and,
	 * if so, finds such a cycle.
	 *
	 * @param G the undirected graph
	 */
	public Cycle(Graph G) {
		if (hasSelfLoop(G)) return;
		if (hasParalleEdges(G)) return;
		marked = new boolean[G.V()];
		edgeTo = new int[G.V()];
		for (int v = 0; v<G.V(); v++)
			if(!marked[v])
				dfs(G,-1,v);
	}
	
	/**
	 * 是否存在自循环的图?
	 * 副作用：初始化循环为自循环
	 * */
	private boolean hasSelfLoop(Graph G) {
		for(int v = 0; v<G.V(); v++) {
//			System.out.print(v + "'s neighbor : " +G.adj(v));
//			System.out.println();
			for (int w : G.adj(v)) {
				if (v == w) {
					System.out.println(v + " " + w);
					cycle = new Stack<Integer>();
					cycle.push(v);
					cycle.push(v);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 这个图有两个平行边？
	 * 副作用：初始化循环为两个平行边
	 * */
	private boolean hasParalleEdges(Graph G) {
		marked = new boolean[G.V()];
		for(int v = 0; v < G.V(); v++) {
			for(int w : G.adj(v)) {
				if(marked[w]) {
					cycle = new Stack<Integer>();
					cycle.push(v);
					cycle.push(w);
					cycle.push(v);
					return true;
				}
				marked[w] = true;
//				System.out.print(w+" ");
			}
//			System.out.println();
//			System.out.println(Arrays.toString(marked));
			for (int w : G.adj(v)) 
				marked[w] = false;
		}
		return false;
	}
	
	public boolean hasCycle() {
		return cycle != null;
	}
	
	public Iterable<Integer> cycle() {
		return cycle;
	}
	
	private void dfs(Graph G, int u, int v) {
		marked[v] = true;
		for (int w : G.adj(v)) {
			if (cycle != null)	return;
			if (!marked[w]) {
				edgeTo[w] = v;
				dfs(G, v, w);
			}
			else if (w != u) {
				cycle = new Stack<Integer>();
				for (int x = v; x != w; x = edgeTo[x])
					cycle.push(x);
				cycle.push(w);
				cycle.push(v);
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{new File(".\\algs4-data\\tinyG.txt"),
					new File(".\\algs4-data\\mediumG.txt"),
					new File(".\\algs4-data\\largeG.txt")
					};
			for(File file : files){
				In in = new In(file);
				Graph G = new Graph(in); 
				Cycle finder = new Cycle(G);
				if (finder.hasCycle()) {
					for (int v : finder.cycle())
						System.out.print(v+" ");
					System.out.println();
				}
				else {
					System.out.println("Graph is acyclic");
				}
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
