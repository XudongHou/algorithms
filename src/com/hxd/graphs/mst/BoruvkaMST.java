package com.hxd.graphs.mst;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Bag;
import com.hxd.base.uf.UF;
import com.hxd.base.uf.WeightQuickUnionCompressionUF;
import com.hxd.introcs.stdlib.In;

/**
 * Boruvka的算法：通过添加边缘到一个不断增长的树木森林，如Kruskal{@link KruskalMST}}的算法，但分阶段构建MST。
 * 在每个阶段，找到将每个树连接到不同树的最小权重边，然后将所有这些边添加到MST。假设边权重全部不同，以避免周期。提示：保持在
 * 顶点索引数组中，以识别将每个组件连接到其最近邻的边，并使用union-find数据结构。 备注。由于树的数目在每个阶段中减少至少
 * 2倍，因此存在大多数对数V个阶段。有吸引力，因为它是高效的，可以并行运行。
 * {@code BoruvkaMST}类表示用于计算加权无向图中的最小生成树的数据类型。 边权重可以是正的，零或负的，并且不需要是不同的。
 * 如果图形未连接，则计算最小生成林，其是每个连接的组件中的最小生成树的联合。 {@code weight（）}方法返回最小生成树的权重，
 * {@code edges（）}方法返回其边。 这个实现使用Boruvka的算法和union-find数据类型。 构造函数需要与Elog V成正比的
 * 时间和与V成正比的额外空间（不包括图），其中V是顶点的数量，E是边的数量。 之后，{@code weight（）}方法需要一定的时间，
 * 而{@code edges（）}方法需要的时间与V成正比。
 * @author houxu_000 20170225
 * */
public class BoruvkaMST {
	private static final double FLOATING_POINT_EPSILON = 1E-12;
	
	private double weight;
	private Bag<Edge> mst = new Bag<Edge>(); // edges in MST
	
	
	public BoruvkaMST(EdgeWeightedGraph G) {
		UF uf = new WeightQuickUnionCompressionUF(G.V());
		
		for (int t = 1; t < G.V() && mst.size() < G.V() - 1; t = t+t){
			Edge[] closest = new Edge[G.V()];
			for (Edge e : G.edges()) {
				int v = e.either() , w = e.other(v);
				int i = uf.find(v), j = uf.find(w);
				if (i == j)	continue;	//same tree
				if (closest[i] == null || less(e, closest[i]))	closest[i] = e;
				if (closest[j] == null || less(e, closest[j]))	closest[j] = e;
			}
			
			for (int i = 0; i < G.V(); i++) {
				Edge e = closest[i];
				if (e != null) {
					int v = e.either(), w = e.other(v);
					if (!uf.connected(v, w)) {
						mst.add(e);
						weight += weight;
						uf.union(v, w);
					}
				}
			}
		}
		assert check(G);
	}

	/**
	 * 
	 * @return
	 */
	public double weight() {
		return weight;
	}

	/**
	 * 
	 * @return
	 */
	public Iterable<Edge> edges() {
		return mst;
	}
	
	private boolean check(EdgeWeightedGraph G) {
		 // check weight
        double totalWeight = 0.0;
        for (Edge e : edges()) {
            totalWeight += e.weight();
        }
        if (Math.abs(totalWeight - weight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weight());
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

	private boolean less(Edge e, Edge f) {
		return e.weight() < f.weight();
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
				BoruvkaMST mst = new BoruvkaMST(G);
				for (Edge edge : mst.edges())
					System.out.println(edge);
				System.out.printf("%.5f\n",mst.weight());
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}

	
}
