package com.hxd.base;

import com.hxd.introcs.stdlib.StdRandom;

/**
 * 候旭东 20161214 倍率实验 能够计算每次实验和上一次的运行时间的比值(比值会趋于一个常数)
 * 运行时间的增长数量级约为N^b
 * 符合运行时间为aN^b的猜想
 * */

public class DoublingRatio {
	public static double timeTrial(int N){
		int MAX=1000000;
		int[] a = new int[N];
		for (int i = 0; i < N; i++)
			a[i]=StdRandom.uniform(-MAX, MAX);
		Stopwatch timer = new Stopwatch();
		int cnt = ThreeSumFast.count(a);
		System.out.print(cnt+" ");
		return timer.elapsedTime();
	}
	 
	public static void main(String[] args) {
		double prev = timeTrial(125);
		for (int  N=250;true; N+=N) {
			double time = timeTrial(N);
			System.out.printf("%6d %7.1f ",N,time);
			System.out.printf("%5.1f\n",time/prev);
			prev=time;
		}
	}
}
