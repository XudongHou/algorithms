package com.hxd.search.symbolTables;

import com.hxd.base.Queue;

/**
 * @author 候旭东  20161226		书序查找(基于无序链表)
 * 符号表是一种存储键值对的数据结构,支持两种操作:插入(put),即将一组新的键值对存入表中:查找(get),
 * 即根据给定的键得到相应的值
 * 
 * 优点: 适用于小型问题
 * 缺点: 对于大型符号很慢
 * */

public class SequentialSearchST<Key, Value> {
	private int n;
	private Node first;
	
	private class Node{
		Key key;
		Value val;
		Node next;
		public Node(Key key,Value val,Node next) {
			this.key=key;
			this.val=val;
			this.next=next;
		}
	}
	
    /**
     * Initializes an empty symbol table.
     */
	public SequentialSearchST() {}
    
	/**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
	public int size(){return n;}
	
	/**
     * Returns true if this symbol table is empty.
     *
     * @return {@code true} if this symbol table is empty;
     *         {@code false} otherwise
     */
	public boolean isEmpty(){return size()==0;}
	
	/**
     * Returns true if this symbol table contains the specified key.
     *
     * @param  key the key
     * @return {@code true} if this symbol table contains {@code key};
     *         {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public boolean contains(Key key){
		if(key==null) throw new IllegalArgumentException("argument to contains() is null");
		return get(key)!=null;
	}
	
	/**
	 * 在含有N对键值的基于无序)链表的符号表中,未命中的查找和插入操作都需要N次比较.命中的查找在最坏情
	 * 况下需要N次比较.特别地,向一个空表中插入N个不同的键需要~N^2/2次比较
	 * 
	 * Returns the value associated with the given key in this symbol table.
     *
     * @param  key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
	 * */
	public Value get(Key key){
		if (key == null) throw new IllegalArgumentException("argument to get() is null"); 
		for (Node x = first;x!=null;x=x.next)
			if(key.equals(x.key))
				return x.val;
		return  null;
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
	public void put(Key key,Value val){
		if (key == null) throw new IllegalArgumentException("argument to put() is null"); 
		if (val == null){
			delete(key);
			return;
		}
		for (Node x =first;x!=null;x=x.next)
			if(key.equals(x.key)){
				x.val=val;		//命中,更新
				return;
			}
		first=new Node(key, val, first);//未命中,新建节点
		n++;
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
		first=delete(first,key);
	}
	
	// delete key in linked list beginning at Node x 链式删除
    // warning: function call stack too large if table is large
	private Node delete(Node x, Key key) {
		if(x==null)return null;
		if(key.equals(x.key)){
			n--;
			return x.next;
		}
		x.next=delete(x.next, key);
		return x;
	}
	
	/**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *
     * @return all keys in the symbol table
     */
	public Iterable<Key> keys(){
		Queue<Key> q = new Queue<Key>();
		for (Node x = first; x!=null; x=x.next)
			q.enqueue(x.key);
		return q;
	}
	
	/**
     * Unit tests the {@code SequentialSearchST} data type.
     *
     * @param args the command-line arguments
     */
	public static void main(String[] args) {
		SequentialSearchST<String, Integer> st = new SequentialSearchST<String,Integer>();
		String[] strings = new String[]{"S","E","A","R","C","H","E","X","A","M","P","L","E"};
		int i =0;
		for (String key : strings)
			st.put(key,i++);
		for (String s: st.keys())
			System.out.println(s+" "+st.get(s));
	}
}
