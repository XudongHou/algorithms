package com.hxd.graphs.directedGraphs;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Stack;
import com.hxd.introcs.stdlib.In;

/**
 * 寻找有向环;
 * {@code DirectedCycle}类表示用于确定有向图是否具有有向循环的数据类型。 hasCycle操作确定
 * 有向图是否具有有向循环，并且因此，循环操作返回1。 此实现使用深度优先搜索。 构造函数需要与V + E成比
 * 例的时间（在最坏的情况下），其中 V 是顶点的数量，E 是边的数量。 之后，hasCycle操作需要恒定的时间;
 * 循环操作需要与循环的长度成比例的时间。
 * <p>
 * @author 候旭东 20170220
 * */

public class DirectedCycle {
	private boolean[] marked;				// marked[v] = has vertex v been marked?
	private int[] edgeTo;					// edgeTo[v] = previous vertex on path to v
	private Stack<Integer> cycle;			// directed cycle (or null if no such cycle) 有向环中的所有顶点
	private boolean[] onStack;				// onStack[v] = is vertex on the stack? 递归调用的栈上的所有顶点(如果存在)
	
	public DirectedCycle(Digraph G) {
		onStack = new boolean[G.V()];
		edgeTo = new int[G.V()];
		marked = new boolean[G.V()];
		for (int v = 0; v < G.V(); v++)
			if (!marked[v] && cycle == null)
				dfs(G, v);
	}
	
	
	private void dfs(Digraph G, int v) {
		onStack[v] = true;
		marked[v] = true;
		for (int w : G.adj(v)) {
			if (cycle != null) return;
			else	if (!marked[w]) {
				edgeTo[w] = v;
				dfs(G, w);
			}
			else if (onStack[w]) {
				cycle = new Stack<Integer>();
				for (int x = v; x != w; x = edgeTo[x])
					cycle.push(x);
				cycle.push(w);
				cycle.push(v);
				assert check();
			}
		}
		onStack[v] = false;
	}

	public boolean hasCycle() {
		return cycle != null;
	}
	
	public Iterable<Integer> cycle(){
		return cycle;
	}
	
	private boolean check() {
		if (hasCycle()) {
			int first = -1, last = -1;
			for (int v : cycle()) {
				if (first == -1) first = v;
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
			File[] files = new File[]{
					new File(".\\algs4-data\\tinyDG.txt"),
					new File(".\\algs4-data\\mediumDG.txt"),
					};
			for(File file : files) {
				In in = new In(file);
				Digraph G = new Digraph(in); 
				DirectedCycle finder = new DirectedCycle(G);
				if (finder.hasCycle()) {
					System.out.print("Directed cycle: ");
					for (int v : finder.cycle())
						System.out.print(v + " ");
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
