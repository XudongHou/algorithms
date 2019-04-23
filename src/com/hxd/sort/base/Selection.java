package com.hxd.sort.base;

/**
 * 候旭东 20161216
 * 选择排序 找到数组中最小的那个元素,其次,将它和数组中的第一个元素交换位置(如果第一个元素就是最小的元素那么就和自己交换)
 * 再次,在剩下的元素中找到最小的元素,将它与数组的第二个元素交换位置,如此反复
 * */
@SuppressWarnings("rawtypes")
public class Selection extends Example{

	/**
	 * 对于长度N的数组,选择排序需要大约N^2次比较和N次交换
	 * 
	 * 运行时间和输入无关.为了找出最小的元素而扫描一遍数组并不能为下一遍扫描提供什么信息.这种性质在某些情况下是缺点,因为
	 * 一个有序的数组或是主键全部相等的数组和一个随机排列的数组所用的排序时间一样长
	 * 数据移动是最少的每次交改变两个数组元素的值,因此选择排序用了N次交换-交换次数和数组的大小是线性关系其他的算法都不具
	 * 备这个特性
	 * */
	public static void sort(Comparable[] a) {
		int N=a.length;
		for (int i = 0; i < N; i++) {
			int min=i;
			for (int j = i+1; j < N; j++) {
				if (less(a[j], a[min]))
					min=j;
				exch(a, i, min);
			}
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
