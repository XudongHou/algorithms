package com.hxd.graphs.directedGraphs;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Queue;
import com.hxd.base.Stack;
import com.hxd.introcs.stdlib.In;

/**
 * {@code BreadthDirectedFirstPaths}类表示用于找到从 <em>源顶点 </em>
 * （或源顶点集合）到有向图中每个其他顶点的最短路径（边数）的数据类型。 此实现使用广度优先搜索。 
 * 构造函数需要与 <em>V</em> + <em>E</em>成正比的时间，其中 <em>V</em>是顶点
 * 的数量，<em>E</em> 的边缘。它使用与 <em>V</em>成比例的额外空间（不包括有向图）。
 * <p>
 * @author 候旭东 20170220
 * */

public class BreadthFirstDirectedPaths {
	private static final int INFINITY = Integer.MAX_VALUE;
	private boolean[] marked;		// marked[v] = is there an s->v path?
	private int[] edgeTo;				// edgeTo[v] = last edge on shortest s->v path
	private int[] distTo;			// distTo[v] = length of shortest s->v path
	
	public BreadthFirstDirectedPaths(Digraph G, int s) {
		marked = new boolean[G.V()];
		distTo = new int[G.V()];
		edgeTo = new int[G.V()];
		for (int v = 0; v < G.V(); v++)
			distTo[v] = INFINITY;
		validateVertex(s);
		bfs(G, s);
	}

	public BreadthFirstDirectedPaths(Digraph G, Iterable<Integer> sources) {
		marked = new boolean[G.V()];
		distTo = new int[G.V()];
		edgeTo = new int[G.V()];
		for (int v = 0; v < G.V(); v++)
			distTo[v] = INFINITY;
		validateVertices(sources);
		bfs(G, sources);
	}
	
	

	private void bfs(Digraph G, int s) {
		marked[s] =true;
		Queue<Integer> q = new Queue<Integer>();
		distTo[s] = 0;
		q.enqueue(s);
		while (!q.isEmpty()) {
			int v = q.dequeue();
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					marked[w] = true;
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					q.enqueue(w);
				}
			}
		}
	}

	public void bfs(Digraph G, Iterable<Integer> sources) {
		Queue<Integer> q = new Queue<Integer>();
		for (int s : sources) {
			marked[s] = true;
			distTo[s] = 0;
			q.enqueue(s);
		}
		while (!q.isEmpty()) {
			int v = q.dequeue();
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					marked[w] = true;
					q.enqueue(w);
				}
			}
		}
	}
	
	public boolean hasPathTo(int v) {
		validateVertex(v);
		return marked[v];
	}
	
	public int distTo(int v) {
		validateVertex(v);
		return distTo[v];
	}
	
	public Iterable<Integer> pathTo(int v) {
		validateVertex(v);
		
		if (!hasPathTo(v))	return null;
		Stack<Integer> path = new Stack<Integer>();
		int x;
		for (x = v; distTo[x] != 0; x = edgeTo[x])
			path.push(x);
		path.push(x);
		return path;
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
				for (int s = 0; s < G.V(); s+=2) {
					BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(G, s);
					for (int v = 0 ; v < G.V(); v++) {
						if (bfs.hasPathTo(v)) {
							System.out.printf("%d to %d: ",s,v);
							for (int w : bfs.pathTo(v)) {
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
