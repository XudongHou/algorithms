package com.hxd.search.symbolTables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 性能测试用例
 * 从输入中得到一列字符串并记录每个(长度至少达到指定的阀值)字符串的出现次数,然后遍历所有的键并找出出现频率最高的键
 * @author 候旭东
 * */

public class FrequencyCounter {
	public static void main(String[] args) throws FileNotFoundException {
		int minlen = 1;
		StringBuilder sb = new StringBuilder();
		if(args.length>0)
			minlen = Integer.parseInt(args[0]); 
		BinarySearchST<String, Integer> st = new BinarySearchST<String,Integer>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					new File("D:\\HADOOPJAVAstation\\algorithms\\algs4-data\\tinyTale2.txt").getAbsolutePath()));
			try {
				String word;
				while((word = in.readLine())!=null)
					sb.append(word+" ");
				for(String w : sb.toString().split(" ")){
					if(w.length()<minlen)	return;
					if(!st.contains(w)) 		st.put(w, 1);
					else						st.put(w, st.get(w)+1);
				}
	
			}finally {
				in.close();
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		String max = " ";
		st.put(max, 0);
		for (String word:st.keys())
			if(st.get(word)>st.get(max))
				max=word;
		System.out.println(max + " "+ st.get(max));
	}
}
