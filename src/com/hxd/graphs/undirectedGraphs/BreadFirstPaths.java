package com.hxd.graphs.undirectedGraphs;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Queue;
import com.hxd.base.Stack;
import com.hxd.introcs.stdlib.In;

/**
 * 	单点最短路径问题时深度优先搜索在这个问题上就没有什么作为,遍历整个图的顺序和找出最短路径的目标没有任何关系
 * 	要找到从s到v的最短路径,从s开始,在所有有一条边就可以到达的顶点中寻找v,如果找不到就继续在于s距离两条边的
 * 	所有顶点中查找v,如此一直进行.在搜索一副图时遇到多条边需要遍历的情况时,会选择其中一条并将其他通道留到以后
 * 	再继续搜索.按照与起点的距离的顺序来遍历所有顶点,看来这种顺序很容易实现:使用FIFO队列来代替LIFO栈即可
 * 	<p>
 * 	The {@code BreadthFirstPaths} class represents a data type for finding
 *  shortest paths (number of edges) from a source vertex <em>s</em>
 *  (or a set of source vertices)
 *  to every other vertex in an undirected graph.
 *  <p>
 *  This implementation uses breadth-first search.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  It uses extra space (not including the graph) proportional to <em>V</em>.
 *  <p>
 *  实现广度优先搜索使用了一个队列来保存所有已经被标记过但其邻接表还未被检查过的顶点,先将起点加入队列,然后重复以下步骤
 *  直至队列为空;
 *  取队列中的下一个顶点v并标记过它;
 *  将于v相邻的所有未被标记过的的 顶点加入队列
 *  
 *  <p>
 * 	@author 候旭东 20170216
 * */

public class BreadFirstPaths {
	private static final int INFINITY = Integer.MAX_VALUE;
	private boolean[] marked;		//达到该顶点的最短路径已知吗?
	private int[] edgeTo;			//达到该顶点的已知路径上的最后一个顶点
	private int[] distTo;			//边数最短S-V路径
	
	/**
     * Computes the shortest path between the source vertex {@code s}
     * and every other vertex in the graph {@code G}.
     * @param G the graph
     * @param s the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
	public BreadFirstPaths(Graph G,int s) {
		marked = new boolean[G.V()];
		distTo = new int[G.V()];
		edgeTo = new int[G.V()];
		validateVertex(s);
		bfs(G,s);
		
		assert check(G,s);
	}
	/**
     * Computes the shortest path between any one of the source vertices in {@code sources}
     * and every other vertex in graph {@code G}.
     * @param G the graph
     * @param sources the source vertices
     * @throws IllegalArgumentException unless {@code 0 <= s < V} for each vertex
     *         {@code s} in {@code sources}
     */
	public BreadFirstPaths(Graph G,Iterable<Integer> sources) {
		marked = new boolean[G.V()];
		distTo = new int[G.V()];
		edgeTo = new int[G.V()];
		for (int v = 0; v<G.V(); v++)
			distTo[v] = INFINITY;
		validateVertices(sources);
		bfs(G, sources);
	}

