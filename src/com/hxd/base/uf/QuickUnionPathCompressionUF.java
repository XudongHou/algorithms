package com.hxd.base.uf;

import java.util.Arrays;

public class QuickUnionPathCompressionUF extends UF {
	
	public QuickUnionPathCompressionUF(int N) {
		super(N);
	}
	
	
	/**
	 * 路径压缩 在检查节点的同时将它们直接连接到根节点.
	 * 为find()添加一个循环,将在路径上遇到的所有节点都直接链接到根节点上
	 * */
	@Override
	public int find(int p) {
		int root=p;
		while(root!=id[root])
			root=id[root];
		while (p!=root) {
			int newp=id[p];
			id[p]=root;
			p=newp;
		}
		return root;
	}

	@Override
	public void union(int p, int q) {
		int pRoot=find(p);
		int qRoot=find(q);
		if (pRoot==qRoot)return;
		id[pRoot]=qRoot;
		count--;
	}
	public static void main(String[] args) {
		int N = 10;				//读取触点数量
		int a[] = new int[]{4,3,3,8,6,5,9,4,2,1,8,9,5,0,7,2,6,1,1,0,6,7};
		UF uf = new QuickUnionPathCompressionUF(N);				//初始化N个节点
		for (int i = 0; i < a.length; i+=2) {
			int p=a[i];				//读取整数对
			int q=a[i+1];
			if(uf.connected(p, q))continue;		//如果已经连通则忽略
			uf.union(p, q);						//连接并打印
			System.out.println(p+" "+q);
			System.out.println(Arrays.toString(uf.getId()));
		}
		System.out.println(uf.count()+" components");
		
	}
}
