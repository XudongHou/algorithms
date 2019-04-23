package com.hxd.strings.radix;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.introcs.stdlib.In;

/**
 * {@code LSD}低位优先的字符串排序:从右到左检查键中的字符,最适合键的长度都相同的字符串的排序应用.
 * 对于基于R个字符串的字母表的N个以长为W的字符串为键的元素,低位优先的字符串的排序需要访问~7WN+3WR
 * 次数组,使用额外空间与N+R成正比
 * @author houxu_000 20170227
 * */

public class LSD {
	private static final int BITS_PRE_BYTE = 8;
	
	private LSD() {}
	
	/**  
     * Rearranges the array of W-character strings in ascending order.
     *
     * @param a the array to be sorted
     * @param w the number of characters per string
     */
	public static void sort(String[] a, int w) {
		int n = a.length;
		int R = 256;		//extend ASCII alphbet size
		String[] aux = new String[n];
		
		for (int d = w-1;d >= 0; d--){
			int[] count = new int[R+1];
			//频数统计 对于数组中的每个元素,都使用它的键访问count[]中相应的元素并将其加1.如果键为i,则将count[i+1]加1
			for (int i = 0; i < n; i++)
				count[a[i].charAt(d) + 1]++;
			//频数转为索引  count[]来计算每个键在排序结果中的起始索引位置,一般来说给,定的键起始索引均为所有较小的键所对应的出
			//现频率之和.对于每个键值r,小于r+1的键的频率之和为小于r的键的频率之和加上count[r],因此从左向右将count[]转
			//化为一张用于排序索引表是很容易的
			for (int r = 0; r < R; r++)
				count[r+1] += count[r];
			//数据分类
			for (int i = 0; i < n; i++)
				aux[count[a[i].charAt(d)]++] = a[i];
			//回写
			for (int i = 0; i < n; i++)
				a[i] = aux[i];
		}
	}
	
	public static void sort(int[] a) {
		final int BITS = 32;
		final int R = 1 << BITS_PRE_BYTE;
		final int MASK = R - 1;
		final int w = BITS / BITS_PRE_BYTE;
		
		int n = a.length;
		int aux[] = new int[n];
		
		for (int d = 0; d < w; d++) {
			
			int[] count = new int[R+1];
			//频率统计
			for (int i =0; i < n; i++) {
				int c = (a[i] >> BITS_PRE_BYTE*d) & MASK;
				count[c + 1]++;
			}
			
			//频率转索引
			for (int r = 0; r < R; r++)
				count[r+1] += count[r];
			// for most significant byte, 0x80-0xFF comes before 0x00-0x7F
			if (d == w-1) {
				int shift1 = count[R] - count[R/2];
				int shift2 = count[R/2];
				for (int r = 0; r < R/2; r++)
					count[r] += shift1;
				for (int r = R/2; r < R; r++)
					count[r] = shift2;
			}
			
			// 数据分类
			for (int i = 0; i < n; i++) {
				int c = (a[i] >> BITS_PRE_BYTE) & MASK;
				aux[count[c]++] = a[i];
			}
			
			// 回写
			for (int i = 0; i < n; i++)
				a[i] = aux[i];
		}
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
				
				int w = s[0].length();
				for (int i = 0; i < n; i++)
					assert s[i].length() == w : "Strings must have fixed length";
				sort(s, w);
				
				for (int i = 0; i < n; i++)
					System.out.println(s[i]);
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
