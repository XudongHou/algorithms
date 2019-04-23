package com.hxd.base;

/***
 * 
 * 约瑟夫环问题
 * @author houxu_000
 *
 */

public class Josephus {
	
	private static class Node {
		public Node() {
		}
		int data;
		Node next;
	}
	
	private static Node create(int n) {
		Node root,p;
		root = new Node(); 
		p = root;
		Node s;
		int i = 1;
		
		if(n > 0) {
			while (i < n) {
				s = new Node();
				s.data = i++;
				p.next = s;
				p = s;
			}
			s = new Node();
			s.data = n;
			s.next = root.next;
			p.next = s;
			return s.next;
		}
		return null;
	}
	
	public static void main(String[] args) {
		int  n =41;
		int m = 3;
		Node p = create(n);
		Node tmp;
		int i;
		m %= n;
		System.out.println(m);
		while (p !=p.next) {
			for (i = 1; i < m-1; i++)
				p = p.next;
			System.out.printf("%d-> ", p.next.data);
			tmp = p.next;
			p.next = tmp.next;
			p = p.next; 
		}
		System.out.println();
		System.out.println(p.data);
	}
}
