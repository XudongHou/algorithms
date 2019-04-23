package com.hxd.search.balancedBinarySearchTrees;

import java.util.NoSuchElementException;

import com.hxd.base.Queue;

/**
 *  一颗大小为N的红黑树的高度不会超过2lgN
 *  一颗大小为N的红黑树中,根结点到任意结点的平均路径长度为~1.00lgN
 *  The {@code BST} class represents an ordered symbol table of generic
 *  key-value pairs.
 *  It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods.
 *  It also provides ordered methods for finding the <em>minimum</em>,
 *  <em>maximum</em>, <em>floor</em>, and <em>ceiling</em>.
 *  It also provides a <em>keys</em> method for iterating over all of the keys.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be {@code null}—setting the
 *  value associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  This implementation uses a left-leaning red-black BST. It requires that
 *  the key type implements the {@code Comparable} interface and calls the
 *  {@code compareTo()} and method to compare two keys. It does not call either
 *  {@code equals()} or {@code hashCode()}.
 *  The <em>put</em>, <em>contains</em>, <em>remove</em>, <em>minimum</em>,
 *  <em>maximum</em>, <em>ceiling</em>, and <em>floor</em> operations each take
 *  logarithmic time in the worst case, if the tree becomes unbalanced.
 *  The <em>size</em>, and <em>is-empty</em> operations take constant time.
 *  Construction takes constant time.
 *  <p>
 *
 *  @author 候旭东 20170109
 */

public class RedBlackBST <Key extends Comparable<Key>,Value>{
	private static final boolean RED = true;
	private static final boolean BLACK = false;
	
	private Node root;
	
	/**
	 * 红黑二叉树的基本思想是用标准的二叉查找树(完全有2-结点构成)和一些额外的信息(替换3-结点)
	 * 来表示2-3树.红链接将两个2-结点连接起来构成一个3-结点,黑链接则是2-3树中的普通链接.将
	 * 3-结点表示为由一条左斜的红色链接(两个2-结点其中一个是另一个的左子结点)相连成的两个2-结
	 * 点,这种表示法的一个优点就是无需修改就可以直接使用标准二叉查找树的get()方法.对于任意的2
	 * -3树,只要对结点进行转换,都可以立即派生出一颗对应的二叉查找树
	 * 
	 * 红黑树的另一种定义是含有红黑链接并满足下列条件的二叉查找树
	 * 1.红链接均为左链接
	 * 2.没有任何一个结点同时和两条红链接相连;
	 * 3.该树是完美黑色平衡的,即任意空链接到根结点的路径上的黑链接数量相同
	 * 
	 * 每个结点都只会有一条指向自己的链接(从它的父结点指向它),我们将链接的颜色保存在表示结点的N-
	 * ode数据类型的布尔变量color中.如果指向它的链接是红色的那么变量便为true,黑色则为false
	 * 约定空链接为黑色.为了代码的清晰定义两个常量RED和BLACK
	 * */
	private class Node {
		private Key key;
		private Value value;
		private Node left , right;
		private boolean color;	//其父节点指向它的链接的颜色
		private int size;
		
		public Node(Key key, Value value, boolean color, int size) {
			this.key = key;
			this.value = value;
			this.color = color;
			this.size = size;
		}
	}
	
	public RedBlackBST() {
	}
	/***************************************************************************
    *  Node helper methods.
    ****************************************************************************/
    // is node x red; false if x is null ?	
	private boolean isRed(Node x) {
		if (x == null)	return false;
		return	x.color == RED;
	}
	
    // number of node in subtree rooted at x; 0 if x is null
	private int size (Node x) {
		if (x == null)	return 0;
		return x.size;
	}

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
	public int size() {
		return size(root);
	}
	
	/**
     * Is this symbol table empty?
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
	public boolean isEmpty() {
		return root == null;
	}
	

	/***************************************************************************
	*  Standard BST search.
	***************************************************************************/
    
