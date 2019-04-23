package com.hxd.graphs.directedGraphs;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Queue;
import com.hxd.base.Stack;
import com.hxd.introcs.stdlib.In;

/**
 * 使用和无向图相同的效率解决所有有向图的连通性问题.
 * {@code TarjanSCC}类表示用于确定有向图中的强分量的数据类型。 id操作确定给定顶点位于哪个强组件中;
 * areStronglyConnected操作确定两个顶点是否在同一强组件中; 并且countoperation确定强组件的数量。 
 * 组件的组件标识符是强组件中的一个顶点：当且仅当它们在同一强组件中时，两个顶点具有相同的组件标识符。 这个实现
 * 使用Tarjan的算法。 构造函数需要与V + E成正比的时间（在最坏的情况下），其中V是顶点的数量，E是边的数量。
 *  之后，id，count和areStronglyConnected操作需要固定的时间。 
 *  对于同一API的替代实现，请参阅{@link KosarajuSharirSCC}和{@link GabowSCC}。
 * <p>
 * @author 候旭东 20170221
 * */

public class TrianSCC {
	
	private boolean[] marked;			//marked[v] = has v been visited?
	private int[] id;					// id[v] = id of strong component containing v
	private int[] low;					// low[v] = low number of v
	private int pre;					// preorder number counter
	private int count;					// number of strongly-connected components
	private Stack<Integer> stack;
	
	public TrianSCC(Digraph G) {
		marked = new boolean[G.V()];
		stack = new Stack<Integer>();
		id = new int[G.V()];
		low = new int[G.V()];
		for (int v = 0; v < G.V(); v++)
			if (!marked[v])
				dfs(G, v);
		assert check(G);
	}
/**
 * tarjan算法的基础是DFS。我们准备两个数组Low和id。Low数组是一个标记数组，记录该点所在的强连通子图所在搜索子树
 * 的根节点的id值（很绕嘴，往下看你就会明白），id数组记录搜索到该点的时间，也就是第几个搜索这个点的。
 * 根据以下几条规则，经过搜索遍历该图（无需回溯）和对栈的操作，我们就可以得到该有向图的强连通分量。
 * 数组的初始化：当首次搜索到点p时，id与Low数组的值都为到该点的时间。
 * 堆栈：每搜索到一个点，将它压入栈顶。
 * 当点p有与点p’相连时，如果此时（时间为id[p]时）p’不在栈中，p的low值为两点的low值中较小的一个。
 * 当点p有与点p’相连时，如果此时（时间为id[p]时）p’在栈中，p的low值为p的low值和p’的id值中较小的一个。
 * 每当搜索到一个点经过以上操作后（也就是子树已经全部遍历）的low值等于id值，则将它以及在它之上的元素弹出栈。
 * 这些出栈的元素组成一个强连通分量。继续搜索（或许会更换搜索的起点，因为整个有向图可能分为两个不连通的部分），直到所有点被遍历。
 * 由于每个顶点只访问过一次，每条边也只访问过一次，我们就可以在O（n+m）的时间内求出有向图的强连通分量。但是，这么做的原因是什么呢？
 * Tarjan算法的操作原理如下：
 * Tarjan算法基于定理：在任何深度优先搜索中，同一强连通分量内的所有顶点均在同一棵深度优先搜索树中。也就是说，强连通分量一定是
 * 有向图的某个深搜树子树。可以证明，当一个点既是强连通子图Ⅰ中的点，又是强连通子图Ⅱ中的点，则它是强连通子图Ⅰ∪Ⅱ中的点。
 * 这样，我们用low值记录该点所在强连通子图对应的搜索子树的根节点的id值。注意，该子树中的元素在栈中一定是相邻的，且根节点在栈中
 * 一定位于所有子树元素的最下方。强连通分量是由若干个环组成的。所以，当有环形成时（也就是搜索的下一个点已在栈中），我们将
 * 这一条路径的low值统一，即这条路径上的点属于同一个强连通分量。如果遍历完整个搜索树后某个点的id值等于low值，
 * 则它是该搜索子树的根。这时，它以上（包括它自己）一直到栈顶的所有元素组成一个强连通分量。
 * */
	private void dfs(Digraph G, int v) {
		marked[v] = true;
		low[v] = pre++;
		int min = low[v];
		stack.push(v);
		for (int w : G.adj(v)) {
			
			if (!marked[w]) dfs(G, w);
//			System.out.println("low["+v+"]: "+" = "+low[v]+"  min "+min);
			if (low[w] < min) min = low[w];
		}
		if (min < low[v]) {
			low[v] = min;
			return;
		}
		int w;
		do {
			w = stack.pop();
			id[w] = count;
			low[w] = G.V();
		}while (w != v);
		count++;
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
        return id[v] == id[w];
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

    // does the id[] array contain the strongly connected components?
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

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Unit tests the {@code TarjanSCC} data type.
     *
     * @param args the command-line arguments
     */
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
				TrianSCC scc = new TrianSCC(G);
				
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
