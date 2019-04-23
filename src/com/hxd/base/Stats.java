package com.hxd.base;

import com.hxd.introcs.stdlib.StdOut;

/**
 * 候旭东 20161212 背包(bag)是一种不支持从中删除元素的集合数据类型--它的目的就是帮助用例收集
 * 元素并迭代遍历所有收集到的元素(用例也可以检查背包是否为空或者获取背包中元素的数量)背包可以说明元
 * 素的处理顺序不重要
 * 
 * Stats类的任务是简单地计算标准输入中所有double值的平均值和样本标准差.
 * 如果标准输入中有N个数字,那么平均值为它们的和除以N,样本标准差为每个值和平均值之差的平方之和除以N-1
 * 之后的平方根
 * */

public class Stats {
	public static void main(String[] args) {
		double[] test = new double[]{100,99,101,120,98,107,109,81,101,90};
		Bag<Double> numbers = new Bag<Double>();
		for (double x: test) 
			numbers.add(x);
		int N = numbers.size();
		double sum=0.0;
		for(double x:numbers)
			sum+=x;
		double mean=sum/N;
		sum=0.0;
		for(double x:numbers)
			sum+=(x-mean)*(x-mean);
		double std = Math.sqrt(sum/(N-1));
		StdOut.printf("Mean: %.2f\n",mean);
		StdOut.printf("Std dev: %.2f\n", std);
	}
}
