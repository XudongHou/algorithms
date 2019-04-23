package com.hxd.graphs.directedGraphs;

import com.hxd.introcs.SET;
import com.hxd.introcs.stdlib.StdRandom;

/**
 * {@code DigraphGenerator}类提供用于创建各种有向图的静态方法，包括Erdos-Renyi随机二进制图，
 * 随机DAG，随机根树，随机根DAG，随机锦标赛，路径二字图，循环二字图和完整的图。
 * @author 候旭东 20170222
 * */

public class DigraphGenerator {
	private static final class Edge implements Comparable<Edge> {
		
		private final int v, w;
		
		private Edge(int v, int w) {
			this.v = v;
			this.w = w;
		}
		
		@Override
		public int compareTo(Edge that) {
			if (this.v < that.v) return -1;
			if (this.v > that.v) return +1;
			if (this.w < that.w) return -1;
			if (this.w > that.w) return +1;
			return 0;
		}
		
	}
	
	private DigraphGenerator() {}
	
	/**
     * 返回一个包含{@code V}个顶点和{@code E}条边的随机简单有向图。
     * Returns a random simple digraph containing {@code V} vertices and {@code E} edges.
     * @param V the number of vertices
     * @param E the number of vertices
     * @return a random simple digraph on {@code V} vertices, containing a total
     *     of {@code E} edges
     * @throws IllegalArgumentException if no such simple digraph exists
     */
	public static Digraph simple(int V, int E) {
		if (E > (long) V*(V-1))	throw new IllegalArgumentException("Too many edges");
		if (E < 0)				throw new IllegalArgumentException("Too few edges");
		Digraph G = new Digraph(V); 
		SET<Edge> set = new SET<Edge>();
		while (G.E() < E) {
			int v = StdRandom.uniform(V);
			int w = StdRandom.uniform(V);
			Edge e = new Edge(v, w);
			if ((v != w) && !set.contains(e)) {
				set.add(e);
				G.addEdge(v, w);
			}
		}
		return G;
	}
	
	/**
	 * 在{@code V}顶点返回一个随机简单的有向图，任何两个顶点之间的边以概率{@code p}。 
	 * 这有时被称为Erdos-Renyi随机有向图模型。 这种实现需要花费与V ^ 2相关的时间（即使{@code p}很小）。
     * Returns a random simple digraph on {@code V} vertices, with an 
     * edge between any two vertices with probability {@code p}. This is sometimes
     * referred to as the Erdos-Renyi random digraph model.
     * This implementations takes time propotional to V^2 (even if {@code p} is small).
     * @param V the number of vertices
     * @param p the probability of choosing an edge
     * @return a random simple digraph on {@code V} vertices, with an edge between
     *     any two vertices with probability {@code p}
     * @throws IllegalArgumentException if probability is not between 0 and 1
     */
	public static Digraph simple(int V, double p) {
		if (p < 0.0 || p > 1.0)
			throw new IllegalArgumentException("Probability must be between 0 and 1");
		Digraph G = new Digraph(V);
		for (int v = 0; v < V; v++)
			for (int w = 0; w < V; w++)
				if (v != w)
					if(StdRandom.bernoulli(p))
						G.addEdge(v, w);
		return G;
	}
	
	/**
	 * 返回{@code V}顶点上的完全稠密图。
     * Returns the complete digraph on {@code V} vertices.
     * @param V the number of vertices
     * @return the complete digraph on {@code V} vertices
     */
	public static Digraph complete(int V) {
		return simple(V, V*(V-1));
	}
	
	/**
     * Returns a random simple DAG containing {@code V} vertices and {@code E} edges.
     * Note: it is not uniformly selected at random among all such DAGs.
     * @param V the number of vertices
     * @param E the number of vertices
     * @return a random simple DAG on {@code V} vertices, containing a total
     *     of {@code E} edges
     * @throws IllegalArgumentException if no such simple DAG exists
     */
	public static Digraph dag(int V, int E) {
		if (E > (long) V*(V-1)/2 )	throw new IllegalArgumentException("Too many edges");
		if (E < 0)					throw new IllegalArgumentException("Too few edges");
		Digraph G = new Digraph(V);
		SET<Edge> set = new SET<Edge>();
		int[] vertices = new int[V];
		for (int i = 0; i < V; i++)
			vertices[i] = i;
		StdRandom.shuffle(vertices);
		while (G.E() < E) {
			int v = StdRandom.uniform(V);
			int w = StdRandom.uniform(V);
			Edge e = new Edge(v, w);
			if ((v < w) && !set.contains(e)) {
				set.add(e);
				G.addEdge(vertices[v], vertices[w]);
			}
		}
		return G;
	}
	
