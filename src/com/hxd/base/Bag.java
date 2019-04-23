package com.hxd.base;

import java.util.Iterator;

public class Bag<T> implements Iterable<T> {
	
	private Node first;
	private int N;
	
	private class Node{
		T item;
		Node next;
	}
	/**
	 * 先进后出
	 * */
	public void add(T t){
		Node oldFirst=first;
		first=new Node();
		first.item=t;
		first.next=oldFirst;
		N++;
	}

	public boolean isEmpty(){return first==null;}
	
	public int size(){
		return N;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for(T t: this) {
			s = s.append(t + " ");
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
}
