package com.hxd.search.binarySearchTrees;

import java.util.NoSuchElementException;

import com.hxd.base.Queue;

/**
 * 	二叉树查找:一种能够将插入的灵活性和有序数组查找的高效性结合起来的符号表实现,所有使用的数据结构由节点
 * 	组成,结点包含的链接可以为空(null)或者指向其他结点.在二叉树中每个结点只能有一个父节点(只有一个例外
 * 	,根节点),而且每个结点都只有左右两个链接,我们可以将每个链接看做指向了另一棵二叉树,而这棵树的根节点就
 * 	是我们被指向的根节点。因此我们可以将二叉树定义为一个空链接,或者一个有左右两个链接的结点每个链接都指向
 * 	一棵独立的子二叉树.在二叉树中每个节点还包含了一个键和一个值,键之间也有顺序之分以支持高效的查找.
 * 	命题:
 * 		在一棵二叉查找树中,所有操作在最坏的情况下所需的时间都和树的高度成正比
 * The {@code BST} class represents an ordered symbol table of generic
 *  key-value pairs.
 *  It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods.
 *  It also provides ordered methods for finding the <em>minimum</em>,
 *  <em>maximum</em>, <em>floor</em>, <em>select</em>, <em>ceiling</em>.
 *  It also provides a <em>keys</em> method for iterating over all of the keys.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be {@code null}—setting the
 *  value associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  This implementation uses an (unbalanced) binary search tree. It requires that
 *  the key type implements the {@code Comparable} interface and calls the
 *  {@code compareTo()} and method to compare two keys. It does not call either
 *  {@code equals()} or {@code hashCode()}.
 *  The <em>put</em>, <em>contains</em>, <em>remove</em>, <em>minimum</em>,
 *  <em>maximum</em>, <em>ceiling</em>, <em>floor</em>, <em>select</em>, and
 *  <em>rank</em>  operations each take
 *  linear time in the worst case, if the tree becomes unbalanced.
 *  The <em>size</em>, and <em>is-empty</em> operations take constant time.
 *  Construction takes constant time.
 *  <p>
 * 	@author 候旭东 20170107
 * */

public class BST <Key extends Comparable<Key>,Value>{
	private Node root;				//二叉树查找的根节点
	/**
	 * 
	 * 一颗二叉查找数(BST)是一颗二叉树,其中每个结点都包含一个Comparable的键(以及相关联的值)且
	 * 每个结点的键都大于其左子树中的任意结点的键而小于右子树的任意结点的键
	 * */
	private class Node {
		private Key key;			//键
		private Value value;		//值
		private Node left,right;	//指向子树的链接
		private int size;			//以该结点为根的子树中的结点总数
		
		public Node(Key key,Value value,int size) {
			this.key = key;
			this.value = value;
			this.size = size;
		}
	}
	
	/**
     * Initializes an empty symbol table.
     */
	public BST() {}
	
	/**
	  * 返回判断树是否为空
	  * Returns true if this symbol table is empty.
	  * @return {@code true} if this symbol table is empty; {@code false} otherwise
	  */
	public boolean isEmpty(){
		return size() == 0;
	}
    
	/**
     * 返回数量
     * @return the number of key-value pairs in this symbol table
     */
	public int size(){
		return size(root);
	}
	
	/**
	 * 返回以x为根节点的结点数量
	 * return number of key-value pairs in BST rooted at x
	 * @return return number of key-value pairs in BST rooted at x
	 * */
	private int size(Node x) {
		if(x==null) return 0;
		else 		return x.size;
	}
    
	/**
     * 判断是否包含给定的键key
     * Does this symbol table contain the given key?
     *
     * @param  key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *         {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public boolean contains(Key key){
		if(key==null) throw new IllegalArgumentException("argument to contains() is null");
		return get(key) != null;
	}
    
	/**
     * 如果被查找的键和根节点的键相同,查找命中,否则就递归在适合的子树中接续查找,键较小选择左子树,键较大选择右子树
     * 命题: 由N个随机键构造的二叉查找树中,查找命中平均所需的比较次数为~2NlnN(约1.39lgN)
     * 		由N个随机键构造的二叉查找树中插入操作和查找未命中平均所需的比较次数为~2lnN(约1.39lgN)
     * Returns the value associated with the given key.
     *
     * @param  key the key
     * @return the value associated with the given key if the key is in the symbol table
     *         and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public Value get(Key key) {
		return get(root,key);
	}
	/**
	 * 代码保证只有该结点所表示的子树才会含有和被查找的键相同的键,命中的查找路径在含有被查找的键的结点出结束.
	 * 对于未命中的查找,路径的终点是一个空链接
	 * */
	private Value get(Node x, Key key) {
		if(x==null)			return null;
		int cmp = key.compareTo(x.key);
		if(cmp < 0)			return get(x.left,key);
		else	if(cmp > 0) return get(x.right, key);
		else				return x.value;
	}
    
