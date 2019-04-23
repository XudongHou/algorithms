package com.hxd.context.btree;

/**
 * 平衡树算法的扩展,多向平衡查找树<br>
 * 不会将数据保存在树中,而是构造一课由键的副本组成的树,每个副本都关联着一条连接.这种方式
 * 能够更加方便的地将索引和符号表本身分开,就像一本实体书中的索引一样.同时如同2-3树,限制
 * 每个结点中能够含有的"键-链接"对的上下数量界限:选择一个参数M(一般都是偶数)并构造一棵
 * 多向树,每个结点最多能给含有M-1对键和链接(假设M足够小,使得每个M向结点都能够存放在一个
 * 页中),最少含有M/2对键和链接,但也不能少于2对.
 * 例如:在一棵4阶B-树中,每个结点都含有至少2对至多3对键-链接;六阶中最少三个最多五个;
 * 而使用有序集{@link com.hxd.introcs.SET.java}将查找树推广位,因为这依赖于键的
 * 有序性.
 * 外部查找的应用常将索引和数据隔离:
 * 		内部结点:含有与页相关联的键的副本;
 * 		外部结点:含有指向实际数据的引用;
 * <br>-------------------------------------><br>
 * 内部结点中的每个键都与一个结点相关联,以此结点为根的子树中,所有的键大于等于与此结点关联
 * 的键.但小于原内部结点中更大的键(如果存在的话).为了使用方便,使用一个特殊的哨兵键,它小于
 * 其他所有键.;一开始B-树只含有一个根节点,而根结点在初始化时仅含有一个该哨兵键. 符号表不
 * 含有重复键,但我们会在(在内部节点中)使用键的多个副本来引导查找.B-树查找的基础是在可能
 * 含有被查找键的唯一子树中进行递归搜索.当且仅当被查找的键包含在集合中是,每次查找便会结束于
 * 一个外部结点;这样做可以简化将B-树拓展为有序符号表的实现(当M很大时,这种情况很少出现);
 * <br>-------------------------------------><br>
 * 定义:
 * 		一颗M阶B-树(M为正偶数)或者近视一个外部k-结点(含有k个键和相关信息的树),或者由若
 * 干内部k-结点(每个结点都含有k个键和k条链接,链接指向的子树表示了键之间的间隔区域)组成.它
 * 的结构性质如下:从根结点到每个外部结点的路径长度均相同完美平衡); 对于根节点,k在2到M-1之
 * 间,对于其他结点k在M/2到M-1之间.<br>
 * {@code BTree}类表示通用键值对的有序符号表。它支持put，get，contains，size，和 
 * isEmpty方法。符号表实现关联数组抽象：将值与已经在符号表中的键相关联时，约定是用新值替换旧
 * 值。与{@link java.util.Map}不同，此类使用值不能为{@code null}的约定 - 设置与
 * {@code null}的关键字相关的值等同于从符号表中删除密钥。此实现使用B-树。它要求密钥类型
 * 实现{@code Comparable}接口并调用{@code compareTo（）}和方法来比较两个密钥。
 * 它不调用{@code equals（）}或{@code hashCode（）}。在最坏的情况下，get，put和
 * contains操作都使logm n探测，其中n是键值对的数量，m是分支因子。大小和空的操作需要不断
 * 的时间。施工需要不断的时间。
 * @author houxu_000
 *
 */
@SuppressWarnings("rawtypes")
public class BTree<Key extends Comparable<Key>, Value> {
	
	private static final int M = 4;
	
	private Node root;			//root of the B-tree
	private int height;			// height of the B-tree
	private int count;				// number of key-value pairs in the B-tree
	
	/**
	 * B-Tree数据结构
	 * @author houxu_000
	 *
	 */
	private static final class Node {
		private int number;								// number of children
		private Entry[] children = new Entry[M];	// the array of children
		private Node(int k) {
			number = k;
		}
	}
	
	/**
	 * 内部结点:只使用 key 和  next
	 * 外部结点:只使用 key 和 value
	 * @author houxu_000
	 *
	 */
	private static class Entry {
		
		private Comparable key;
		private final Object val;
		private Node next;		//哨兵结点-引导遍历子树结点
		public Entry(Comparable key, Object val, Node next) {
			this.key = key;
			this.val = val;
			this.next = next;
		}
	}
	
	/**
     * Initializes an empty B-tree.
     */
	public BTree() {
		root = new Node(0);
	}
	
	/**
     * Returns true if this symbol table is empty.
     * @return {@code true} if this symbol table is empty; {@code false} otherwise
     */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
	public int size() {
		return count;
	}
	
	/**
     * Returns the height of this B-tree (for debugging).
     *
     * @return the height of this B-tree
     */
	public int height(){
		return height;
	}
	
