package com.hxd.graphs.undirectedGraphs;

import java.io.File;
/**
 * 
 * 使用深度优先搜索代替广度优先搜索来查找路径用例
 * @author 候旭东 20170219
 * */
public class DegreeOfSeparationDFS {
	private DegreeOfSeparationDFS() {}
	public static void main(String[] args) {
		File file = new File(".//algs4-data//movies.txt");
		String delimiter = "/";
		String[] sources = new String[]{"Michael, Terence (I)","Primus, Barry","Frederic, Patrick","Lithgow, John","Dooly, Mike"};
		String[] sinks = new String[]{"Hamlet (2000)","Happy Accidents (2000)","High Noon (1952)","Titanic (1997)"};
		SymbolGraph sg = new SymbolGraph(file, delimiter);
		Graph G = sg.graph();
		for (String source : sources) {
			if (!sg.contains(source)) {
				System.out.println(source + "not in database.");
				continue;
			}
			int s = sg.indexOf(source);
			DepthFirstPaths dfs = new DepthFirstPaths(G, s);
			for (String sink : sinks) {
				System.out.print(source+"  connect to "+sink+"\n");
				if (sg.contains(sink)) {
					int t = sg.indexOf(sink);
					if (dfs.hasPathTo(t)) {
						for (int v : dfs.pathTo(t)) {
							System.out.println("\t"+sg.nameOf(v));
						}
					}
					else {
						System.out.println("Not connected");
					}
				}
				else {
					System.out.println("Not in database.");
				}
			}
		}
	}
}
