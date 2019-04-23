package com.hxd.base;

import com.hxd.introcs.stdlib.StdIn;

/**
 * 候旭东 20161211 简单的字符串处理方法Util
 * */

public class SimpleStringHandle {
	
	/**
	 * 判断字符串是否是一条回文
	 * */
	public static boolean isPalindrome(String s){
		int N = s.length();
		for (int i = 0; i < N/2; i++)
			if(s.charAt(i)!=s.charAt(N-1-i))
				return false;
		return true;
	}
	
	/**
	 * 从一个命令行参数中提取文件名和拓展名
	 * */
	public static String fileName(String file){
		int dot=file.lastIndexOf(".");
		return file.substring(0,dot);
	}
	
	public static String fileType(String file){
		int dot=file.lastIndexOf(".");
		return file.substring(dot+1,file.length());
	}
	
	/**
	 * 打印出标准输入中所有含有通过命令行指定的字符串的行
	 * */
	public static void printString(String query){
		while (!StdIn.isEmpty()) {
			String s = StdIn.readLine();
			if (s.contains(query))
				System.out.println(s);
		}
	}
	
	/**
	 * 以空白字符为分隔符从StdIn中创建一个字符串数组
	 * \\s+正则表达式忽略一个或多个制表符,空格,换行符或回车
	 * */
	public static String[] create(String string){
		return string.split("\\s+");
	}
	
	/**
	 * 检查一个字符串数组中的元素是否已按照字母顺序排列
	 * */
	public boolean isSorted(String[] a){
		for (int i = 1; i < a.length; i++) {
			if(a[i-1].compareTo(a[i])>0)return false;
		}
		return true;
	}
}