	/**
     * 查找代码几乎和二分查找的一样简单,这种简洁性是二叉查找树的重要特性之一.而二叉查找树的另一个更重要的特性
     * 就是插入的实现难度和查找差不多.当查找一个不存在于书中的结点并结束于一条空链接时,需要做的就是将链接指向
     * 一个含有被查找的键的新结点,如果树是空的,就返回一个含有该键值对的新结点;如果被查找的键小于根节点的键在
     * 左子树插入,否则在右子树中插入该键.
     * Inserts the specified key-value pair into the symbol table, overwriting the old 
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public void put(Key key,Value value) {
		if (key == null) throw new IllegalArgumentException("first argument to put() is null");
		if (value == null) {
            delete(key);
            return;
        }
		root = put(root, key, value);
		assert check();
	}
	/**
	 * 当查找一个不存在于书中的结点并结束于一条空链接时,需要做的就是将链接指向一个含有被查找的
	 * 键的新结点,如果树是空的,就返回一个含有该键值对的新结点;如果被查找的键小于根节点的键在
     * 左子树插入,否则在右子树中插入该键.
	 * */
	private Node put(Node x,Key key,Value value) {
		if	(x==null)				return new Node(key, value, 1);
		int cmp = key.compareTo(x.key);
		if	(cmp < 0)				x.left = put(x.left, key, value);
		else	if(cmp > 0)			x.right = put(x.right, key, value);
		else						x.value = value;
		x.size = resize(x);
		return x;
	}
    
	/**
     * Removes the smallest key and associated value from the symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
	public void deleteMin() {
		if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
		root = deleteMin(root);
		assert check();
	}
	/**
	 * 递归方法接受一个指向结点的链接,并返回一个指向结点的链接,这样就能够方便地改变树的结构,将返回的链接赋给
	 * 作为参数的链接对于deleteMin(),需要不断深入根节点的左子树中的直至遇见一个空链接.(代码前两行实现),
	 * 然后将指向该结点的链接指向该结点的右子树(只需要在递归调用中返回它的右链接即可).此时已经没有任何链接指向
	 * 要被删除的结点,因此它会被垃圾收集器清理掉.标准递归代码会在删除结点后正确地设置它的父结点的链接并跟新它到
	 * 根节点的路径上的所有计数器的值.
	 * @param x the Node.left
	 * */
	private Node deleteMin(Node x) {
		if (x.left == null) return x.right;
		x.left = deleteMin(x.left);
		x.size = resize(x);
		return x;
	}
	
    /**
     * Removes the largest key and associated value from the symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
	public void deleteMax() {
		if(isEmpty()) throw new NoSuchElementException("Symbol table underflow");
		root = deleteMax(root);
		assert check();
	}
	private Node deleteMax(Node x) {
		if(x.right == null) return x.left;
		x.right = deleteMax(x.right);
		x.size = resize(x);
		return x;
	}

    /**
     * Removes the specified key and its associated value from this symbol table     
     * (if the key is in this symbol table).    
     *
     * @param  key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public void delete(Key key) {
		if(key == null) throw new IllegalArgumentException("argument to delete() is null");
		root = delete(root, key);
		assert check();
	}
	
	/**
	 * 二叉树最难实现的方法就是delete(),可以用类似于deleteMin()和deleteMax()的方式删除任意只有一个
	 * 子结点(或者 没有子结点)的结点,
	 * 对于拥有两个子结点的结点删除之后我们要处理两颗子树,解决如下:
	 * 在删除结点x后用它的后继结点填补它的位置.因为x右侧只有一个右子结点,因此它的后继结点就是其右子树中的最小结点
	 * 这样依然能够保证树的有序性,因为x.key和它的后继结点的键之间不存在其他的键.4个简单的步骤将完成x替换为它的
	 * 后继结点的任务:
	 * 		1  将指向即将删除的结点的链接保存为t;
	 * 		2  将x指向它的后继结点为min(t.right);
	 *		3  将x的右链接(原本指向一颗所有结点都大于x.key的二叉查找树)指向deleteMin(t.right),也就是
	 *		在删除后所有结点仍然都大于x.key的子二叉查找树
	 *		4  将x的左链接(本为空)设为t.left(其下所有的键都小于被删除结点和它的后继结点) 
	 * */
	private Node delete(Node x, Key key) {
		if(x == null) 			return null;
		int cmp = key.compareTo(x.key);
		if		(cmp < 0)		x.left = delete(x.left, key);
		else if	(cmp > 0)		x.right = delete(x.right, key);
		else{
			if(x.right == null) return x.left;
			if(x.left == null) 	return x.right;
			Node t = x;
			x = min(t.right);
			x.right = deleteMin(t.right);
			x.left = t.left;
		}
		x.size = resize(x);
		return x;
	}
	
