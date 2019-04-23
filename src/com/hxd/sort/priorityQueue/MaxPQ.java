package com.hxd.sort.priorityQueue;


/**
 * 
 * 基于堆的优先级队列,优先队列最重要的操作就是删除最大元素和插入元素.数据结构二叉堆能够很好地实现优先队列的基本操作
 * 在二叉堆的数组中,每个元素都要保证大于等于另两个特定位置的元素.当一个二叉树的每个节点都大于等于它的两个子节点时,
 * 它被称为堆有序,从任一结点向下,我们都能得到一列非递增的元素,根节点是堆有序的二叉树中最大结点.二叉堆是一组能够用
 * 堆有序的完全二叉树排序的元素,并在数组中按照层级储存(不使用数组 的第一个位置),一颗大小为N的完全二叉树的高度为
 * LogN
 * @author 候旭东 20161221
 * */

public class MaxPQ<Key extends Comparable<Key>>{
	/**
	 * 展示优先队列的抽象模型的价值,输入N个字符串,每个字符串都对映着一个整数,从中找出最大的(或者最小)M个整数(
	 * 及其关联 的字符串).当优先队列的大小超过M时就删掉其中最小的元素.处理完所有交易,优先队列中存放着以增序排列
	 * 的最大的M个.具体方法就是将二叉树的节点按照层级顺序放入数组中
	 * */
	
	private Key[] pq;		//基于堆的完全二叉树
	private int N = 0;		//存储与pq[1...N]中,pq[0]没有用
	
	@SuppressWarnings("unchecked")
	public MaxPQ(int maxN) {
		pq = (Key[]) new Comparable[maxN+1];
	}
	
	public boolean isEmpty(){return N==0;}
	
	public int size(){return N;}
	
	/**
	 * 将新元素加到数组末尾,增加堆的大小并让这个新元素上浮到合适的位置,不需要超过(lgN+1)次比较
	 * */
	public void insert(Key v){
		pq[++N]=pq[1];
		swim(N);
	}
	/**
	 * 删除最大元素并将数组的最后一个元素放到顶端,减小堆的大小并让这个元素下沉到合适位置,不需要超过2lgN次比
	 * 较
	 * */
	public Key delMax(){
		Key max = pq[1];	//从根节点得到最大元素
		exch(1,N--);		//将其和最后一个结点交换
		pq[N+1]=null;		//防止对象游离
		sink(1);			//恢复堆的秩序
		return max;
	}
	/**
	 * 由下至上的堆有序化(上浮)
	 * 堆的有序状态因为某个结点变得比它的父节点更大而打破,那么就要通过交换它和它的父节点来修复堆.交换后,这个结
	 * 点比它的两个子节点都大,但这个结点仍可能比现在的父节点还大,需要不断的上移直到遇到一个更大的父节点.只要记
	 * 住位置k的结点父节点的位置是[k/2]
	 * */
	private void swim(int k) {
		while(k>1&& less(k/2,k)){
			exch(k/2,k);
			k=k/2;
		}
	}
	
	private boolean less(int i,int j){
		return pq[i].compareTo(pq[j])<0;
	}
	
	/**
	 * 由上至下的堆有序化(下沉)
	 * 如果堆的有序状态因为某个结点变得比它的两个子节点或是其中之一更小而被打破了,那么可以通过将它和它的两个子节
	 * 点中的较大者交换来恢复堆
	 * */
	private void sink(int k) {
		while(k*2<N){
			int j = 2*k;
			if(j<N&&less(j, j+1))
				j++;
			if(!less(k, j))break;
			exch(k, j);
			k=j;
		}
	}

	private void exch(int i, int j) {
		Key t = pq[i];
		pq[i]=pq[j];
		pq[j]=t;
	}
}
