package com.hxd.base;

import com.hxd.introcs.stdlib.StdOut;
import com.hxd.introcs.stdlib.StdRandom;

/**
 * 可视化累加器测试
 * */

public class TestVisualAccumulator {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		int T = 1000;
		VisualAccumulator a = new VisualAccumulator(T, 1.0);
		for (int i = 0; i < T; i++) 
			a.addDataValue(StdRandom.random());
		StdOut.println(a);
	}
}
