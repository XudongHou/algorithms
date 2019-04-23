package com.hxd.strings.compression;

import com.hxd.introcs.stdlib.BinaryStdIn;
import com.hxd.introcs.stdlib.BinaryStdOut;
import com.hxd.sort.priorityQueue.MinPQ;

/******************************************************************************
 *  Compilation:  javac Huffman.java
 *  Execution:    java Huffman - < input.txt   (compress)
 *  Execution:    java Huffman + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   http://algs4.cs.princeton.edu/55compression/abra.txt
 *                http://algs4.cs.princeton.edu/55compression/tinytinyTale.txt
 *                http://algs4.cs.princeton.edu/55compression/medTale.txt
 *                http://algs4.cs.princeton.edu/55compression/tale.txt
 *
 *  Compress or expand a binary input stream using the Huffman algorithm.
 *
 *  % java Huffman - < abra.txt | java BinaryDump 60
 *  010100000100101000100010010000110100001101010100101010000100
 *  000000000000000000000000000110001111100101101000111110010100
 *  120 bits
 *
 *  % java Huffman - < abra.txt | java Huffman +
 *  ABRACADABRA!
 *
 ******************************************************************************/

/**
 * 霍夫曼压缩:用较少的比特表示出现频率高的字符,用较多的比特表示出现频率较少的字符
 * {@code Huffman}类提供静态方法，用于通过8位扩展ASCII字母表使用霍夫曼码压缩和扩展二进制输入。
 * @author houxu_000 20170324
 */

public class Huffman {
	
	/**
	 * 霍夫曼压缩的实现:
	 * 	读取输入:
	 * 	将输入中的每个char值的出现频率制成表格;
	 * 	根据频率构造相应的霍夫曼编码树;
	 * 	构造编译表,将输入中的每个char值和一个比特字符串相关联;
	 * 	将单词查找树编码为比特字符串并写入输出流;
	 * 	将单词总数编码为比特字符串并写入输出流;
	 * 	使用编译表翻译每个输入字符;
	 * 
	 * 展开一条编译过的比特流:
	 * 	读取单词查找树(编码在比特流的开头);
	 *  读取需要解码的字符的数量
	 *  使用单词查找树将比特流解码
	 *  霍夫曼压缩算法含有4个递归方法处理单词查找树,整个压缩过程需要7步
	 */
	
	/**
	 * 变长前缀码:和每个字符所向关联的编码都是一个比特字符串,就好像有一个以字符为键,比特字符串为值的符号表.如果所有
	 * 字符编码都不会成为其他字符编码的前缀,那么就不需要分隔符.含有这种性质的编码规则叫做前缀码.所有的前缀码的解码方
	 * 式都和它一样,是唯一的(不需要任何分隔符),因此前缀码被广泛应用于实际生产之中.像7位的ASCII编码这样的定长编码
	 * 也是前缀码.
	 * 表示前缀码的一种简单方式就是使用单词查找树,事实上,任意含有M个空链接的单词查找树都为M个字符定义一种前缀码方式
	 * :将空链接替换为指向叶子结点(含有两个空链接的结点)的链接,每个叶子结点都含有一个需要编码的字符.这样每个字符的
	 * 编码就是从根结点到该结点的路径表示的比特字符串,左链接表示0,右链接表示1.
	 * 
	 * 寻找最优前缀码的通用方法是D.Huffman发现的,因此成为霍夫曼编码
	 * 
	 * 概述:
	 * 	将待编码的比特流看作一个字节流并按照以下方式:
	 *		构造一棵编码单词查找树;
	 *		将该树以字节流的形式写入输出以供展开时使用;
	 *		使用该树将字节流编码为比特流;
	 *  在展开时需要:
	 *  	读取单词查找树(保存在比特流的开头);
	 *  	使用该树将比特流解码.
	 *  
	 * 最优性:
	 * 	在树中高频率的字符比较比低频率的字符离根结点更近,因此编码所需的比特更小,所以这种编码方式更好.但为什么这是一种
	 * 最优的前缀码?首先需要定义树的加权外部路径长度这个概,它是所有叶子结点的权重(频率)和深度之积的和.
	 * 命题T:
	 * 	对于任意前缀码,编码后的比特字符串的长度等于相应单词查找树的加权外部路径长度
	 * 证明:
	 * 	每个叶子结点的深度就是将该叶子结点的字符编码所需的比特数.因此,加权外部路径长度就是编码后的比特
	 * 字符串的长度:它等于所有字符的出现次数和字符编码长度之积的和
	 * 命题U:
	 * 	给定一个含有r个符号的集合和它们的频率,霍夫曼算法所构成的前缀码是最优的
	 */
	
