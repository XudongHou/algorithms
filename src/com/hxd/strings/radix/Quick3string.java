package com.hxd.strings.radix;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.introcs.stdlib.In;
import com.hxd.introcs.stdlib.StdRandom;

/**
 * 三向字符串快速排序:
 * 根据键的首字母进行三向切分,近在中间子数组中的下一个字符(因为键的首字母都与切分字符相等)继续递归排序.
 * 使用首字母将数据切分为"小于","等于","大于"的三个子数组
 * Dijhstra的解法如"三向切分的快速排序"中极为简洁的切分代码.从左侧到右侧遍历数组一次,维护一个指针
 * lt使得a[lo..lt-1]中的元素都小于v,一个指针gt使得a[gt+1..hi]的元素都大于v,一个指针i使得
 * a[lt..i-1]中的元素都等于v,a[i..gt]中的元素都还未确定.一开始i和lo相等,我们使用Compara-
 * ble接口(而非less())对a[i]进行三向比较来直接处理以下情况:
 *    1. a[i]小于v,将a[lt]和a[i]交换,将lt和i加一;
 *    2. a[i]大于v,将a[gt]和a[i]交换,将gt减一;
 *    3. a[i]等于v,将i加一
 * 这段排序代码的切分能够将和切分元素相等的元素归位,这样它们就不会被包含在递归调用处理的子数组中.对于标准
 * <p>
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
 * @author houxu_000 20170227
 * */

public class Quick3string {
	private static final int CUTOFF  = 15;
	
	private Quick3string() {}
	
	public static void sort(String[] a) {
		StdRandom.shuffle(a);
		sort(a, 0, a.length-1, 0);
		assert isSorted(a);
	}

	private static int charAt(String a, int d) {
		assert d >= 0 && d <= a.length();
		if (d == a.length())	return -1;
		return a.charAt(d);
	}
	
	private static void sort(String[] a, int lo, int hi, int d) {
		if (hi <= lo + CUTOFF) {
			insertion(a, lo, hi, d);
			return;
		}
		
		int lt = lo ,gt = hi;
		int v = charAt(a[lo], d);
		int i = lo + 1;
		while (i <=gt) {
			int t = charAt(a[i], d);
			if (t < v)		exch(a, lt++, i++);
			else if (t > v) exch(a, i, gt--);
			else			i++;
		}
		
		// a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]. 
		sort(a,lo, lt-1, d);
		if (v >= 0) sort(a, lt, gt, d+1);
		sort(a, gt+1, hi, d);
	}
	
	private static void insertion(String[] a, int lo, int hi, int d) {
		for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j-1], d); j--)
                exch(a, j, j-1);
	}
	
	// is v less than w, starting at character d
    // DEPRECATED BECAUSE OF SLOW SUBSTRING EXTRACTION IN JAVA 7
    // private static boolean less(String v, String w, int d) {
    //    assert v.substring(0, d).equals(w.substring(0, d));
    //    return v.substring(d).compareTo(w.substring(d)) < 0; 
    // }

    // is v less than w, starting at character d
	private static boolean less(String v, String w, int d) {
		assert v.substring(0, d).equals(w.substring(0, d));
        for (int i = d; i < Math.min(v.length(), w.length()); i++) {
            if (v.charAt(i) < w.charAt(i)) return true;
            if (v.charAt(i) > w.charAt(i)) return false;
        }
        return v.length() < w.length();
	}

	private static void exch(String[] a, int i, int j) {
		String temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	

	private static boolean isSorted(String[] a) {
		 for (int i = 1; i < a.length; i++)
	            if (a[i].compareTo(a[i-1]) < 0) return false;
	        return true;
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{
					new File(".\\algs4-data\\words3.txt"),
					};
			for(File file : files) {
				In in = new In(file);
				String[] s = in.readAllStrings();
				int n = s.length;
				
				sort(s);
				
				for (int i = 0; i < n; i++)
					System.out.println(s[i]);
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