	/**
	 * 如果根节点耳朵左链接为空,那么一颗日二叉查找树中最小的键就是根节点;如果左链接非空,那么树中的最小健就是左子树
	 * 中的最小健,使用递归
     * Returns the smallest key in the symbol table.
     *
     * @return the smallest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
	public Key min() {
		if (isEmpty()) throw new NoSuchElementException("called min() with empty symbol table");
		return min(root).key;
	}
	private Node min(Node x) {
		if (x.left == null) return x;
		else				return min(x.left);
	}
	
    /**
     * Returns the largest key in the symbol table.
     *
     * @return the largest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
	public Key max() {
		if (isEmpty()) throw new NoSuchElementException("called min() with empty symbol table");
		return max(root).key;
	}
	private Node max(Node x) {
		if (x.right == null) return x;
		else				return min(x.right);
	}
	
    /**
     * 返回小于等于给定参数Key的最大key
     * Returns the largest key in the symbol table less than or equal to {@code key}.
     *
     * @param  key the key
     * @return the largest key in the symbol table less than or equal to {@code key}
     * @throws NoSuchElementException if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public Key floor(Key key) {
		if (key == null) throw new IllegalArgumentException("argument to floor() is null");
		if (isEmpty()) throw new NoSuchElementException("called floor() with empty symbol table");
		Node x = floor(root,key);
		if (x == null) return null;
		else return x.key;
				
	}
	/**
	 * 如果给定的键key小于二叉查找树的根结点的键,那么小于等于key的最大件floor(key)一定在根节点的左子数中;如果给定的
	 * 键key大于二叉查找树的根节点,那么只有当根节点右子树中存在小于等于key的结点时,小于等于key的最大键才会出现在右子树
	 * 否则根节点就是小于等于key的最大键
	 * */
	private Node floor(Node x, Key key) {
		if (x == null)		return null;
		int cmp = key.compareTo(x.key);
		if (cmp == 0)		return x;
		if (cmp < 0)		return floor(x.left, key);
		Node t = floor(x.right, key);
		if (t != null) 		return t;
		else 				return x;
	}
	
    /**
     * 返回大于等于key的最小键
     * Returns the smallest key in the symbol table greater than or equal to {@code key}.
     *
     * @param  key the key
     * @return the smallest key in the symbol table greater than or equal to {@code key}
     * @throws NoSuchElementException if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public Key celling(Key key) {
		if (key == null) throw new IllegalArgumentException("argument to ceiling() is null");
        if (isEmpty()) throw new NoSuchElementException("called ceiling() with empty symbol table");
        Node x = celling(root,key);
        if ( x == null ) 	return null;
        else				return x.key;
	}
	/**
	 * 如果给定的键key小于二叉查找树的根结点的键,那么大于等于key的最小键celling(key)一定在根节点的左子树中;
	 * 如果给定的键key大于二叉查找树的根节点,那么只有当根节点右子树中存在大于等于key的结点时,大于等于key的最小键
	 * 才会出现在右子树否则根节点就是大于等于key的最小键
	 * */
	private Node celling(Node x, Key key) {
		if ( x == null )		return null;
		int cmp = key.compareTo(x.key);
		if ( cmp == 0 )			return x;
		if ( cmp < 0 ) {
			Node t = celling(x.left, key);
			if ( t != null ) 	return t;
			else 				return x;
		}
		return celling(x.right, key);
	}
	
