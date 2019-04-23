package com.hxd.base.uf;

import java.util.Arrays;

/**
 * 候旭东 20161215 一种保证方法是当且仅当id[p]等于id[q]时p和q是连通的.在同一个连通分量中的
 * 所有触点在id[]中的值必须全部相同,这意味着connected(p,q)只需要判断id[p]==id[q],当且
 * 仅当p和q在同一个连通分量中该语句才会返回true.为了调用union(p,q)确保这一点,我们首先要检查
 * 它们是否已经存在同一个连通分量之中.如果是我们就不需要采取任何行动,否则我们面对的情况就是p所在的连
 * 通分量中的所有触点的id[]值均为同一个值,而q所在的连接分量中的所有触点的id[]值均为另一个值.要将
 * 两个分量合二为一,我们必须将两个集合中所有的触点所对应的id[]元素变为同一值.为此,我们需要遍历整个
 * 数组,将所有和id[p]相等的元素的值变为id[q]的值.也可将所有和id[q]相等的元素的值变为id[p]的
 * 值--两者皆可
 * 
 * */

public class QuickFindUF extends UF{
	public QuickFindUF(int N) {
		super(N);
	}
	@Override
	public int find(int p) {
		return id[p];
	}
	
	/**
	 * quick-find算法的分析
	 * find()操作的速度虽然快,因为它只需要访问id[]数组一次,但quick-find算法一班无法处理大型问题
	 * ,因为对于每一对输入unoin()都需要扫描整个id[]数组
	 * 
	 * 每次find()调用只需要访问数组一次,而归并两个分量的union()操作访问数组的次数在(N+3)到(2N+1)
	 * 之间
	 * 每次connected()调用都会检查id[]数组中的两个元素是否相等,归并两个分量的union()操作会调用两次
	 * find(),检查id[]数组中的全部N个元素并改变它们中1到N-1个元素的值
	 * 
	 * 使用quick-find算法来解决动态连接性问题并且最后只得到了一个连通分量,那么这至少需要调用N-1次union()
	 * ,即至少(N+3)(N-1) ~ n^2次数组 --所以马上可以猜想动态连通性的quick-find算法是平方机的
	 * */
	@Override
	public void union(int p, int q) {
		//将p和q归并到相同的分量中
		int pID=find(p);
		int qID=find(q);
		//如果p和q已经在相同的分量之中则不需要采取任何行动
		if(pID==qID)return;
		for (int i = 0; i < id.length; i++)
			if(id[i]==pID) id[i]=qID;
		count--;
	}
	
	public static void main(String[] args) {
		int N = 10;				//读取触点数量
		int a[] = new int[]{4,3,3,8,6,5,9,4,2,1,8,9,5,0,7,2,6,1,1,0,6,7};
		UF uf = new QuickFindUF(N);				//初始化N个节点
		for (int i = 0; i < a.length; i+=2) {
			int p=a[i];				//读取整数对
			int q=a[i+1];
			if(uf.connected(p, q))continue;		//如果已经连通则忽略
			uf.union(p, q);						//连接并打印
			System.out.println(p+" "+q);
		}
		System.out.println(uf.count()+" components");
		System.out.println(Arrays.toString(uf.getId()));
	}
}
