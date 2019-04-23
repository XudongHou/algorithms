package com.hxd.search.symbolTables;


import java.util.NoSuchElementException;

import com.hxd.base.Queue;

/**
 * @author 候旭东 20161226 有序数组中的二分查找
 * 使用的数据结构是一对平行的数组,一个存储键一个存储值.算法可以保证数组中Comparable类型的键有序,
 * 然后使用数组的索引来高效地实现get()和其他操作.实现的核心是rank()方法,对于put()方法,只要给定
 * 的键存在于表中,rank()方法就能够精确地告诉我们到哪里去更新它的值,以及当键不存在表中是将键存储到表
 * 的何处.将更大的键向后移动一格来腾出位置(从后向前移动)并将给定的键值对分别插入到各自数组中的合适位置
 * 
 * 优点:最优的查找效率和空间需求,能够进行有序性相关操作
 * 缺点:插入操作很慢
 * */

public class BinarySearchST<Key extends Comparable<Key>, Value> {
	private static final int INIT_CAPACITY = 2;
	private Key[] keys;
	private Value[] vals;
	private int N=0;
	
	/**
     * Initializes an empty symbol table.
     */
	public BinarySearchST() {
		this(INIT_CAPACITY);
	}
	
	/**
     * Initializes an empty symbol table with the specified initial capacity.
     * @param capacity the maximum capacity
     */
	@SuppressWarnings("unchecked")
	public BinarySearchST(int capacity) {
		keys=(Key[]) new Comparable[capacity];
		vals=(Value[]) new Object[capacity];
	}

	// resize the underlying arrays
	@SuppressWarnings("unchecked")
	private void resize(int capacity){
		assert capacity >= N;
		Key[] tempk = (Key[]) new Comparable[capacity];
		Value[] tempv = (Value[]) new Object[capacity];
		for (int i = 0; i < N; i++) {
			tempk[i] = keys[i];
			tempv[i] = vals[i];
		}
		vals=tempv;
		keys=tempk;
	}
	
	/**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
	public int size(){
		return N;
	}
	
	/**
     * Returns the value associated with the given key in this symbol table.
     *
     * @param  key the key
     * @return the value associated with the given key if the key is in the symbol table
     *         and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public Value get(Key key){
		if(isEmpty())return null;
		int i = rank(key);
		if(i<N&&keys[i].compareTo(key)==0)	return vals[i];
		else							 	return null;
	}
	
	/**
	 * 在N个键的有序数组中进行二分查找最多需要(lgN+1)次比较(无论是否成功).向大小为N的有序数组中插入
	 * 个新的元素在最坏情况下需要访问~2N次数组,因此向一个空符号表中插入N个元素在最坏情况下需要访问~N^2
	 * 次数组
	 * Returns the number of keys in this symbol table strictly less than {@code key}.
     *
     * @param  key the key
     * @return the number of keys in the symbol table strictly less than 
     * @throws IllegalArgumentException if {@code key} is {@code null}
	 * */
	public int rank(Key key) {
		if (key == null) throw new IllegalArgumentException("argument to rank() is null"); 
		int lo=0,hi=N-1;
		while(lo<=hi){
			int mid = lo+(hi-lo)/2;
			int cmp = key.compareTo(keys[mid]);
			if( cmp < 0)	hi=mid-1;
			else	if(cmp>0) lo=mid+1;
			else	return mid;
		}
		return lo;
	}
	
	/**
     * Does this symbol table contain the given key?
     *
     * @param  key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *         {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public boolean contains(Key key) {
		if(key==null) throw new IllegalArgumentException("argument to contains() is null");
		return get(key)!=null;
	}
	
	/**
	 * 在N个键的有序数组中进行二分查找最多需要(lgN+1)次比较(无论是否成功).向大小为N的有序数组中插入
	 * 个新的元素在最坏情况下需要访问~2N次数组,因此向一个空符号表中插入N个元素在最坏情况下需要访问~N^2
	 * 次数组0000000000000000000000000000000000000000000000000
	 * Returns the number of keys in this symbol table strictly less than {@code key}.
     *
     * @param  key the key
     * @return the number of keys in the symbol table strictly less than 
     * @throws IllegalArgumentException if {@code key} is {@code null}
	 * */
	public int rank(Key key,int lo,int hi){
		if (key == null) throw new IllegalArgumentException("argument to rank() is null"); 
		if(lo>hi)return lo;
		int mid=lo+(hi-lo)/2;
		int cmp=key.compareTo(keys[mid]);
		if(cmp<0)		return rank(key, lo, mid-1);
		else if(cmp>0)	return rank(key, mid+1, hi);
		else return mid;
	}
	
