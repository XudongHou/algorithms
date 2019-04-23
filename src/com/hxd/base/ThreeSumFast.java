package com.hxd.base;

import java.util.Arrays;

import com.hxd.introcs.stdlib.StdRandom;

/**
 * 候旭东 20161214 3-sum问题的快速算法
 * 当且仅当-(a[i]+a[j])在数组中时,整数对(a[i]和a[j])为某个和为0的三元组的一部分
 * 将数组排序并进行N(N-1)/2次二分查找,每次查找所需的时间都和logN成正比.
 * 因此总运行时间和N^2logN成正比
 * */

public class ThreeSumFast {
	public static int count(int[] a){
		Arrays.sort(a);
		int N =a.length;
		int cnt=0;
		for (int i = 0; i < N; i++)
			for (int j = i+1; j < N; j++)
				if(BinarySearch.rank(-a[i]-a[j], a)>j)
					cnt++;
		return cnt;
	}
	public static void main(String[] args) {
		int N =8000;
		int MAX=1000000;
		int[] a = new int[N];
		for (int i = 0; i <N ; i++)
			a[i]=StdRandom.uniform(-MAX, MAX);
		System.out.println(count(a));
	}
}
