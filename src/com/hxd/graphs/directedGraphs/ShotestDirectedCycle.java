package com.hxd.graphs.directedGraphs;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Stack;
import com.hxd.introcs.stdlib.In;

/**
 * 最短定向循环。 给定一个有向图，设计一个算法来找到具有最小边数的有向循环（或者报告该图是非循环的）。
 * 在最坏的情况下，您的算法的运行时间应与E V成正比。 
 * 应用：给一组需要肾移植的患者，其中每个患者有一个家庭成员愿意捐献肾脏，但是类型错误。 愿意捐赠给另一个人，
 * 只要他们的家人得到一个肾脏。 然后医院进行“多米诺手术”，其中所有移植同时进行。
 * 解决方案：从每个顶点运行BFS。 通过s的最短周期是边v-> s，加上从s到v的最短路径。
 * <p>
 * @author 候旭东 20170222
 * */

public class ShotestDirectedCycle {
	private Stack<Integer> cycle;	// directed cycle (or null if no such cycle)
	private int length;
	
	public ShotestDirectedCycle(Digraph G) {
		Digraph R = G.reverse();
		length = G.V();
		for (int v = 0; v < G.V(); v++) {
			BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(R, v);
			for (int w : G.adj(v)) {
				if (bfs.hasPathTo(w) && (bfs.distTo(w) + 1) < length) {
					length = bfs.distTo(w) + 1;
					cycle = new Stack<Integer>();
					for (int x : bfs.pathTo(w))
						cycle.push(x);
					cycle.push(v);
				}
			}
		}
	}
	
	public boolean hasCycle() {
		return cycle != null;
	}
	
	public Iterable<Integer> cycle() {
		return cycle;
	}
	
	public int length() {
		return length;
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{
					new File(".\\algs4-data\\tinyDG.txt"),
					new File(".\\algs4-data\\mediumDG.txt"),
					new File(".\\algs4-data\\tinyDAG.txt"),
					};
			for(File file : files) {
				In in = new In(file);
				Digraph G = new Digraph(in); 
				ShotestDirectedCycle finder = new ShotestDirectedCycle(G);
				if (finder.hasCycle()) {
					System.out.println("Shortest directed cycle: ");
					for (int v : finder.cycle()) 
						System.out.print(v + " ");
					System.out.println();
				}
				else {
					System.out.println("No directed cycle.");
				}
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
