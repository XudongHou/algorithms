package com.hxd.base;

import com.hxd.introcs.stdlib.StdRandom;

/**
 * 候旭东 20161213 DoublingTest是一个Stopwatch的更复杂的用例
 * 能够在每一步中将数组长度加倍,并打印出ThreeSum.count()处理每种输入规模所需的时间
 * */

public class DoublingTest {
	public static double timeTrial(int N){
		int MAX=1000000;
		int[] a = new int[N];
		for (int i = 0; i < N; i++)
			a[i]=StdRandom.uniform(-MAX, MAX);
		Stopwatch timer = new Stopwatch();
		int cnt = ThreeSum.count(a);
		System.out.print(cnt+" ");
		return timer.elapsedTime();
	}
	public static void main(String[] args) {
		for (int N =500 ; true; N+=N){
			double time=timeTrial(N);
			System.out.println(N+" "+time);
		}
	}
}
