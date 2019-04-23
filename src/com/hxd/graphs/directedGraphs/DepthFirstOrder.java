package com.hxd.graphs.directedGraphs;

/**
 *	  v    pre    post
===========
   0    0    5
   1    5    4
   2    4    0
   3    3    1
   4    2    2
   5    1    3
   6    6   11
   7   12   12
   8   11   10
   9    7    9
  10   10    8
  11    8    7
  12    9    6
PreOrder :   
0 5 4 3 2 1 6 9 11 12 10 8 7 
PostOrder  :   2 3 4 5 1 0 12 11 10 9 8 6 7 
Reverse postOrder  :   7 6 8 9 10 11 12 0 1 5 4 3 2  
 */

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Queue;
import com.hxd.base.Stack;
import com.hxd.graphs.sp.DirectedEdge;
import com.hxd.graphs.sp.EdgeWeightedDigraph;
import com.hxd.introcs.stdlib.In;

/**
 *	处理有向无环图所产生的轨迹,它的思想是深度优先搜索正好只会访问每个顶点一次,将dfs()的参数顶点保存在一个
 *	数据结构中,遍历这个数据结构实际上就能访问图中的所有顶点
 *	{@code DepthFirstOrder}类表示构造函数需要与V+E
 *	（在最坏情况下）成正比的时间，其中V是顶点的数量，E是边的数量。 之后，前序，后序和反向后序
 *	操作花费与V成正比的时间。
 *	@author 候旭东 20170220
 * */

public class DepthFirstOrder {
	private boolean[] marked;
	private int[] pre;					//pre[v]    = preorder  number of v
	private int[] post;					//post[v]   = postorder number of v
	private Queue<Integer> preorder;	//vertices in preorder	前序: 在递归调用之前将顶点加入队列
	private Queue<Integer> postorder;	//vertices in postorder 后序: 在递归调用之后将顶点加入队列
	private int preCounter;				//counter or preorder numbering 前序时顶点V被访问的顺序
	private int postCounter;			//counter for postorder numbering 后序时顶点V被访问的顺序
	
	/**
     * Determines a depth-first order for the digraph {@code G}.
     * @param G the digraph
     */
	public DepthFirstOrder(Digraph G) {
		pre = new int[G.V()];
		post = new int[G.V()];
		postorder = new Queue<Integer>();
		preorder = new Queue<Integer>();
		marked = new boolean[G.V()];
		for (int v = 0; v < G.V(); v++)
			if (!marked[v])
				dfs(G, v);
		assert check();
	}
	
	public DepthFirstOrder(EdgeWeightedDigraph G) {
		pre = new int[G.V()];
		post = new int[G.V()];
		postorder = new Queue<Integer>();
		preorder = new Queue<Integer>();
		marked = new boolean[G.V()];
		for (int v = 0; v < G.V(); v++)
			if (!marked[v])
				dfs(G, v);
	}

	private void dfs(EdgeWeightedDigraph G, int v) {
		marked[v] = true;
		pre[v] = preCounter++;
		preorder.enqueue(v);
		for (DirectedEdge e : G.adj(v)) {
			if (!marked[e.to()])
				dfs(G, e.to());
		}
		postorder.enqueue(v);
		post[v] = postCounter++;
	}

	private void dfs(Digraph G, int v) {
		marked[v] = true;
		pre[v] = preCounter++;
		preorder.enqueue(v);
		for (int w : G.adj(v)) {
			if (!marked[w])
				dfs(G, w);
		}
		postorder.enqueue(v);
		post[v] = postCounter++;
	}
	
	/**
     * Returns the preorder number of vertex {@code v}.
     * @param  v the vertex
     * @return the preorder number of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public int pre(int v) {
		validateVertex(v);
		return pre[v];
	}
	
	/**
     * Returns the postorder number of vertex {@code v}.
     * @param  v the vertex
     * @return the postorder number of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public int post(int v) {
		validateVertex(v);
		return post[v];
	}
	
	/**
     * Returns the vertices in postorder.
     * @return the vertices in postorder, as an iterable of vertices
     */
	public Iterable<Integer> post() {
		return postorder;
	}
	
	/**
     * Returns the vertices in preorder.
     * @return the vertices in preorder, as an iterable of vertices
     */
	public Iterable<Integer> pre() {
		return preorder;
	}
	/**
	 * 逆后序: 在递归调用之后将顶点压入栈
	 * Returns the vertices in reverse postorder.
     * @return the vertices in reverse postorder, as an iterable of vertices
     */
	public Iterable<Integer> reversePost() {
		Stack<Integer> reverse = new Stack<Integer>();
		for (int v : postorder)
			reverse.push(v);
		return reverse;
	}
	
	private void validateVertex(int v) {
		int V = marked.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}
	
	private boolean check() {
		int r = 0;
		for (int v : post()) {
			if (post(v) != r) {
				System.out.println("post(v) and post() inconsistent");
				return false;
			}
			r++;
		}
		r = 0;
		for (int v : post) {
			if (pre(v) != r) {
				System.out.println("pre(v) and pre() inconsistent");
				return false;
			}
			r++;
		}
		return true;
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{
//					new File(".\\algs4-data\\tinyDAG.txt"),
					new File(".\\algs4-data\\tinyDG.txt"),
					};
			for(File file : files) {
				In in = new In(file);
				Digraph G = new Digraph(in);
				DepthFirstOrder dfs = new DepthFirstOrder(G.reverse());
				System.out.println("  v    pre    post");
				System.out.println("===========");
				for (int v = 0; v < G.V(); v++)
					System.out.printf("%4d %4d %4d\n",v,dfs.pre(v),dfs.post(v));
				System.out.print("PreOrder :   ");
				for (int v : dfs.pre())
					System.out.print(v + " ");
				System.out.println();
				System.out.print("PostOrder  :   ");
				for (int v : dfs.post())
					System.out.print(v + " ");
				System.out.println();
				System.out.print("Reverse postOrder  :   ");
				for (int v : dfs.reversePost())
					System.out.print(v + " ");
				System.out.println();
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
