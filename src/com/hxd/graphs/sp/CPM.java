package com.hxd.graphs.sp;

import java.io.File;

import com.hxd.introcs.stdlib.In;

/**
 * 优先限制下的并行任务调度问题的关键路径访法
 * {@code CPM}类提供了一个客户端，通过关键路径方法解决并行优先级约束的作业调度问题。 它将问题减少到加权DAG中
 * 的最长路径问题。 它从作业调度问题规范构建加权有向图（必须是DAG），找到最长路径树，并计算最长路径长度（这正是每
 * 个作业的开始时间）。 此实现使用{@link AcyclicLP}在DAG中查找最长路径。 运行时间与V + E成正比(线性级别)
 * ，其中V是作业数，E是优先约束数。
 * <p>
 * 解决任务调度问题的关键路径的方法步骤如下:创建一幅无环加权有向图,其中包含 一个起点s和一个终点t 且每个任务都对应着
 * 两个顶点(一个起始顶点和一个结束顶点).
 * 对于每个任务都有一条从它的起始顶点指向结束顶点的边.边的权重为任务所需的时间
 * .对于每条优先级限制v->w,添加一条从v的结束顶点指向w的起始顶点的权重为零的边.
 * 并需要为每个任务添加一条从起点指向该任务起始顶点的权重为零的边以及一条从该任务的结束顶点到终点的权重为零的边.
 * 这样,每个任务预计的开始时间即为从起点到它的起始顶点的最长距离
 * <p>
 * @author houxu_000 20170226
 * */

public class CPM {
	
	private CPM(){}
	
	public static void main(String[] args) {
		In in = new In(new File(".\\algs4-data\\jobsPC.txt"));
		// number of jobs;
		int n = in.readInt();
		// source and sink;
		int source = 2*n, sink = 2*n + 1;
		EdgeWeightedDigraph G  = new EdgeWeightedDigraph(2*n + 2);
		for (int i = 0; i < n; i++) {
			double duration = in.readDouble();
			G.addEdge(new DirectedEdge(source, i, 0.0));		//连接起点
			G.addEdge(new DirectedEdge(i+n, sink, 0.0));		//连接终点
			G.addEdge(new DirectedEdge(i, i+n, duration));		//任务开始到结束
			//precedence constraints 优先任务
			int m = in.readInt();
			for (int j = 0; j < m; j++) {
				int precedent = in.readInt();
				G.addEdge(new DirectedEdge(n+i, precedent, 0.0));
			}
		}
		//compute longest path
		AcyclicLP lp = new AcyclicLP(G, source);	
		//print 
		System.out.println("");
		System.out.println("-----------------");
		for (int i = 0; i < n; i++)
			System.out.printf("%4d %7.1f %7.1f\n", i, lp.distTo(i), lp.distTo(i+n));
		System.out.printf("Finish time: %7.1f\n", lp.distTo(sink));
	}
}