    /**
     * Return the kth smallest key in the symbol table.
     *
     * @param  k the order statistic
     * @return the {@code k}th smallest key in the symbol table
     * @throws IllegalArgumentException unless {@code k} is between 0 and
     *        <em>n</em>–1
     */
	public Key select(int k) {
		 if (k < 0 || k >= size()) throw new IllegalArgumentException();
		 Node x = select(root,k);
		 return x.key;
	}
	/**
	 * 假设想要找到排名为k的键(即树中正好有k个小于它的键).如果左子树中的结点数t大于k,那么继续(递归地)在
	 * 左子树中查找排名为k的键;如果t等于k,就返回根结点中的键;如果t小于k,就(递归地)在右子树中查找排名为
	 * (k-t-1)的键.
	 * */
	private Node select(Node x, int k) {
		if (x == null)			return null;
		int t = size (x.left);
		if (t > k)				return select(x.left, k);
		else if (t < k)			return select(x.right, k-t-1);
		else					return x;
	}
	
    /**
     * rank()是select()的逆方法,会返回给定键的排名
     * Return the number of keys in the symbol table strictly less than {@code key}.
     *
     * @param  key the key
     * @return the number of keys in the symbol table strictly less than {@code key}
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public int rank(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to rank() is null");
        return rank(key, root);
	}
	/**
	 * 实现和select()的类似:如果给定的键和根结点的键相等,返回左子树中的结点总数t;如果给定的键小于根节点,
	 * 返回该键在左子树中的排名(递归计算);如果给定的键大于等于根结点,会返回t+1(根节点)加上它在右子树中的排
	 * 名(递归计算)
	 * */
	private int rank(Key key, Node x) {
		if (x == null)			return 0;
		int cmp = key.compareTo(x.key);
		if (cmp < 0)			return rank(key,x.left);
		else if (cmp > 0)		return 1 + size(x.left) + rank(key, x.right);
		else					return size(x.left);
	}
	
	/**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *
     * @return all keys in the symbol table
     */
	public Iterable<Key> keys(){
		return keys(min(),max());
	}

