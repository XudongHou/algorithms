package com.hxd.base;

import java.util.Arrays;

/**
 * 候旭东 20161212 将二分法重写为一段面向对象的程序
 * */

public class StaticSETofInts {
	private int[] a;
	
	public StaticSETofInts(int[] keys) {
		 a=new int[keys.length];
		 for (int i = 0; i < keys.length; i++) {
			a[i]=keys[i];//保护性复制
			Arrays.sort(a);
		}
	}
	/**
	 * key是否存在于集合中
	 * */
	boolean contains(int key){return rank(key)!=-1;}
	private int rank(int key) {
		int lo=0;
		int hi=a.length-1;
		while (lo<=hi){
			int mid=lo+(hi-lo)/2;
			if(key<a[mid])hi=mid-1;
			else if(key>a[mid])lo=mid+1;
			else return mid;
		}
		return -1;
	}
	
}
