package com.hxd.strings.compression;

import com.hxd.introcs.stdlib.BinaryStdOut;

/**
 * 不存在能够压缩任意比特流的算法.
 * 
 * {@code RandomBits}是未随机生成器的一个实例,通过ASCII文本编写生成程序来进行压缩,
 * 通过读取并运行该程序来展开被压缩字符串的压缩算法能够取得0.3%的压缩率
 * @author houxu_000 20170314
 *
 */

public class RandomBits {
	public static void main(String[] args) {
		int x = 1111;
		for (int i = 0; i < 10000000; i++) {
			x = x * 314159 + 218281;
			BinaryStdOut.write(x > 0);
		}
		BinaryStdOut.close();
	}
}