	/**
	 * 根据键值查找
	 * @param  key the key
     * @return the value associated with the given key if the key is in the symbol table
     *         and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public Value get(Key key){
		if (key == null) throw new IllegalArgumentException("argument to get() is null");
		return search(root, key, height);
	}

	@SuppressWarnings("unchecked")
	private Value search(Node x, Key key, int ht) {
		Entry[] children = x.children;
		
		//外部结点
		if (ht == 0) {
			for (int j = 0; j < x.number; j++)
				if (eq(key, children[j].key))
					return (Value) children[j].val; 
		}
		//内部结点
		else {
			for (int j = 0; j < x.number; j++)
				if (j+1 == x.number || less(key, children[j+1].key))
					return search(children[j].next, key, ht-1);
		}
		return null;
	}

	/**
	 * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
	 * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public void put(Key key, Value val) {
		if (key == null) throw new IllegalArgumentException("argument key to put() is null");
		Node u = insert(root, key, val, height);
		count++;
		if (u == null) return;
		
		Node t = new Node(2);
		t.children[0] = new Entry(root.children[0].key, null, root);
		t.children[1] = new Entry(u.children[0].key, null, u);
		root = t;
		height++;
	}
	
	private Node insert(Node h, Key key, Value val, int ht) {
		int j = 0;
		Entry t = new Entry(key, val, null);
		
		//外部结点
		if (ht == 0) {
			for (j = 0; j < h.number; j++)
				if (less(key, h.children[j].key))	break;//获得j值来确定key在children中应该插入的位置
		}
		//内部结点
		else {
			for (j = 0; j < h.number; j++) 
				if ((j + 1 == h.number) || less(key, h.children[j+1].key)) {
					Node u = insert(h.children[j++].next, key, val, ht -1);
					if (u == null) return null;			
					t.key  = u.children[0].key;			//平衡
					t.next = u;		
					break;
				}
		}
		
		for (int i = h.number; i > j; i--)
			h.children[i] = h.children[i-1];
		h.children[j] = t;
		h.number++;
		if (h.number < M)	return null;
		else			return split(h);
	}

	private Node split(Node h) {
		Node t = new Node(M/2);
		h.number = M/2;
		for (int j = 0; j < M/2; j++)
			t.children[j] = h.children[M/2+j];
		return t;
	}

	@Override
	public String toString() {
		return toString(root, height, "") + "\n";
	}
	
	private String toString(Node h, int ht, String indent) {
		StringBuilder s = new StringBuilder();
		Entry[] children = h.children;
		
		if (ht == 0) {
			for (int j = 0; j < h.number; j++)
				s.append(indent + children[j].key + " "+ children[j].val + "\n");
		}
		else {
			for (int j = 0; j < h.number; j++){
				//递归打印显示动态平衡
				if (j > 0)	s.append(indent + "(" + children[j].key + ")\n");
				s.append(toString(children[j].next, ht-1, indent + "\t"));
			}
		}
		return s.toString();
	}

	@SuppressWarnings("unchecked")
	private boolean less(Comparable k1, Comparable k2) {
		return k1.compareTo(k2) < 0;
	}

	@SuppressWarnings("unchecked")
	private boolean eq(Comparable k1, Comparable k2) {
		return k1.compareTo(k2) == 0;
	}

    /**
     * Unit tests the {@code BTree} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        BTree<String, String> st = new BTree<String, String>();

        st.put("www.cs.princeton.edu", "128.112.136.12");
        st.put("www.cs.princeton.edu", "128.112.136.11");
        st.put("www.princeton.edu",    "128.112.128.15");
        st.put("www.yale.edu",         "130.132.143.21");
        st.put("www.simpsons.com",     "209.052.165.60");
        st.put("www.apple.com",        "17.112.152.32");
        st.put("www.amazon.com",       "207.171.182.16");
        st.put("www.ebay.com",         "66.135.192.87");
        st.put("www.cnn.com",          "64.236.16.20");
        st.put("www.google.com",       "216.239.41.99");
        st.put("www.nytimes.com",      "199.239.136.200");
        st.put("www.microsoft.com",    "207.126.99.140");
        st.put("www.dell.com",         "143.166.224.230");
        st.put("www.slashdot.org",     "66.35.250.151");
        st.put("www.espn.com",         "199.181.135.201");
        st.put("www.weather.com",      "63.111.66.11");
        st.put("www.yahoo.com",        "216.109.118.65");


        System.out.println("cs.princeton.edu:  " + st.get("www.cs.princeton.edu"));
        System.out.println("www.princeton.edu:	" + st.get("www.princeton.edu"));
        System.out.println("hardvardsucks.com: " + st.get("www.harvardsucks.com"));
        System.out.println("simpsons.com:      " + st.get("www.simpsons.com"));
        System.out.println("apple.com:         " + st.get("www.apple.com"));
        System.out.println("ebay.com:          " + st.get("www.ebay.com"));
        System.out.println("dell.com:          " + st.get("www.dell.com"));
        System.out.println();

        System.out.println("size:    " + st.size());
        System.out.println("height:  " + st.height());
        System.out.println(st);
        System.out.println();
        
        
        /**
         * 理解示例
         */
        BTree<String, Integer> st2 = new BTree<String, Integer>();
        st2.put("A", 1);
        st2.put("B", 2);
        st2.put("C", 3);
        st2.put("D", 4);
        st2.put("E", 5);
        st2.put("F", 6);
//        st2.put("G", 7);
//        st2.put("H", 8);
//        st2.put("I", 9);
        
        System.out.println();
        System.out.println("size:\t\t" + st2.size());
        System.out.println("height:\t\t" + st2.height());
        System.out.println(st2);
        System.out.println();
    }
}
