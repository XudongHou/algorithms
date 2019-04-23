/******************************************************************************
 *  Compilation:  javac SuffixArray.java
 *  Execution:    java SuffixArray < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/63suffix/abra.txt
 *
 *  A data type that computes the suffix array of a string.
 *
 *   % java SuffixArray < abra.txt
 *    i ind lcp rnk  select
 *   ---------------------------
 *    0  11   -   0  "!"
 *    1  10   0   1  "A!"
 *    2   7   1   2  "ABRA!"
 *    3   0   4   3  "ABRACADABRA!"
 *    4   3   1   4  "ACADABRA!"
 *    5   5   1   5  "ADABRA!"
 *    6   8   0   6  "BRA!"
 *    7   1   3   7  "BRACADABRA!"
 *    8   4   0   8  "CADABRA!"
 *    9   6   0   9  "DABRA!"
 *   10   9   0  10  "RA!"
 *   11   2   2  11  "RACADABRA!"
 *
 *  See SuffixArrayX.java for an optimized version that uses 3-way
 *  radix quicksort and does not use the nested class Suffix.
 *
 ******************************************************************************/

package com.hxd.context.suffixArrays;

import java.io.File;
import java.util.Arrays;

import com.hxd.introcs.stdlib.In;

/**
 * {@code SuffixArray}类表示长度为n的字符串的后缀数组。它支持选择第i个最小后缀，
 * 获取第i个最小后缀的索引，计算第i个最小后缀与第i个最小后缀之间最长公共前缀的长度，
 * 并确定查询字符串的排名（这是后缀的数量严格小于查询字符串）。此实现使用嵌套类{@code
 * Suffix}来表示字符串的后缀（使用常量时间和空间）和{@code java.util.Arrays.sort（）}对后
 * 缀数组进行排序。索引和长度操作在最坏的情况下需要恒定的时间。 lcp操作需要与最长公共前
 * 缀的长度成比例的时间。选择操作需要与后缀长度成正比的时间，应主要用于调试。对于相同API
 * 的替代实现，请参阅{@link SuffixArrayX}，这在实践中更快（使用3向快速排序），
 * 并且使用较少的内存（不创建{@code Suffix}对象）它依赖于Java 6中存在的常量子字符串
 * 提取方法。
 * <p>
 * 重要的提示。从Oracle和OpenJDK Java 7 Update 6开始，substring（）方法在提取的
 * 子字符串的大小（而不是恒定的时间和空间）中获取线性时间和空间。 String API不提供任何方法
 * 的性能保证，包括substring（）和charAt（）。 
 * <p>
 * @author houxu_000 20170411
 *
 */

public class SuffixArray {
	private Suffix[] suffixes;
	
	public SuffixArray(String text) {
		int n = text.length();
		this.suffixes = new Suffix[n];
		for (int i = 0; i < n; i++)
			suffixes[i] = new Suffix(text, i);
		Arrays.sort(suffixes);
	}
	
	private static class Suffix implements Comparable<Suffix> {
		
		private final String text;
		private final int index;
		
		private Suffix(String text, int index) {
			this.text = text;
			this.index = index;
		}
		
		private int length() {
			return text.length() - index;
		}
		
		private int charAt(int i) {
			return text.charAt(index + i);
		}
		
		@Override
		public int compareTo(Suffix that) {
			if (this == that) return 0;
			int n = Math.min(this.length(), that.length());
			for (int i = 0; i < n; i++) {
				if (this.charAt(i) < that.charAt(i))	return -1;
				if (this.charAt(i) > that.charAt(i))	return +1;
			}
			return this.length() - that.length();
		}
		
		@Override
		public String toString() {
			return text.substring(index);
		}
	}
	
	/**
     * Returns the length of the input string.
     * @return the length of the input string
     */
	public int length() {
		return suffixes.length;
	}
	
	public String select(int i) {
		if (i < 0 || i >= suffixes.length) throw new IndexOutOfBoundsException();
		return suffixes[i].toString();
	}

	/**
     * Returns the index into the original string of the <em>i</em>th smallest suffix.
     * That is, {@code text.substring(sa.index(i))} is the <em>i</em>th smallest suffix.
     * @param i an integer between 0 and <em>n</em>-1
     * @return the index into the original string of the <em>i</em>th smallest suffix
     * @throws java.lang.IndexOutOfBoundsException unless {@code 0 <= i < n}
     */
	public int index(int i) {
		if (i < 0 || i >= suffixes.length)	throw new IndexOutOfBoundsException();
		return suffixes[i].index;
	}
	
	
	public int lcp(int i) {
		if (i < 1 || i >= suffixes.length) throw new IndexOutOfBoundsException();
		return lcp(suffixes[i], suffixes[i-1]);
	}
	//S和T的最长公共前缀
	private int lcp(Suffix s, Suffix t) {
		int n = Math.min(s.length(), t.length());
		for (int i = 0; i < n; i++)
			if (s.charAt(i) != t.charAt(i))	
				return i;
		return n;
	}
	
	public int rank(String query) {
		int lo = 0, hi = suffixes.length - 1;
		while (lo <= hi) {
			int mid = (lo + hi) / 2;
			int cmp = compare(query, suffixes[mid]);
			if (cmp < 0)	hi = mid -1;
			else if (cmp > 0)	lo = mid + 1;
			else return mid;
		}
		return lo;
	}

	private int compare(String query, Suffix suffix) {
		int n = Math.min(query.length(), suffix.length());
		for (int i = 0; i < n; i++) {
			if (query.charAt(i) < suffix.charAt(i))	return -1;
			if (query.charAt(i) > suffix.charAt(i))	return +1;
		}
		return query.length() - suffix.length();
	}
	
	public static void main(String[] args) {
		File file = new File(".\\algs4-data\\abra.txt");
		In in = new In(file);
		String s = in.readAll().replaceAll("\\s+", " ").trim();
		SuffixArray suffix = new SuffixArray(s);
		
		System.out.println("  i ind lcp rnk select");
		System.out.println("-------------------------");
		
		for (int i = 0; i < s.length(); i++) {
			int index = suffix.index(i);
			String ith = "\"" + s.substring(index, Math.min(index + 50, s.length())) + "\"";
			assert s.substring(index).equals(suffix.select(i));
			int rank = suffix.rank(s.substring(index));
			if (i == 0)
				System.out.printf("%3d %3d %3s %3d %s\n", i, index, "-", rank, ith);
			
			else {
				int lcp = suffix.lcp(i);
				System.out.printf("%3d %3d %3d %3d %s\n", i, index, lcp, rank, ith);
			}
				
				
		}
	}
}