	/**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */	
	public Value get(Key key) {
		if (key == null)	throw new IllegalArgumentException("argument to get() is null");
		return get(root, key);
	}
	/**
	 * 同二叉查找树的get方法
	 * */
	private Value get(Node x, Key key) {
		while (x != null) {
			int cmp = key.compareTo(x.key);
			if		(cmp < 0)	x = x.left;
			else if	(cmp > 0)	x = x.right;
			else				return x.value;
		}
		return null;
	}
	

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public boolean contains(Key key) {
		return get(key) != null;
	}
	
	/***************************************************************************
	 *  Red-black tree insertion.
	 ***************************************************************************/

	/**
	 *  向单个2-结点中插入新键:
	 *    一颗只含有一个键的红黑树只含有一个2-结点.插入另一个键之后,马上需要进行旋转操作.
	 *  如果老键大于新键(插入到左侧),只需要新增一个红色的结点即可,新的红黑树和单个3-结点完
	 *  全等价.如果老键小于新键,新增的红色结点将会产生一条红色的右链接.我需要使用root = 
	 *  rotateLeft(root);来将其旋转为红色左链接并修正根结点的链接,插入操作才算完成.
	 *  结果相当于和单个3-节点等价的红黑树.
	 *  
	 *  向树底部的2-结点插入新键:
	 *    用和二叉查找树相同的方式向一颗红黑树中插入一个新键会在树的底部新增一个结点,但总是
	 *  用红链接将新结点和它的父结点相连.如果它的父结点是一个2-结点,那么刚才讨论的两种处理
	 *  方法仍然适用.如果指向的是父结点的左链接,那么父结点接直接成为了一个3-结点;如果指向新
	 *  结点的是父结点的右链接,这就是一个错误的3-结点,但一次旋转就能够修正
	 *  
	 *  向一棵双键树(3-结点)中插入一个新键:
	 *    这种情况又可分为三种子情况:
	 *    1 新键小于树的两个键
	 *    2 在两者之间
	 *    3 大于树中的两个键.
	 *    每种情况都会产生一个同时连接到两条红链接的结点,目标即为修正这一点
	 *    新键大于原树中的两个键:
	 *    会被连接到3-结点的右链接,如此树是平衡的根结点为中间大小的键,它有两条红链接分别和
	 *  较小和较大的结点相连.如果将两条链接的颜色都有红变黑,那么得到了一棵由三个结点组成,
	 *  高度为2的平衡树.对应一颗2-3树,其他两种情况最终也会转化为这种情况
	 *    新键小于原树中的两个键,会被连接到最左边的空链接,这就产生了两条连续的红链接,此时只
	 *  需要将上层的红链接右旋转即可得到第一种情况
	 *    新键介于原树中的两个键的中间,将有产生两条连续的红链接,一条红色左链接接一条红色右链
	 *  接,此时将下层的红链接左旋转即可得到第二种情况
	 *    
	 *  请确认完全理解了这些转换,这是红黑树的动态变化的关键
	 *  
	 *  根结点总是黑色的,因此在每次插入后都会将根结点设置为黑色.每当根结点由红变黑时树的黑链接
	 *  高度就会加1
	 *  
	 *  向树底部的3-结点插入新键:
	 *    上述的几种情况都会出现,指向新结点的链接可能是3-结点的右链接(此时需要转换颜色即可),
	 *  或是左链接(此时需要进行右旋转然后再转换颜色),或是中链接(此时需要先左旋转下层链接然后右
	 *  旋转上层链接,最后再转换颜色),颜色转换会使中结点的链接变红,相当于将它送入了父结点.这意
	 *  味着在父结点中继续插入一个新键,继续使用相同的方法解决这个问题
	 *  
	 *  将红链接在树中向上传递:
	 *    每次必要的旋转之后都将进行颜色转换,这使得中结点变红.在父结点看来处理这样一个红结点的
	 *  方式和处理一个新插入的红色结点完全相同,即继续把红链接转移到中结点上去: 在一个3-结点下
	 *  插入新键,先创建一个临时的4-结点,将其分解并将红链接由中间键传递给它的父结点.重复这个过
	 *  程直至遇到一个2-结点或者根结点
	 *  
	 *  转换使用时间:
	 *  1  如果右子结点是红色的而左子结点是黑色的,进行左旋转
	 *  2  如果左子结点是红色的且它的左子结点也是红色的,进行右旋转
	 *  3  如果左右子结点均为红色,进行颜色转换
	 *  
	 *  为了保证树的平衡性所需的操作都是由下向上在每个所经过的结点中进行的,将他们植入我们已有的
	 *  实现中非常简单:只需在递归调用之后完成这些操作即可;通过一个检测两个结点的颜色的if语句完
	 *  成.
	 * Inserts the specified key-value pair into the symbol table, overwriting the old 
	 * value with the new value if the symbol table already contains the specified key.
	 * Deletes the specified key (and its associated value) from this symbol table
	 * if the specified value is {@code null}.
	 *
	 * @param key the key
	 * @param val the value
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public void put(Key key, Value value) {
		if (key == null) throw new IllegalArgumentException("first argument to put() is null");
		if (value == null){
			delete(key);
			return;
		}
		root = put(root, key, value);
		root.color = BLACK;		
		//根结点总是黑色的,因此在每次插入后都会将根结点设置为黑色.每当根结点由红变黑时树的黑链接高度就会加1
		assert check();
	}
	
	private Node put(Node h, Key key, Value value) {
		//标准的插入操作,和父节点用红链接相连
		if (h == null)	return new Node(key, value, RED, 1);
		
		int cmp = key.compareTo(h.key);
		if 		(cmp < 0)	h.left = put(h.left, key, value);
		else if (cmp > 0)	h.right = put(h.right, key, value);
		else				h.value = value;
		/**
		 *  1  如果右子结点是红色的而左子结点是黑色的,进行左旋转
		 *  2  如果左子结点是红色的且它的左子结点也是红色的,进行右旋转
		 *  3  如果左右子结点均为红色,进行颜色转换
		 *  
		 *  第一条if语句会将任意含有红色右链接的3-结点(或临时的4-结点)向左旋转;
		 *  第二条if语句会将临时的4-结点中两条连续红链接中的上层链接向右旋转;
		 *  第三天if语句会进行颜色转换并将红链接在树中向上传递
		 * */
		if(isRed(h.right) && !isRed(h.left))		h = rotateLeft(h);
		if(isRed(h.left) && isRed(h.left.left))		h = rotateRight(h);
		if(isRed(h.left) && isRed(h.right))			flipColors(h);
		
