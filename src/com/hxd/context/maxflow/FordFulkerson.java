package com.hxd.context.maxflow;
/******************************************************************************
 *  Compilation:  javac FordFulkerson.java
 *  Execution:    java FordFulkerson V E
 *  Dependencies: FlowNetwork.java FlowEdge.java Queue.java
 *
 *  Ford-Fulkerson algorithm for computing a max flow and
 *  a min cut using shortest augmenting path rule.
 *
 ******************************************************************************/

import com.hxd.base.Queue;
import com.hxd.introcs.stdlib.In;
import com.hxd.introcs.stdlib.StdOut;

/**
 * The {@code FordFulkerson} class represents a data type for computing a
 * <em>maximum st-flow</em> and <em>minimum st-cut</em> in a flow
 * network.
 * <p>
 * This implementation uses the <em>Ford-Fulkerson</em> algorithm with
 * the <em>shortest augmenting path</em> heuristic.
 * The constructor takes time proportional to <em>E V</em> (<em>E</em> + <em>V</em>)
 * in the worst case and extra space (not including the network)
 * proportional to <em>V</em>, where <em>V</em> is the number of vertices
 * and <em>E</em> is the number of edges. In practice, the algorithm will
 * run much faster.
 * Afterwards, the {@code inCut()} and {@code value()} methods take
 * constant time.
 * <p>
 * If the capacities and initial flow values are all integers, then this
 * implementation guarantees to compute an integer-valued maximum flow.
 * If the capacities and floating-point numbers, then floating-point
 * roundoff error can accumulate.
 * <p>
 *
 * @author 候旭东
 */
public class FordFulkerson {
    private static final double FLOAT_POINT_EPSILON = 1E-11;

    private final int V;        // number of vertices
    private boolean[] marked;   // marked[v] = true if s-> v path in residual graph
    private FlowEdge[] edgeTo;
    private double value;

