package com.hxd.graphs.sp;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Stack;
import com.hxd.introcs.stdlib.In;
import com.hxd.sort.priorityQueue.IndexMinPQ;

/**
 * {@code DijkstraSP}类表示用于求解加权有向图中的单点最短路径问题的数据类型，其中边缘权重是非负的。 这个实现使用
 * Dijkstra的算法和二进制堆。 构造函数需要与ElogV成正比的时间，其中V是顶点的数量，E是边的数量。之后，
 * {@code distTo（）}和{@code hasPathTo（）}方法需要一定的时间，而{@code pathTo（）}方法所花费的时
 * 间与返回的最短路径的边数成正比。
 * <p>
 * (最短路径的最优性条件).令G为一幅加权有向图,顶点s是G中的起点,distTo[]是一个有顶点索引的数组,保存的是G中路径的
 * 长度.对于从s可达的所有顶点v,distTO[v]的值是从s到v的某条路径的长度,对于从s不可达的所有顶点v,该值为无穷大.当且
 * 仅当对于从v到w的任意一条边e,这些值都满足distTo[w] <= distTo[v] + e.weight()时(换句话说,不存在有效
 * 边时),它们是最短路径的长度.将distTo[s]初始化为0,其他distTo[]元素初始化为无穷大,继续如下操作:放松G中的任意
 * 边,直到不存在有效边为止.通用算法没有指定边的放松顺序.
 * <p>
 * 问：Dijkstra的算法是否使用负权重？ A.是和否。有两种称为Dijkstra算法的最短路径算法，这取决于一个顶点是否可以在
 * 优先级队列中多次排队。当权重是非负的时，两个版本重合（因为没有顶点将被排队不止一次）。在DijkstraSP.java中实现
 * 的版本（允许顶点不止一次入列）在负边沿权重存在（但没有负周期）的情况下是正确的，但是在最坏的情况下它的运行时间是指数的。
 *  （我们注意到，如果边缘加权有向图具有负权重的边，则DijkstraSP.java会抛出异常，因此程序员不会对此指数行为感到惊
 *  讶）。如果我们修改DijkstraSP.java，使得顶点无法入列（例如，使用标记的[]数组来标记已放松的那些顶点），则该算
 *  法被保证以E log V时间运行，但是当存在具有负权重的边时可能产生不正确的结果。
 * <p>
 * @author houxu_000 20170225
 * */

public class DijkstraSP {
	private double[] distTo;			// distTo[v] = distance  of shortest s->v path
	private DirectedEdge[] edgeTo;		// edgeTo[v] = last edge on shortest s->v path
	private IndexMinPQ<Double> pq;		// priority queue of vertices
	
	/**
     * Computes a shortest-paths tree from the source vertex {@code s} to every other
     * vertex in the edge-weighted digraph {@code G}.
     *
     * @param  G the edge-weighted digraph
     * @param  s the source vertex
     * @throws IllegalArgumentException if an edge weight is negative
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
	public DijkstraSP(EdgeWeightedDigraph G, int s) {
		for (DirectedEdge e : G.edges()) 
			if (e.weight() < 0)
				throw new IllegalArgumentException("edge " + e + " has negative weight");
		distTo = new double[G.V()];
		edgeTo = new DirectedEdge[G.V()];
		
		validateVertex(s);
		
		for (int v = 0; v < G.V(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;
		distTo[s] = 0.0;
		
		/**
		 * 优先队列  保存一个点能够到达的点 和权重  队列中 权重最小的会排在最前面
		 * */
		pq = new IndexMinPQ<>(G.V());
		pq.insert(s, distTo[s]);
		
		/**
		 * 遍历队列
		 * */
		while (!pq.isEmpty()) {
			int v = pq.delMin();
			for (DirectedEdge e : G.adj(v))
				relax(e);
		}
		assert check(G, s);
	}
	
	/***
	 *	放松边
	 * @param e
	 */
	private void relax(DirectedEdge e) {
//		from是出去的顶点,to是指向的顶点
		int v = e.from() , w = e.to();
//		放松过程 计算dist[w]的权重之和 计算出最小值
		if (distTo[w] > distTo[v] + e.weight()) {
			distTo[w] = distTo[v] + e.weight();
			edgeTo[w] = e;
			//如果到达的这个点存在队列中 删除这个点
			if (pq.contains(w))	pq.decreaseKey(w, distTo[w]);
			//或者就删除 保证已经访问过的点 不会再被遍历
			else				pq.insert(w, distTo[w]);
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
     * Returns true if there is a path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return {@code true} if there is a path from the source vertex
     *         {@code s} to vertex {@code v}; {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public boolean hasPathTo(int v) {
		validateVertex(v);
		return distTo[v] < Double.POSITIVE_INFINITY;
	}
	
	/**
     * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
     *
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
	
	// check optimality conditions:
    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
	private boolean check(EdgeWeightedDigraph G, int s) {
		// check that edge weights are nonnegative
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
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
					DijkstraSP sp = new DijkstraSP(G, s);
					
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
