package com.hxd.base;

import com.hxd.introcs.stdlib.StdDraw;

/**
 * 候旭东 20161212 可视化累加器
 * */

public class VisualAccumulator extends Accumulator{
	public VisualAccumulator(int trials,double max) {
		StdDraw.setXscale(0,trials);
		StdDraw.setYscale(0, max);
		StdDraw.setPenRadius(.005);
	}
	
	@Override
	public void addDataValue(double val) {
		N++;
		total+=val;
		StdDraw.setPenColor(StdDraw.DARK_GRAY);
		StdDraw.point(N, val);
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.point(N, total/N);
	}
	@Override
	public double mean() {
		return super.mean();
	}
	@Override
	public String toString() {
		return super.toString();
	}
}