    /**
     * Compute a maximum flow and minimum cut in the network {@code G}
     * from vertex {@code s} to vertex {@code t}.
     *
     * @param G the flow network
     * @param s the source vertex
     * @param t the sink vertex
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     * @throws IllegalArgumentException unless {@code 0 <= t < V}
     * @throws IllegalArgumentException if {@code s == t}
     * @throws IllegalArgumentException if initial flow is infeasible
     */
    public FordFulkerson(FlowNetwork G, int s, int t) {
        V = G.V();
        validate(s);
        validate(t);
        if (s == t) throw new IllegalArgumentException("Source equals sink");
        if (!isFeasible(G, s, t)) throw new IllegalArgumentException("Initial flow is infeasible");

        // while there exists an augmenting path, use it
        value = excess(G, t);
        while (hasAugmentingPath(G, s, t)) {
            //compute bottleneck capacity
            double bottle = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = edgeTo[v].other(v))
                bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
            // augment flow
            for (int v = t; v != s; v = edgeTo[v].other(v))
                edgeTo[v].addRedidualFlowTo(v, bottle);
            value += bottle;
        }
        assert check(G, s, t);
    }

    /**
     * Returns the value of the maximum flow.
     *
     * @return the value of the maximum flow
     */
    public double value() {
        return value;
    }

    /**
     * Returns true if the specified vertex is on the {@code s} side of the mincut.
     *
     * @param v vertex
     * @return {@code true} if vertex {@code v} is on the {@code s} side of the micut;
     * {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean inCut(int v) {
        validate(v);
        return marked[v];
    }

    private boolean hasAugmentingPath(FlowNetwork G, int s, int t) {
        edgeTo = new FlowEdge[G.V()];
        marked = new boolean[G.V()];

        // breadth-first search
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(s);
        marked[s] = true;
        while (!queue.isEmpty() && !marked[t]) {
            int v = queue.dequeue();

            for (FlowEdge e : G.adj(v)) {
                int w = e.other(v);
                if (e.residualCapacityTo(w) > 0) {
                    if (!marked[w]) {
                        edgeTo[w] = e;
                        marked[w] = true;
                        queue.enqueue(w);
                    }
                }
            }
        }
        return marked[t];
    }

    // throw an IllegalArgumentException if v is outside prescibed range
    private void validate(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    private double excess(FlowNetwork G, int v) {
        double excess = 0.0;
        for (FlowEdge e : G.adj(v)) {
            if (v == e.from()) excess -= e.flow();
            else excess += e.flow();
        }
        return excess;
    }

    private boolean isFeasible(FlowNetwork G, int s, int t) {
        for (int v = 0; v < G.V(); v++) {
            for (FlowEdge e : G.adj(v)) {
                if (e.flow() < -FLOAT_POINT_EPSILON || e.flow() > e.capacity() + FLOAT_POINT_EPSILON) {
                    System.err.println("Edge does not satisfy capacity constrins: " + e);
                    return false;
                }
            }
        }
        if (Math.abs(value + excess(G, s)) > FLOAT_POINT_EPSILON) {
            System.err.println("Excess at source=" + excess(G, s));
            System.err.println("Max flow    = " + value);
            return false;
        }
        if (Math.abs(value - excess(G, t)) > FLOAT_POINT_EPSILON) {
            System.err.println("Excess at source=" + excess(G, t));
            System.err.println("Max flow    = " + value);
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s || v == t) continue;
            else if (Math.abs(excess(G, v)) > FLOAT_POINT_EPSILON) {
                System.err.println("Net flow out of " + v + " doesn't equal zero");
                return false;
            }
        }
        return true;
    }

    private boolean check(FlowNetwork G, int s, int t) {
        // check that flow is feasible
        if (!isFeasible(G, s, t)) {
            System.err.println("Flow is infeasible");
            return false;
        }
        // check that s is on the source side of min cut and that t is not on source side
        if (!inCut(s)) {
            System.err.println("source " + s + " is not on source side of min cut");
            return false;
        }
        if (inCut(t)) {
            System.err.println("sink " + t + " is on source side of min cut");
            return false;
        }
        // check that value of min cut = value of max flow
        double mincutValue = 0.0;
        for (int v = 0; v < G.V(); v++) {
            for (FlowEdge e : G.adj(v)) {
                if ((v == e.from()) && inCut(e.from()) && !inCut(e.to()))
                    mincutValue += e.capacity();
            }
        }
        if (Math.abs(mincutValue - value) > FLOAT_POINT_EPSILON) {
            System.err.println("Max flow value = " + value + ", min cut value = " + mincutValue);
            return false;
        }
        return true;
    }


    /**
     * Unit tests the {@code FordFulkerson} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        try {
            FlowNetwork G ;
            G = new FlowNetwork(new In(args[0]));
            if (args.length == 2) {
                // create flow network with V vertices and E edges
                int V = Integer.parseInt(args[0]);
                int E = Integer.parseInt(args[1]);
                G = new FlowNetwork(V, E);
            } else if (args.length == 1)
                G = new FlowNetwork(new In(args[0]));
            else
                System.exit(1);
            int V = G.V();
            int s = 0;
            int t = V - 1;
            StdOut.println(G);

            // compute maximum flow and minimum cut
            FordFulkerson maxflow = new FordFulkerson(G, s, t);
            StdOut.println("Max flow from " + s + " to " + t);
            for (int v = 0; v < G.V(); v++) {
                for (FlowEdge e : G.adj(v)) {
                    if ((v == e.from()) && e.flow() > 0)
                        StdOut.println("   " + e);
                }
            }

            // print min-cut
            StdOut.print("Min cut: ");
            for (int v = 0; v < G.V(); v++) {
                if (maxflow.inCut(v)) StdOut.print(v + " ");
            }
            StdOut.println();

            StdOut.println("Max flow value = " + maxflow.value());

        } catch (Exception e) {
            StdOut.println(e);
        }
    }
}
