		h.size = resize(h);
		
		return h;
	}

	/***************************************************************************
	*  Red-black tree deletion.
	***************************************************************************/

	/**
	 * 从2-结点中删除一个键会留下一个空结点,为了保证不会删除一个2-结点沿着左链接向下进行变换,
	 * 确保 当前结点不是2-结点(可能是3-结点,也可能是临时的4-结点).首先根节点有两种情况.如果
	 * 根节点是2-结点且它的两个子结点都是2-结点,可以直接将三个结点变成4-结点;否则需要保证根
	 * 结点的做结点不是2-结点,如有必要可以从它右侧的兄弟结点"借"一个键来.
	 * 沿着左链接向下查找的过程中,保证以下情况之一成立:
	 * 1  如果当前结点的左子结点不是2-结点,完成;
	 * 2  如果当前结点的左子结点是2-结点而它的亲兄弟结点不是2-结点,将左子结点的兄弟结点中的一
	 * 个键移动到左子结点中;
	 * 3  如果当前结点的左子结点和它的亲兄弟结点都是2-结点,将左子结点,父结点中的最小健和左子键,
	 * 和左子结点最近的兄弟结点合并为一个4-结点,使父结点由3-结点变为2-结点或者由4-结点变为
	 * 3-结点.
	 * 在遍历过程中,最后能够得到一个含有最小健的3-结点或者4-结点,然后可以直接从中将其删除,将
	 * 3-结点变为2-结点,或者将4-结点变为3-结点.然后再回头向上解析所有临时的4-结点
	 * 
	 * Removes the smallest key and associated value from the symbol table.
	 * @throws NoSuchElementException if the symbol table is empty
	 */
	public void deleteMin() {
		if (isEmpty()) throw new NoSuchElementException("BST underflow");
		if (!isRed(root.left) && !isRed(root.right))
			root.color = RED;
		root = deleteMin(root);
		if (!isEmpty()) root.color = BLACK;
		assert check();
	}
	
	private Node deleteMin(Node h) {
		if (h.left == null)
			return null;
		
		if (!isRed(h.left) && !isRed(h.left.left))
			h= moveRedLeft(h);
		
		h.left = deleteMin(h.left);
		return balance(h);
	}
	
    /**
     * Removes the largest key and associated value from the symbol table.
     * @throws NoSuchElementException if the symbol table is empty
     */
	public void deleteMax() {
		if (isEmpty()) throw new NoSuchElementException("BST underflow");
		
		if (!isRed(root.left) && !isRed(root.right))
			root.color = RED;
		
		root = deleteMax(root);
		if (!isEmpty()) root.color = BLACK;
		assert check();
	}
	
	private Node deleteMax(Node h) {
		if (isRed(h.left))
			h = rotateRight(h);
		
		if (h.right == null)
			return null;
		
		if (!isRed(h.right) && !isRed(h.right.left))
			h = moveRedLeft(h);
		
		h.right = deleteMax(h.right);
		
		return balance(h);
	}
	
    /**
     * 在查找路径上和删除最小键相同的变换同样可以保证在查找过程中任意当前结点均不是2-结点.如果
     * 被查找的键在树的底部,可以直接删除它.如果不在,需要将它和它的后继结点交换,就和二叉查找树
     * 一样.因为当前结点必然不是2-结点,当前的问题一转化为在一棵根结点不是2-结点的子树中 
     * Removes the specified key and its associated value from this symbol table     
     * (if the key is in this symbol table).    
     *
     * @param  key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public void delete(Key key) {
		if (key == null) throw new IllegalArgumentException("argument to delete() is null");
		if (!contains(key)) return;
		
		if (!isRed(root.left) && !isRed(root.right))
			root.color = RED;
		
		root = delete(root,key);
		if (!isEmpty())	root.color = BLACK;
		assert check();
	}
	
	private Node delete(Node h, Key key) {
		
		if (key.compareTo(h.key) < 0){
			if (!isRed(h.left) && !isRed(h.left.left))
				h = moveRedLeft(h);
			h.left = delete(h.left, key);
		}else {
			if (isRed(h.left))
				h = rotateRight(h);
			if (key.compareTo(h.key) == 0 && (h.right == null))
				return null;
			if (!isRed(h.right) && !isRed(h.right.left))
				h = moveRedRight(h);
			if (key.compareTo(h.key) == 0) {
				Node x = min(h.right);
				h.key = x.key;
				h.value = x.value;
				h.right = deleteMin(h.right);
			}else
				h.right = delete(h.right, key);
		}
		return balance(h);
	}

	/***************************************************************************
	*  Red-black tree helper functions.
	***************************************************************************/

	/**
	 * 旋转:在实现的某些操作中可能会出现红色右链接或两条连续的红链接但在操作完成前
	 * 这些情况都会被小心地旋转修复 .旋转操作会改变红链接的指向.
	 * 在插入新键时使用旋转操作保证2-3树和红黑树之间的一一对应关系,因为旋转操作可
	 * 以保持红黑树的两个重要的性质:有序性和完美平衡性.在对红黑树汇总进行旋转时无
	 * 需为树的有序性或完美平衡性担心
	 * */
	/**
	 *  首先假设有一条红色的右链接按需要被转换成左链接,这个操作叫做左旋转,对应的方
	 * 法是接受一条指向红黑树中的某个结点的链接作为参数.假设被指向的结点的右链接红
	 * 色,这个方法会对树进行必要的调整并返回一个指向包含同一组键的子树且其左链接为
	 * 红色的根结点的链接.
	 * 这个操作很容易理解:将两个键中的较小键作为根结点变为将较大者作为根节点.
	 * */

	// make a right-leaning link lean to the left
	private Node rotateLeft(Node h) {
		Node x = h.right;
		h.right = x.left;
		x.left = h;
		x.color = x.left.color;		//BLACK or RED
		x.left.color = RED;
		x.size = h.size;
		h.size = resize(h);
		return x;
	}
	
	/**
	 * 实现将一个红色左链接转换为红色右链接,将两个键中的较大键作为根结点改变为
	 * 将较小键作为根结点
	 * */
	// make a left-leaning link lean to the right	
	private Node rotateRight(Node h) {
		Node x = h.left;
		h.left = x.right;
		x.right = h;
		x.color = x.right.color;	//BLACK or RED
		x.right.color = RED;
		x.size = h.size;
		h.size = resize(h);
		return x;
	}

	/**
	 * 来转换一个结点的两个红色子结点的颜色.除了将子结点的颜色由红变黑之外,同时将父结点的颜色
	 * 由黑变红.这项操作的重要性质在于它和旋转操作一样是局部变量,不会影响整棵树的黑色平衡性.
	 * 
	 * 根结点总是黑色的,因此在每次插入后都会将根结点设置为黑色.每当根结点由红变黑时树的黑链接
	 * 高度就会加1
	 * */
    // flip the colors of a node and its two children
	private void flipColors(Node h) {
        // h must have opposite color of its two children
        // assert (h != null) && (h.left != null) && (h.right != null);
        // assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right))
        //    || (isRed(h)  && !isRed(h.left) && !isRed(h.right));
		h.color = !h.color;
		h.left.color = !h.left.color;
		h.right.color = !h.right.color;
	}
	
    // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
	private Node moveRedRight(Node h) {
		flipColors(h);
		if (isRed(h.left.left)) {
			h = rotateRight(h);
			flipColors(h);
		}
		return h;
	}

	// Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
	private Node moveRedLeft(Node h) {
		flipColors(h);
		if(isRed(h.right.left)) {
			h.right = rotateRight(h.right);
			h = rotateLeft(h);
			flipColors(h);
		}
		return h;
	}

	// restore red-black tree invariant
	private Node balance(Node h) {
		if (isRed(h.right))							h = rotateLeft(h);
		if (isRed(h.left) && isRed(h.left.left))	h = rotateRight(h);
		if (isRed(h.left) && isRed(h.right))		flipColors(h);
		h.size = resize(h);
		return h;
	}
	
   /***************************************************************************
    *  Utility functions.
    ***************************************************************************/

	/**
	* Returns the height of the BST (for debugging).
	* @return the height of the BST (a 1-node tree has height 0)
	*/
	public int height() {
		return height(root);
	}
	
	private int height(Node x) {
		if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
	}
	
	/***************************************************************************
	*  Ordered symbol table methods.
	***************************************************************************/

	/**
	* Returns the smallest key in the symbol table.
	* @return the smallest key in the symbol table
	* @throws NoSuchElementException if the symbol table is empty
	*/	
	public Key min() {
		if (isEmpty()) throw new NoSuchElementException("called min() with empty symbol table");
		return min(root).key;
	}
	
	private Node min(Node x) {
		if (x.left == null)		return x;
		else					return min(x.left);
	}
	
	/**
     * Returns the largest key in the symbol table.
     * @return the largest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
	public Key max() {
		if (isEmpty()) throw new NoSuchElementException("called max() with empty symbol table");
        return max(root).key;
	}
	
	private Node max(Node x) {
		if (x.right == null)	return x;
		else					return max(x.right);
	}
    /**
     * Returns the largest key in the symbol table less than or equal to {@code key}.
     * @param key the key
     * @return the largest key in the symbol table less than or equal to {@code key}
     * @throws NoSuchElementException if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public Key floor(Key key) {
		if (key == null) throw new IllegalArgumentException("argument to floor() is null");
        if (isEmpty()) throw new NoSuchElementException("called floor() with empty symbol table");
        Node x = floor(root,key);
        if (x == null)	return null;
        else			return x.key;
	}
	private Node floor(Node x, Key key) {
		if (x == null) return null;
		int cmp = key.compareTo(x.key);
		if (cmp == 0)	return x;
		if (cmp < 0)	return floor(x.left, key);
		Node t = floor(x.right, key);
		if (t != null)	return t;
		else			return x;
	}

    /**
     * Returns the smallest key in the symbol table greater than or equal to {@code key}.
     * @param key the key
     * @return the smallest key in the symbol table greater than or equal to {@code key}
     * @throws NoSuchElementException if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public Key celling(Key key) {
		if (key == null) throw new IllegalArgumentException("argument to ceiling() is null");
		if (isEmpty()) throw new NoSuchElementException("called ceiling() with empty symbol table");
		Node x = celling(root, key);
		if (x == null)	return null;
		else			return x.key;
	}
	private Node celling(Node x, Key key) {
		if (x == null) return null;
		int cmp = key.compareTo(x.key);
		if (cmp == 0) return x;
		if (cmp > 0) return celling(x.right, key);
		Node t = celling(x.left, key);
		if (t != null)	return t;
		else			return x;
	}
	
    /**
     * Return the kth smallest key in the symbol table.
     * @param k the order statistic
     * @return the {@code k}th smallest key in the symbol table
     * @throws IllegalArgumentException unless {@code k} is between 0 and
     *     <em>n</em>–1
     */
	public Key select(int k) {
		if (k < 0 || k >= size()) throw new IllegalArgumentException();
		Node x = select(root,k);
		return x.key;
	}
	private Node select(Node x, int k) {
		int t = size(x.left);
		if		(t > k)		return select(x.left, k);
		else if	(t < k)		return select(x.right, k-t-1);
		else				return x;
	}
	
    /**
     * Return the number of keys in the symbol table strictly less than {@code key}.
     * @param key the key
     * @return the number of keys in the symbol table strictly less than {@code key}
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
	public int rank(Key key) {
		if (key == null) throw new IllegalArgumentException("argument to rank() is null");
        return rank(key, root);
	}
	private int rank(Key key, Node x) {
		if (x == null)	return 0;
		int cmp = key.compareTo(x.key);
		if		(cmp < 0)	return rank(key,x.left);
		else if	(cmp > 0)	return rank(key, x.right) + 1 + size(x.left);
		else				return size(x.left);
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
	
	/***************************************************************************
	*  Range count and range search.
	***************************************************************************/
	/**
	* Returns all keys in the symbol table as an {@code Iterable}.
	* To iterate over all of the keys in the symbol table named {@code st},
	* use the foreach notation: {@code for (Key key : st.keys())}.
	* @return all keys in the symbol table as an {@code Iterable}
	*/
	public Iterable<Key> keys() {
		if (isEmpty())	return new Queue<Key>();
		return keys(min(),max());
	}
	
    /**
     * Returns all keys in the symbol table in the given range,
     * as an {@code Iterable}.
     *
     * @param  lo minimum endpoint
     * @param  hi maximum endpoint
     * @return all keys in the sybol table between {@code lo} 
     *    (inclusive) and {@code hi} (inclusive) as an {@code Iterable}
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *    is {@code null}
     */
	public Iterable<Key> keys(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");
        Queue<Key> queue = new Queue<Key>();
        keys(root, queue, lo, hi);
        return queue;
	}
	
	private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
		if (x == null)	return;
		int cmpLo = lo.compareTo(x.key);
		int cmpHi = hi.compareTo(x.key);
		if	(cmpLo < 0)	keys(x.left, queue, lo, hi);
		if	(cmpLo <= 0 && cmpHi >=0)	queue.enqueue(x.key);
		if	(cmpHi > 0)	keys(x.right, queue, lo, hi);
	}
	
    /**
     * Returns the number of keys in the symbol table in the given range.
     *
     * @param  lo minimum endpoint
     * @param  hi maximum endpoint
     * @return the number of keys in the sybol table between {@code lo} 
     *    (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *    is {@code null}
     */
	public int size(Key lo,Key hi) {
		if	(lo == null) throw new IllegalArgumentException("first argument to size() is null");
        if	(hi == null) throw new IllegalArgumentException("second argument to size() is null");
        if	(lo.compareTo(hi) > 0)	return 0;
        if	(contains(hi))			return rank(hi) - rank(lo) + 1;
        else						return rank(hi) - rank(lo);
	}
	
	private int resize(Node h) {
		return 1 + size(h.left) + size(h.right);
	}
	/***************************************************************************
	*  Check integrity of red-black tree data structure.
	***************************************************************************/
	private boolean check() {
		if (!isBST())            System.out.println("Not in symmetric order");
	    if (!isSizeConsistent()) System.out.println("Subtree counts not consistent");
	    if (!isRankConsistent()) System.out.println("Ranks not consistent");
	    if (!is23())             System.out.println("Not a 2-3 tree");
	    if (!isBalanced())       System.out.println("Not balanced");
	    return isBST() && isSizeConsistent() && isRankConsistent() && is23() && isBalanced();
	}
	
	// does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
	private boolean isBST() {
		return isBST(root,null,null);
	}
	
	// is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: Bob Dondero's elegant solution
	private boolean isBST(Node x, Key min, Key max){
		if (x == null)	return true;
		if (min != null && x.key.compareTo(min) <= 0)	return false;
		if (max != null && x.key.compareTo(max) >= 0)	return false;
		return	isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
	}

	private boolean isSizeConsistent() {
		return isSizeConsistent(root);
	}
	private boolean isSizeConsistent(Node x) {
		if (x == null) return true;
		if (x.size != size(x.left) + size(x.right) + 1) return false;
		return isSizeConsistent(x.left) && isSizeConsistent(x.right);
	}
	
	// check that ranks are consistent
	private boolean isRankConsistent() {
		for (int i = 0 ;i < size(); i++)
			if (i != rank(select(i)))	return false;
		for (Key key :keys())
			if (key.compareTo(select(rank(key)))!=0)	return false;
		return true;
	}
	
	// Does the tree have no red right links, and at most one (left)
    // red links in a row on any path?
	private boolean is23() {
		return is23(root);
	}
	private boolean is23(Node x) {
		if (x == null) return true;
		if (isRed(x.right)) return false;
		if (x != root && isRed(x) && isRed(x.left))	return false;
		return is23(x.left) && is23(x.right);
	}
	
	// do all paths from root to leaf have same number of black edges?
	private boolean isBalanced() {
		int black = 0;
		Node x = root;
		while (x != null) {
			if (!isRed(x)) black++;
			x = x.left;
		}
		return isBalanced(root, black);
	}
    // does every path from the root to a leaf have the given number of black links?
	private boolean isBalanced(Node x, int black) {
		if (x == null) return black == 0;
		if (!isRed(x)) black --;
		return isBalanced(x.left, black) && isBalanced(x.right, black);
	}
	
	public static void main(String[] args) {
		RedBlackBST<String, Integer> st = new RedBlackBST<String,Integer>();
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