	/**
	 * 只要给定的键存在于表中,rank()方法就能够精确地告诉我们到哪里去更新它的值,以及当键不在表中时将键存储到表中的
	 * 何处.我们将所有更大的键向后移动一格来腾出位置(从后向前移动)并将给定的键值对分别插入到各自数组中的合适位置.
     * Removes the specified key and its associated value from this symbol table
     * (if the key is in this symbol table).
     *
     * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public void put(Key key,Value val){
		if (key == null) throw new IllegalArgumentException("first argument to put() is null"); 
        if (val == null) {
            delete(key);
            return;
        }
        //rank()方法就能够精确地告诉我们到哪里去更新它的值
		int i = rank(key);
		// key is already in table
		if(i<N&&keys[i].compareTo(key)==0){
			vals[i]=val;		//命中更新
			return;
		}
		// insert new key-value pair
		if(N == keys.length) resize(2*keys.length);
		for (int j = N; j >i; j--) {
			keys[j]=keys[j-1];
			vals[j]=vals[j-1];
		}
		keys[i]=key;
		vals[i]=val;
		N++;
	}
	
	/**
     * Returns true if this symbol table is empty.
     *
     * @return {@code true} if this symbol table is empty;
     *         {@code false} otherwise
     */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/**
     * Removes the specified key and associated value from this symbol table
     * (if the key is in the symbol table).
     *
     * @param  key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public void delete(Key key){
		if (key == null) throw new IllegalArgumentException("argument to delete() is null"); 
        if (isEmpty()) return;
    
        int i = rank(key);
        
        if(i==N || keys[i].compareTo(key)!=0) return;
        
        for (int j = i; j < N-1; j++) {
			keys[j]=keys[j+1];
			vals[j]=vals[j+1];
		}
        N--;
        keys[N] = null;
        vals[N] = null;
        if(N>0 && N == keys.length/4) resize(keys.length/2);
        assert check();
	}

	/**
     * Removes the smallest key and associated value from this symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
	public void deleteMin(){
		if (isEmpty()) throw new NoSuchElementException("Symbol table underflow error");
        delete(min());
	}
	
	/**
     * Removes the largest key and associated value from this symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
	public void deleteMax(){
		if (isEmpty()) throw new NoSuchElementException("Symbol table underflow error");
        delete(max());
	}
	
	/***************************************************************************
	    *  Ordered symbol table methods.
	    ***************************************************************************/
	
	/**
     * Returns the smallest key in this symbol table.
     *
     * @return the smallest key in this symbol table
     * @throws NoSuchElementException if this symbol table is empty
     */
	public Key min(){
		if (isEmpty()) return null;
		return keys[0];
	}
	
	/**
     * Returns the largest key in this symbol table.
     *
     * @return the largest key in this symbol table
     * @throws NoSuchElementException if this symbol table is empty
     */
	public Key max(){
		if(isEmpty()) return null;
		return keys[N-1];
	}
	
	/**
     * Return the kth smallest key in this symbol table.
     *
     * @param  k the order statistic
     * @return the kth smallest key in this symbol table
     * @throws IllegalArgumentException unless {@code k} is between 0 and
     *        <em>n</em> &minus; 1
     */
	public Key select(int k){
		if(k < 0 || k >= N) return null;
		return keys[k];
	}
	
	/**
	 * 返回在符号表中比key大的最小的key1
     * Returns the smallest key in this symbol table greater than or equal to {@code key}.
     *
     * @param  key the key
     * @return the smallest key in this symbol table greater than or equal to {@code key}
     * @throws NoSuchElementException if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public Key ceiling(Key key){
		if (key == null) throw new IllegalArgumentException("argument to ceiling() is null"); 
		int i = rank(key);
		if (i == N) return null;
		return keys[i];
	}
	
	/**
	 * 返回在符号表中比key小的最大那key1
     * Returns the largest key in this symbol table less than or equal to {@code key}.
     *
     * @param  key the key
     * @return the largest key in this symbol table less than or equal to {@code key}
     * @throws NoSuchElementException if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public Key floor(Key key){
		if (key == null) throw new IllegalArgumentException("argument to floor() is null"); 
		int i = rank(key);
		if(i < N && key.compareTo(keys[i]) == 0) return keys[i];
		else return keys[i-1];
	}
	
	/**
     * Returns the number of keys in this symbol table in the specified range.
     *
     * @param lo minimum endpoint
     * @param hi maximum endpoint
     * @return the number of keys in this symbol table between {@code lo} 
     *         (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *         is {@code null}
     */
	public int size(Key lo,Key hi){
		if (lo == null) throw new IllegalArgumentException("first argument to size() is null"); 
		if (hi == null) throw new IllegalArgumentException("second argument to size() is null"); 
		if(lo.compareTo(hi)>0) return 0;
		if(contains(hi))	return rank(hi) - rank(lo)+1;
		else				return rank(hi) - rank(lo); 
	}
	
	/**
     * Returns all keys in this symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *
     * @return all keys in this symbol table
     */
    public Iterable<Key> keys() {
        return keys(min(), max());
    }
    
    /**
     * Returns all keys in this symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *
     * @return all keys in this symbol table
     */
    public Iterable<Key> keys(Key lo,Key hi) {
    	if (lo == null) throw new IllegalArgumentException("first argument to keys() is null"); 
        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");
        Queue<Key> queue = new Queue<Key>();
        if(lo.compareTo(hi)>0)return queue;
        for (int  i= rank(lo);  i < rank(hi); i++)
        	queue.enqueue(keys[i]);
        if(contains(hi))
        	queue.enqueue(hi);
        return queue;
    }
    
    /***************************************************************************
     *  Check internal invariants.
     ***************************************************************************/
    
	private boolean check() {
		return isShort() && rankCheck();
	}

	// check that rank(select(i)) = i
	private boolean rankCheck() {
		for (int i = 0; i < size(); i++)
			if(i!=rank(select(i)))	return false;
		for (int i = 0; i < size(); i++)
			if(keys[i].compareTo(select(rank(keys[i])))!=0) return false;
		return true;
	}
	
	// are the items in the array in ascending order?
	private boolean isShort() {
		for (int i = 1; i < size(); i++)
			if(keys[i].compareTo(keys[i-1])<0) return false;
		return true;
	}
	
	/**
     * Unit tests the {@code BinarySearchST} data type.
     *
     * @param args the command-line arguments
     */
	public static void main(String[] args) {
		BinarySearchST<String, Integer> st = new BinarySearchST<String,Integer>();
		String[] strings = new String[]{"S","E","A","R","C","H","E","X","A","M","P","L","E"};
		int i =0;
		for (String key : strings)
			st.put(key,i++);
		for (String s: st.keys())
			System.out.println(s+" "+st.get(s));
	}
}