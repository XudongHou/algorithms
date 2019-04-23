package com.hxd.graphs.undirectedGraphs;

/******************************************************************************
 *  Compilation:  javac DegreesOfSeparation.java
 *  Execution:    java DegreesOfSeparation filename delimiter source
 *  Dependencies: SymbolGraph.java Graph.java BreadthFirstPaths.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/41graph/routes.txt
 *                http://algs4.cs.princeton.edu/41graph/movies.txt
 *  
 *  
 *  %  java DegreesOfSeparation routes.txt " " "JFK"
 *  LAS
 *     JFK
 *     ORD
 *     DEN
 *     LAS
 *  DFW
 *     JFK
 *     ORD
 *     DFW
 *  EWR
 *     Not in database.
 *
 *  % java DegreesOfSeparation movies.txt "/" "Bacon, Kevin"
 *  Kidman, Nicole
 *     Bacon, Kevin
 *     Woodsman, The (2004)
 *     Grier, David Alan
 *     Bewitched (2005)
 *     Kidman, Nicole
 *  Grant, Cary
 *     Bacon, Kevin
 *     Planes, Trains & Automobiles (1987)
 *     Martin, Steve (I)
 *     Dead Men Don't Wear Plaid (1982)
 *     Grant, Cary
 *
 *  % java DegreesOfSeparation movies.txt "/" "Animal House (1978)"
 *  Titanic (1997)
 *     Animal House (1978)
 *     Allen, Karen (I)
 *     Raiders of the Lost Ark (1981)
 *     Taylor, Rocky (I)
 *     Titanic (1997)
 *  To Catch a Thief (1955)
 *     Animal House (1978)
 *     Vernon, John (I)
 *     Topaz (1969)
 *     Hitchcock, Alfred (I)
 *     To Catch a Thief (1955)
 *
 ******************************************************************************/

import java.io.File;

/**
 *	{@code DegreesOfSeparation}类提供了一个客户端，用于查找社交网络中一个特定个体和每个其他
 *	个体之间的分离程度。作为示例，如果社交网络由两个演员通过链接连接的演员组成 他们出现在同一部电影中，
 *	Kevin Bacon是个人，然后客户端计算网络中每个参与者的Kevin Bacon号。运行时间与网络中的个人和
 *	连接数成正比。 如果隐式给出连接，如在电影网络示例中（其中每两个演员连接，如果它们出现在同一电影中），
 *	则通过允许电影和演员顶点并且将每个电影连接到所有的 在那部电影中出现的演员。
 *	The {@code DegreesOfSeparation} class provides a client for finding
 *  the degree of separation between one distinguished individual and
 *  every other individual in a social network.
 *  As an example, if the social network consists of actors in which
 *  two actors are connected by a link if they appeared in the same movie,
 *  and Kevin Bacon is the distinguished individual, then the client
 *  computes the Kevin Bacon number of every actor in the network.
 *  <p>
 *  The running time is proportional to the number of individuals and
 *  connections in the network. If the connections are given implicitly,
 *  as in the movie network example (where every two actors are connected
 *  if they appear in the same movie), the efficiency of the algorithm
 *  is improved by allowing both movie and actor vertices and connecting
 *  each movie to all of the actors that appear in that movie.
 *  <p>	
 *	@author 候旭东 20170219
 * */

public class DegreeOfSeparation {
	private DegreeOfSeparation() {}
	
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
			BreadFirstPaths bfs = new BreadFirstPaths(G, s);
			for (String sink : sinks) {
				System.out.print(source+"  connect to "+sink+"\n");
				if (sg.contains(sink)) {
					int t = sg.indexOf(sink);
					if (bfs.hasPathTo(t)) {
						for (int v : bfs.pathTo(t)) {
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