	/**
	 * alphabet size of extended ASCII
	 */
	private static final int R = 256;
	
	private Huffman() {}
	
	/**
	 * Huffman trie node
	 * @author houxu_000
	 *
	 */
	private static class Node implements Comparable<Node> {

		private char ch;				//内部结点不会使用该变量
		private int freq;				//展开过程不会使用该变量
		private final Node left, right;
		
		
		public Node(char ch, int freq, Node left,Node right) {
			this.ch = ch;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}
		
		private boolean isLeaf() {
			assert ((left == null) && (right == null) || (left != null) && (right != null));
			return (left == null) && (right == null);
		}
		
		@Override
		public int compareTo(Node that) {
			return this.freq - that.freq;
		}
		
		
	}

	/**
	 * 压缩.
	 * 为了保证数据压缩流程的完整,必须在压缩时将树写入比特流并在展开时读取它.通过使用单词查找树的前序遍历
	 */
	public static void compress() {
		String s = BinaryStdIn.readString();
		char[] input = s.toCharArray();
		
		//tabulate frequency counts 制表记录字符频率
		int[] freq = new int[R];
		for(int i = 0; i < input.length; i++)
			freq[input[i]]++;
		//build huffman trie
		Node root = buildTrie(freq);
		
		//构造编译表
		String[] st = new String[R];
		buildCode(st, root, "");
		
		//print trie for decoder 打印字符查找树以构建压缩,前序遍历
		writeTrie(root);
		
		//打印原始未压缩消息中的字节数
		BinaryStdOut.write(input.length);
		
		/**
		 * 使用霍夫曼码对输入进行编码
		 * 查找输入的每个字符所对应的编码String对象,将char数组中字符转化为0和1的值并写入输出的比特字符串中
		 */
		for (int i = 0; i < input.length; i++) {
			String code = st[input[i]];
			for (int j = 0; j < code.length(); j++) {
				if (code.charAt(j) == '0')
					BinaryStdOut.write(false);
				else if (code.charAt(j) == '1')
					BinaryStdOut.write(true);
				else throw new IllegalStateException("Illegal state");
			}
		}
		
		BinaryStdOut.close();
	}
		
	/**
	 * 根据freqeuncies来构建霍夫曼树
	 * 将被编码的字符放在叶子结点中并在每个结点中维护一个名为freq的实例变量来表示以它为根结点的子树中的所有字符出现的频率.
	 * 构造过程:
	 *   创建一片由许多叶子结点的树组成的森林.每棵树都表示输入流中的一个字符,每个结点中的freq变量的值都表示了它在输入流中的
	 * 出现频率;
	 * 	  为了得到这些频率需要读取整个输入流--霍夫曼编码是一个两轮算法,因为需要再次读取输入流才能压缩;
	 *   接下来自底向上根据频率信息构造这颗编码的单词查找树,首先找到两个频率最小的结点,然后创建一个以二者为子结点的新结点(
	 * 新结点的频率值为它的两个子结点的频率值之和),这个操作将会将森林树的数量减一.使用优先队列能够轻易实现这个过程.
	 * 构建完成:
	 *   这棵树中的叶子结点为所有待编码的字符和它们在输入中出现的频率,每个非叶子结点中的频率为它的两个子结点之和.频率较低的
	 * 结点会被安排在树的底层,而高频率的结点则会被安排在根结点附近的地方,根结点的频率值等于输入中的字符数量
	 * 
	 *   当一个结点被选中时,也可能是有若干个结点和它的权重相同,霍夫曼算法并没有说明如何区别它们,也没有说明应该如何确定子结点
	 *   的左右位置.不同的选择会得到不同的霍夫曼编码,单用它们将信息编码所得到的比特字符串在所有前缀码中都是最优的
	 *  
	 * @param freq
	 * @return
	 */
	private static Node buildTrie(int[] freq) {
		
		// 使用优先队列来存储森林
		MinPQ<Node> pq = new MinPQ<Node>();
		for (char i = 0; i < R; i++)
			if (freq[i] > 0)
				pq.insert(new Node(i, freq[i], null, null));		//初始化时队列为单节点森林
		//特殊情况下，只有一个字符具有非零频率
		if (pq.size() > 1) {
			if (freq['\0'] == 0)	pq.insert(new Node('\0', 0, null, null));
			else					pq.insert(new Node('\1', 0, null, null));
		}
		
		//合并两颗频率最小的树
		while (pq.size() > 1) {
			Node left = pq.delMin();
			Node right = pq.delMin();
			Node parent = new Node('\0', left.freq + right.freq, left, right);
			pq.insert(parent);
		}
		return pq.delMin();
	}

