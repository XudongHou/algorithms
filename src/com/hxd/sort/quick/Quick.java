package com.hxd.sort.quick;

import com.hxd.introcs.stdlib.StdRandom;
import com.hxd.sort.base.Example;

/**
 *  
 * 快速排序是一种分治的排序算法.它将一个数组分成两个子数组,将两部分独立地排序.快速排序和归并排序是互补的:归并排序
 * 将数组分成两字数组分别排序,并将有序的子数组归并到整个数组数组排序;快速排序是将当两个子数组都有序时整个数组也都自
 * 然有序.在第一种情况下,递归调用发生在处理整个数组之前;在第二种情况中,递归调用发生在处理整个数组之后.在归并排序
 * 中,一个数组被等分为两半;快速排序中切分的位置取决于数组的内容
 * @author 候旭东 20161216快速排序
 * */
@SuppressWarnings("rawtypes")
public class Quick extends Example {
	private Quick() {}

	/**
	 * 将长度为N的无重复数组排序,快速排序平均需要~2NlnN次比较(以及1/6的交换)
	 * 快速排序最多需要约N^2/2次比较,但随机打乱数组能够预防这种情况
	 * @param a
	 */
	public static void sort(Comparable[] a){
		StdRandom.shuffle(a);		//消除对输入的依赖
		sort(a,0,a.length-1);
	}
	
	private static void sort(Comparable[] a, int lo, int hi) {
		if(lo>=hi)return;
		int j=partition(a,lo,hi);	//切分
		sort(a,lo,j-1);				//将左半边a[lo...j-1]排序
		sort(a,j+1,hi);				//将右半边a[j+1...hi]排序
	}
	/**
	 * 该方法的关键在于切分,这个过程使得数组满足下面三个条件
	 * 对于某个j,a[j]已经排定;
	 * a[lo]到a[j-1]中的所有元素都不大于a[j];
	 * a[j+1]到a[hi]中的所有元素都不小于a[j].
	 * 切分过程总能排定一个元素,用归纳不难证明递归能够正确地将数组排序:如果左子数组和右子数组都是有序的,那么由左子
	 * 数组(有序且没有任何元素大于切分元素),切分元素和右子数组(有序且没有任何元素小于切分元素)组成的结果数组也一
	 * 定是有序的.
	 * 切分的实现思想:
	 *   先随意地取a[lo]作为切分元素,即那个将会被排定的元素,然后我们从数组的左端开始向右扫描知道找到一个大于等于
	 *   它的元素,再从数组的右端开始向左扫描直到找到一个小于等于它的元素.两个元素显然是没有排定的,因此我们交换他们
	 *   的位置.如此继续,我们就可以保证左指针i的左侧元素都不大于切分元素,右指针j的右侧元素都不小于切分元素.当两个
	 *   指针相遇时,我们只需要将切分元素a[lo]和左子数组最右侧的元素(a[j])交换然后返回j即可
	 * 实现:
	 *   按照a[lo]的值v进行切分.当指针i和j相遇时主循环退出.在循环中,a[i]和小于v的我们增大i,a[j]大于v时我
	 *   们缩小j,然后交换a[i]和a[j]来保证i左侧的元素都不大于v,j右侧的元素都不小于v.当指针相遇时交换a[lo]和
	 *   a[j],切分结束(切分值留在a[j]中了)
	 * */
	private static int partition(Comparable[] a, int lo, int hi) {
		int i=lo,j=hi+1;	//左右扫描指针
		Comparable v=a[lo];
		while (true) {
			while(less(a[++i],v)) if(i==hi)break;//保证不越界
			while(less(v,a[--j])) if(j==lo) break;
			if(i>=j)break;
			exch(a, i, j);
		}
		exch(a, lo, j);		//将v=a[j]放入正确的位置
		return j;			//a[lo..j-1]<=a[j]<=a[j+1..hi]打成
	}
	
	/**
	 * 寻找中位数或者指定位置的数,解决部分排序问题
	 * @param a
	 * @param k
	 * @return 
	 */
	public static Comparable select(Comparable[] a, int k) {
		StdRandom.shuffle(a);
		int lo = 0, hi = a.length -1;
		while (hi > lo) {
			int j = partition(a, lo, hi);
			if (j==k)	return a[k];
			else if (j > k) hi = j - 1;
			else if (j < k) lo = j + 1;
		}
		return a[k];
	}
	
	public static void main(String[] args) {
		String[] a = new String[]{"bed","zizi","xixi","bug","dad","yes","zoo","now","for"
				,"tip","ilk","dim","tag","jot","sob","nob","sky","hut","men","egg","few","jay","owl",
				"joy","rap","gig","wee","was","wad","fee","tap","tar","dug","jam","all","bad","yet"};
		System.out.println(a.length);
		System.out.println(select(a, (a.length-1)/2));
		
		sort(a);
		assert isSorted(a);
		show(a);
		
		
	}
}
