package com.hxd.base.uf;

import java.util.Arrays;

/**
 * 候旭东 20161215 提高union()方法的速度和quick-find是互补的
 * 
 * 以触点作为索引的id[]数组,但我们赋予这些值的意义不同,我们需要每个触点所对应的id[]元素都是
 * 同一个分量中的另一个触点的名称(也可能是它自己)--将这种联系称为链接,实现find()方法时,我们
 * 从给定的触点开始,有它的链接得到另一个触点,再由这个触点的连接的链接到达第三个触点,以此类推直到
 * 到达一个根触点,即链接指向自己的触点(这样的一个触点必然存在)
 * 当且仅当分别由两个触点开始的这个过程到达了同一个根触点时他们存在于同一个连通分量中,为了保证这个
 * 过程的有效性,需要union(p,q)来保证这一点,它的实现很简单:我们由p和q的链接分表找到它的根触点
 * ,然后将一个根触点链接到另一个即可将一个分量重命名为另个一个分量
 * */

public class QuickUnionUF extends UF {

	public QuickUnionUF(int N) {
		super(N);
	}

	@Override
	public int find(int p) {
		while(p!=id[p])
			p=id[p];
		return p;
	}

	/**
	 * 森林的表示
	 * quick-union算法的代码很简洁,用节点(带标签的圆圈)表示触点,用从一个节点到另一个节点的箭头表示链接
	 * 得到的结构是一个树.,id[]数组用父链接的形式表示了一片森林.无论我们从任何触点所对应的节点开始跟随链接.
	 * 最终都将到达含有该节点的树的根节点.用归纳法证明这个性质的正确性:在数组被初始化之后,每日个节点的链接都
	 * 指向它自己;如果在某次union()操作之前这条性质成立,那么操作之后的它必然也成立.因此quick-union
	 * 中的find()方法能够返回根节点所对应的触底触点的名称.这种表示方法对于 这个问题很实用,因为当且仅当两个
	 * 点存在于相同的分量之中时他们对应的节点才会在同一个树中.另外构造树并不困难,union实现只用了一条语句就
	 * 将一个根节点变为另一个根节点的父节点,从而归并了两棵树
	 * 
	 * quick-union算法的分析
	 * 分析此算法的成本比quick-find()算法的成本更困难,在最好的情况下 find()只需要访问数组一次就能够得
	 * 到一个触点所在的分量的标识符;在最坏的情况下需要2N+1次数组访问例如 a = new int[]{0,1,0,2,0,
	 * 3,0,4,0,5,0,6,0,7,0,8,0,9};,此时的允许时间是平方级的
	 * 
	 * 定义 一棵树的大小是它的节点的数量,树中的一个节点的深度是它到根节点的路径上的链接数.树的高度是它的所有节
	 * 点中的最大深度
	 * 
	 * 命题  quick-union 算法中的find()方法访问数组的次数为1加上给定触点所对应的节点的深度的两倍.union()
	 * 和connected()访问数组的次数为两次find()操作(如果union()中给定的两个触点分别存在不同的树中则
	 * 还需要加1)
	 * */
	@Override
	public void union(int p, int q) {
		//将p,q的根节点统一
		int pRoot=find(p);
		int qRoot=find(q);
//		System.out.print(qRoot+" is Root ");
		if(pRoot==qRoot)return;
		id[pRoot]=qRoot;
		count--;
	}
	public static void main(String[] args) {
		int N = 10;				//读取触点数量
		int a[] = new int[]{4,3,3,8,6,5,9,4,2,1,8,9,5,0,7,2,6,1,1,0,6,7};
		UF uf = new QuickUnionUF(N);				//初始化N个节点
		for (int i = 0; i < a.length; i+=2) {
			int p=a[i];				//读取整数对
			int q=a[i+1];
			if(uf.connected(p, q))continue;		//如果已经连通则忽略
			uf.union(p, q);						//连接并打印
			System.out.println(p+" "+q);
			System.out.println(Arrays.toString(uf.getId()));
		}
	}
}
