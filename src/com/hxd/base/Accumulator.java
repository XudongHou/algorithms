package com.hxd.base;

/**
 * 候旭东 20161212一种能够累加数据的抽象数据类型
 * */

public class Accumulator {
	protected double total;
	protected int N;
	
	public Accumulator() {
	}
	
	/**
	 * 添加一个新的数据值
	 * */
	public void addDataValue(double val){
		N++;
		total+=val;
	}
	/**
	 * 所有数据值的平均值
	 * */
	public double mean(){
		return total/N;
	}
	public String toString() {
		return "Mean ("+N+" Value): "+String.format("%7.5f", mean());
	}
}
