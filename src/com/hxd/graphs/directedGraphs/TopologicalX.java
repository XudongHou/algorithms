package com.hxd.graphs.directedGraphs;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Queue;
import com.hxd.graphs.sp.EdgeWeightedDirectedCycle;

/**
 * 基于队列的拓扑阶算法。 开发一个非递归拓扑排序实现TopologicalX.java，它维护一个顶点索引数组，
 * 跟踪每个顶点的indegree。 在单次通过所有边缘时初始化数组和源队列
 * {@code TopologicalX}类表示用于确定有向无环图（DAG）的拓扑顺序的数据类型。 回想一下，当且
 * 仅当它是DAG时，有向图具有拓扑顺序。 hasOrder操作确定有向图是否具有拓扑顺序，如果有，则顺序操作
 * 返回一个。 此实现使用非递归的，基于队列的算法。 构造函数需要与V + E成正比的时间（在最坏的情况下），
 * 其中V是顶点的数量，E是边的数量。 之后，hasOrder和rank操作需要恒定的时间; 订单操作花费与V成
 * 比例的时间。
 * 如果有向图不是DAG，则参见{@link DirectedCycle}，{@link DirectedCycleX}和
 * {@link EdgeWeightedDirectedCycle}来计算有向循环。 
 * 对于使用深度优先搜索的递归版本，请参见{@link Topological}。
 * @author 候旭东 20170221
 * */

public class TopologicalX {
	private Queue<Integer> order;	// vertices in topological order
	private int[] ranks;			// ranks[v] = order where vertex v appers in order
	
	/**
     * Determines whether the digraph {@code G} has a topological order and, if so,
     * finds such a topological order.
     * @param G the digraph
     */
	public TopologicalX(Digraph G) {
		
		// indegrees of remaining vertices
		int[] indegree = new int[G.V()];
		for (int v = 0; v < G.V(); v++)
			indegree[v] = G.indegree(v);
		ranks = new int[G.V()];
		order = new Queue<Integer>();
		int count = 0;
		
		//初始化队列以包含所有具有indegree = 0的顶点
		Queue<Integer> queue = new Queue<Integer>();
		for (int v = 0; v < G.V(); v++)
			if(indegree[v] == 0) queue.enqueue(v);
		while (!queue.isEmpty()) {
			int v = queue.dequeue();
			order.enqueue(v);
			ranks[v] = count++;
			for (int w : G.adj(v)) {
				indegree[w] --;
				if (indegree[w] == 0) queue.enqueue(w);
			}
		}
		if (count != G.V())
			order = null;
		assert check(G);
	}
	
	public Iterable<Integer> order() {
		return order;
	}
	
	public boolean hasOrder() {
		return order != null;
	}
	
	public int rank(int v) {
		if (hasOrder())		return ranks[v];
		else				return -1;
	}
	
	private boolean check(Digraph G) {
		// digraph is acyclic
		if (hasOrder()) {
			boolean[] found = new boolean[G.V()];
			for (int i = 0; i < G.V(); i++)
				found[rank(i)] = true;
			for (int i = 0; i < G.V(); i++)
				if (!found[i]){
					System.err.println("No vertex with rank " + i);
					return false;
				}

			//检查等级是否提供有效的拓扑顺序
			for (int v = 0; v < G.V(); v++)
			for (int w : G.adj(v))
				if (rank(v) > rank(w)) {
                    System.err.printf("%d-%d: rank(%d) = %d, rank(%d) = %d\n",
                            v, w, v, rank(v), w, rank(w));
                    return false;
				}
			
			//检查order（）是否与rank（）一致
			int r = 0;
			for (int v : order()) {
				if (rank(v) !=r) {
					 System.err.println("order() and rank() inconsistent");
	                 return false;
				}
				r++;
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{
					new File(".\\algs4-data\\jobs.txt"),
					};
			for(File file : files) {
				String delimiter = "/";
				SymbolDigraph sg = new SymbolDigraph(file, delimiter);
				TopologicalX topological = new TopologicalX(sg.digraph());
				for (int v : topological.order())
					System.out.println(sg.nameOf(v));
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
