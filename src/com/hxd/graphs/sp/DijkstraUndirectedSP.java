package com.hxd.graphs.sp;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Stack;
import com.hxd.graphs.mst.Edge;
import com.hxd.graphs.mst.EdgeWeightedGraph;
import com.hxd.introcs.stdlib.In;
import com.hxd.sort.priorityQueue.IndexMinPQ;

/**
 * 使用Dijkstra算法解决非负权重的无向图中单源最短路径问题。
 * {@code DijkstraUndirectedSP}类表示用于求解边缘权重图中的单源最短路径问题的数据类型，其中边权重是非负的。
 * 这个实现使用Dijkstra的算法和二进制堆。 构造函数需要与E logV成正比的时间，其中V是顶点的数量，E是边的数量。 之
 * 后，{@code distTo（）}和{@code hasPathTo（）}方法需要一定的时间，而{@code pathTo（）}方法所花
 * 费的时间与返回的最短路径的边数成正比。
 * @author houxu_000 20170226
 * */

public class DijkstraUndirectedSP {
	private double[] distTo;		// distTo[v] = distance  of shortest s->v path
	private Edge[] edgeTo;			// edgeTo[v] = last edge on shortest s->v path
	private IndexMinPQ<Double> pq;	// priority queue of vertices
	
	/**
     * Computes a shortest-paths tree from the source vertex {@code s} to every
     * other vertex in the edge-weighted graph {@code G}.
     *
     * @param  G the edge-weighted digraph
     * @param  s the source vertex
     * @throws IllegalArgumentException if an edge weight is negative
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
	public DijkstraUndirectedSP(EdgeWeightedGraph G, int s) {
		for (Edge e : G.edges()) {
			if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
		}
		
		distTo = new double[G.V()];
		edgeTo = new Edge[G.V()];
		
		validateVertex(s);
		
		for (int v = 0; v < G.V(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;
		distTo[s] = 0.0;
		pq = new IndexMinPQ<Double>(G.V());
		pq.insert(s, distTo[s]);
		while (!pq.isEmpty()) {
			int v = pq.delMin();
			for (Edge e : G.adj(v))
				relax(e, v);
		}
		
		assert check(G, s);
	}

	

	private void relax(Edge e, int v) {
		int w = e.other(v);
		if (distTo[w] > distTo[v] + e.weight()) {
			distTo[w] = distTo[v] + e.weight();
			edgeTo[w] = e;
			if (pq.contains(w))	pq.decreaseKey(w, distTo[w]);
			else				pq.insert(w, distTo[w]);
		}
	}

	/**
     * Returns the length of a shortest path between the source vertex {@code s} and
     * vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return the length of a shortest path between the source vertex {@code s} and
     *         the vertex {@code v}; {@code Double.POSITIVE_INFINITY} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public double distTo(int v) {
		validateVertex(v);
		return distTo[v];
	}
	
	/**
     * Returns true if there is a path between the source vertex {@code s} and
     * vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return {@code true} if there is a path between the source vertex
     *         {@code s} to vertex {@code v}; {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public boolean hasPathTo(int v) {
		validateVertex(v);
		return distTo[v] < Double.POSITIVE_INFINITY;
	}
	
	/**
     * Returns a shortest path between the source vertex {@code s} and vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return a shortest path between the source vertex {@code s} and vertex {@code v};
     *         {@code null} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
	public Iterable<Edge> pathTo(int v) {
		validateVertex(v);
		if (!hasPathTo(v))	return null;
		Stack<Edge> path = new Stack<Edge>();
		int x = v;
		for (Edge edge = edgeTo[v]; edge != null; edge = edgeTo[x]) {
			path.push(edge);
			x = edge.other(x);
		}
		return path;
	} 
	
	private void validateVertex(int v) {
		int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}
	
	private boolean check(EdgeWeightedGraph G, int s) {
		// check that edge weights are nonnegative
        for (Edge e : G.edges()) {
            if (e.weight() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v-w satisfy distTo[w] <= distTo[v] + e.weight()
        for (int v = 0; v < G.V(); v++) {
            for (Edge e : G.adj(v)) {
                int w = e.other(v);
                if (distTo[v] + e.weight() < distTo[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v-w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w] == null) continue;
            Edge e = edgeTo[w];
            if (w != e.either() && w != e.other(e.either())) return false;
            int v = e.other(w);
            if (distTo[v] + e.weight() != distTo[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{
					new File(".\\algs4-data\\tinyEWG.txt"),
					new File(".\\algs4-data\\mediumEWG.txt"),
					};
			for(File file : files) {
				In in = new In(file);
				EdgeWeightedGraph G = new EdgeWeightedGraph(in);
				for (int s = 0; s < G.V(); s=s+3) {
					DijkstraUndirectedSP sp = new DijkstraUndirectedSP(G, s);
					for (int t = 0; t < G.V(); t++) {
						if (sp.hasPathTo(t)) {
							System.out.printf("%d to %d (%.2f) ",s,t,sp.distTo(t));
							for (Edge e : sp.pathTo(t))
								System.out.print(e + "  ");
							System.out.println();
						}
						else 
							System.out.printf("%d to %d   no path\n",s,t);
					}
				}
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
