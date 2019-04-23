package com.hxd.search.symbolTables;

/**
 * @author 候旭东 20161227 开发一个符号表的实现ArraySt,使用(无序)数组来实现
 * */

public class ArrayST<Key, Value> {
	private static final int INIT_SIZE = 8;
	
	private Value[] vals;
	private Key[] keys;
	private int n = 0;
	
	@SuppressWarnings("unchecked")
	public ArrayST() {
		keys = (Key[]) new Comparable[INIT_SIZE];
		vals = (Value[]) new Object[INIT_SIZE];
	}
	
	public int size(){
		return n;
	}
	
	public boolean isEmpty(){
		return size() == 0;
	}
	@SuppressWarnings("unchecked")
	private void resize(int capacity){
		Key[] tempk = (Key[]) new Comparable[capacity];
		Value[] tempv = (Value[]) new Object[capacity];
		for (int i = 0; i < n; i++) {
			tempk[i] = keys[i];
			tempv[i] = vals[i];
		}
		keys=tempk;
		vals=tempv;
	}
	
	public void put(Key key,Value val){
		delete(key);
		
		if(n>= vals.length) resize(2*n);
		
		vals[n] = val;
		keys[n] = key;
		n++;
	}
	
	public Value get(Key key){
		for (int i = 0; i < size(); i++)
			if(keys[i].equals(key))return vals[i];
		return null;
	}

	public void delete(Key key) {
		for (int i = 0; i < size(); i++) {
			if(key.equals(keys[i])){
				keys[i] = keys[n-1];
				vals[i] = vals[n-1];
				keys[n-1] = null;
				vals[n-1] = null;
				n--;
				if(n>0 && n == keys.length/4) resize(keys.length/2);
				return;
			}
		}
	}
}
