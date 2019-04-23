package com.hxd.base;

/**
 * 候旭东 20161211 计数器
 * */

public class Counter {
	private final String name;
	private int count;
	public Counter(String id) {
		name=id;
	}
	
	public void increment(){
		count++;
	}
	public int tally(){
		return count;
	}
	@Override
	public String toString() {
		return count+" "+name;
	}
	/**
	 * main是一个很好的单元测试
	 * */
	public static void main(String[] args) {
		Counter head = new Counter("head");
		Counter tail = new Counter("tail");
		
		head.increment();
		head.increment();
		tail.increment();
		System.out.println(head+" "+tail);
		System.out.println(head.tally()+" "+tail.tally());
	}
}
