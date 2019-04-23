package com.hxd.base;

import com.hxd.introcs.stdlib.In;

/**
 * 候旭东 20161213统计和为0的元组的数量
 * */

public class ThreeSum {
	public static int count(int[] a){
		int N = a.length;
		int cnt=0;
		for (int i = 0; i < N; i++)
			for (int j = i+1; j < N; j++)
				for (int j2 = j+1; j2 < N; j2++)
					if(a[i]+a[j]+a[j2]==0)
						cnt++;
		return cnt;
	}
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		int[] a =  In.readInts(args[0]);
		System.out.println(count(a));
	}
}
