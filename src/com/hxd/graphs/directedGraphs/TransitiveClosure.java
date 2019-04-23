package com.hxd.graphs.directedGraphs;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.introcs.stdlib.In;

/**
 * {@code TransitiveClosure}类表示用于计算有向图的传递闭包的数据类型。 这个实现从每个顶点运行
 * 深度优先搜索。 构造函数需要与V（V + E）（在最坏情况下）成正比的时间，并使用与V^2成比例的空间，其中
 * V是顶点的数量，E是边的数量。 
 * 对于大型图，你可能需要考虑一个更复杂的算法。<a href = "http://www.cs.hut.fi/~enu/thesis.html"> Nuutila </a>
 * 为在E中运行的问题（基于强分量和间隔表示）提出了两种算法 分别运行在（ E + V）时间。
 * <p>
 * 有向图G的传递闭包是由相同的一组顶点组成的另一幅有向图,在传递闭包中存在一条从v指向w的边当且仅当在G中
 * w是从v可达的.根据约定,每个顶点对于自己都是可达的,因此传递闭包会含有V个自环.示例有向图只有22条有向边,
 * 但在传递闭包含有可能的169条有向边中的102条.一般来说一幅有向图的传递闭包中所含有的边都比原图多得多,一
 * 幅稀疏的传递闭包却是一幅稠密图也是很常见的.因为传递闭包一班都很稠密,通常将它们表示为一个布尔值矩阵,其中
 * v行w列的值为true当且仅当w是从v可达的.本质上,TransitiveClosure通过计算G的传递闭包来支持常数
 * 时间的查询--传递闭包矩阵中的第v行就是TransitiveClosure类中的DirectDFS[]数组的第v个元素的
 * marked[]数组.
 * <p>
 * @author 候旭东 20170221
 * */

public class TransitiveClosure {
	private DirectedDFS[] tc;	// TC [V] =从V可达
	

    /**
     * Computes the transitive closure of the digraph {@code G}.
     * @param G the digraph
     */
	public TransitiveClosure(Digraph G) {
		tc = new DirectedDFS[G.V()];
		for (int v = 0; v < G.V(); v++)
			tc[v] = new DirectedDFS(G, v);
	}
	
	/**
     * Is there a directed path from vertex {@code v} to vertex {@code w} in the digraph?
     * @param  v the source vertex
     * @param  w the target vertex
     * @return {@code true} if there is a directed path from {@code v} to {@code w},
     *         {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     * @throws IllegalArgumentException unless {@code 0 <= w < V}
     */
	public boolean reachable(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		return tc[v].marked(w);
	}

	private void validateVertex(int v) {
		 int V = tc.length;
	        if (v < 0 || v >= V)
	            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{
					new File(".\\algs4-data\\tinyDG.txt"),
					new File(".\\algs4-data\\mediumDG.txt"),
					};
			for(File file : files) {
				In in = new In(file);
				Digraph G = new Digraph(in);
				 TransitiveClosure tc = new TransitiveClosure(G);
				 System.out.println("    ");
				 for (int v = 0; v < G.V(); v++)
					 System.out.printf("%3d",v);
				 System.out.println();
				 System.out.println("-----------------------------------");
				 for (int v = 0; v < G.V(); v++) {
					 System.out.printf("%3d: ",v);
					 for (int w = 0; w < G.V(); w++) {
						 if (tc.reachable(v, w))	System.out.printf(" T");
						 else						System.out.printf("  ");
					 }
					 System.out.println();
				 }
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
