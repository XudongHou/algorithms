package com.hxd.graphs.sp;

import java.io.File;

import com.hxd.introcs.stdlib.In;

/**
 * {@code Arbitrage}类提供了一个客户端，通过构造交换表的完整有向图表示，然后在有向图中找到负周期，在货币
 * 兑换表中找到套利机会。 该实现使用Bellman-Ford算法在完整的图中找到负周期。在最坏情况下，运行时间与V ^ 3
 * 成比例，其中V是货币数量。
 * @author houxu_000 20170227
 * */

public class Arbitrage {
	private Arbitrage() {}
	
	public static void main(String[] args) {
		In in = new In(new File(".\\algs4-data\\rates.txt"));
		int V = in.readInt();
		String [] name = new String[V];
		
		EdgeWeightedDigraph G = new EdgeWeightedDigraph(V);
		for (int v = 0; v < G.V(); v++) {
			name[v] = in.readString();
			for (int w =0; w < V; w++) {
				double rate = in.readDouble();
				DirectedEdge e = new DirectedEdge(v, w, -Math.log(rate));
				G.addEdge(e);
			}
		}
		
		BellmanFordSP spt = new BellmanFordSP(G, 0);
		if (spt.hasNegativeCycle()) {
			double stake = 1000.0;
			for (DirectedEdge e : spt.negativeCycle()) {
				System.out.printf("%10.5f %s ",stake,name[e.from()]);
				/**
				 * 返回欧拉数e增加到double值的幂。特殊情况：
				 * •如果参数为NaN，则结果为NaN。
				 * •如果参数为正无穷大，则结果为正无穷大。 
				 * •如果参数为负无穷大，则结果为正零。计算结果必须在精确结果的1 ulp内。结果必须是半单调的
				 * */
				stake *= Math.exp(-e.weight());
				System.out.printf("=10.5f %s\n",stake,name[e.to()]);
			}
		}
		else {
			System.out.println("No arbitrage opportunity");
		}
	}
}
