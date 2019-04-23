package com.hxd.graphs.directedGraphs;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

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
 *	{@code SymbolDigraph}类表示一个无向图，其中顶点名称是任意字符串。通过提供字符串顶点名称和整数之间
 *	的映射，它用作{@link Didraph}数据类型的包装器，它假设顶点名称 是0和V - 1之间的整数。它还支持从文件
 *	初始化符号图。此实现使用{@link RedBlackBST;}从字符串映射到整数，数组从整数映射到字符串，并且
 *	{@link Digraph}来存储indexOf和contains操作占用与log V成比例的时间，其中V是顶点的数量。
 *	nameOf操作需要恒定时间。
 *	<p>
 *	The {@code SymbolDigraph} class represents an undirected graph, where the
 *  vertex names are arbitrary strings.
 *  By providing mappings between string vertex names and integers,
 *  it serves as a wrapper around the
 *  {@link Digraph} data type, which assumes the vertex names are integers
 *  between 0 and <em>V</em> - 1.
 *  It also supports initializing a symbol graph from a file.
 *  <p>
 *  This implementation uses an {@link RedBlackBST} to map from strings to integers,
 *  an array to map from integers to strings, and a {@link Digraph} to store
 *  the underlying graph.
 *  The <em>indexOf</em> and <em>contains</em> operations take time 
 *  proportional to log <em>V</em>, where <em>V</em> is the number of vertices.
 *  The <em>nameOf</em> operation takes constant time.
 *  <p>
 *	@author 候旭东 20170219
 * */

public class SymbolDigraph {
	private RedBlackBST<String, Integer> st;	//String -> index
	private String[] keys;						//index -> String
	private Digraph graph;						//基础图
	
	/**  
     * Initializes a digraph from a file using the specified delimiter.
     * Each line in the file contains
     * the name of a vertex, followed by a list of the names
     * of the vertices adjacent to that vertex, separated by the delimiter.
     * @param filename the name of the file
     * @param delimiter the delimiter between fields
     */
	public SymbolDigraph(String filename,String delimiter) {
		this(new File(filename), delimiter);
	}
	/**  
     * Initializes a digraph from a file using the specified delimiter.
     * Each line in the file contains
     * the name of a vertex, followed by a list of the names
     * of the vertices adjacent to that vertex, separated by the delimiter.
     * @param filen the file
     * @param delimiter the delimiter between fields
     */
	public SymbolDigraph(File file, String delimiter) {
		st = new RedBlackBST<String, Integer>();
		
		In in = new In(file);
		while (in.hasNextLine()) {
			String[] a = in.readLine().split(delimiter);
			for (int i = 0; i < a.length; i++) {
				if (!st.contains(a[i]))
					st.put(a[i], st.size());
			}
		}
		
		keys = new String[st.size()];
		for (String key : st.keys())
			keys[st.get(key)] = key;
		
		graph = new Digraph(st.size());
		in = new In(file);
		while (in.hasNextLine()) {
			String[] a = in.readLine().split(delimiter);
			int v = st.get(a[0]);
			for (int i = 1; i < a.length; i++) {
				int w = st.get(a[i]);
				graph.addEdge(v, w);
			}
		}
	}
	
	/**
     * Does the digraph contain the vertex named {@code s}?
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
	public int indexOf(String s) {
		return st.get(s);
	}
	
	/**
     * Returns the digraph assoicated with the symbol graph. It is the client's responsibility
     * not to mutate the digraph.
     *
     * @return the digraph associated with the symbol digraph
     */
	public Digraph digraph() {
		return graph;
	}
	
    /**
     * Returns the name of the vertex associated with the integer {@code v}.
     * @param  v the integer corresponding to a vertex (between 0 and <em>V</em> - 1) 
     * @return the name of the vertex associated with the integer {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public String nameOf(int v) {
		validateVertex(v);
		return keys[v];
	}
	
	
	private void validateVertex(int v) {
		int V = graph.V();
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{
					new File(".//algs4-data//routes.txt"),
					};
			for(File file : files) {
				String delimiter = " ";
				SymbolDigraph sg = new SymbolDigraph(file, delimiter);
				Digraph graph = sg.digraph();
				String[] sources = {"DEN","JFK","LAS","HOU","ATL"};
				for (String source : sources) {
					System.out.print(source +" -> ");
					for (int w : graph.adj(sg.indexOf(source)))
						System.out.print("  " + sg.nameOf(w));
					System.out.println();
				}
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
