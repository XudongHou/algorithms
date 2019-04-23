package com.hxd.graphs.undirectedGraphs;
/******************************************************************************
 *  Compilation:  javac CC.java
 *  Execution:    java CC filename.txt
 *  Dependencies: Graph.java StdOut.java Queue.java
 *  Data files:   http://algs4.cs.princeton.edu/41graph/tinyG.txt
 *                http://algs4.cs.princeton.edu/41graph/mediumG.txt
 *                http://algs4.cs.princeton.edu/41graph/largeG.txt
 *
 *  Compute connected components using depth first search.
 *  Runs in O(E + V) time.
 *
 *  % java CC tinyG.txt
 *  3 components
 *  0 1 2 3 4 5 6
 *  7 8 
 *  9 10 11 12
 *
 *  % java CC mediumG.txt 
 *  1 components
 *  0 1 2 3 4 5 6 7 8 9 10 ...
 *
 *  % java -Xss50m CC largeG.txt 
 *  1 components
 *  0 1 2 3 4 5 6 7 8 9 10 ...
 *
 *  Note: 此实现使用递归DFS。避免需要可能非常大
 *  的堆栈大小，替换为非递归 DFS NonrecursiveDFS.java。
 *
 ******************************************************************************/

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Queue;
import com.hxd.introcs.stdlib.In;

/**
 * 	连通分量,深度优先搜索的下一个直接应用就是找到一幅图的所有连通分量.
 * 	The {@code CC} class represents a data type for 
 *  determining the connected components in an undirected graph.
 *  The <em>id</em> operation determines in which connected component
 *  a given vertex lies; the <em>connected</em> operation
 *  determines whether two vertices are in the same connected component;
 *  the <em>count</em> operation determines the number of connected
 *  components; and the <em>size</em> operation determines the number
 *  of vertices in the connect component containing a given vertex.
 *  The <em>component identifier</em> of a connected component is one of the
 *  vertices in the connected component: two vertices have the same component
 *  identifier if and only if they are in the same connected component.
 *  <p>
 *  This implementation uses depth-first search.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>
 *  (in the worst case),
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Afterwards, the <em>id</em>, <em>count</em>, <em>connected</em>,
 *  and <em>size</em> operations take constant time.
 *  <p>
 *  CC的实现
 *  <p>
 * 	@author 候旭东 20170216
 * */

public class CC {
	private boolean[] marked;		//标记为[V] =顶点v已经被标记？
	private int[] id;				//id [v] =包含v的连接组件的id
	private int[] size;				//size [id] =给定组件中的顶点数
	private int count;				//连接组件的数量
	
	/**
	 * 计算无向图的连接分量
     * Computes the connected components of the undirected graph {@code G}.
     *
     * @param G the undirected graph
     */
	public CC(Graph G) {
		marked = new boolean[G.V()];
		id = new int[G.V()];
		size = new int[G.V()];
		for (int v = 0; v < G.V(); v++) {
			if (!marked[v]) {
				dfs(G,v);
				count++;
			}
		}
	}

	private void dfs(Graph G, int v) {
		marked[v] = true;
		id [v] = count;
		size[count]++;
		for (int w : G.adj(v)) {
			if (!marked[w])
				dfs(G, w);
		}
	}
	
	/**
	 * 返回包含顶点{@code v}的连接组件的组件ID
     * Returns the component id of the connected component containing vertex {@code v}.
     *
     * @param  v the vertex
     * @return the component id of the connected component containing vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public  int id(int v) {
		validateVertex(v);
		return id[v];
	}

	/**
	 * 返回包含顶点{@code v}的连接组件中的顶点数。
     * Returns the number of vertices in the connected component containing vertex {@code v}.
     *
     * @param  v the vertex
     * @return the number of vertices in the connected component containing vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public int size(int v) {
		validateVertex(v);
		return size[id[v]];
	}
	
	/**
	 * 返回图中{@code G}中连接的组件的数量
     * Returns the number of connected components in the graph {@code G}.
     *
     * @return the number of connected components in the graph {@code G}
     */
    public int count() {
        return count;
    }
	
	/**
	 * 是否顶点{@code v}和{@code w}连通，则返回true
     * Returns true if vertices {@code v} and {@code w} are in the same
     * connected component.
     *
     * @param  v one vertex
     * @param  w the other vertex
     * @return {@code true} if vertices {@code v} and {@code w} are in the same
     *         connected component; {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     * @throws IllegalArgumentException unless {@code 0 <= w < V}
     */
	public boolean connected(int v,int w) {
		validateVertex(v);
		validateVertex(w);
		return id(v) == id(w);
	}
	
	private void validateVertex(int v) {
		int V = marked.length;
		 if (v < 0 || v >= V)
	            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			File file1 = new File(".\\algs4-data\\tinyG.txt");
			In in = new In(file1);
			Graph G = new Graph(in); 
			CC cc = new CC(G);
			
			int m = cc.count();
			System.out.println(m + " components");
			Queue<Integer>[] components = (Queue<Integer>[]) new Queue[m];
			for (int i = 0; i<m; i++)
				components[i] = new Queue<Integer>();
			for (int v = 0; v<G.V(); v++)
				components[cc.id(v)].enqueue(v);
			
			for (int i = 0; i<m; i++) {
				for (int v : components[i])
					System.out.print(v + " ");
				System.out.println();
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
