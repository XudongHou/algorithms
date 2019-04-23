package com.hxd.strings.radix;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.introcs.stdlib.In;

/**
 * 高位优先的字符串排序
 * {@code MSD}类提供了用于排序的静态方法使用MSD基数排序的扩展ASCII字符串或整数数组。
 * <p>
 * 首先用键索引计数法将所有字符串按照字母顺序排序,然后再将所有字符串的首字母所对应的子数组排序和快速排序一样,高位优先的
 * 字符串排序会将数组切分为能够独立排序的子数组来完成排序任务,但它的切分会为每个首字母得到一个子数组,而不是像快速排序
 * {@link com.hxd.sort.quick.Quick}那样产生固定的两个或者三个切分
 * <p>
 * 要将基于大小为R的字母表的N个字符串排序,高位优先的字符串排序算法平均需要检查Nlog r N个字符.(随机输入亚线性时间);
 * 要将基于大小为R的字母表的N个字符串排序,高位优先的字符串排序算法访问数组的次数在8N + 3R到~7wN+3wR之间,其中w是
 * 字符串的平均长度;
 * 要将基于大小为R的字母表的N个字符串排序,最坏情况下高位优先的字符串排序算法所需的空间与R乘以最长的字符串的长度之积成正
 * 比(再加上N)
 * <p>
 * @author houxu_000 20170227
 * */

public class MSD {
	private static final int BITS_PRE_BYTE = 8;
	private static final int BITS_PRE_INT = 32;		//each Java int is 32 bits 
	private static final int R = 256;				//extended ASCII alphabet size
	private static final int CUTOFF = 15;			//cutoff to insertion sort
	
	private MSD() {}
	
	/**
     * Rearranges the array of extended ASCII strings in ascending order.
     *
     * @param a the array to be sorted
     */
	public static void sort(String[] a) {
		int n = a.length;
		String[] aux = new String[n];
		sort(a, 0, n-1, 0, aux);
	}

	// return dth character of s, -1 if d = length of string
	public static int charAt(String s, int d) {
		assert d >= 0 && d <= s.length();
		if (d == s.length()) return -1;
		return s.charAt(d);
	}
	
	// sort from a[lo] to a[hi], starting at the dth character
	private static void sort(String[] a, int lo, int hi, int d, String[] aux) {
		//对于小型字母表使用插入排序
		if (hi <= lo + CUTOFF) {
			insertion(a, lo, hi, d);
			return;
		}
		
		//频率统计
		int[] count = new int[R+2];
		for (int i = lo; i <= hi; i++) {
			int c = charAt(a[i], d);
			count[c+2]++;
		}
		
		//统计频率转成索引
		for (int r = 0; r < R+1; r++)
			count[r+1] += count[r];
		
		//数据分类
		for (int i = lo; i <= hi; i++) {
			int c = charAt(a[i], d);
			aux[count[c+1]++] = a[i];
		}
		
		//回写
		for (int i = lo; i <= hi; i++)
			a[i] = aux[i - lo];
		
		//递归的以每个字符为键进行排序 哨兵-1除外
		for (int r = 0; r < R; r++)
			sort(a, lo + count[r], lo + count[r+1] - 1, d+1, aux);
	}
	
	// insertion sort a[lo..hi], starting at dth character
	private static void insertion(String[] a, int lo, int hi, int d) {
		for (int i = lo; i <= hi; i++)
			for (int j = i; j > lo && less(a[j], a[j-1], d); j--)
				exch(a, j, j-1);
	}
	// is v less than w, starting at character d
	private static boolean less(String v, String w, int d) {
		 // assert v.substring(0, d).equals(w.substring(0, d));
		for (int i = d; i < Math.min(v.length(), w.length()); i++) {
			if (v.charAt(i) < w.charAt(i))	return true;
			if (v.charAt(i) > w.charAt(i))	return false;
		}
		return v.length() < w.length();
	}

	private static void exch(String[] a, int i, int j) {
		String temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	
	/**
     * Rearranges the array of 32-bit integers in ascending order.
     * Currently assumes that the integers are nonnegative.
     *
     * @param a the array to be sorted
     */
	public static void sort(int[] a) {
		int n = a.length;
		int [] aux = new int[n];
		sort(a, 0, n-1, 0, aux);
	}

	private static void sort(int[] a, int lo, int hi, int d, int[] aux) {
		if (hi <= lo + CUTOFF) {
			insertion(a, lo, hi, d);
			return;
		}
		
		//计算频率计数（需要R = 256）
		int[] count = new int[R+1];
		int mask = R-1;	//0xFF;
		int shift = BITS_PRE_INT - BITS_PRE_BYTE*d - BITS_PRE_BYTE;
		for (int i = lo; i <= hi; i++) {
			int c = (a[i] >> shift) & mask;
			count[c + 1]++;
		}
		
		//频率转换成索引
		for (int r = 0; r < R; r++)
			count[r + 1] += count[r];
		//分类
		for (int i = lo; i <= hi; i++) {
			int c = (a[i] >> shift) & mask;
			aux[count[c]++] = a[i];
		}
		//回写
		for (int i = lo; i <= hi; i++)
			a[i] = aux[i - lo];
		//没有更多的位
		if (d == 4)	return;
		
		//对每个字符递归排序
		if (count[0] > 0)
			sort(a, lo, lo + count[0] - 1, d+1, aux);
		for (int r = 0; r < R; r++)
			if (count[r+1] > count[r])
				sort(a, lo + count[r], lo + count[r+1] - 1, d+1,aux);
	}

	private static void insertion(int[] a, int lo, int hi, int d) {
		 for (int i = lo; i <= hi; i++)
	            for (int j = i; j > lo && a[j] < a[j-1]; j--)
	                exch(a, j, j-1);
	}

	private static void exch(int[] a, int i, int j) {
		int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
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
