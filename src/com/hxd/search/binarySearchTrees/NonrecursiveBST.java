package com.hxd.search.binarySearchTrees;

import com.hxd.base.Queue;
import com.hxd.base.Stack;

/**
 * 递归实现的更容易验证其正确性,而非递归实现的二叉查找树实现效率更高.如果树不是平衡的,函数调用的栈的深度
 * 可能会成为递归实现的一个问题.
 * 下面是一个不使用递归实现的二叉查找树
 * @author 候旭东 20170108
 * */

public class NonrecursiveBST<Key extends Comparable<Key>,Value> {
	private Node root;
	
	private class Node {
		private Key key;
		private Value value;
		private Node left;
		private Node right;
		
		public Node(Key key, Value value) {
			this.key = key;
			this.value = value;
		}
	}
	
	public void put(Key key, Value value) {
		Node r = new Node(key, value);
		if (root == null) {
			root = r;
			return;
		}
		
		Node parent = null, x = root;
		while (x != null) {
			parent = x;
			int cmp = key.compareTo(x.key);
			if 		( cmp < 0 )	x = x.left;
			else if ( cmp > 0 ) x = x.right;
			else {
				x.value = value;
				return;
			}
		}
		int cmp = key.compareTo(parent.key);
		if ( cmp < 0) 	parent.left = r;
		else			parent.right = r;
	}
	
	public Value get(Key key) {
		Node x = root;
		while (x != null) {
			int cmp = key.compareTo(x.key);
			if		(cmp < 0)	x = x.left;
			else if	(cmp > 0)	x = x.right;
			else				return x.value;
		}
		return null;
	}
	
	public Iterable<Key> keys() {
		Stack<Node> stack = new Stack<Node>();
		Queue<Key> queue = new Queue<Key>();
		Node x = root;
		while (x != null || !stack.isEmpty()){
			if (x != null) {
				stack.push(x);
				x = x.left;
			}else {
				x = stack.pop();
				queue.enqueue(x.key);
				x = x.right;
			}
		}
		return queue;
	}
	
	public static void main(String[] args) {
		NonrecursiveBST<String, Integer> st = new NonrecursiveBST<String,Integer>();
		String[] strings = new String[]{"S","E","A","R","C","H","E","X","A","M","P","L","E"};
		int i =0;
		for (String key : strings)
			st.put(key,i++);
		for (String s: st.keys())
			System.out.println(s+" "+st.get(s));
		System.out.println(st.get("S"));
	}
}