	/**
	 * 在{@code V}顶点返回随机锦标赛图。 锦标赛图是一个DAG，其中对于每两个顶点，有一个有向边。 比赛是一个定向的完整图。
     * Returns a random tournament digraph on {@code V} vertices. A tournament digraph
     * is a DAG in which for every two vertices, there is one directed edge.
     * A tournament is an oriented complete graph.
     * @param V the number of vertices
     * @return a random tournament digraph on {@code V} vertices
     */
	public static Digraph tournament(int V) {
		Digraph G = new Digraph(V);
		for (int v = 0; v < G.V(); v++) 
			for (int w = 1+v; w < G.V(); w ++){
				if (StdRandom.bernoulli(0.5))	G.addEdge(v, w);
				else							G.addEdge(w, v);
			}
		return G;
	}
	
	/**
	 * 返回在{@code V}顶点和{@code E}边上的随机导入DAG。 根树是一个DAG，其中有一个单独的顶点可以从每个其他顶点到达。 
	 * 返回的DAG不是在所有这样的DAG中随机均匀地选择的。
	 * Returns a random rooted-in DAG on {@code V} vertices and {@code E} edges.
     * A rooted in-tree is a DAG in which there is a single vertex
     * reachable from every other vertex.
     * The DAG returned is not chosen uniformly at random among all such DAGs.
     * @param V the number of vertices
     * @param E the number of edges
     * @return a random rooted-in DAG on {@code V} vertices and {@code E} edges
	 * */
	public static Digraph rootedInDAG(int V, int E) {
		if (E > (long) V*(V-1) / 2) throw new IllegalArgumentException("Too many edges");
        if (E < V-1)                throw new IllegalArgumentException("Too few edges");
        Digraph G = new Digraph(V);
        SET<Edge> set = new SET<Edge>();
        // fix a topological order
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++)
        	vertices[i] = i;
        StdRandom.shuffle(vertices);
        
        // one edge pointing from each vertex, other than the root = vertices[V-1]从每个顶点指向的一个边，除了根=顶点[V-1]
        for (int v = 0; v < V-1; v++) {
        	int w = StdRandom.uniform(v+1, V);
        	Edge e = new Edge(v, w);
        	set.add(e);
        	G.addEdge(vertices[v], vertices[w]);
        }
        while (G.E() < E) {
        	int v = StdRandom.uniform(V);
        	int w = StdRandom.uniform(V);
        	Edge e = new Edge(v, w);
        	if ((v < w) && ! set.contains(e)) {
        		set.add(e);
        		G.addEdge(vertices[v], vertices[w]);
        	}
        }
        
