package com.hxd.sort.priorityQueue;


/**
 * 候旭东 20161224 堆排序
 * 把任意优先队列变成一种排序方法.将所有元素插入一个查找最小元素的优先队列,然后再重复调用删除最小元素的操作都是将它们
 * 按顺序删去.用无序数组实现的优先队列这么做相当于进行一次插入排序.用基于堆的优先队列这样做将是一种全新的排序堆排序
 * 在构造堆的构造阶段中,将原始数组重新组织安排进一个堆中;然后在下沉排序阶段.从堆中按递减顺序取出所有元素并得到排序结
 * 果.使用一个面向最大元素的优先队列并重复删除最大元素.为了排序的需要,不再将优先队列的具体表示隐藏,并将直接使用
 * swim()和sink()操作
 * */
@SuppressWarnings("rawtypes")
public class Heap {
	private Heap(){}
	/**
	 * 堆的构造:从右至左用sink()函数构造子堆.数组的每个位置都已经是一个子堆的根节点,sink()对于这些子堆也适用.如果
	 * 一个节点的两个子结点都已经是堆了,那么在该节点上调用sink()可将他们变成一个堆.这个过程会递归地建立起堆的秩序.开始
	 * 时只需要扫描一半元素.可以跳过大小为一的子堆.最后我们在位置1上调用sink()方法,扫描结束.这个阶段的目的就是构造一
	 * 个堆有序的数组并使最大元素位于数组的开头
	 * */
	public static void sort(Comparable[] pq){
		int n = pq.length;
		for (int k = n/2; k >=1; k--)
			sink(pq,k,n);
		while(n>1){
			exch(pq,1,n--);
			sink(pq,1,n);
		}
	}
	/**
	 * 最大的在最上面,下沉排序,如果子结点大于父节点交换位置
	 * */
	private static void sink(Comparable[] pq, int k, int n) {
		while(2*k<=n){
			int j=2*k;
			if(j<n&&less(pq,j,j+1))j++;
			if(!less(pq,k,j))break;
			exch(pq,k,j);
			k=j;
		}
	}
	
	private static void exch(Object[] pq, int i, int j) {
		Object swap=pq[i-1];
		pq[i-1]=pq[j-1];
		pq[j-1]=swap;
	}
	
	@SuppressWarnings("unchecked")
	private static boolean less(Comparable[] pq, int j, int i) {
		return pq[j-1].compareTo(pq[i-1])<0;
	}
	@SuppressWarnings("unchecked")
	private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }
	/**
	 * 输出
	 * */
	protected static void show (Comparable[] a) {
		for (int i = 0; i < a.length; i++)
			System.out.print(a[i]+" ");
		System.out.println();
	}
	/**
	 * 检测是否排序
	 * */
	public static boolean isSorted(Comparable[] a){
		for (int i = 0; i < a.length; i++)
			if(less(a[i], a[i-1]))
				return false;
		return true;
	}
	public static void main(String[] args) {
		String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };
		Heap.sort(strings);
        show(strings);
        assert isSorted(strings);
	}
}
