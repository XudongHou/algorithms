package com.hxd.graphs.directedGraphs;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Queue;
import com.hxd.introcs.stdlib.In;

/**
 *	如果两个顶点v和w是互相可达的,则称他们为强连通的,如果一幅有向图中的任意两个顶点都是强连通的,则称这副有向图也就
 *	是强连通的.有向图中的强连通性也是 一种顶点之间等价关系,有着以下性质:
 *	自反性: 任意顶点v和自己都是强连通的;
 *	对称性: 如果v和w是强连通的,那么w和v也是强连通的;
 *	传递性: 如果v和w是强连通的,w和x也是强连通的,那么v和x也是强连通的;
 *	<p>
 *	作为一种等价关系,强连通性将所有顶点分为了一些等价类,每个等价类都是由相互均为强连通的顶点的最大子集组成的.这些子集
 *	称之为强连通分量.一个含有V个顶点的有向图中含有1~V个强连通分量--一个强连通图只含有一个强连通分量,而一个有向无环图
 *	含有V个强连通分量.需要注意的是强连通分量是基于点,而非边.有些边连接的两个顶点在同一个强连通分量中,而有些边连接的两
 *	个顶点则在不同的强连通分量中.后者不会出现在任何有向环中.
 *	<p>
 *	{@code KosarajuSharirSCC}类表示用于确定有向图中的强分量的数据类型。 id操作确定给定顶点位于哪个强组件中;
 *	areStronglyConnected操作确定两个顶点是否在同一强组件中; 并且计数操作确定强分量的数量。组件的组件标识符是
 *	强组件中的一个顶点：当且仅当它们在同一强组件中时，两个顶点具有相同的组件标识符。此实现使用Kosaraju-Sharir算法。
 *	 构造函数需要与V + E成正比的时间（在最坏的情况下），其中V是顶点的数量，E是边的数量。 之后，id，count和
 *	areStronglyConnected操作需要固定的时间。 
 *	对于同一API的替代实现，请参阅{@link TarjanSCC}和{@link GabowSCC}。
 *	<p>
 *	@author 候旭东 20170221
 * */

public class KosarajuSharirSCC {
	private boolean marked[];		// marked[v] = has vertex v been visited?
	private int[] id;				// id[v] = id of strong component containing v
	private int count;				// number of strongly-connected components
	
	/**
	 * 在给定的一幅有向图G中,使用DepthFirstOrder来计算它的反向图G1的逆后序排列;
	 * 在G中进行标准的深度优先搜索,但是要按照刚才计算得到的是顺序而非标准的顺序来访问所
	 * 有未被标记过的顶点;
	 * 在构造函数中,所有在同一个递归dfs()调用中被访问到的顶点都在同一个强连通分量中,将
	 * 它们按照和CC相同的方式识别出来;
	 * 使用深度优先搜索查找给定有向图G的反向图G1,根据由此得到的所有顶点的逆后序再次用深
	 * 度优先搜索处理有向图G,其构造函数中的每一次递归调用所标记的顶点都在同一个强连通分量中
     * Computes the strong components of the digraph {@code G}.
     * @param G the digraph
     */
	public KosarajuSharirSCC(Digraph G) {
		
		DepthFirstOrder dfs = new DepthFirstOrder(G.reverse());
		
		marked = new boolean[G.V()];
		id = new int[G.V()];
		for (int v : dfs.reversePost()){
			if(!marked[v]) {
				dfs(G, v);
				count++;
			}
		}
		assert check(G);
	}


	private void dfs(Digraph G, int v) {
		marked[v] = true;
		id[v] = count;
		for (int w : G.adj(v)) {
			if (!marked[w])
				dfs(G, w);
		}
	}	
	
	/**
     * Returns the number of strong components.
     * @return the number of strong components
     */
	public int count() {
		return count;
	}
	
	/**
     * Are vertices {@code v} and {@code w} in the same strong component?
     * @param  v one vertex
     * @param  w the other vertex
     * @return {@code true} if vertices {@code v} and {@code w} are in the same
     *         strong component, and {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     * @throws IllegalArgumentException unless {@code 0 <= w < V}
     */
	public boolean stronglyConnected(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		return id[v] == id [w];
	}
	
	/**
     * Returns the component id of the strong component containing vertex {@code v}.
     * @param  v the vertex
     * @return the component id of the strong component containing vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
	public int id(int v) {
		validateVertex(v);
		return id[v];
	}
	
	private void validateVertex(int v) {
		int V = marked.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}
	
	private boolean check(Digraph G) {
		TransitiveClosure tc = new TransitiveClosure(G);
		for (int v = 0; v < G.V(); v++)
			for (int w = 0; w < G.V(); w++)
				if (stronglyConnected(v, w) != (tc.reachable(v, w) && tc.reachable(v, w)))
					return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			File[] files = new File[]{
					new File(".\\algs4-data\\tinyDG.txt"),
					new File(".\\algs4-data\\mediumDG.txt"),
					};
			for(File file : files) {
				In in = new In(file);
				Digraph G = new Digraph(in); 
				KosarajuSharirSCC scc = new KosarajuSharirSCC(G);
				
				int m = scc.count();
				System.out.println(m + " strong components");
				Queue<Integer>[] components = (Queue<Integer>[]) new Queue[m];
				for (int i = 0; i < m; i++)
					components[i] = new Queue<Integer>();
				for (int v = 0; v < G.V(); v++)
					components[scc.id(v)].enqueue(v);
				for (int i = 0; i < m; i++){
					for (int v : components[i])
						System.out.print(v+"  ");
					System.out.println();
				}
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