        return G;
	}
	
	/**
	 * 在{@code V}顶点和{@code E}条边中返回一个随机根导出DAG。 根导出树是其中每个顶点可从单个顶点到达的DAG。
	 * 返回的DAG不是在所有这样的DAG中随机均匀地选择的。
     * Returns a random rooted-out DAG on {@code V} vertices and {@code E} edges.
     * A rooted out-tree is a DAG in which every vertex is reachable from a
     * single vertex.
     * The DAG returned is not chosen uniformly at random among all such DAGs.
     * @param V the number of vertices
     * @param E the number of edges
     * @return a random rooted-out DAG on {@code V} vertices and {@code E} edges
     */
	public static Digraph rootedOutDAG(int V, int E) {
		if (E > (long) V*(V-1) / 2) throw new IllegalArgumentException("Too many edges");
        if (E < V-1)                throw new IllegalArgumentException("Too few edges");
        Digraph G = new Digraph(V);
        SET<Edge> set = new SET<Edge>();
        
        int[] vertices = new int[V];
        for (int i = 0; i < V; i++)
        	vertices[i] = i;
        StdRandom.shuffle(vertices);
        
        //从每个顶点指向的一个边，除了root = vertices [V-1]
        for (int v = 0; v < V-1; v++) {
        	int w = StdRandom.uniform(v+1, V);
        	Edge e = new Edge(w, v);
        	set.add(e);
        	G.addEdge(vertices[w], vertices[v]);
        }
        
      
        while (G.E() < E) {
        	int v = StdRandom.uniform(V);
        	int w = StdRandom.uniform(V);
        	Edge e = new Edge(w, v);
        	if ((v < w) && !set.contains(e)) {
        		set.add(e);
        		G.addEdge(vertices[w], vertices[v]);
        	}
        }
        
        return G;
	}
	/**
	 * 在{@code V}顶点返回一个随机的根树。 根植入树是其中存在从每个其他顶点可达的单个顶点的定向树。 
	 * 返回的树不是在所有这样的树中随机均匀地选择的。
	 * Returns a random rooted-in tree on {@code V} vertices.
     * A rooted in-tree is an oriented tree in which there is a single vertex
     * reachable from every other vertex.
     * The tree returned is not chosen uniformly at random among all such trees.
     * @param V the number of vertices
     * @return a random rooted-in tree on {@code V} vertices
	 * */
	public static Digraph rootedInTree(int V) {
		return rootedInDAG(V, V-1);
	}
	
	/**
	 * 在{@code V}顶点返回一个随机rooted-out树。 根导出的树是其中每个顶点可从单个顶点到达的定向树。 
	 * 它也被称为树枝状或分支。 返回的树不是在所有这样的树中随机均匀地选择的。
	 * Returns a random rooted-out tree on {@code V} vertices. A rooted out-tree
     * is an oriented tree in which each vertex is reachable from a single vertex.
     * It is also known as a <em>arborescence</em> or <em>branching</em>.
     * The tree returned is not chosen uniformly at random among all such trees.
     * @param V the number of vertices
     * @return a random rooted-out tree on {@code V} vertices
     */
	public static Digraph rootedOutTree(int V) {
		return rootedOutDAG(V, V-1);
	}
	
	/**
	 * 返回{@code V}顶点上的路径图
	 * Returns a path digraph on {@code V} vertices.
     * @param V the number of vertices in the path
     * @return a digraph that is a directed path on {@code V} vertices
	 * */
	public static Digraph path(int V) {
		Digraph G = new Digraph(V);
		int[] vertices = new int[V];
		for (int i = 0; i < V; i++)
			vertices[i] = i;
		StdRandom.shuffle(vertices);
		for (int i = 0; i < V-1; i++)
			G.addEdge(vertices[i], vertices[i+1]);
		return G;
	}
	
	/**
	 * 在{@code V}顶点返回一个完整的二叉树图。
     * Returns a complete binary tree digraph on {@code V} vertices.
     * @param V the number of vertices in the binary tree
     * @return a digraph that is a complete binary tree on {@code V} vertices
     */
	public static Digraph binaryTree(int V) {
		Digraph G = new Digraph(V);
		int[] vertices = new int[V];
		for (int i = 0; i < V; i++)
			vertices[i] = i;
		StdRandom.shuffle(vertices);
		for (int i = 1; i < V; i++)
			G.addEdge(vertices[i], vertices[(i-1)/2]);
		return G;
	}
	
	/**
     * Returns a cycle digraph on {@code V} vertices.
     * @param V the number of vertices in the cycle
     * @return a digraph that is a directed cycle on {@code V} vertices
     */
	public static Digraph cycle(int V) {
		Digraph G = new Digraph(V);
		int[] vertices = new int[V];
		for (int i = 0; i < V; i++)
			vertices[i] = i;
		StdRandom.shuffle(vertices);
		for (int i = 0; i < V-1; i++)
			G.addEdge(vertices[i], vertices[i+1]);
		G.addEdge(vertices[V-1], vertices[0]);
		return G;
	}
	
	/**
	 * 在{@code V}顶点上返回欧拉循环图。
     * Returns an Eulerian cycle digraph on {@code V} vertices.
     *
     * @param  V the number of vertices in the cycle
     * @param  E the number of edges in the cycle
     * @return a digraph that is a directed Eulerian cycle on {@code V} vertices
     *         and {@code E} edges
     * @throws IllegalArgumentException if either {@code V <= 0} or {@code E <= 0}
     */
	public static Digraph eulerianCycle(int V, int E) {
		if (E <= 0)
            throw new IllegalArgumentException("An Eulerian cycle must have at least one edge");
        if (V <= 0)
            throw new IllegalArgumentException("An Eulerian cycle must have at least one vertex");
        Digraph G = new Digraph(V);
        int[] vertices = new int[E];
        for (int i = 0; i < E; i++)
        	vertices[i] = StdRandom.uniform(V);
        for (int i = 0; i < E-1; i++)
        	G.addEdge(vertices[i], vertices[i+1]);
        G.addEdge(vertices[E-1], vertices[0]);
        return G;
	}
	
	/**
     * Returns an Eulerian path digraph on {@code V} vertices.
     *
     * @param  V the number of vertices in the path
     * @param  E the number of edges in the path
     * @return a digraph that is a directed Eulerian path on {@code V} vertices
     *         and {@code E} edges
     * @throws IllegalArgumentException if either {@code V <= 0} or {@code E < 0}
     */
	public static Digraph eulerianPath(int V, int E) {
		if (E < 0)
            throw new IllegalArgumentException("negative number of edges");
        if (V <= 0)
            throw new IllegalArgumentException("An Eulerian path must have at least one vertex");
        Digraph G = new Digraph(V);
        int[] vertices = new int[E+1];
        for (int i = 0; i < E+1; i++)
        	vertices[i] = StdRandom.uniform(V);
        for (int i = 0; i < E; i++)
        	G.addEdge(vertices[i], vertices[i+1]);
        return G;
	}
	
	 /**
     * Returns a random simple digraph on {@code V} vertices, {@code E}
     * edges and (at least) {@code c} strong components. The vertices are randomly
     * assigned integer labels between {@code 0} and {@code c-1} (corresponding to 
     * strong components). Then, a strong component is creates among the vertices
     * with the same label. Next, random edges (either between two vertices with
     * the same labels or from a vetex with a smaller label to a vertex with a 
     * larger label). The number of components will be equal to the number of
     * distinct labels that are assigned to vertices.
     *
     * @param V the number of vertices
     * @param E the number of edges
     * @param c the (maximum) number of strong components
     * @return a random simple digraph on {@code V} vertices and
               {@code E} edges, with (at most) {@code c} strong components
     * @throws IllegalArgumentException if {@code c} is larger than {@code V}
     */
	
	public static Digraph strong(int V, int E, int c) {
		 if (c >= V || c <= 0)
			 throw new IllegalArgumentException("Number of components must be between 1 and V");
		 if (E <= 2*(V-c))
			 throw new IllegalArgumentException("Number of edges must be at least 2(V-c)");
		 if (E > (long) V*(V-1) / 2)
			 throw new IllegalArgumentException("Too many edges");
		 
		 Digraph G = new Digraph(V);
		 
		 SET<Edge> set = new SET<Edge>();
		 
		 int[] label = new int[V];
		 for (int v = 0; v < V; v++)
			 label[v] = StdRandom.uniform(c);
		 //使所有带有标签c的顶点成为强连通 
		 //连通根树和根树
		 for (int i = 0; i < c; i++) {
			 int count = 0;
			 for (int v = 0; v < G.V(); v++)
				 if (label[v] == i)	count++;
			 
			 int[] vertices = new int[count];
			 int j = 0;
			 for (int v = 0; v < V; v++)
				 if (label[v] == i) vertices[j++] = v;
			 StdRandom.shuffle(vertices);
			 
			 for (int v = 0; v < count-1; v++) {
				 int w = StdRandom.uniform(v+1, count);
				 Edge e = new Edge(w, v);
				 set.add(e);
				 G.addEdge(vertices[w], vertices[v]);
			 }
			 
			 for (int v = 0; v < count-1; v++) {
				 int w = StdRandom.uniform(v+1, count);
				 Edge e = new Edge(v, w);
				 set.add(e);
				 G.addEdge(vertices[v], vertices[w]);
			 }
		 }
		 while (G.E() < E) {
			 int v = StdRandom.uniform(V);
			 int w = StdRandom.uniform(V);
			 Edge e = new Edge(v, w);
			 if (!set.contains(e) && v != w && label[v] <= label[w]) {
				 set.add(e);
				 G.addEdge(v, w);
			 }
		 }
		 return G;
	}
	
	public static void main(String[] args) {
		int V = 13;
        int E = 22;
        System.out.println("complete graph");
        System.out.println(complete(V));
        System.out.println();

        System.out.println("simple");
        System.out.println(simple(V, E));
        System.out.println();

        System.out.println("path");
        System.out.println(path(V));
        System.out.println();

        System.out.println("cycle");
        System.out.println(cycle(V));
        System.out.println();

        System.out.println("Eulierian path");
        System.out.println(eulerianPath(V, E));
        System.out.println();

        System.out.println("Eulierian cycle");
        System.out.println(eulerianCycle(V, E));
        System.out.println();

        System.out.println("binary tree");
        System.out.println(binaryTree(V));
        System.out.println();

        System.out.println("tournament");
        System.out.println(tournament(V));
        System.out.println();

        System.out.println("DAG");
        System.out.println(dag(V, E));
        System.out.println();

        System.out.println("rooted-in DAG");
        System.out.println(rootedInDAG(V, E));
        System.out.println();

        System.out.println("rooted-out DAG");
        System.out.println(rootedOutDAG(V, E));
        System.out.println();

        System.out.println("rooted-in tree");
        System.out.println(rootedInTree(V));
        System.out.println();

        System.out.println("rooted-out DAG");
        System.out.println(rootedOutTree(V));
        System.out.println();
	}
}
