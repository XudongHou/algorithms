package com.hxd.strings.regexp;

/******************************************************************************
 *  Compilation:  javac NFA.java
 *  Execution:    java NFA regexp text
 *  Dependencies: Stack.java Bag.java Digraph.java DirectedDFS.java
 *
 *  % java NFA "(A*B|AC)D" AAAABD
 *  true
 *
 *  % java NFA "(A*B|AC)D" AAAAC
 *  false
 *
 *  % java NFA "(a|(bc)*d)*" abcbcd
 *  true
 *
 *  % java NFA "(a|(bc)*d)*" abcbcbcdaaaabcbcdaaaddd
 *  true
 *
 *  Remarks
 *  -----------
 *  The following features are not supported:
 *    - The + operator
 *    - Multiway or
 *    - Metacharacters in the text
 *    - Character classes.
 *
 ******************************************************************************/


import com.hxd.base.Bag;
import com.hxd.base.Stack;
import com.hxd.graphs.directedGraphs.Digraph;
import com.hxd.graphs.directedGraphs.DirectedDFS;

/**
 * 正则表达式,非确定有限自动状态机
 * {@code NFA}类提供了从正则表达式创建非确定性有限状态自动机（NFA）的数据类型，并测试给定字符串是否与该正则表达式匹配。 
 * 它支持以下操作：连接，闭包，二进制或括号。 它不支持多路或字符类，元字符（在文本或模式中），捕获功能，贪婪或可变性修饰符以及
 * 工业级实现中的其他功能，例如{@link java.util.regex.Pattern}和 {@link java.util.regex.Matcher}。 
 * 该实现使用有向图和堆栈构建NFA，并使用图搜索来模拟NFA。 构造函数需要与m成正比的时间，其中m是正则表达式中的字符数。 
 * 识别方法需要与m n成正比的时间，其中n是文本中的字符数。
 * @author houxu_000 20170404
 *
 */

public class NFA {
	
	private Digraph graph;		//epslionz转换
	private String regexp;		//匹配转换
	private final int m;		//状态数量
	
	/**
	 * 从指定的正则表达式初始化NFA。
	 * @param regexp regexp the regular expression
	 */
	public NFA(String regexp) {
		this.regexp = regexp;
		m = regexp.length();
		Stack<Integer> ops = new Stack<Integer>();
		graph = new Digraph(m+1);
		for (int i = 0; i < m; i++) {
			int lp = i;
			if (regexp.charAt(i) == '(' || regexp.charAt(i) == '|')
				ops.push(i);
			else if (regexp.charAt(i) == ')') {
				int or = ops.pop();
				// 2路 或者操作符
				if (regexp.charAt(or) == '|') {
					lp = ops.pop();
					graph.addEdge(lp, or+1);
					graph.addEdge(or, i);
				}
				else if (regexp.charAt(or) == '(')
					lp = or;
				else assert false;
			}
			
			//闭包操作
			if (i < m-1 && regexp.charAt(i+1) == '*') {
				graph.addEdge(lp, i+1);
				graph.addEdge(i+1, lp);
			}
			if (regexp.charAt(i) == '(' || regexp.charAt(i) == '*' || regexp.charAt(i) == ')')
				graph.addEdge(i, i+1);
		}
		if (ops.size() != 0)
			throw new IllegalArgumentException("Invalid regular expression");
	}
	
	/**
	 * 如果正则表达式匹配文本，则返回true。
	 * @param txt
	 * @return {@code true} if the text is matched by the regular expression,
     *         {@code false} otherwise
	 */
	public boolean recognizes(String txt) {
		DirectedDFS dfs = new DirectedDFS(graph, 0);
		Bag<Integer> pc = new Bag<Integer>();
		for (int v = 0; v < graph.V(); v++)
			if (dfs.marked(v)) pc.add(v);
		
		for (int i = 0; i < txt.length(); i++) {
			if (txt.charAt(i) == '*' || txt.charAt('i') == '|' || txt.charAt(i) == '(' || txt.charAt(i) == ')')
				throw new IllegalArgumentException("text contains the metacharacter '" + txt.charAt(i) + "'");
			
			Bag<Integer> match = new Bag<Integer>();
			for (int v : pc) {
				if (v == m)	continue;
				if ((regexp.charAt(v) == txt.charAt(i)) || regexp.charAt(v) == '.')
					match.add(v+1);
			}
			dfs = new DirectedDFS(graph, match);
			pc = new Bag<Integer>();
			for (int v = 0; v < graph.V(); v++)
				if (dfs.marked(v)) pc.add(v);
			// 无匹配
			if (pc.size() == 0)
				return false;
		}
		
		for (int v : pc)
			if (v == m) return true;
		return false;
	}
	
	/**
     * Unit tests the {@code NFA} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String regexp = "(" + args[0] + ")";
        String txt = args[1];
        NFA nfa = new NFA(regexp);
        System.out.println(nfa.recognizes(txt));
    }
}
