package com.hxd.graphs.sp;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Queue;
import com.hxd.base.Stack;
import com.hxd.introcs.stdlib.In;

/**
 * {@code BellmanFordSP}类表示用于解决绕过负权重环的加权有向图中的单源最短路径问题的数据类型。 边权重可以是正，负或零。
 * 这个类找到从源顶点到每个其他顶点的最短路径或从源顶点到达的负周期。 这个实现使用Bellman-Ford-Moore算法。 在最坏的情况下，
 * 构造函数需要的时间与V（V + E）成正比需要的空间和V成正比，其中V是顶点的数量，E是边的数量。 之后，{@code distTo（）}，
 * {@code hasPathTo（）}和{@code hasNegativeCycle（）}方法需要一定的时间; {@code pathTo（）}和
 * {@code negativeCycle（）}方法所花费的时间与返回的边数成正比。
 * 通用算法:在任意含有V个顶点的加权有向图中给定起点s,从s无法到达任何负权重环,将distTo[s]初始化为0,其他distTo[]元素初始化为无穷
 * 大.以任意顺序放松所有边,重复V轮
 * 根据经验很容易知道在任意一轮中许多边的放松都不会成功:只有上一轮中的distTo[]值发生变化的顶点指出的边才能够改变其他distTo[]
 * 元素的值
 * <p>
 * 加权有向图中的负权重环是一个总权重(环上的所有边的权重之和)为负的有向环,当且仅当加权有向图中至少存在一条从s到v的有向路径且所有
 * 从s到v的有向路径上的任意顶点都不存在于任何负权重环中时,s到v的最短路径才是存在的.
 * 一个定义明确且可以解决加权有向图最短路径问题的算法要明确:
 * 对于从起点不可达的顶点,最短路径为正无穷;
 * 对于起点可达但路径上的某个顶点属于一个负权重环的顶点,最短路径为负无穷;
 * 对于其他所有顶点,计算最短路径的权重(已经最短路径树).
 * <p>
 * @author houxu_000 20170227
 * */

public class BellmanFordSP {

	private double[] distTo;				// distTo[v] = distance  of shortest s->v path
	private DirectedEdge[] edgeTo;			// edgeTo[v] = last edge on shortest s->v path
	private boolean[] onQueue;				// onQueue[v] = is v currently on the queue?
	private Queue<Integer> queue;			// queue of vertices to relax
	private int cost;						// number of calls to relax()
	private Iterable<DirectedEdge> cycle;	// negative cycle (or null if no such cycle)
	
	/**
     * Computes a shortest paths tree from {@code s} to every other vertex in
     * the edge-weighted digraph {@code G}.
     * @param G the acyclic digraph
     * @param s the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
	public BellmanFordSP(EdgeWeightedDigraph G, int s) {
		distTo = new double[G.V()];
		edgeTo = new DirectedEdge[G.V()];
		onQueue = new boolean[G.V()];
		for (int v = 0; v < G.V(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;
		distTo[s] = 0.0;
		
		queue = new Queue<Integer>();
		queue.enqueue(s);
		onQueue[s] = true;
		while (!queue.isEmpty() && !hasNegativeCycle()) {
			int v = queue.dequeue();
			onQueue[v] = false;
			relax(G, v);
		}
		
		assert check(G, s);
	}

	private void relax(EdgeWeightedDigraph G, int v) {
		for (DirectedEdge e : G.adj(v)) {
			int w = e.to();
			if (distTo[w] > distTo[v] + e.weight()) {
				distTo[w] = distTo[v] + e.weight();
				edgeTo[w] = e;
				if (!onQueue[w]) {
					queue.enqueue(w);
					onQueue[w] = true;
				}
			}
			if (cost++ % G.V() == 0) {
				findNegativeCycle();
				if (hasNegativeCycle())	return;
			}
		}
	}
	
	/**
     * Is there a negative cycle reachable from the source vertex {@code s}?
     * @return {@code true} if there is a negative cycle reachable from the
     *    source vertex {@code s}, and {@code false} otherwise
     */
	public boolean hasNegativeCycle() {
		return cycle != null;
	}
	
	/**
     * Returns a negative cycle reachable from the source vertex {@code s}, or {@code null}
     * if there is no such cycle.
     * @return a negative cycle reachable from the soruce vertex {@code s} 
     *    as an iterable of edges, and {@code null} if there is no such cycle
     */
	public Iterable<DirectedEdge> negativeCycle() {
		return cycle;
	}
	
