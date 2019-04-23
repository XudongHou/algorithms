package com.hxd.graphs.directedGraphs;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Queue;
import com.hxd.base.Stack;
import com.hxd.introcs.stdlib.In;

/**
 * Gabow的强连通算法。 GabowSCC.java实现Gabow的计算强组件的算法。
 * {@code GabowSCC}类表示用于确定有向图中的强连通分量的数据类型。 <em> id </em>操作确定给定顶点位于哪个强连通子集中;
 * <em> areStronglyConnected </em>操作确定两个顶点是否在同一强子集中; 和<em> count </em>操作确定强连通子集的数量。 
 * 组件的<em>组件标识符</em>是强连通中的一个顶点：当且仅当它们在同一个强连通中时，两个顶点具有相同的组件标识符。
 * 这个实现使用Gabow的算法。 构造函数需要与<em> V </em> + <em> E </em>成正比的时间（在最坏的情况下），
 * 其中<em> V </em>是顶点的数量， <em>E</em>是边的数量。 之后，<em> id </em>，<em>计数</em>和
 * <em> areStronglyConnected </em>操作需要固定时间。 
 * 对于同一API的其他实现，请参阅{@link KosarajuSharirSCC}和{@link TarjanSCC}。
 * @author houxu_000 20170224
 * */
public class GabowSCC {
	private boolean[] marked;				// marked[v] = has v been visited?
	private int[] id;						// id[v] = id of strong component containing v
	private int[] preorder;					// preorder[v] = preorder of v
	private int pre;						// preorder number counter
	private int count;						// number of strongly-connected components
	private Stack<Integer> stack1,stack2;
	
	public GabowSCC(Digraph G) {
		marked = new boolean[G.V()];
		stack1 = new Stack<Integer>();
		stack2 = new Stack<Integer>();
		id = new int[G.V()];
		preorder = new int[G.V()];
		for (int v = 0; v < G.V(); v++)
			id[v] = -1;
		
		for (int v = 0; v < G.V(); v++)
			if (!marked[v]) dfs(G, v);
		
		assert check(G);
	}

	private void dfs(Digraph G, int v) {
		marked[v] = true;
		preorder[v] = pre++;
		stack1.push(v);
		stack2.push(v);
		for (int w : G.adj(v)) {
			if (!marked[w])		dfs(G, w);
			else	if (id[w] == -1) {
				while (preorder[stack2.peak()] > preorder[w])
					stack2.pop();
			}
		}
		
		if(stack2.peak() == v) {
			stack2.pop();
			int w;
			do {
				w = stack1.pop();
				id[w] = count;
			}while (w != v);
			count++;
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
		return id[w] == id[v];
	}
	
	/**
     * Returns the component id of the strong component containing vertex {@code v}.
     * @param  v the vertex
     * @return the component id of the strong component containing vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
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
//    	TransitiveClosure闭包传递的缺点 对于大型图的构建有缺陷
		TransitiveClosure tc = new TransitiveClosure(G);
	    	for (int v = 0; v < G.V(); v++) {
	    		for (int w = 0; w < G.V(); w++) {
	    			if (stronglyConnected(v, w) != (tc.reachable(v, w) && tc.reachable(w, v)))
	    				return false;
	            }
	        }
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
				System.out.println(G);
				GabowSCC scc = new GabowSCC(G);
				
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
