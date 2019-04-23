package com.hxd.graphs.sp;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Stack;
import com.hxd.graphs.directedGraphs.Topological;
import com.hxd.introcs.stdlib.In;

/**
 * 无环加权有向图中的单点最长路径问题,解决无环加权有向图中的最长路径问题所需的时间与E+V成正比;
 * 在一般的加权有向图中寻找最长简单路径的已知最好算法的最坏情况下所需要的时间是指数级别的
 * <p>
 * {@code AcyclicLP}类表示用于解决加权有向无环图（DAG）中的单源最长路径问题的数据类型。 边权重可以是正，负或零。 
 * 此实现使用基于拓扑排序的算法。 构造函数需要与V + E成正比的时间，其中V是顶点的数量，E是边的数量。 之后，
 * {@code distTo（）}和{@code hasPathTo（）}方法需要一定的时间，{@code pathTo（）}方法所花费的时间与返
 * 回的最长路径中的边数成正比。
 * <p>
 * @author houxu_000 20170226
 * */

public class AcyclicLP {
	private double[] distTo;		//distTo[v] = distance  of longest s->v path
	private DirectedEdge[] edgeTo;	// edgeTo[v] = last edge on longest s->v path
	
	public AcyclicLP(EdgeWeightedDigraph G, int s) {
		distTo = new double[G.V()];
		edgeTo = new DirectedEdge[G.V()];
		
		validateVertex(s);
		
		for (int v = 0; v < G.V(); v++)
			distTo[v] = Double.NEGATIVE_INFINITY;
		distTo[s] = 0.0;
		
		Topological topological = new Topological(G);
		if (!topological.hasOrder())
			throw new IllegalArgumentException("Digraph is not acyclic.");
		for (int v : topological.order())
			for (DirectedEdge e : G.adj(v))
				relax(e);
	}

	private void relax(DirectedEdge e) {
		int v = e.from(), w = e.to();
		if (distTo[w] < distTo[v] + e.weight()){
			distTo[w] = distTo[v] + e.weight();
			edgeTo[w] = e;
		}
	}

	public double distTo(int v) {
		validateVertex(v);
		return distTo[v];
	}
	
	public boolean hasPathTo(int v) {
		validateVertex(v);
		return distTo[v] > Double.NEGATIVE_INFINITY;
	}
	
	public Iterable<DirectedEdge> pathTo(int v) {
		validateVertex(v);
		if (!hasPathTo(v)) return null;
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
					AcyclicLP sp = new AcyclicLP(G, s);
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
