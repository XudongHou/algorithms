package com.hxd.sort.priorityQueue;

import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class IndexMaxPQ<Key extends Comparable<Key>> implements Iterable<Integer> {

	private int maxN;		// pq上的最大元素
	private int n;			// pq上的元素数
	private int[] pq;		// 二进制堆使用基于1的索引;存储索引
	private int[] qp;		// 存储插入元素的位置 倒序pq ---- qp[pq[i]] == pq[qp[i]] == i 
	private Key[] keys;		// keys[i]的优先级
	
	/**
	 * 对具有索引的空的索引的优先级队列进行初始化{@code 0}
     * and {@code maxN - 1}.
     * @param  maxN the keys on this priority queue are index from {@code 0}
     *         {@code maxN - 1}
     * @throws IllegalArgumentException if {@code maxN < 0}
	 * */
	
	public IndexMaxPQ(int maxN) {
		if(maxN<0) throw new IllegalArgumentException();
		this.maxN=maxN;
		n=0;
		keys=(Key[]) new Comparable[maxN+1];	//使数组长度为maxN
		pq = new int[maxN+1];
		qp = new int[maxN+1];
		for (int i = 0; i <=maxN; i++)
			qp[i]=-1;
	}
	
	/**
	 * 如果此优先级队列为空，则返回true。
	 * 
     * @return {@code true} if this priority queue is empty;
     *         {@code false} otherwise
	 * */
	public boolean isEmpty(){ return n==0;}
	/**
	 * 此索引是否是优先队列上的索引     
	 * @param  i an index
     * @return {@code true} if {@code i} is an index on this priority queue;
     *         {@code false} otherwise
     * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
	 * */
	public boolean contains(int i){
		if(i<0||i>=maxN) throw new IndexOutOfBoundsException();
		return qp[i]!=-1;
	}
	/**
	 * 返回此优先级队列上的键数
	 * @return the number of keys on this priority queue
	 * */
	public int size(){return n;}
	/**
	 * 使用索引关联键
	 * @param  i an index
     * @param  key the key to associate with index {@code i}
     * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if there already is an item associated
     *         with index {@code i}
	 * */
	public void insert(int i,Key key){
		if(i<0||i>=maxN) throw new IndexOutOfBoundsException();
		if(contains(i)) throw new IllegalArgumentException("index is already in priority Queue");
		n++;
		qp[i]=n;//存储插入元素位置
		pq[n]=i;//存储索引
		keys[i]=key;
		swim(n);
	}
	/**
	 * 返回与最小键相关联的索引
	 * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
	 * */
	public int maxIndex(){
		if(n==0) throw new NoSuchElementException("Priority queue underflow");
		return pq[1];
	}
	/**
	 * 返回最小键值
	 * @return a minimum key
     * @throws NoSuchElementException if this priority queue is empty
	 * */
	public Key maxKey(){
		if(n==0) throw new NoSuchElementException("Priority queue underflow");
		return keys[pq[1]];
	}
	/**
	 * 删除最小键并返回其关联的索引
	 * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
	 * */
	public int delMax(){
		if(n==0) throw new NoSuchElementException();
		int min=pq[1];
		exch(1,n--);
		sink(1);
		assert min == pq[n+1];	//堆恢复有序
		qp[min]=-1;				//删除
		keys[min]=null;			//帮助垃圾处理
		pq[n+1] = -1;			//不需要的
		return min;
	}
	/**
	 * 返回与与索引i相关联的键
	 * @param  i the index of the key to return
     * @return the key associated with index {@code i}
     * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException no key is associated with index {@code i}
	 * */
	public Key keyOf(int i){
		if(i<0||i>=maxN) throw new IndexOutOfBoundsException();
		if(!contains(i)) throw new NoSuchElementException("index is already in priority Queue");
		else return keys[i];
	}
	/**
	 * 将与索引{@code i}关联的键更改为指定的值
	 * @param  i the index of the key to change
     * @param  key change the key associated with index {@code i} to this key
     * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException no key is associated with index {@code i}
	 * */
	public void changeKey(int i,Key key){
		if(i<0||i>=maxN) throw new IndexOutOfBoundsException();
		if(!contains(i)) throw new NoSuchElementException("index is already in priority Queue");
		keys[i]=key;
		swim(qp[i]);
		sink(qp[i]);
	}
	/**
	 * 将与索引{@code i}关联的键更改为指定的值
	 * @param  i the index of the key to change
     * @param  key change the key associated with index {@code i} to this key
     * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
     * @deprecated Replaced by {@code changeKey(int, Key)}.
	 * */
	public void change(int i,Key key){changeKey(i, key);}
	/**
	 * 将与索引{@code i}相关联的键减少为指定的值。
	 * @param  i the index of the key to decrease
     * @param  key decrease the key associated with index {@code i} to this key
     * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if {@code key >= keyOf(i)}
     * @throws NoSuchElementException no key is associated with index {@code i}
	 * */
	public void decreaseKey(int i,Key key){
		if(i<0||i>=maxN) throw new IndexOutOfBoundsException();
		if(!contains(i)) throw new NoSuchElementException("index is already in priority Queue");
		if(keys[i].compareTo(key)<=0)
			throw new IllegalArgumentException("Calling decreaseKey() with given argument would not strictly decrease the key");
		keys[i] = key;
		swim(qp[i]);
	}
	/**
	 * 将与索引{@code i}关联的键增加到指定的值
	 * @param  i the index of the key to increase
     * @param  key increase the key associated with index {@code i} to this key
     * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if {@code key <= keyOf(i)}
     * @throws NoSuchElementException no key is associated with index {@code i}
	 * */
	public void increaseKey(int i,Key key){
		if(i<0||i>=maxN) throw new IndexOutOfBoundsException();
		if(!contains(i)) throw new NoSuchElementException("index is already in priority Queue");
		if(keys[i].compareTo(key)>=0)
			throw new IllegalArgumentException("Calling decreaseKey() with given argument would not strictly decrease the key");
		keys[i]=key;
		sink(qp[i]);
	}
	/**
	 * 删除与索引{@code i}关联的键
	 * @param  i the index of the key to remove
     * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException no key is associated with index {@code i}
	 * */
	public void delete(int i){
		if(i<0||i>=maxN) throw new IndexOutOfBoundsException();
		if(!contains(i)) throw new NoSuchElementException("index is already in priority Queue");
		int index=qp[i];
		exch(index, n--);
		swim(index);
		sink(index);
		keys[i]=null;
		qp[i]=-1;
	}
	
	private boolean less(int i,int j){
		return keys[pq[i]].compareTo(keys[pq[j]])<0;
	}

	private void exch(int i, int j) {
		int swap = pq[i];
		pq[i]=pq[j];
		pq[j]=swap;
		qp[pq[i]]=i;
		qp[pq[j]]=j;
	}

	private void swim(int k) {
		while(k>1&& less(k/2, k)){
			exch(k,k/2);
			k=k/2;
		}
	}

	private void sink(int k) {
		while(2*k<=n){
			int j=2*k;
			if(j<n&&less(j, j+1))j++;
			if(!less(k, j))break;
			exch(k, j);
			k=j;
		}
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return new HeapIterator();
	}
	
	private class HeapIterator implements Iterator<Integer>{

		private IndexMinPQ<Key> copy;
		
		public HeapIterator() {
			copy=new IndexMinPQ<Key>(pq.length-1);
			for (int i = 1; i <= n; i++)
				copy.insert(pq[i], keys[pq[i]]);
		}
		
		@Override
		public boolean hasNext() {return !copy.isEmpty();}
		@Override
		public Integer next() {
			if(!hasNext()) throw new NoSuchElementException();
			return copy.delMin();
		}
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	public static void main(String[] args) {
        // insert a bunch of strings
        String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };

        IndexMaxPQ<String> pq = new IndexMaxPQ<String>(strings.length);
        for (int i = 0; i < strings.length; i++) {
            pq.insert(i, strings[i]);
        }

        // delete and print each key
        while (!pq.isEmpty()) {
            int i = pq.delMax();
            System.out.println(i + " " + strings[i]);
        }
        System.out.println();

        // reinsert the same strings
        for (int i = 0; i < strings.length; i++) {
            pq.insert(i, strings[i]);
        }

        // print each key using the iterator
        for (int i : pq) {
            System.out.println(i + " " + strings[i]);
        }
        while (!pq.isEmpty()) {
            System.out.print(pq.delMax());
        }

    }

}
