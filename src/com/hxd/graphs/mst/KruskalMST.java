package com.hxd.graphs.mst;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Queue;
import com.hxd.base.uf.UF;
import com.hxd.base.uf.WeightQuickUnionCompressionUF;
import com.hxd.introcs.stdlib.In;
import com.hxd.sort.priorityQueue.MinPQ;

/**
 * {@code KruskalMST}类表示用于计算边缘加权图中的最小生成树的数据类型。 边权重可以是正的，零或负的，并且不需要是不同的。
 * 如果图形未连接，则计算最小生成林，其是每个连接的组件中的最小生成树的联合。 {@code weight（）}方法返回最小生成树的权重，
 * {@code edges（）}方法返回其边。 这个实现使用Krusal的算法和union-find数据类型。 构造函数需要与E log E成正比的
 * 时间和与V成正比的额外空间（不包括图），其中V是顶点的数量，E是边的数量。 之后，{@code weight（）}方法需要一定的时间，
 * 而{@code edges（）}方法需要的时间与V成正比。
 * @author houxu_000 20170225
 * */

public class KruskalMST {
	private static final double FLOATING_POINT_EPSILON = 1E-12;
	
	private double weight;
	private Queue<Edge> mst = new Queue<Edge>();
	
	/**
     * 使用一条队列来保存最小生成树中的所有边,一条优先队列来保存还未被检查的边和
     * 一个union-find的数据结构来判断无效的边.
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     * @param G the edge-weighted graph
     */
	public KruskalMST(EdgeWeightedGraph G) {
		MinPQ<Edge> pq = new MinPQ<Edge>();
		for (Edge e : G.edges())
			pq.insert(e);
		
		UF uf = new WeightQuickUnionCompressionUF(G.V());
		while (!pq.isEmpty() && mst.size() < G.V() - 1) {
			Edge e = pq.delMin();			//得到权重最小的边和它的顶点
			int v = e.either();
			int w = e.other(v);
			if (!uf.connected(v, w)) {
				uf.union(v, w);				//合并分量
				mst.enqueue(e);				//添加到最小生成树中
				weight += e.weight();
			}
		}
		assert check(G);
	}

	public Iterable<Edge> edges() {
		return mst;
	}
	
	public double weight() {
		return weight;
	}
	
	private boolean check(EdgeWeightedGraph G) {
		// check total weight
        double total = 0.0;
        for (Edge e : edges()) {
            total += e.weight();
        }
        if (Math.abs(total - weight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", total, weight());
            return false;
        }

        // check that it is acyclic
        UF uf = new WeightQuickUnionCompressionUF(G.V());
        for (Edge e : edges()) {
            int v = e.either(), w = e.other(v);
            if (uf.connected(v, w)) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(v, w);
        }

        // check that it is a spanning forest
        for (Edge e : G.edges()) {
            int v = e.either(), w = e.other(v);
            if (!uf.connected(v, w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (Edge e : edges()) {

            // all edges in MST except e
            uf = new WeightQuickUnionCompressionUF(G.V());
            for (Edge f : mst) {
                int x = f.either(), y = f.other(x);
                if (f != e) uf.union(x, y);
            }
            
            // check that e is min weight edge in crossing cut
            for (Edge f : G.edges()) {
                int x = f.either(), y = f.other(x);
                if (!uf.connected(x, y)) {
                    if (f.weight() < e.weight()) {
                        System.err.println("Edge " + f + " violates cut optimality conditions");
                        return false;
                    }
                }
            }

        }

        return true;
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{
					new File(".\\algs4-data\\tinyEWG.txt"),
					new File(".\\algs4-data\\mediumEWG.txt"),
					};
			for(File file : files) {
				In in = new In(file);
				EdgeWeightedGraph G = new EdgeWeightedGraph(in);
				System.out.println(G);
				System.out.println("the mst is: ");
				KruskalMST mst = new KruskalMST(G);
				for (Edge edge : mst.edges())
					System.out.println(edge);
				System.out.printf("%.5f\n",mst.weight());
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
