package com.hxd.sort.merge;

import com.hxd.sort.base.Example;

/**
 * 候旭东 20161216 归并排序
 * 自上向底的归并排序
 * 归并排序,要将一个数组排序,可以先(递归地)将它分成两半分别排序,然后将结果归并起来.归并排序是的
 * 性质是它能保证将任意长度为N的数组排序所需时间和NlogN成正比:它的主要缺点则是它所需要的额外空
 * 间和N成正比
 * */
@SuppressWarnings("rawtypes")
public class Merge extends Example {
	
	/**
	 * 对于长度为N的任意数组,自顶向下的归并排序需要1/2NlgN至NlgN次比较
	 * 对于长度为N的任意数组,自顶向下的归并排序最多需要6NlgN次访问数组(2N次用于复制,2N次用来
	 * 将排好的元素移动回去,另外最多比较2N次)
	 * 只需要比遍历整个数组多个对数因子的时间就能将一个庞大的数组排序
	 * */
	public static void sort(Comparable[] a){
		Comparable[] aux = new Comparable[a.length];
		sort(a,aux,0,a.length-1);
	}
	/**
	 * 要对子数组a[lo..hi]进行排序,先将它分为a[lo..mid]和a[mid+1..hi]两部分,分别通过递归调用将
	 * 它们单独排序,最后将有序的子数组归并为最终的排序结果.
	 * 要理解归并排序就要仔细研究该方法调用的动态情况,比如:要将a[0..15]排序,sort()方法会调用自己将a[0..7]
	 * 排序,再在其中调用自己将a[0..3]和a[0..1]排序.再将a[0]和a[i]排序之后,终于才会开始将a[0]和a[1]归
	 * 并,第二次归并是a[2]和a[3],然后是a[0..1]和a[2..3],以此类推.从这段轨迹可以看到,sort()方法的作用
	 * 其实在于安排多次的merge()方法调用
	 * */
	private static void sort(Comparable[] a, Comparable[] aux,int lo, int hi) {
		if(hi<=lo)return;
		int mid=lo+(hi-lo)/2;
		sort(a,aux,lo,mid);		//将左半边排序
		sort(a,aux,mid+1,hi);	//将右半边排序
		merge(a,aux,lo, mid, hi);//归并结果
	}
	
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
	
	public static void main(String[] args) {
		String[] a = new String[]{"bed","bug","dad","yes","zoo","now","for"
				,"tip","ilk","dim","tag","jot","sob","nob","sky","hut","men","egg","few","jay","owl",
				"joy","rap","gig","wee","was","wad","fee","tap","tar","dug","jam","all","bad","yet"};
		sort(a); 
		assert isSorted(a);
		show(a);
	}
}
