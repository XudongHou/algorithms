package com.hxd.base;

import java.util.Arrays;

import com.hxd.introcs.stdlib.StdRandom;

/**
 * 候旭东 20161214 使用归并排序和二分查找来找出一个输入文件中所有和为0的整数对的数量 
 * 线性对数级别解决2-sum问题
 * 算法思想是当且仅当-a[i]存在于数组中(且a[i]不为0)时,a[i]存在于某个和为0的整数
 * 对之中.要解决这个问题,首先需要将数组排序(为二分查找做准备),然后对于数组中的每个a[i]
 * ,使用BinarySearch的rank()方法对-a[i]进行二分查找
 * 如果结果为j>i,计数器就将+1.这个简单的条件测试覆盖了三种情况
 *    如果二分查找不成功则会返回-1,因此不增加计数器的值
 *    如果二分查找返回的是j>i,我们就有a[j]+a[i]=0,增加计数器的值
 *    如果返回的值j在0和i之间.我们也有a[j]+a[i]=0的情况,但不能增加计数器的值,以避免重复计数
 * */

public class TwoSumFast {
	public static int count(int[] a){
		Arrays.sort(a);
		int N = a.length;
		int cnt=0;
		for (int i = 0; i < N; i++)
			if (BinarySearch.rank(-a[i], a)>i)
				cnt++;
		return cnt;
	}
	
	public static void main(String[] args) {
		int N = 50000;
		int MAX=1000000;
		int[] a = new int[N];
		for (int i = 0; i <N ; i++)
			a[i]=StdRandom.uniform(-MAX, MAX);
		System.out.println(count(a));
	}
}
