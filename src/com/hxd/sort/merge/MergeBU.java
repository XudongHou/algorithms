package com.hxd.sort.merge;

import com.hxd.sort.base.Example;

/**
 * 候旭东 20161217 归并排序
 * 自底向上的归并排序.递归实现的归并排序是算法设计中分治思想的典型应用,实现归并排序的另一种
 * 方法是先归并那些微型数组,然后再成对归并得到的子数组,这种实现方法所需要的代码会更少.首先
 * 我们进行的是两辆归并(每个元素想象成一个大小为1的数组),然后是四四归并(将两个大小为2的数
 * 组归并成为一个有4个元素的数组),然后是八八归并.最后一次归并的第二个子数组可能比第一个子
 * 数组要小(对于merge()方法不是问题)
 * 当数组长为2的幂次时,自底向上和自顶向下的归并排序所用的比较次数和数组访问次数相同,只是顺
 * 序不同.自底向上的排序比较适合用链表组织的数据.这种方法只需要重新组织链表链接就能将链表原
 * 地排序
 * */
@SuppressWarnings("rawtypes")
public class MergeBU extends Example {
	
	
	private static void merge(Comparable[] a,Comparable[] aux,int lo,int mid,int hi){
		if (less(a[mid],a[mid+1])) return; 	//只要a[mid]小于等于a[mid+1]时,就不调用merge()方法,使处理一个已有序列的比较是线性级别
		int i=lo,j=mid+1;
		for (int k = lo; k <= hi; k++)		//将a[lo..hi]复制到aux[lo..hi]
			aux[k] = a[k];
		for (int k = lo; k <= hi; k++) {		//归并回到a[lo..hi]
			if(i>mid)						a[k]=aux[j++];	//左半边用尽(取右半边的元素)
			else if(j>hi)					a[k]=aux[i++];	//右左半边用尽(取左半边的元素)
			else if(less(aux[j],aux[i]))	a[k]=aux[j++];	//右半边的当前元素小于左半边的当前元素(取右半边的元素)
			else							a[k]=aux[i++];	//左半边的当前元素小于右半边的当前元素(取左半边的元素)
		}
	}
	/**
	 * 自底向上的归并排序会多次遍历整个数组,根据子数组大小进行两两归并.子数组的大小sz的初始值为1,每次加倍.
	 * 最后一个子数组的大小只有在数组大小是sz的偶数倍的时候才会等于sz(否则会比sz小)
	 * 例如对于一个长度为15的数组进行排序
	 *  sz=1时				sz=2				sz=4				sz=8
	 * merge(a,0,0,1)		merge(a,0,1,3)		merge(a,0,3,7)		merge(a,0,7,15)
	 * merge(a,2,2,3)		merge(a,4,5,7)		merge(a,8,11,15)
	 * merge(a,4,4,5)		merge(a,8,9,11)
	 * merge(a,6,6,7)		merge(a,12,13,15)
	 * merge(a,8,8,9)
	 * merge(a,10,10,11)
	 * merge(a,12,12,13)
	 * merge(a,14,14,15)
	 * 
	 * 命题
	 *   对于长度为N的任意数组,自底向上的归并排序需要1/2NlgN至NlgN次比较,最多访问数组6NlgN次
	 * 处理一个数组的遍数正好是[lgN]即(2^n<=N<=2^(n+1)中的N).每一遍会访问数组6N次,比较次数在N/2和N之间
	 * 命题
	 *   没有任何基于比较的算法能够保证使用少于lg(N!)~NlgN次比较讲长度为N的数组排序
	 * 命题
	 *   归并排序是一种渐进最优的基于比较排序的算法
	 * */
	public static void sort(Comparable[] a){
		int N =a.length;
		Comparable[] aux=new Comparable[N];
		for (int sz = 1; sz < N; sz=sz+sz)
			for (int lo = 0; lo < N-sz; lo+=sz+sz)
				merge(a, aux,lo, lo+sz-1,Math.min(lo+sz+sz-1,N-1));
	}
	
	public static void main(String[] args) {
		String[] a = new String[]{"bed","bug","dad","yes","zoo","now","for"
				,"tip","ilk","dim","tag","jot","sob","nob","sky","hut","men","egg","few","jay","owl",
				"joy","rap","gig","wee","was","wad","fee","tap","tar","dug","jam","all","bad","yet"};
		sort(a); 
		assert isSorted(a);
		show(a);
	}
}