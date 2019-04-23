package com.hxd.graphs.mst;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Queue;
import com.hxd.base.uf.UF;
import com.hxd.base.uf.WeightQuickUnionCompressionUF;
import com.hxd.introcs.stdlib.In;
import com.hxd.sort.priorityQueue.MinPQ;

/**
 * {@code LazyPrimMST}类表示用于在加权无向图中计算最小生成树的数据类型。 边权重可以是正的，零或负的，并且不需要是不同的。 
 * 如果图形未连接，则计算最小生成深林，其是每个连接的组中的最小生成树的集合。 {@code weight（）}方法返回最小生成树的权重，
 * {@code edges（）}方法返回其边。 这个实现使用Prim's算法的延时实现版本和优先队列。 
 * 构造函数计算一个含有V个顶点和E条边的连通加权无向图的最小生成树.所花费的时间与ElogE成正比(在最坏情况下),所需的空间和V成正比
 * （不包括图），之后，{@code weight（）}方法需要一定的时间，而{@code edges（）}方法需要的时间与V成正比。
 * <p>
 * 用一条边连接数中的任意两个顶点都会产生一个新的环;
 * 从树中删去一条边将会得到两棵独立的树;
 * 这两条性质是证明最小生成树的另一条基本性质的基础,而由这条基本性质就能够得到最小生成树算法.
 * 切分定理:把加权图中的所有顶点分为两个集合,检查横跨两个集合的所有边并识别哪条边应属于图的最小生成树.
 * 图的一种切分是将图的所有顶点分为两个非空且不重叠的两个集合,横切边是一条连接两个属于不同集合的顶点的边,通过指定一个顶点集并隐式
 * 地认为它的补集为另一个顶点集来指定一个切分.一条横切边就是连接该集合的一个顶点和不在该集合中的另一个顶点的一条边;
 * <p>
 * 命题:在一幅加权图中,给定任意的切分,横切边中的权重最小者必然属于图的最小生成树
 * @author houxu_000 20170225
 * */

public class LazyPrimMST {
	
	private static final double FLOATING_POINT_EPSILON = 1E-12;
	
	private boolean[] marked;		//最小生成树的的顶点
	private Queue<Edge> mst;		//最小生成树的边
	private MinPQ<Edge> pq;			//横切边
	private double weight;
	
	public LazyPrimMST(EdgeWeightedGraph G) {
		marked = new boolean[G.V()];
		mst = new Queue<Edge>();
		pq = new MinPQ<Edge>();
		for (int v = 0; v < G.V(); v++)
			if (!marked[v])
				prim(G, v);
		assert check(G);
	}

	private void prim(EdgeWeightedGraph G, int s) {
		scan(G, s);//假设G是连通的
		while (!pq.isEmpty()) {
			Edge e = pq.delMin();					//从pq中获得权重最小的边
			int v = e.either(), w = e.other(v);
			assert marked[v] || marked[w];
			if (marked[w] && marked[v])	continue;	//跳过失效的边
			mst.enqueue(e);							//添加边到树中
			weight += e.weight();					//权重计算
			if (!marked[v])	scan(G, v);				//将顶点(v或w)添加到树中
			if (!marked[w])	scan(G, w);
		}
	}

	private void scan(EdgeWeightedGraph G, int v) {
		assert !marked[v];
		//标记顶点v并将所有连接V和未被标记的顶点的边加入pq;
		marked[v] = true;
		for (Edge e : G.adj(v))
			if (!marked[e.other(v)]) pq.insert(e);
	}

	public Iterable<Edge> edges() {
		return mst;
	}
	
	public double weight() {
		return weight;
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
				LazyPrimMST mst = new LazyPrimMST(G);
				for (Edge edge : mst.edges())
					System.out.println(edge);
				System.out.printf("%.5f\n",mst.weight());
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
