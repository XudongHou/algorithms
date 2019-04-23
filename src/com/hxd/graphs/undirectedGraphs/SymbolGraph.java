package com.hxd.graphs.undirectedGraphs;
/******************************************************************************
 *  Compilation:  javac SymbolGraph.java
 *  Execution:    java SymbolGraph filename.txt delimiter
 *  Dependencies: ST.java Graph.java In.java StdIn.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/41graph/routes.txt
 *                http://algs4.cs.princeton.edu/41graph/movies.txt
 *                http://algs4.cs.princeton.edu/41graph/moviestiny.txt
 *                http://algs4.cs.princeton.edu/41graph/moviesG.txt
 *                http://algs4.cs.princeton.edu/41graph/moviestopGrossing.txt
 *  
 *  %  java SymbolGraph routes.txt " "
 *  JFK
 *     MCO
 *     ATL
 *     ORD
 *  LAX
 *     PHX
 *     LAS
 *
 *  % java SymbolGraph movies.txt "/"
 *  Bacon, Kevin
 *     Woodsman, The (2004)
 *     Wild Things (1998)
 *     Where the Truth Lies (2005)
 *     Tremors (1990)
 *     ...
 *     Apollo 13 (1995)
 *     Animal House (1978)
 *  Assumes that input file is encoded using UTF-8.
 *  % iconv -f ISO-8859-1 -t UTF-8 movies-iso8859.txt > movies.txt
 *
 ******************************************************************************/

import java.io.File;

import com.hxd.introcs.ST;
import com.hxd.introcs.stdlib.In;
import com.hxd.search.balancedBinarySearchTrees.RedBlackBST;

/**
 *	符号图:在典型应用中,图都是通过文件或者网页定义的,使用的是字符串而非整数来表示和指代顶点.为了适应这样的
 *	应用,定义了拥有如下性质的输入格式:
 *	1 顶点名为字符串;
 *	2 用指定的分隔符来隔开顶点名(允许顶点名中含有空格);
 *	3 每一行都表示一组边的集合,每一条边都连接这一行的第一个名称表示的顶点和其他名称所表示的顶点;
 *	4 顶点总数V和边的总数E都是隐式定义的;
 *	<p>
 *	{@code SymbolGraph}类表示一个无向图，其中顶点名称是任意字符串。通过提供字符串顶点名称和整数之间
 *	的映射，它用作{@link Graph}数据类型的包装器，它假设顶点名称 是0和V - 1之间的整数。它还支持从文件
 *	初始化符号图。此实现使用{@link RedBlackBST}从字符串映射到整数，数组从整数映射到字符串，并且{@link Graph}
 *	来存储indexOf和contains操作占用与log V成比例的时间，其中V是顶点的数量。nameOf操作需要恒定时间。
 *	<p>
 *	The {@code SymbolGraph} class represents an undirected graph, where the
 *  vertex names are arbitrary strings.
 *  By providing mappings between string vertex names and integers,
 *  it serves as a wrapper around the
 *  {@link Graph} data type, which assumes the vertex names are integers
 *  between 0 and <em>V</em> - 1.
 *  It also supports initializing a symbol graph from a file.
 *  <p>
 *  This implementation uses an {@link ST} to map from strings to integers,
 *  an array to map from integers to strings, and a {@link Graph} to store
 *  the underlying graph.
 *  The <em>indexOf</em> and <em>contains</em> operations take time 
 *  proportional to log <em>V</em>, where <em>V</em> is the number of vertices.
 *  The <em>nameOf</em> operation takes constant time.
 *  <p>
 *	@author 候旭东 20170219
 * */

public class SymbolGraph {
	private RedBlackBST<String, Integer> st;		//String -> index;
	private String[] keys;								//index -> string;
	private Graph graph;								//基础图
	
