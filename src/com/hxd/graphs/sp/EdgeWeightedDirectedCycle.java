package com.hxd.graphs.sp;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Stack;
import com.hxd.graphs.directedGraphs.Topological;
import com.hxd.introcs.stdlib.In;

/**
 * {@code EdgeWeightedDirectedCycle}类表示用于确定边缘加权有向图是否具有有向循环的数据类型。 hasCycle操作确
 * 定边缘加权有向图是否具有有向循环，如果是有循环操作，则返回1。 此实现使用深度优先搜索。 构造函数需要与V + E成正比的时间（在
 * 最坏的情况下），其中V是顶点的数量，E是边的数量。 之后，hasCycle操作需要恒定的时间; 循环操作需要与循环的长度成比例的时间。 
 * 如果边缘加权有向图是非循环的，请参见{@link Topological}计算拓扑顺序。
 * @author houxu_000 20170226
 * */

public class EdgeWeightedDirectedCycle {
	
	private boolean[] marked;			// marked[v] = has vertex v been marked?
	private boolean[] onStack;			// onStack[v] = is vertex on the stack?
	private DirectedEdge[] edgeTo;		// edgeTo[v] = previous edge on path to v
	private Stack<DirectedEdge> cycle;	// directed cycle (or null if no such cycle)
	
	public EdgeWeightedDirectedCycle(EdgeWeightedDigraph G) {
		marked = new boolean[G.V()];
		onStack = new boolean[G.V()];
		edgeTo = new DirectedEdge[G.V()];
		for (int v = 0; v < G.V(); v++)
			if (!marked[v]) dfs(G, v);
		assert check();
	}
	
	private void dfs(EdgeWeightedDigraph G, int v) {
		onStack[v] = true;
		marked[v] = true;
		for (DirectedEdge e : G.adj(v)) {
			int w = e.to();
			if (cycle != null)	return;
			else if (!marked[w]) {
				edgeTo[w] = e;
				dfs(G, w);
			}
			else if (onStack[w]) {
				cycle = new Stack<DirectedEdge>();
				DirectedEdge f = e;
				while (f.from() != w) {
					cycle.push(f);
					f = edgeTo[f.from()];
				}
				cycle.push(f);
				return;
			}
		}
		onStack[v] = false;
	}

	public boolean hasCycle() {
		return cycle != null;
	}
	
	public Iterable<DirectedEdge> cycle() {
		return cycle;
	}
	
	private boolean check() {
		
		// edge-weighted digraph is cyclic
        if (hasCycle()) {
            // verify cycle
            DirectedEdge first = null, last = null;
            for (DirectedEdge e : cycle()) {
                if (first == null) first = e;
                if (last != null) {
                    if (last.to() != e.from()) {
                        System.err.printf("cycle edges %s and %s not incident\n", last, e);
                        return false;
                    }
                }
                last = e;
            }

            if (last.to() != first.from()) {
                System.err.printf("cycle edges %s and %s not incident\n", last, first);
                return false;
            }
        }
        return true;
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{
					new File(".\\algs4-data\\tinyEWD.txt"),
					new File(".\\algs4-data\\mediumEWD.txt"),
					};
			for(File file : files) {
				In in = new In(file);
				EdgeWeightedDigraph G = new EdgeWeightedDigraph(in); 
				EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(G);
				if (finder.hasCycle()) {
					System.out.print("Directed cycle: ");
					for (DirectedEdge e : finder.cycle())
						System.out.print(e + " ");
					System.out.println();
				}
				else{
					System.out.println("No directed cycle");
				}
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
