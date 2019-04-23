package com.hxd.context.suffixArrays;

import java.io.File;

import com.hxd.introcs.stdlib.In;

/**
 * {@code SuffixArrayX}类表示长度为n的字符串的后缀数组。它支持选择第i个最小后缀，获取第i个最小后缀的索引，
 * 计算第i个最小后缀和第i个最小后缀之间最长公共前缀的长度，并确定查询字符串的等级（这是后缀的数量严格小于查询字
 * 符串）。此实现使用3向快速排序对后缀数组进行排序。对于相同API的更简单（但效率较低）的实现，请参见{@link
 *  SuffixArray}。索引和长度操作在最坏的情况下需要恒定的时间。 lcp操作需要与最长公共前缀的长度成比例的时间。
 *  选择操作需要与后缀长度成正比的时间，应主要用于调试。该实现使用'\ 0'作为哨兵，并假定字符“\ 0”不会出现在文本
 *  中。实际上，该算法运行速度非常快。然而，在最糟糕的情况下，它可能非常差（例如，由相同字符的N个副本组成的字符串
 *  ）。在排序之前不会随机排列后缀数组，因为shuffle操作相对昂贵，并且后缀开始的病理输入以不良顺序排列（例如，
 *  排序）可能是该或没有洗牌的该算法的不良输入。
 * @author houxu_000
 *
 */
public class SuffixArrayX {
	private static final int CUTOFF = 5;
	
	private final char[] text;
	private final int[]	index;
	private final int n;

	
	public SuffixArrayX(String text) {
		n = text.length();
		text = text + '\0';
		
		this.text = text.toCharArray();
		this.index = new int[n];
		for (int i = 0; i < n; i++)
			index[i] = i;
		
		sort(0, n-1, 0);
	}


	private void sort(int lo, int hi, int d) {
		if (hi <= lo + CUTOFF) {
			insertion(lo, hi, d);
			return;
		}
		
		int lt = lo, gt = hi;
		//切分点
		char v = text[index[lo] + d];
		
		int i = lo + 1;
		while (i <= gt) {
			char t = text[index[i] + d];
			if (t < v)		exch(lt++, i--);
			else if (t > v)	exch(i , gt--);
			else i++;
		}
		
		// a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]. 
		sort(lo, lt -1, d);
		if (v > 0)
			sort(lt, gt, d+1);
		sort(gt+1, hi, d);
	}

	
	private void insertion(int lo, int hi, int d) {
		for (int i = lo; i <= hi; i++)
			for (int j = i; j > lo && less(index[j], index[j-1], d);j--)
				exch(i, j-1);
	}

	private boolean less(int i, int j, int d) {
		if (i == j)	return false;
		i = j + d;
		j = j + d;
		while (i < n && j < n) {
			if (text[i] < text[j])	return true;
			if (text[i] < text[j])	return false;
			i++;
			j++;
		}
		return i > j;
	}

	 // exchange index[i] and index[j]
	private void exch(int i, int j) {
		int swap = index[i];
		index[i] = index[j];
		index[j] = swap;
	}
	
	public int length() {
		return n;
	}
	
	public int index(int i) {
		if (i < 0 || i >= n) throw new IndexOutOfBoundsException();
        return index[i];
    }
	
	public int lcp(int i) {
		if (i < 1 || i >= n) throw new IndexOutOfBoundsException();
        return lcp(index[i], index[i-1]);
	}
	
	public int lcp(int i, int j) {
		int length = 0;
		while (i < n && j < n) {
			if(text[i] != text[j])	return length;
			i++;
			j++;
			length++;
		}
		return length;
	}
	
	public String select(int i) {
		if (i < 0 || i >= n) throw new IndexOutOfBoundsException();
		return new String(text, index[i], n - index[i]);
	}
	
	public int rank(String query) {
		int lo = 0, hi = n-1;
		while (lo <= hi) {
			int mid = lo + (hi - lo) / 2;
			int cmp = compare(query, index[mid]);
			if (cmp < 0) hi = mid - 1;
			else if (cmp > 0)	lo = mid + 1;
			else return mid;
		}
		return lo;
	}


	private int compare(String query, int i) {
		int m = query.length();
		int j = 0;
		while (i < n && j < m) {
			if (query.charAt(j) != text[i])
				return query.charAt(j) - text[i];
			i++;
			j++;
		}
		if (i < n)	return -1;
		if (j < m)	return +1;
		return 0;
	}
	
	public static void main(String[] args) {
		File file = new File(".\\algs4-data\\abra.txt");
		In in = new In(file);
		String s = in.readAll().replaceAll("\n", " ").trim();
		
		SuffixArrayX suffixArrayX1 = new SuffixArrayX(s);
		SuffixArrayX suffixArrayX2 = new SuffixArrayX(s);
		
		boolean check = true;
		for (int i = 0; check && i < s.length(); i++) {
			if (suffixArrayX1.index(i) != suffixArrayX2.index(i)) {
				System.out.println("suffix1(" + i + ") = " + suffixArrayX1.index(i));
				System.out.println("suffix2(" + i + ") = " + suffixArrayX2.index(i));
				
				String ith = "\"" + s.substring(suffixArrayX1.index(i), Math.min(suffixArrayX1.index(i) + 50, s.length())) + "\"";
				String jth = "\"" + s.substring(suffixArrayX2.index(i), Math.min(suffixArrayX2.index(i) + 50, s.length())) + "\"";
				System.out.println(ith);
				System.out.println(jth);
				check = false;
			}
		}
		
		System.out.println("  i ind lcp rnk  select");
		System.out.println("---------------------------");
		
		for (int  i = 0; i < s.length(); i++) {
			int index = suffixArrayX2.index(i);
			String ith = "\"" + s.substring(suffixArrayX2.index(i), Math.min(suffixArrayX2.index(i) + 50, s.length())) + "\"";
			int rank = suffixArrayX2.rank(s.substring(index));
			assert s.substring(index).equals(suffixArrayX2.select(i));
			if (i == 0) 
				System.out.printf("%3d %3d %3s %3d  %s\n", i, index, "-", rank, ith);
			else {
				int lcp = suffixArrayX2.lcp(i);
				System.out.printf("%3d %3d %3d %3d  %s\n", i, index, lcp, rank, ith);
			}
		}
	}
}
