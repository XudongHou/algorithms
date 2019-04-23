package com.hxd.sort.quick;

import com.hxd.sort.base.Example;
import com.hxd.sort.base.InsertionX;

/**
 * 候旭东 20161220
 * 快速三向切分 用将重复元素放置于子数组两端的方式实现一个信息量最优的排序算法.使用两个索引p和q,使得a[lo..p-1]
 * 和a[q+1..hi]的元素都和a[lo]相等.使用另外两个元素i和j,使得a[p..i-1]小于小于a[lo],a[j+i..q]大于
 * a[lo].在内循环中加入代码,在a[i]和v相当时将其与a[p]交换(并将p加1),在a[j]和v相当且a[i]和a[j]尚未和v
 * 进行比较之前将其与a[q]交换.添加在切分循环结束后将和v相等的元素交换到正确位置的代码.
 * 这里实现的代码和正文中给出的代码是等价的,因为这里额外的交换用于和切分元素相等的元素,而正文中的代码将额外的交换用于
 * 和切分元素不等的元素
 * */
@SuppressWarnings("rawtypes")
public class Quick3WayX extends Example {
	//切换到插入排序,转换参数的最佳值是和系统相关的,但是5-15之间的任意值在大多数情况下都能令人满意
	private static final int INSERTION_SORT_CUTOFF = 8;
	//
	private static final int MEDIAN_OF_3_CUTOFF=40;
	private Quick3WayX(){}
	
	public static void sort(Comparable[] a){
		sort(a,0,a.length-1);
	}

	private static void sort(Comparable[] a, int lo, int hi) {
		int n=hi-lo+1;
		if(n<=INSERTION_SORT_CUTOFF){
			InsertionX.sort(a);
			return;
		}else if(n<=MEDIAN_OF_3_CUTOFF){
			int m = median3(a,lo,lo+n/2,hi);
			exch(a,m,lo);
		}//使用Tukey ninther作为分割元素
		else{
			int eps=n/8;
			int mid=lo+n/2;
			int m1=median3(a, lo,lo+eps,lo+eps+eps);
			int m2=median3(a,mid-eps,mid,mid+eps);
			int m3=median3(a,hi-eps-eps,hi-eps,hi);
			int ninther=median3(a,m1,m2,m3);
			exch(a,ninther,lo);
		}
		//Bentley-McIIroy 3路分区
		int i = lo,j=hi+1;
		int p = lo,q=hi+1;
		Comparable v =a[lo];
		/**
		 * 使用两个索引p和q,使得a[lo..p-1]
		 * 和a[q+1..hi]的元素都和a[lo]相等.使用另外两个元素i和j,使得a[p..i-1]小于小于a[lo],a[j+i..q]大于
		 * a[lo].在内循环中加入代码,在a[i]和v相当时将其与a[p]交换(并将p加1),在a[j]和v相当且a[i]和a[j]尚未和v
		 * 进行比较之前将其与a[q]交换.添加在切分循环结束后将和v相等的元素交换到正确位置的代码.
		 * */
		while(true){
			while(less(a[++i],v))	if(i==hi)	break;
			while(less(v,a[--j]))	if(j==lo)	break;
			if(i==j&&eq(a[i],v))	exch(a,++p,i);
			if(i>=j)				break;
			exch(a, i, j);
			if(eq(a[i],v))			exch(a,++p,i);//在a[i]和v相当时将其与a[p]交换(并将p加1)
			if(eq(a[j],v))			exch(a,--q,j);//在a[j]和v相当且a[i]和a[j]尚未和v进行比较之前将其与a[q]交换.
		}
		
		i=j+1;
		for (int k = lo; k <=p; k++)
			exch(a,k,j--);
		for (int k = hi; k >=q; k--)
			exch(a,k,i++);
		sort(a, lo,j);
		sort(a,i,hi);;
	}

	@SuppressWarnings("unchecked")
	private static boolean eq(Comparable v, Comparable w) {
		return v.compareTo(w)<0;
	}
	/**
	 * 返回 a[i],a[j],a[k]中间元素的索引
	 */
	private static int median3(Comparable[] a, int i, int j, int k) {
		return (less(a[i], a[j]) ?
	               (less(a[j], a[k]) ? j : less(a[i], a[k]) ? k : i) :
	               (less(a[k], a[j]) ? j : less(a[k], a[i]) ? k : i));
	}
	
	public static void main(String[] args) {
		String[] a = new String[]{"bed","all","rap","bug","dad","rap","yes","rap","zoo","now","for","rap"
				,"tip","ilk","all","rap","dim","all","rap","tag","jot","sob","nob","rap","sky","hut","all","all","men","egg","few","jay","owl",
				"joy","all","rap","gig","wee","was","wad","rap","all","all","fee","tap","rap","rap","tar","dug","jam","all","bad","yet"};
		sort(a);
		assert isSorted(a);
		show(a);
	}
}