	/**  
     * 使用指定的分隔符从文件初始化图形。文件中的每一行包含一个顶点的名称，后面是与该顶点相邻的顶点
     * 的名称列表，用分隔符分隔。
     * Initializes a graph from a file using the specified delimiter.
     * Each line in the file contains
     * the name of a vertex, followed by a list of the names
     * of the vertices adjacent to that vertex, separated by the delimiter.
     * @param filename the name of the file
     * @param delimiter the delimiter between fields
     */
	public SymbolGraph(String fileName,String delimiter) {
		this(new File(fileName), delimiter);
	}
	/**  
     * 使用指定的分隔符从文件初始化图形。文件中的每一行包含一个顶点的名称，后面是与该顶点相邻的顶点
     * 的名称列表，用分隔符分隔。
     * Initializes a graph from a file using the specified delimiter.
     * Each line in the file contains
     * the name of a vertex, followed by a list of the names
     * of the vertices adjacent to that vertex, separated by the delimiter.
     * @param file the name of the file
     * @param delimiter the delimiter between fields
     */
	public SymbolGraph(File file, String delimiter) {
		st = new RedBlackBST<String,Integer>();
		In in = new In(file);
		
		//通过读取字符串来创建索引，以将不同的字符串与索引相关联
		while(!in.isEmpty()) {
			String[] a = in.readLine().split(delimiter);
			for (int i = 0; i < a.length; i++)
				if(!st.contains(a[i]))
					st.put(a[i], st.size());
		}
		System.out.println("Done reading "+file.getName());
		//反向索引在数组中获取字符串键
		keys = new String[st.size()];
		for (String name : st.keys())
			keys[st.get(name)] = name;
		
		//通过将每一行上的第一顶点连接到所有其他顶点来构建图
		graph = new Graph(st.size());
		in = new In(file);
		while (in.hasNextLine()) {
			String[] a = in.readLine().split(delimiter);
		//每一行都表示一组边的集合,每一条边都连接这一行的第一个名称表示的顶点和其他名称所表示的顶点
			int v = st.get(a[0]);
			for (int i = 1; i < a.length; i++){
				int w = st.get(a[i]);
				graph.addEdge(v, w);
			}
		}
	}
	
	/**
     * Does the graph contain the vertex named {@code s}?
     * @param s the name of a vertex
     * @return {@code true} if {@code s} is the name of a vertex, and {@code false} otherwise
     */
	public boolean contains(String s) {
		return st.contains(s);
	}
	
	/**
     * Returns the integer associated with the vertex named {@code s}.
     * @param s the name of a vertex
     * @return the integer (between 0 and <em>V</em> - 1) associated with the vertex named {@code s}
     */
	public int indexOf (String s) {
		return st.get(s);
	}
	
	/**
     * Returns the name of the vertex associated with the integer {@code v}.
     * @param  v the integer corresponding to a vertex (between 0 and <em>V</em> - 1) 
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     * @return the name of the vertex associated with the integer {@code v}
     */
	public String nameOf (int v) {
		validateVertex(v);
		return keys[v];
	}
	
	/**
	 * 返回与符号图关联的图。 客户端的责任不是改变图表。
     * Returns the graph assoicated with the symbol graph. It is the client's responsibility
     * not to mutate the graph.
     * @return the graph associated with the symbol graph
     */
	public Graph graph() {
		return graph;
	}
	private void validateVertex(int v) {
		int V = graph.V();
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}
	
	public static void main(String[] args) {
		File file = new File(".//algs4-data//routes.txt");
		String delimiter = " ";
		String[] sources = {"DEN","JFK","LAS","HOU","ATL"};
		SymbolGraph sg = new SymbolGraph(file, delimiter);
		Graph graph = sg.graph();
		for (String source : sources) {
			if (sg.contains(source)) {
				int s = sg.indexOf(source);
				System.out.println(source);
				for (int v : graph.adj(s)) {
					System.out.print("  " + sg.nameOf(v));
				}
				System.out.println();
			}
			else {
				System.out.println("input not contain '" + source + "'");
			}
		}
//		 仅供测试,数据庞大
		file = new File(".//algs4-data//movies.txt");
		delimiter = "/";
		sources = new String[]{"Michael, Terence (I)","Primus, Barry","Frederic, Patrick","Lithgow, John","Dooly, Mike"};
		sg = new SymbolGraph(file, delimiter);
		graph = sg.graph();
		for (String source : sources) {
			if (sg.contains(source)) {
				int s = sg.indexOf(source);
				System.out.println(source);
				for (int v : graph.adj(s)) {
					System.out.println("  " + sg.nameOf(v));
				}
			}
			else {
				System.out.println("input not contain '" + source + "'");
			}
		}
	}
}