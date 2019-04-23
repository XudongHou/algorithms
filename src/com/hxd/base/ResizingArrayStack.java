package com.hxd.base;

import java.util.Iterator;

/**
 * 候旭东 20161213 这个算法几乎(还没有)达到了任意集合类数据类型的实现的最佳性能
 * 	每项操作的用时都与集合大小无关
 * 	空间需求总是不超过集合大小乘以一个常数
 * */

public class ResizingArrayStack<T> implements Iterable<T> {

	@SuppressWarnings("unchecked")
	private T[] a  = (T[]) new Object[1];
	private int N=0;
	public boolean isEmpty(){return N==0;}
	public int size(){return N;}
	/**
	 * 加入元素
	 * 检查数组是否太小,如果没有了多余的空间,将数组长度加倍
	 * */
	public void push(T t){
		if(N==a.length)resize(2*a.length);
		a[N++]=t;
	}
	/**
	 * 从栈中删除元素
	 * 如果数组太大.就将长度减半
	 * Java的垃圾收集策略是回收所有无法访问的对象的内存,被弹出的元素引用仍然存在于数组中,
	 * 这个元素实际上已经是一个孤儿了--它永远不会再被访问,但是Java的垃圾收集器没法知道这
	 * 一点,除非该引用被覆盖.即使用例已经已经不再需要这个元素了,书中的引用仍然可以让它继续
	 * 存在.这种情况(保存一个不需要的对象的引用)成为游离,避免游离很容易只需将被弹出的数组
	 * 元素的值设为null,覆盖无用的引用并使系统可以在用例使用完被弹出的元素后回收它的内存
	 * */
	public T pop(){
		T t  = a[--N];
		a[N] = null;//解除游离
		if(N>0 && N==a.length/4)resize(a.length/2);
		return t;
	}
	/**
	 * 扩容
	 * */
	@SuppressWarnings("unchecked")
	private void resize(int max){
		T[] temp = (T[])new Object[max];
		for (int i = 0; i < N; i++)
			temp[i]=a[i];
		a=temp;
	}

	 
	/**
	 * 倒序迭代
	 * */
	private class ReverseArrayIterator implements Iterator<T>{

		private int i=N;
		@Override
		public boolean hasNext() {return i>0;}
		@Override
		public T next() {return a[--i];}
		@Override
		public void remove() {throw new UnsupportedOperationException();}
	}

	/**
	 * 倒序迭代
	 * */
	@Override
	public Iterator<T> iterator() {
		return new ReverseArrayIterator();
	}
	
	public static void main(String[] args) {
		ResizingArrayStack<String> t = new ResizingArrayStack<String>();
		t.push("h");
		t.push("e");
		t.push("l");
		t.push("l");
		t.push("o");
		for (String string : t)
			System.out.print(string);
	}

}
