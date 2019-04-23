package com.hxd.base.uf;

/**
 *  union-find算法
 * 将对象称为触点,整数对成为连接,等价类成为连通分量简称为分量
 * 成本模型是统计任意数组元素的访问次数,无论读写
 * @author houxu_000 20161215
 * */

public abstract class UF {
	/**
	 * 使用以触点为索引的数组来表示所有的分量id 使用分量中的某个触点的名称作为分量的标识符,
	 * 作为基础数据结构来表示所有分量
	 * */
	protected int[] id;
	protected int count; //分量数量
	public UF() {}
	/**
	 * 以整数标识(0到N-1)初始化N个触点
	 * */
	public UF(int N) {
		count=N;
		id=new int[N];
		for (int i = 0; i < N; i++)
			id[i]=i;
	}
	/**
	 * 连通的分量的数量
	 * */
	public int count(){return count;}
	/**
	 * 如果p和q存在于同一个分量中则返回true
	 * */
	public boolean connected(int p,int q){return find(p)==find(q);}
	
	/**
	 * 
	 * */
	public void validate(int p){
		int n=id.length;
		if(p<0||p>n)
			throw new IndexOutOfBoundsException();
	}
	/**
	 * p(0到N-1)所在分量的标识
	 * find()对于处于同一个连通分量中的触点均返回相同的整数值.union方法必须保证这一点
	 * */
	public abstract int find(int p);
	/**
	 * 在p和q之间添加一条连接
	 * */
	public abstract void union(int p,int q);
	protected int[] getId() {
		return id;
	}
}
