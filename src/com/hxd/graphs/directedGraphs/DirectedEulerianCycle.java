package com.hxd.graphs.directedGraphs;

import java.util.Iterator;

import com.hxd.base.Stack;
import com.hxd.graphs.undirectedGraphs.BreadFirstPaths;
import com.hxd.graphs.undirectedGraphs.Graph;
import com.hxd.introcs.stdlib.StdRandom;

/**
 * 有向无环图的枚举,给出一个公式,计算含有V个顶点和E条边的所有有向无环图的数量
 * {@code DirectedEulerianCycle}类表示用于在有向图中寻找欧拉循环或路径的数据类型。 
 * 欧拉循环是一个循环（不一定简单），它使用图中的每个边缘一次。 该实现使用非递归深度优先搜索。 
 * 构造函数在O（E + V）时间运行，并使用O（V）额外空间，其中E是边数，V是顶点数所有其他方法
 * 需要O（1）时间。 要计算有向图中的欧拉路径，请参见{@link DirectedEulerianPath}。 
 * 要计算无向图中的欧拉循环和路径，请参见{@link EulerianCycle}和{@link EulerianPath}。
 * @author 候旭东 20170221
 * */

public class DirectedEulerianCycle {
	private Stack<Integer> cycle = null;	//欧拉循环;null代表没有这样的cylce
	
	@SuppressWarnings("unchecked")
	public DirectedEulerianCycle(Digraph G) {
		
		// must have at least one edge
		if (G.E() ==0)	return;
		
		// necessary condition: indegree(v) = outdegree(v) for each vertex v
        // (without this check, DFS might return a path instead of a cycle)
		for (int v = 0; v < G.V(); v++)
			if (G.outdegree(v) != G.indegree(v))
				return;
		Iterator<Integer>[] adj = (Iterator<Integer>[]) new Iterator[G.V()];
		for (int v = 0; v < G.V(); v++)
			adj[v] = G.adj(v).iterator();
		
		// initialize stack with any non-isolated vertex 用任何非隔离的顶点初始化堆栈
		int s = nonIsolatedVertex(G);
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(s);
		
		// greedily add to putative cycle, depth-first search style
		cycle = new Stack<Integer>();
		while (!stack.isEmpty()) {
			int v = stack.pop();
			while (adj[v].hasNext()) {
				stack.push(v);
				v = adj[v].next();
			}
			// add vertex with no more leaving edges to cycle
			cycle.push(v);
		}
		
		// check if all edges have been used
        // (in case there are two or more vertex-disjoint Eulerian cycles)
		if (cycle.size() != G.E() + 1)
			cycle = null;
		
		assert certifySolution(G);
	}
	
	/**
     * Returns the sequence of vertices on an Eulerian cycle.
     * 
     * @return the sequence of vertices on an Eulerian cycle;
     *         {@code null} if no such cycle
     */
	public Iterable<Integer> cycle() {
		return cycle;
	}
	
	/**
     * Returns true if the digraph has an Eulerian cycle.
     * 
     * @return {@code true} if the digraph has an Eulerian cycle;
     *         {@code false} otherwise
     */
	public boolean hasEulerianCycle() {
		return cycle != null;
	}
	
	// returns any non-isolated vertex; -1 if no such vertex
	private static int nonIsolatedVertex(Digraph G) {
		for (int v = 0; v < G.V(); v++)
			if (G.outdegree(v) > 0)
				return v;
		return -1;
	}
	
	/**************************************************************************
    *
    *  The code below is solely for testing correctness of the data type.
    *
    **************************************************************************/

   // Determines whether a digraph has an Eulerian cycle using necessary
   // and sufficient conditions (without computing the cycle itself):
   //    - at least one edge
   //    - indegree(v) = outdegree(v) for every vertex
   //    - the graph is connected, when viewed as an undirected graph
   //      (ignoring isolated vertices)
	private static boolean hasEulerianCycle(Digraph G) {
		if (G.E() == 0)	return false;
		for (int v = 0; v < G.V(); v++)
			if (G.outdegree(v) != G.indegree(v))
				return false;
		
		Graph H = new Graph(G.V());
		for (int v = 0; v < G.V(); v++)
			for (int w : G.adj(v))
				H.addEdge(v, w);
		
		int s = nonIsolatedVertex(G);
		
		BreadFirstPaths bfs = new BreadFirstPaths(H, s);
		for (int v = 0; v < G.V(); v++)
			if (H.degree(v) > 0 && !bfs.hasPathTo(v))
				return false;
		return true;
	}
	
	private boolean certifySolution(Digraph G) {
		if (hasEulerianCycle() == (cycle() == null))	return false;
		if (hasEulerianCycle() != hasEulerianCycle(G))	return false;
		if (cycle == null) return true;
		if (cycle.size() != G.E() + 1)	return false;
		return true;
	}
	
	private static void unitTest(Digraph G, String description) {
		System.out.println(description);
        System.out.println("-------------------------------------");
        System.out.print(G);
        DirectedEulerianCycle euler = new DirectedEulerianCycle(G);

        System.out.print("Eulerian cycle: ");
        if (euler.hasEulerianCycle()) {
            for (int v : euler.cycle()) {
                System.out.print(v + " ");
            }
            System.out.println();
        }
        else {
            System.out.println("none");
        }
        System.out.println();
	}
	
	public static void main(String[] args) {
		int V = 13;
		int E = 22;
		// Eulerian cycle
        Digraph G1 = DigraphGenerator.eulerianCycle(V, E);
        unitTest(G1, "Eulerian cycle");

        // Eulerian path
        Digraph G2 = DigraphGenerator.eulerianPath(V, E);
        unitTest(G2, "Eulerian path");

        // empty digraph
        Digraph G3 = new Digraph(V);
        unitTest(G3, "empty digraph");

        // self loop
        Digraph G4 = new Digraph(V);
        int v4 = StdRandom.uniform(V);
        G4.addEdge(v4, v4);
        unitTest(G4, "single self loop");

        // union of two disjoint cycles
        Digraph H1 = DigraphGenerator.eulerianCycle(V/2, E/2);
        Digraph H2 = DigraphGenerator.eulerianCycle(V - V/2, E - E/2);
        int[] perm = new int[V];
        for (int i = 0; i < V; i++)
            perm[i] = i;
        StdRandom.shuffle(perm);
        Digraph G5 = new Digraph(V);
        for (int v = 0; v < H1.V(); v++)
            for (int w : H1.adj(v))
                G5.addEdge(perm[v], perm[w]);
        for (int v = 0; v < H2.V(); v++)
            for (int w : H2.adj(v))
                G5.addEdge(perm[V/2 + v], perm[V/2 + w]);
        unitTest(G5, "Union of two disjoint cycles");

        // random digraph
        Digraph G6 = DigraphGenerator.simple(V, E);
        unitTest(G6, "simple digraph");
	}
}
