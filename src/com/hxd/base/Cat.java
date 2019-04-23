package com.hxd.base;

import com.hxd.introcs.stdlib.In;
import com.hxd.introcs.stdlib.Out;

/**
 * 候旭东 20161211 
 * 一个程序可以处理多个输入流,输出流和图像 In Out Draw
 * */

public class Cat {
	public static void main(String[] args) {
		Out out = new Out(args[args.length-1]);
		for (int i = 0; i < args.length-1; i++) {
			In in = new In(args[i]);
			String s = in.readAll();
			out.println(s);
			in.close();
		}
		out.close();
	}
}
