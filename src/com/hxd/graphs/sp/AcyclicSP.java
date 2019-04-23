package com.hxd.graphs.sp;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Stack;
import com.hxd.graphs.directedGraphs.Topological;
import com.hxd.introcs.stdlib.In;

/**
 * @code AcyclicSP}类表示用于求解加权有向无环图（DAG）中的单源最短路径问题的数据类型。 边权重可以是正，负或零。
 * 此实现使用基于拓扑排序的算法。 构造函数需要与V + E成正比的时间，其中V是顶点的数量，E是边的数量。 之后，
 * {@code distTo（）}和{@code hasPathTo（）}方法需要一定的时间，而{@code pathTo（）}方法所花费的
 * 时间与返回的最短路径的边数成正比。
 * <P>
 * 按照拓扑顺序放松顶点,就能在和E+V成正比的时间内解决无环加权有向图的单点最短路径问题
 * <p>
 * @author houxu_000 20170226
 * */

public class AcyclicSP {
	private double[] distTo;		// distTo[v] = distance  of shortest s->v path
	private DirectedEdge[] edgeTo;	// edgeTo[v] = last edge on shortest s->v path
	
	/**
     * Computes a shortest paths tree from {@code s} to every other vertex in
     * the directed acyclic graph {@code G}.
     * @param G the acyclic digraph
     * @param s the source vertex
     * @throws IllegalArgumentException if the digraph is not acyclic
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
	public AcyclicSP(EdgeWeightedDigraph G, int s) {
		distTo = new double[G.V()];
		edgeTo = new DirectedEdge[G.V()];
		
		validateVertex(s);
		
		for (int v = 0; v < G.V(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;
		distTo[s] = 0.0;
		
		Topological topological = new Topological(G);
		if (!topological.hasOrder())
			throw new IllegalArgumentException("Digraph is not acyclic.");
		for (int v : topological.order())
			for (DirectedEdge e : G.adj(v))
				relax(e);
	}

	private void relax(DirectedEdge e) {
		int v = e.from() ,w = e.to();
		if (distTo[w] > distTo[v] + e.weight()) {
			distTo[w] = distTo[v] + e.weight();
			edgeTo[w] = e;
		}
	}

	/**
     * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
     * @param  v the destination vertex
     * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
     *         {@code Double.POSITIVE_INFINITY} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public double distTo(int v) {
		validateVertex(v);
		return distTo[v];
	}

    /**
     * Is there a path from the source vertex {@code s} to vertex {@code v}?
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
     * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
     * @param  v the destination vertex
     * @return a shortest path from the source vertex {@code s} to vertex {@code v}
     *         as an iterable of edges, and {@code null} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public Iterable<DirectedEdge> pathTo(int v) {
		validateVertex(v);
		if (!hasPathTo(v))	return null;
		Stack<DirectedEdge> path = new Stack<DirectedEdge>();
		for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
			path.push(e);
		return path;
	}
	
	private void validateVertex(int v) {
		int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{
					new File(".\\algs4-data\\tinyEWDAG.txt"),
					};
			for(File file : files) {
				In in = new In(file);
				EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
				for (int s = 0; s < G.V(); s=s+2) {
					AcyclicSP sp = new AcyclicSP(G, s);
					for (int v = 0; v < G.V(); v++) {
						if (sp.hasPathTo(v)) {
							System.out.printf("%d to %d (%.2f)  ",s,v,sp.distTo(v));
							for (DirectedEdge e : sp.pathTo(v))
								System.out.print(e + " ");
							System.out.println();
						}
						else {
							System.out.printf("%d to %d         no path\n", s, v);
						}
					}
				}
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}	