	/**
	 * 将编译表树写入到标准输出
	 * 当访问的是一个叶子结点时,他会写入一个比特1,紧接着是该叶子结点中字符的8为ASCII编码
	 * @param root
	 */
	private static void writeTrie(Node x) {
		if (x.isLeaf()) {
			BinaryStdOut.write(true);
			BinaryStdOut.write(x.ch, 8);
			return;
		}
		
		BinaryStdOut.write(false);
		writeTrie(x.left);
		writeTrie(x.right);
	}
	
	/**
	 * 使用单词查找树定义的编码来构造编译表,对于任意单词查找树,都能产生一张将树中的字符和比特字符串(用由0和1组成的String)
	 * 相对应的编译表.即为一张将每个字符和它的比特字符串相关联的符号表.递归遍历整棵树并为每个结点维护了一条从根结点到它的路径
	 * 对应的二进制字符串,编译表建立后,压缩只需在其中查找输入字符串所对应的编码即可
	 * @param st
	 * @param root
	 * @param string
	 */
	private static void buildCode(String[] st, Node x, String s) {
		if (!x.isLeaf()) {
			buildCode(st, x.left, s + '0');
			buildCode(st, x.right, s + '1');
		}
		else
			st[x.ch] = s;
	}
	
	/**
	 * 解压缩,显示了霍夫曼压缩算法的展开的简单性也是前缀码
	 */
	public static void expand() {
		
		//从输入流读取霍夫曼编码树
		Node root = readTrie();
		
		// 要写入的字节数
		int length = BinaryStdIn.readInt();
		
		//解码,展开第i个编码所对应的字母.根据比特流的输入从根结点开始向下移动(读取一个比特,如果为0则移动到左子结点)
		//为1则移动到右子结点.当遇到叶子结点后,输出该结点的字符并重新回到根结点.
		for (int i = 0; i < length; i++) {
			Node x = root;
			while (!x.isLeaf()) {
				boolean bit = BinaryStdIn.readBoolean();
				if (bit)	x = x.right;
				else		x = x.left;
			}
			BinaryStdOut.write(x.ch, 8);
		}
		BinaryStdOut.close();
	}

	/**
	 * 从比特流字符串中重新构建的了单词查找树:首先读取一个比特以得到当前结点的类型,如果是叶子结点(比特为1)那么就读取
	 * 字符的编码并创建一个叶子结点;如果是内部结点(比特为0)那么就创建一个内部结点并(递归地)继续构造它的左右子树
	 * @return
	 */
	private static Node readTrie() {
		boolean isLeaf = BinaryStdIn.readBoolean();
		
		if (isLeaf) 
			return new Node(BinaryStdIn.readChar(), -1, null, null);
		else
			return new Node('\0', -1, readTrie(), readTrie());
	}
	
	/**
     * Sample client that calls {@code compress()} if the command-line
     * argument is "-" an {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
