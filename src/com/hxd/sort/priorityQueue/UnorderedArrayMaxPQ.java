package com.hxd.sort.priorityQueue;

/**
 * 候旭东 20161224 无序的优先队列使用数组实现
 * */

public class UnorderedArrayMaxPQ <Key extends Comparable<Key>>{
	private Key [] pq;	//元素
	private int n;		//元素数量
	
	@SuppressWarnings("unchecked")
	public UnorderedArrayMaxPQ(int capacity) {
		pq = (Key[]) new Comparable[capacity];
		n=0;
	}
	
	public boolean isEmpty(){return n==0;}
	public int size(){return n;}
	public void insert(Key x){pq[n++]=x;}
	
	public Key delMax(){
		int max=0;
		for (int i = 1; i < n; i++)
			if(less(max,i))max=i;
		exch(max,n-1);
		return pq[--n];
	}

	private void exch(int i, int j) {
		Key swap = pq[i];
		pq[i]=pq[j];
		pq[j]=swap;
	}

	private boolean less(int i, int j) {
		return pq[i].compareTo(pq[j])<0;
	}
    public static void main(String[] args) {
        UnorderedArrayMaxPQ<String> pq = new UnorderedArrayMaxPQ<String>(10);
        pq.insert("this");
        pq.insert("is");
        pq.insert("a");
        pq.insert("test");
        while (!pq.isEmpty()) 
            System.out.println(pq.delMax());
    }
}
