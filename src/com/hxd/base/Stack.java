package com.hxd.base;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.hxd.introcs.stdlib.StdIn;

/**
 * 候旭东 20161213 使用链表的泛型Stack实现
 * 链表的目标
 * 	可以处理任意类型的数据
 * 	所需的空间总是和集合的大小成正比
 * 	操作所需的时间总是和集合的大小无关
 * */

public class Stack<T> implements Iterable<T> {
	
	private Node first;
	private int N;
	/**
	 * 内部实现链表
	 * */
	private class Node{
		T item;
		Node next;
	}

	public Stack() {
	}
	
	public boolean isEmpty(){return first==null;}
	
	public int size(){return N;}
	
	public void push(T t){
		Node oldFirst=first;
		first=new Node();
		first.item=t;
		first.next=oldFirst;
		N++;
	}
	
	public T pop(){
		T item = first.item;
		first=first.next;
		N--;
		return item;
	}
	
	public T peak() {
		if (isEmpty()) throw new NoSuchElementException("Stack underflow");
		return first.item;
	}
	
	public String toString() {
        StringBuilder s = new StringBuilder();
        for (T item : this) {
            s.append(item);
            s.append(' ');
        }
        return s.toString();
    }
	
	@Override
	public Iterator<T> iterator() {
		return new ListIterator();
	}
	
	private class ListIterator implements Iterator<T>{

		private Node current=first;
		
		@Override
		public boolean hasNext() {
			return current!=null;
		}

		@Override
		public T next() {
			T item = current.item;
			current=current.next;
			return item;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	public static void main(String[] args) {
		Stack<String> stack = new Stack<String>();
		while (!StdIn.isEmpty()) {
			String item = StdIn.readString();
			if(!item.equals("-"))
				stack.push(item);
			else if(!item.isEmpty())
				System.out.print(stack.pop()+" ");
		}
		System.out.println("("+stack.size()+" left on stack)");
	}
}
