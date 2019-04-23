package com.hxd.graphs.directedGraphs;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Stack;
import com.hxd.introcs.stdlib.In;

/**
 * 	{@code DepthFirstDirectedPaths}类表示用于找到从源顶点到有向图中每个其他顶点的有向路径的数据类型。
 *  此实现使用深度优先搜索。 构造函数需要与<em> V </ em> + <em> E </ em>成正比的时间，其中<em>是
 *  </ em>是顶点的数量，<em> 的边缘。 它使用与<em> V </ em>成正比的额外空间（不包括图表）。
 *	<p>
 *	@author 候旭东 20170220
 * */

public class DepthFirstDirectedPaths {
	private boolean[] marked;		// marked[v] = is there an s->v path?
	private int[] edgeTo;			// edgeTo[v] = last edge on path from s to v
	private final int s;
	
	/**
     * Computes a directed path from {@code s} to every other vertex in digraph {@code G}.
     * @param  G the digraph
     * @param  s the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
	public DepthFirstDirectedPaths(Digraph G, int s) {
		marked = new boolean[G.V()];
		edgeTo = new int[G.V()];
		this.s = s;
		validateVertex(s);
		dfs(G, s);
	}

	private void dfs(Digraph G, int v) {
		marked[v] = true;
		for (int w : G.adj(v)) {
			if (!marked[w]) {
				edgeTo[w] = v;
				dfs(G, w);
			}
		}
	}

	/**
	 * 是否有从源顶点{@code s}到顶点{@code v}的有向路径？
     * Is there a directed path from the source vertex {@code s} to vertex {@code v}?
     * @param  v the vertex
     * @return {@code true} if there is a directed path from the source
     *         vertex {@code s} to vertex {@code v}, {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public boolean hasPathTo(int v) {
		validateVertex(v);
		return marked[v];
	}
	
	/**
	 * 返回从源顶点{@code s}到顶点{@code v},或的有向路径 ,{@code null}如果没有这样的路径。
     * Returns a directed path from the source vertex {@code s} to vertex {@code v}, or
     * {@code null} if no such path.
     * @param  v the vertex
     * @return the sequence of vertices on a directed path from the source vertex
     *         {@code s} to vertex {@code v}, as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public Iterable<Integer> pathTo(int v) {
		validateVertex(v);
		if (!hasPathTo(v)) return null;
		Stack<Integer> path = new Stack<Integer>();
		for (int x = v; x != s; x = edgeTo[x])
			path.push(x);
		path.push(s);
		return path;
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
				for (int s = 0; s < G.V(); s+=2) {
					DepthFirstDirectedPaths dfs = new DepthFirstDirectedPaths(G, s);
					for (int v = 0 ; v < G.V(); v++) {
						if (dfs.hasPathTo(v)) {
							System.out.printf("%d to %d: ",s,v);
							for (int w : dfs.pathTo(v)) {
								if (w == s)		System.out.print(s);
								else			System.out.print("-" + w);
							}
						}else
							System.out.printf("%d to %d:  not connected\n", s, v);
					}
				}
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
