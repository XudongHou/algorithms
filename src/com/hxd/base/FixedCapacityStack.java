package com.hxd.base;


/**
 * 候旭东 20161213 定容栈
 * 这个实现中,栈永远不会溢出,使用率也永远不会低于四分之一
 * */

public class FixedCapacityStack<T>{
	private T[] a;
	private int N;
	@SuppressWarnings("unchecked")
	public FixedCapacityStack(int cap) {
		a=(T[])new Object[cap];
	}
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
	
}
