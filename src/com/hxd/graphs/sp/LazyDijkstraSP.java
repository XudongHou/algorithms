package com.hxd.graphs.sp;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;
import java.util.Comparator;

import com.hxd.introcs.Stack;
import com.hxd.introcs.stdlib.In;
import com.hxd.sort.priorityQueue.MinPQ;

/**
 * 延长版的Dijkstra算法 类似LazyPrim算法
 * @author houxu_000 20170226
 * */

public class LazyDijkstraSP {
	private boolean[] marked;			// has vertex v been relaxed?
	private double[] distTo;			// distTo[v] = length of shortest s->v path
	private DirectedEdge[] edgeTo;		// edgeTo[v] = last edge on shortest s->v path
	private MinPQ<DirectedEdge> pq;		// PQ of fringe edges
	
	private class ByDistanceFromSource implements Comparator<DirectedEdge> {
		@Override
		public int compare(DirectedEdge e, DirectedEdge f) {
			double dist1 = distTo[e.from()] + e.weight();
			double dist2 = distTo[f.from()] + f.weight();
			return Double.compare(dist1, dist2);
		}
	}
	
	
	public LazyDijkstraSP(EdgeWeightedDigraph G, int s) {
		for (DirectedEdge e : G.edges()) {
			if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
		}
		
		pq = new MinPQ<DirectedEdge>(new ByDistanceFromSource());
		marked = new boolean[G.V()];
		edgeTo = new DirectedEdge[G.V()];
		distTo = new double[G.V()];
		
		for (int v = 0; v < G.V(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;
		distTo[s] = 0.0;
		relax(G, s);
		
		while(!pq.isEmpty()) {
			DirectedEdge e = pq.delMin();
			int w = e.to();
			if (!marked[w])	relax(G, w);
		}
		
		assert check(G, s);
	}




	private void relax(EdgeWeightedDigraph G, int v) {
		marked[v] = true;
		for (DirectedEdge e : G.adj(v)) {
			int w = e.to();
			if (distTo[w] > distTo[v] + e.weight()) {
				distTo[w] = distTo[v] + e.weight();
				edgeTo[w] = e;
				pq.insert(e);
			}
		}
	}
	
	public double distTo (int v) {
		validateVertex(v);
		return distTo[v];
	}
	
	public boolean hasPathTo(int v) {
		validateVertex(v);
		return marked[v];
	}

	 // return view of shortest path from s to v, null if no such path
    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }

	
	// check optimality conditions: either 
    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
	private boolean check(EdgeWeightedDigraph G, int s) {
		// all edge are nonnegative
        for (DirectedEdge e : G.edges()) {
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
                System.err.println("distTo[s] and edgeTo[s] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy dist[w] <= dist[v] + e.weight()
        for (int v = 0; v < G.V(); v++) {
            for (DirectedEdge e : G.adj(v)) {
                int w = e.to();
                if (distTo[v] + e.weight() < distTo[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy dist[w] == dist[v] + e.weight()
        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w] == null) continue;
            DirectedEdge e = edgeTo[w];
            int v = e.from();
            if (w != e.to()) return false;
            if (distTo[v] + e.weight() != distTo[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        System.out.println("Satisfies optimality conditions");
        System.out.println();
        return true;
	}
	private void validateVertex(int v) {
		int V = distTo.length;
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{
					new File(".\\algs4-data\\tinyEWD.txt"),
//					new File(".\\algs4-data\\mediumEWD.txt"),
					};
			for(File file : files) {
				In in = new In(file);
				EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
				for (int s = 0; s < G.V(); s=s+3) {
					LazyDijkstraSP sp = new LazyDijkstraSP(G, s);
					for (int t = 0; t < G.V(); t++) {
						if (sp.hasPathTo(t)) {
							System.out.printf("%d to %d (%.2f) ",s,t,sp.distTo(t));
							for (DirectedEdge e : sp.pathTo(t))
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
