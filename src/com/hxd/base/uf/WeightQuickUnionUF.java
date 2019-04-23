package com.hxd.base.uf;

import java.util.Arrays;

/**
 * 候旭东 20161215 加权quick-union算法
 * 记录每一棵树的大小,并总是将较小的树链接到较大的数上
 * 
 * 其中将要被归并的数的大小总是相等的(且总是2的幂),这些树的结构看起来很复杂,
 * 但他们均含有2^n个节点,因此高度都正好是n.另外,当我们归并开两个含有2^n个节点的数时,
 * 我们得到的数含有2^(n+1)个节点,由此将数的高度增加到了n+1.由此推广我们可以证明加权
 * quick-union算法能够保证对数级别的性能
 * */

public class WeightQuickUnionUF extends UF {

	private int[] sz;
	
	public WeightQuickUnionUF(int N) {
		super(N);
		sz=new int[N];
		for (int i = 0; i < N; i++)
			sz[i]=1;
	}

	/**
	 * 找到根节点
	 * */
	@Override
	public int find(int p) {
		while(p!=id[p])
			p=id[p];
		return p;
	}

	/**
	 * 我们加入了一个由触点索引的实例变量数组sz[],这样union()就可以将小树的根节点连接到大叔的根节点
	 * 命题 
	 * 对于N个触点,加权quick-union算法构造的森林中的任意节点的深度最多为LgN
	 * 证明
	 * 我们可以用归纳法证明一个更强的命题,即森林中大小为K的树的高度最多为LgK.在原始情况下,当等于1时树
	 * 的高度为0.根据归纳法,假设大小为i的树的高度最多为Lgi,其中i<k.设i<=j且i+i=k,当我们将大小为
	 * i和大小为j的树归并时,小树中的所有节点的深度增加了1,但它们现在所在的树的大小为i+j=k.而1+Lgi=
	 * (Lg(i+j))<=lgk,性质成立
	 * 推论 对于加权quick-union算法和N个触点,在最坏的情况下find(),connected()和union()的
	 * 成本的增长数量级为LogN
	 * 
	 * 在森林中,对于从一个节点到它的根节点的路径上的每个节点,每种操作最多都只会访问数组的常数次
	 * */
	@Override
	public void union(int p, int q) {
		int i=find(p);
		int j=find(q);
		if (i==j)
			return;
		//将小的树连接到大树的节点上
		if(sz[i]<sz[j]){
			id[i]=j;
			sz[j]+=sz[i];
		}else{
			id[j]=i;
			sz[i]+=sz[j];
		}
		count--;
	}
	
	public static void main(String[] args) {
		int N = 10;				//读取触点数量
		int a[] = new int[]{4,3,3,8,6,5,9,4,2,1,8,9,5,0,7,2,6,1,1,0,6,7};
		WeightQuickUnionUF uf = new WeightQuickUnionUF(N);				//初始化N个节点
		for (int i = 0; i < a.length; i+=2) {
			int p=a[i];				//读取整数对
			int q=a[i+1];
			if(uf.connected(p, q))continue;		//如果已经连通则忽略
			uf.union(p, q);						//连接并打印
			System.out.println(p+" "+q);
			System.out.println(Arrays.toString(uf.getId()));
			System.out.println(Arrays.toString(uf.getSz()));
		}
	}

	public int[] getSz() {
		return sz;
	}
}