    /**
     * Returns all keys in the symbol table in the given range,
     * as an {@code Iterable}.
     *
     * @param  lo minimum endpoint
     * @param  hi maximum endpoint
     * @return all keys in the symbol table between {@code lo} 
     *         (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *         is {@code null}
     */
	public Iterable<Key> keys(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");
        Queue<Key> queue = new Queue<Key>();
        keys(root,queue,lo,hi);
		return queue;
	}
	private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
		if (x == null) return;
		int cmpLo = lo.compareTo(x.key);
		int cmpHi = hi.compareTo(x.key);
		if (cmpLo < 0)					keys(x.left,queue,lo,hi);
		if (cmpLo <= 0 && cmpHi >= 0)	queue.enqueue(x.key);
		if (cmpHi > 0)					keys(x.right, queue, lo, hi);
	}

	/**
	 * Returns the number of keys in the symbol table in the given range.
     *
     * @param  lo minimum endpoint
     * @param  hi maximum endpoint
     * @return the number of keys in the symbol table between {@code lo} 
     *         (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *         is {@code null}
	 * */
	public int size(Key lo, Key hi){
        if (lo == null) throw new IllegalArgumentException("first argument to size() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to size() is null");
        if (lo.compareTo(hi) > 0)	return 0;
        if (contains(hi))			return rank(hi) - rank(lo) + 1;
        else						return rank(hi) - rank(lo);
	}

    /**
     * Returns the height of the BST (for debugging).
     *
     * @return the height of the BST (a 1-node tree has height 0)
     */
	public int height(){
		return height(root);
	}
	private int height(Node x) {
		if (x == null) return -1;
		return 1 + Math.max(height(x.left),height(x.right));
	}

	/**
	 * 中序遍历,首先打印出根节点的左子树中的所有键(根据二叉查找树的定义它们应该都小于根节点的键),之后打印根节点
	 * 最后打印出根结点的右子树中的所有键(根据二叉查找树的定义它们应该都大于根节点的键)
	 * */
	public void printInOrder() {
		printInOrder(root);
	}
	private void printInOrder(Node x) {
		if (x == null)	return;
		printInOrder(x.left);
		System.out.print(x.key + " ");
		printInOrder(x.right);
	}

	/**
	 * 按层遍历,按照层级顺序打印以该结点为根的子树(即按照每个结点到根节点的距离的顺序,同一层的结点应该按找从左至
	 * 右的顺序)
	 * */
	public void printLeveOrder() {
		Iterable<Key> keys = leveOrder();
		for(Key key : keys)
			System.out.print(key + " ");
	}
	
    /**
     * 按层遍历
     * Returns the keys in the BST in level order (for debugging).
     *
     * @return the keys in the BST in level order traversal
     */
	public Iterable<Key> leveOrder() {
		Queue<Key> keys = new Queue<Key>();
		Queue<Node> queue = new Queue<Node>();
		queue.enqueue(root);
		while (!queue.isEmpty()){
			Node x = queue.dequeue();
			if (x == null) continue;
			keys.enqueue(x.key);
			queue.enqueue(x.left);
			queue.enqueue(x.right);
		}
		return keys;
	}
	

	private int resize(Node x){
		return size(x.left) + size(x.right) + 1;
	}
	/*************************************************************************
	 *  Check integrity of BST data structure.
	 ************************************************************************/	
	private boolean check() {
		if (!isBST())		System.out.println("Not in symmetric order");
		if (!isSizeConsistent()) System.out.println("Subtree counts not consistent");
		if (!isRankConsistent()) System.out.println("Ranks not consistent");
		return isBST() && isSizeConsistent() && isRankConsistent();
	}

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
	private boolean isBST() {
		return isBST(root,null,null);
	}
    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: Bob Dondero's elegant solution
	private boolean isBST(Node x, Key min, Key max) {
		if (x == null)									return false;
		if (min != null && x.key.compareTo(min) <= 0)	return false;
		if (max != null && x.key.compareTo(max) >= 0)	return false;
		return isBST(x.left,min,max) && isBST(x.right, min, max);
	}
	
    // check that ranks are consistent
	private boolean isRankConsistent() {
		for (int i = 0; i < size(); i++)
			if (i != rank(select(i)))					return false;
		for (Key key : keys())
			if (key.compareTo(select(rank(key))) != 0)	return false;
		return true;
	}
	
    // are the size fields correct?
	private boolean isSizeConsistent() {
		return isSizeConsistent(root);
	}
	private boolean isSizeConsistent(Node x) {
		if (x == null)									return true;
		if (x.size != size(x.left) + size(x.right) + 1)	return false;
		return isSizeConsistent(x.left) && isSizeConsistent(x.right);
	}
	
	public static void main(String[] args) {
		BST<String, Integer> st = new BST<String,Integer>();
		String[] strings = new String[]{"S","E","A","R","C","H","E","X","A","M","P","L","E"};
		int i =0;
		for (String key : strings)
			st.put(key,i++);
		for (String s: st.keys())
			System.out.println(s+" "+st.get(s));
		System.out.println("size "+st.size());
		System.out.println("seleced key is "+ st.select(0));
		System.out.println("key S ranked is "+ st.rank("S"));
		System.out.println("floor S key is "+st.floor("S"));
		System.out.println("celling S key is "+ st.celling("S"));
		System.out.print("print key in levelOrder");st.printLeveOrder();
		System.out.println();
		System.out.print("print key in In-Order");st.printInOrder();
		System.out.println();
		System.out.println("BST's height is " + st.height());
		System.out.println("min is " + st.min());
		System.out.println("max is " + st.max());
		System.out.println("deleteMin ");	st.deleteMin();
		System.out.println("deleteMax ");	st.deleteMax();
		System.out.println("delete Key M "); st.delete("M");
		System.out.println("size "+st.size());
		System.out.print("print key in levelOrder ");st.printLeveOrder();
		System.out.println();
		System.out.print("print key in In-Order");st.printInOrder();
		System.out.println();
		System.out.println("delete Key S "); st.delete("S");
		System.out.print("print key in levelOrder ");st.printLeveOrder();
		System.out.println();
		System.out.print("print key in In-Order ");st.printInOrder();
		System.out.println();
		System.out.println("delete Key C "); st.delete("C");
		System.out.println("delete Key except rootKey "); 
		st.delete("H");st.delete("E");st.delete("L");st.delete("P");
		System.out.println("BST's height is " + st.height());
		System.out.print("print key in In-Order ");st.printInOrder();
		System.out.println();
	}
}
