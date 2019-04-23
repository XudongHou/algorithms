package com.hxd.sort.base;

/**
 * 候旭东 20161216 插入排序
 * 为了给插入的元素腾出空间,我们需要将其余所有元素在插入之前向右移动一位
 * 当前索引左边的所有元素都是有序的,但它们的最终位置还不确定,为了给更小的元素腾出空间,他们可能会被移动
 * 但是当前索引到达数组的右端时,数组的排序就完成了.
 * 和选择排序不同的是,插入排序所需的时间取决于输入中元素的初始顺序.
 * */
@SuppressWarnings("rawtypes")
public class Insertion extends Example{
	/**
	 * 对于随机排序的长度为N且主键不重复的数组,平均情况下插入排序需要~N^2/4次比较以及~N^2/4次交
	 * 换.最坏情况下需要~N^2/2次比较和~N^2/2交换最好情况下需要N-1次比较和0次交换
	 * 插入排序对于实际应中常见的某些类型的非随机数组很有效
	 * 更一般的情况是部分有序的数组,如果数组中倒置的数量小于数组大小的某个倍数,那么我们说这个数组是部
	 * 分有序的,典型的部分有序的数组:
	 *    数组中的每个元素距离它的最终位置都不远;
	 *    一个有序的大数组接一个小数组
	 *    数组中只有几个元素的位置不正确
	 * 插入排序对这样的数组很有效
	 * 插入排序需要的交换操作和数组中倒置的数量相同,需要的比较次数大于等于倒置的数量,小于等于倒置的数量
	 * 加上数组的大小再减一
	 * */
	public static void sort(Comparable[] a){
		int N=a.length;
		for (int i = 0; i < N; i++) {
			for (int j = i; j >0 && less(a[j],a[j-1]); j--)
				exch(a, j, j-1);
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