	/**
	 * bfs不是递归的.不像递归中的隐式的使用的栈,它显示地使用了一个队列,结果也存到一个数组
	 * edgeTo[],也是一颗用父链接表示的根结点为s的树.表示了s到每个与s连通的顶点的最短路径
	 * */
	private void bfs(Graph G, int s) {
		Queue<Integer> q = new Queue<Integer>();
		for (int v=0; v<G.V(); v++)
			distTo[v] = INFINITY;
		distTo[s] = 0;
		marked[s] = true;
		q.enqueue(s);
		
		while (!q.isEmpty()) {
			int v = q.dequeue();
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					marked[w] = true;
					q.enqueue(w);
				}
			}
		}
	}
	
	private void bfs(Graph G, Iterable<Integer> sources) {
		Queue<Integer> q = new Queue<Integer>();
		for (int s : sources) {
			marked[s] = true;
			distTo[s] = 0;
			q.enqueue(s);
		}
		
		while (!q.isEmpty()) {
			int v = q.dequeue();
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					marked[w] =true;
					q.enqueue(w);
				}
			}
		}
	}
	
	/**
	 * 在源顶点{@code s}（或源）和顶点{@code v}之间是否有一个路径？
     * Is there a path between the source vertex {@code s} (or sources) and vertex {@code v}?
     * @param v the vertex
     * @return {@code true} if there is a path, and {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public boolean hasPathTo(int v) {
		validateVertex(v);
		return marked[v];
	}
	
	/**
	 * 返回源顶点之间最短路径中的边数{@code s}
     * Returns the number of edges in a shortest path between the source vertex {@code s}
     * (or sources) and vertex {@code v}?
     * @param v the vertex
     * @return the number of edges in a shortest path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public int distTo(int v) {
		validateVertex(v);
		return distTo[v];
	}
	
	public Iterable<Integer> pathTo(int v) {
		validateVertex(v);
		if (!hasPathTo(v))
			return null;
		Stack<Integer> path = new Stack<Integer>();
		int x;
		for (x = v; distTo[x] != 0; x = edgeTo[x])
			path.push(x);
		path.push(x);
		return path;
	}
	
	// check optimality conditions for single source
    private boolean check(Graph G, int s) {

        // check that the distance of s = 0
        if (distTo[s] != 0) {
            System.out.println("distance of source " + s + " to itself = " + distTo[s]);
            return false;
        }

        // check that for each edge v-w dist[w] <= dist[v] + 1
        // provided v is reachable from s
        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj(v)) {
                if (hasPathTo(v) != hasPathTo(w)) {
                    System.out.println("edge " + v + "-" + w);
                    System.out.println("hasPathTo(" + v + ") = " + hasPathTo(v));
                    System.out.println("hasPathTo(" + w + ") = " + hasPathTo(w));
                    return false;
                }
                if (hasPathTo(v) && (distTo[w] > distTo[v] + 1)) {
                    System.out.println("edge " + v + "-" + w);
                    System.out.println("distTo[" + v + "] = " + distTo[v]);
                    System.out.println("distTo[" + w + "] = " + distTo[w]);
                    return false;
                }
            }
        }

        // check that v = edgeTo[w] satisfies distTo[w] = distTo[v] + 1
        // provided v is reachable from s
        for (int w = 0; w < G.V(); w++) {
            if (!hasPathTo(w) || w == s) continue;
            int v = edgeTo[w];
            if (distTo[w] != distTo[v] + 1) {
                System.out.println("shortest path edge " + v + "-" + w);
                System.out.println("distTo[" + v + "] = " + distTo[v]);
                System.out.println("distTo[" + w + "] = " + distTo[w]);
                return false;
            }
        }
        return true;
    }

	private void validateVertices(Iterable<Integer> vertices) {
		if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int V = marked.length;
        for (int v : vertices) {
            if (v < 0 || v >= V) {
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
            }
        }
	}

	private void validateVertex(int v) {
		int V = marked.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	/**
     * Unit tests the {@code BreadthFirstPaths} data type.
     *
     * @param args the command-line arguments
     */
	public static void main(String[] args) {
		try {
			File file1 = new File(".\\algs4-data\\tinyG.txt");
			In in = new In(file1);
			Graph G = new Graph(in);
			
			for(int j = 0 ; j < G.V(); j+=2) {
				BreadFirstPaths bfs = new BreadFirstPaths(G, j);
				for (int v = 0; v < G.V(); v++) {
					if (bfs.hasPathTo(v)) {
						System.out.printf("%d to %d (%d)",j,v,bfs.distTo(v));
						for (int x : bfs.pathTo(v)) {
							if (x == j)		System.out.print(x);
							else			System.out.print("-"+x);
						}
						System.out.println();
					}
					else {
						System.out.printf("%d to %d (-):  not connected\n", j, v);
					}
				}
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
