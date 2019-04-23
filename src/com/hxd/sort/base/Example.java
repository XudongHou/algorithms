package com.hxd.sort.base;

/**
 * 候旭东 20161216 排序算法类模板，将排序代码方法放在类的sort()方法,less()进行
 * 对元素的比较,和exch()的方法实现元素交换
 * 
 * compareTo()必须实现一个全序关系
 *   自反性
 *   反对称性
 *   传递性
 * */
@SuppressWarnings("rawtypes")
public abstract class Example {
	
	/**
	 * 比较
	 * */
	
	@SuppressWarnings("unchecked")
	protected static boolean less(Comparable v,Comparable w){
		return v.compareTo(w)<0;
	}
	/**
	 * 交换
	 * */
	protected static void exch(Comparable[] a,int i,int j){
		Comparable t = a[i];
		a[i]=a[j];
		a[j]=t;
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
}