	private void findNegativeCycle() {
		int V = edgeTo.length;
		EdgeWeightedDigraph spt = new EdgeWeightedDigraph(V);
		for (int v = 0; v < V; v++)
			if (edgeTo[v] != null)
				spt.addEdge(edgeTo[v]);
		EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(spt);
		cycle = finder.cycle();
	}

	/**
     * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
     * @param  v the destination vertex
     * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
     *         {@code Double.POSITIVE_INFINITY} if no such path
     * @throws UnsupportedOperationException if there is a negative cost cycle reachable
     *         from the source vertex {@code s}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public double distTo(int v) {
		validateVertex(v);
		if (hasNegativeCycle())
			throw new UnsupportedOperationException("Negative cost cycle exists");
		 return distTo[v];
	}

	/**
     * Is there a path from the source {@code s} to vertex {@code v}?
     * @param  v the destination vertex
     * @return {@code true} if there is a path from the source vertex
     *         {@code s} to vertex {@code v}, and {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public boolean hasPathTo(int v) {
		validateVertex(v);
		return distTo[v] < Double.POSITIVE_INFINITY;
	}
	
	/**
     * Returns a shortest path from the source {@code s} to vertex {@code v}.
     * @param  v the destination vertex
     * @return a shortest path from the source {@code s} to vertex {@code v}
     *         as an iterable of edges, and {@code null} if no such path
     * @throws UnsupportedOperationException if there is a negative cost cycle reachable
     *         from the source vertex {@code s}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public Iterable<DirectedEdge> pathTo(int v) {
		validateVertex(v);
		if (hasNegativeCycle())
			throw new UnsupportedOperationException("Negative cost cycle exists");
		if (!hasPathTo(v))	return null;
		Stack<DirectedEdge> path = new Stack<DirectedEdge>();
		for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
			path.push(e);
		return path;
	}
	
	 // check optimality conditions: either 
    // (i) there exists a negative cycle reacheable from s
    //     or 
    // (ii)  for all edges e = v->w:            distTo[w] <= distTo[v] + e.weight()
    // (ii') for all edges e = v->w on the SPT: distTo[w] == distTo[v] + e.weight()
    private boolean check(EdgeWeightedDigraph G, int s) {

        // has a negative cycle
        if (hasNegativeCycle()) {
            double weight = 0.0;
            for (DirectedEdge e : negativeCycle()) {
                weight += e.weight();
            }
            if (weight >= 0.0) {
                System.err.println("error: weight of negative cycle = " + weight);
                return false;
            }
        }

        // no negative cycle reachable from source
        else {

            // check that distTo[v] and edgeTo[v] are consistent
            if (distTo[s] != 0.0 || edgeTo[s] != null) {
                System.err.println("distanceTo[s] and edgeTo[s] inconsistent");
                return false;
            }
            for (int v = 0; v < G.V(); v++) {
                if (v == s) continue;
                if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                    System.err.println("distTo[] and edgeTo[] inconsistent");
                    return false;
                }
            }

            // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
            for (int v = 0; v < G.V(); v++) {
                for (DirectedEdge e : G.adj(v)) {
                    int w = e.to();
                    if (distTo[v] + e.weight() < distTo[w]) {
                        System.err.println("edge " + e + " not relaxed");
                        return false;
                    }
                }
            }

            // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
            for (int w = 0; w < G.V(); w++) {
                if (edgeTo[w] == null) continue;
                DirectedEdge e = edgeTo[w];
                int v = e.from();
                if (w != e.to()) return false;
                if (distTo[v] + e.weight() != distTo[w]) {
                    System.err.println("edge " + e + " on shortest path not tight");
                    return false;
                }
            }
        }

        System.out.println("Satisfies optimality conditions");
        System.out.println();
        return true;
    }
	
	private void validateVertex(int v) {
		int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{
					new File(".\\algs4-data\\tinyEWD.txt"),
//					new File(".\\algs4-data\\mediumEWD.txt"),
					};
			for(File file : files) {
				In in = new In(file);
				EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
				for (int s = 0; s < G.V(); s=s+3) {
					BellmanFordSP sp = new BellmanFordSP(G, s);
					for (int t = 0; t < G.V(); t++) {
						if (sp.hasPathTo(t)) {
							System.out.printf("%d to %d (%.2f) ",s,t,sp.distTo(t));
							for (DirectedEdge e : sp.pathTo(t))
								System.out.print(e + "  ");
							System.out.println();
						}
						else 
							System.out.printf("%d to %d   no path\n",s,t);
					}
				}
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
