package com.hxd.search.hashlTable;

import com.hxd.base.Queue;
import com.hxd.search.symbolTables.SequentialSearchST;

/**
 * 	假设J:使用的散列函数能够均匀并独立地将所有的键散布于0到M-1之间
 *  <p>
 *  The {@code SeparateChainingHashST} class represents a symbol table of generic
 *  key-value pairs.
 *  It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods.
 *  It also provides a <em>keys</em> method for iterating over all of the keys.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be {@code null}—setting the
 *  value associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  This implementation uses a separate chaining hash table. It requires that
 *  the key type overrides the {@code equals()} and {@code hashCode()} methods.
 *  The expected time per <em>put</em>, <em>contains</em>, or <em>remove</em>
 *  operation is constant, subject to the uniform hashing assumption.
 *  The <em>size</em>, and <em>is-empty</em> operations take constant time.
 *  Construction takes constant time.
 *  <p>
 *  一个散列函数能够将键转化为数组索引.散列算法的第二步是碰撞处理,也就是处理两个或者多个键的散列值相同的情况.一种直接
 *  的方法是将大小为M的数组中的每个元素都指向一条链表,链表中的每个结点都存储了散列值为该元素的索引的键值对.这种方法称
 *  之为拉链法.
 *  <p>
 *  因为发生冲突的元素都都被存储在链表中.这个方法的基本思想就是选择足够大的M,使得所有的链表都尽可能短以保证高效的查找
 *  查找分为两步:首先根据散列值找到对应的链表,然后沿着链表的顺序查找相应的键.一般采用的简单方法(效率较低)为M个元素分
 *  别构建符号表来保存散列到这里的键,这样也可以重用之前的代码
 * 	<p>
 * 	使用M条链表保存N个键,无论键在各个链表中的分布如何,链表的平均长度肯定是n/m,在一般情况下可以由它验证假设J并且可以
 * 依赖这种高效的查找和插入实现
 * 	<P>
 * 	@author 候旭东 20170215
 * */

public class SeparateChainingHashST<Key, Value> {
	private static final int INIT_CAPACITY = 4;
	
	private int n;									//键值对总数
	private int m;									//散列表的大小
	private SequentialSearchST<Key, Value>[] st;	//存放链表对象的数组
    
	/**
     * Initializes an empty symbol table.
     */
	public SeparateChainingHashST() {
		this(INIT_CAPACITY);
	}


    /**
     * Initializes an empty symbol table with {@code m} chains.
     * @param m the initial number of chains
     */
	@SuppressWarnings("unchecked")
	public SeparateChainingHashST(int m) {
		this.m = m;
		st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[m];
		for (int i = 0; i < m; i++)
			st[i] = new SequentialSearchST<>();
	}
	
	private void resize(int chains) {
		SeparateChainingHashST<Key, Value> temp = new SeparateChainingHashST<>(chains);
		for (int i = 0; i < m; i++) {
			for (Key key : st[i].keys()) {
				temp.put(key,st[i].get(key));
			}
		}
		this.m = temp.m;
		this.n = temp.n;
		this.st = temp.st;
	}
	
	private int hash(Key key) {
		/**
		 *	屏蔽符号位将一个32位整数变为一个31位非负数,然后用除留余数法计算它除以m的余数,
		 *	一般会将数组的大小取为素数以充分利用原散列值的所有位
		 */
		return (key.hashCode() & 0x7fffffff) % m; 
	}
	

    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
	public int size() {
		return n;
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
     * Returns true if this symbol table contains the specified key.
     *
     * @param  key the key
     * @return {@code true} if this symbol table contains {@code key};
     *         {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public boolean contains(Key key) {
		if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
	}
	
	/**
     * Returns the value associated with the specified key in this symbol table.
     *
     * @param  key the key
     * @return the value associated with {@code key} in the symbol table;
     *         {@code null} if no such value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public Value get(Key key) {
		if (key == null) throw new IllegalArgumentException("argument to get() is null");
		int i = hash(key);
		return st[i].get(key);
	}
	
	/**
     * Inserts the specified key-value pair into the symbol table, overwriting the old 
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public void put(Key key, Value value) {
		if (key == null) throw new IllegalArgumentException("first argument to put() is null");
		if (value == null) {
			delete(key);
			return;
		}
		
		if (n >= 10*m)
			resize(2*m);
		int i = hash(key);
		if(!st[i].contains(key)) n++;
		st[i].put(key, value);
	}
	
	/**
     * Removes the specified key and its associated value from this symbol table     
     * (if the key is in this symbol table).    
     *
     * @param  key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public void delete(Key key) {
		if (key == null) throw new IllegalArgumentException("argument to delete() is null");
		
		int i = hash(key);
		if (st[i].contains(key)) n--;
		st[i].delete(key);
		
		if (m > INIT_CAPACITY && n <= 2*m) resize(m/2);
	}
	
	public Iterable<Key> keys() {
		Queue<Key> queue = new Queue<Key>();
		for (int i = 0; i < m; i++){
			for (Key key : st[i].keys())
				queue.enqueue(key);
		}
		return queue;
	}
	
	public static void main(String[] args) {
		SeparateChainingHashST<String, Integer> st = new SeparateChainingHashST<String,Integer>();
		String[] strings = new String[]{"S","E","A","R","C","H","E","X","A","M","P","L","E"};
		int i = 0;
		for(String string : strings)
			st.put(string, i++);
		 for (String s : st.keys()) 
	            System.out.println(s + " " + st.get(s)); 
		 System.out.println(st.size());
	}
}
