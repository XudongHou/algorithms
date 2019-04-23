package com.hxd.base;

import com.hxd.introcs.stdlib.StdRandom;

/**
 * 候旭东 20161213表示计时器的抽象数据类型 
 * */

public class Stopwatch {
	private final long start;
	public Stopwatch() {
		start=System.currentTimeMillis();
	}
	public double elapsedTime(){
		long now = System.currentTimeMillis();
		return (now-start)/1000.0;
	}
	
	public static void main(String[] args) {
		int N = 5000;
		int[] a = new int[N];
		for (int i = 0; i < N; i++)
			a[i]=StdRandom.uniform(-1000000,1000000);
		Stopwatch stopwatch = new Stopwatch();
		int cnt=ThreeSum.count(a);
		double time = stopwatch.elapsedTime();
		System.out.println(cnt+" triples "+time+" seconds");
	}
}
