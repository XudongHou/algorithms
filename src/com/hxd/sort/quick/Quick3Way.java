package com.hxd.sort.quick;

import com.hxd.sort.base.Example;

/**
 * 候旭东 20161220
 * 三向切分的快速排序比归并排序和其他排序在包括重复排序很多的实际应用中更快
 * */
@SuppressWarnings("rawtypes")
public class Quick3Way extends Example{
	private Quick3Way(){}
	/**
	 * Dijhstra的解法如"三向切分的快速排序"中极为简洁的切分代码.从左侧到右侧遍历数组一次,维护一个指针
	 * lt使得a[lo..lt-1]中的元素都小于v,一个指针gt使得a[gt+1..hi]的元素都大于v,一个指针i使得
	 * a[lt..i-1]中的元素都等于v,a[i..gt]中的元素都还未确定.一开始i和lo相等,我们使用Compara-
	 * ble接口(而非less())对a[i]进行三向比较来直接处理以下情况:
	 *    1. a[i]小于v,将a[lt]和a[i]交换,将lt和i加一;
	 *    2. a[i]大于v,将a[gt]和a[i]交换,将gt减一;
	 *    3. a[i]等于v,将i加一
	 * 这段排序代码的切分能够将和切分元素相等的元素归位,这样它们就不会被包含在递归调用处理的子数组中.对于标准
	 * */
	@SuppressWarnings("unchecked")
	private static void sort (Comparable[] a,int lo,int hi){
		if(hi<=lo)return;
		int lt=lo,i=lo+1,gt=hi;
		Comparable v = a[lo];
		while(i<=gt){
			int cmp = a[i].compareTo(v);
			if(cmp<0)		exch(a,lt++,i++);	//a[i]小于v,将a[lt]和a[i]交换,将lt和i加一;
			else if(cmp>0)	exch(a,i,gt--);		//a[i]大于v,将a[gt]和a[i]交换,将gt减一;
			else			i++;				//a[i]等于v,将i加一
		}	// 现在 a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]成立
		sort(a,lo,lt-1);
		sort(a,gt+1,hi);
	}
	/**
	 * 对于只有若干不同主键的随机数组,归并排序的时间复杂度是线性对数级别,而三向切分快速排序则是线性
	 * 这些准确的结论来自于对主键概率分布的分析.给定任意一个待排序的数组,通过统计每个主键值出现的频
	 * 率就可以计算出它包含的信息量.值得一提的是,可以通过这个信息量得出三向切分的快速排序所需要的比
	 * 较次数的上下界.
	 * 命题:
	 *    不存在任何基于比较的排序算法能够保证在NH-N次比较之内将N个元素排序,其中H为由主键值出现
	 * 频率定义的香农信息量
	 *    对于大小为N的数组,三向切分的快速排序需要~(2ln2)NH次数比较.其中H为由主键值出现频率
	 * 定义的香农信息量
	 * 当所有的主键不重复时有H=lgN(所有主键的频率均为1/N),三向切分的最坏情况就是所有主键均不相同
	 * .三向切分是信息量最优的即对任意分布的输入,最优的基于比较的算法平均所需的比较次数和三向切分的快
	 * 速排序平均所需的比较次数互相处于常数因子范围内 
	 * */
	public static void sort(Comparable[] a){
		int lo=0,hi=a.length-1;
		sort(a, lo, hi);
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
