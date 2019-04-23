package com.hxd.graphs.sp;

/**
 * {@code DirectedEdge}类表示{@link EdgeWeightedDigraph}中的加权边。 每个边由两个整数（命名两个顶点）
 * 和实数权重组成。 数据类型提供了访问有向边和权重的两个端点的方法。
 * @author houxu_000 20170225
 * */

public class DirectedEdge {
	private final int v;
	private final int w;
	private final double weight;
	
	public DirectedEdge(int v, int w, double weight) {
		if (v < 0)	throw new IllegalArgumentException("Vertex names must be nonnegative integers");
		if (w < 0)	throw new IllegalArgumentException("Vertex names must be nonnegative integers");
		if (Double.isNaN(weight))	throw new IllegalArgumentException("Weight is NaN");
		this.v = v;
		this.w = w;
		this.weight = weight;
	}
	
	public int from() {
		return v;
	}
	
	public int to() {
		return w;
	}
	
	public double weight() {
		return weight;
	}
	
	@Override
	public String toString() {
		return v + " -> " + w + " " + String.format("%.5f", weight);
	}
	
	public static void main(String[] args) {
		DirectedEdge e = new DirectedEdge(12, 34, 5.67);
		System.out.println(e);
	}
}
