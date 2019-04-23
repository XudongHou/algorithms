package com.hxd.graphs.undirectedGraphs;

import java.io.File;

/**
 * 计算有movies.txt得到的图的连通分量的数量和包含的顶点数小于10的连通分量的数量.计算最大的连通分量的
 * 离心率,直径,半径和中点.KevinBacon在最大的连通分量之中吗?
 * */
public class BaconHistogram {
	private BaconHistogram() {}
	public static void main(String[] args) {
		File file = new File(".//algs4-data//movies.txt");
		String delimiter = "/";
		String source = "Bacon, Kevin";
		SymbolGraph sg = new SymbolGraph(file, delimiter);
		Graph G = sg.graph();
		if (!sg.contains(source)) {
			 System.out.println(source + " not in database.");
	         return;
		}
		int s = sg.indexOf(source);
		BreadFirstPaths bfs = new BreadFirstPaths(G, s);
		
		//计算Kevin Bacon数字的直方图 - 无穷大的100
		int MAX_BACON = 100;
		int [] hist = new int[MAX_BACON + 1];
		for (int v = 0; v < G.V(); v++) {
			int bacon = Math.min(MAX_BACON, bfs.distTo(v));
			hist[bacon]++;
			//用大Bacon数打印演员和电影
			if(bacon/2 >= 7 && bacon < MAX_BACON)
				System.out.printf("%d %s\n",bacon/2, sg.nameOf(v));
		}
		//打印直方图 - 甚至索引是演员
		for (int i = 0; i < MAX_BACON; i += 2) {
			if (hist[i] == 0) break;
			System.out.printf("%3d %8d\n", i/2, hist[i]);
		}
		System.out.printf("Inf %8d\n",hist[MAX_BACON]);
	}
}
