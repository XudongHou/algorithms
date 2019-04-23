package com.hxd.strings.trie;

import java.io.File;
import java.nio.file.FileSystemNotFoundException;

import com.hxd.base.Queue;
import com.hxd.introcs.stdlib.In;

/**
 * {@code TrieST}类表示键 -值对的符号表，其中包含字符串键和通用值。它支持通常的put，get，contains，delete，
 * size和is-empty方法。它还提供基于字符的方法，用于查找符号表中的字符串，该符号表是给定前缀的最长前缀，查找符号表中以
 * 给定前缀开头的所有字符串，并查找符号表中符合给定模式。符号表实现关联数组抽象：当将值与已经在符号表中的键相关联时，惯例是
 * 用新值替换旧值。与{@link java.util.Map}不同，此类使用的约定是值不能为{@code null} - 将与键相关联的值设
 * 置为{@code null}等效于从符号表中删除键。此实现使用256路trie。 put，contains，delete和longest前缀操作
 * 占用与密钥长度成比例的时间（在最坏的情况下）。建设需要恒定的时间。大小和is-empty操作需要固定的时间。建设需要恒定的时间。
 * <p>
 * 单词查找树的性质:单词查找树的链表结构(形状)和键的插入或删除顺序无关:对于任意给定的一组键,其单词查找树都是唯一的.
 * 在单词查找树中查找一个键或是插入一个键时,访问数组的次数的次数对多的键的长度加1.
 * 字母表的大小为R,在一颗由N个随机键构造的单词查找树中,未命中查找平均所需检查的结点数量为~logrN.
 * 一颗单词查找树中的连接总数在RN到RNw之间,其中w为键的平均长度
 * <p>
 * @author houxu_000 20170228
 * */
@SuppressWarnings("unchecked")
public class TrieST<Value> {
	private static final int R = 256;
	
	private Node root;		// root of trie
	private int n;			// number of keys in trie
	
	private static class Node {
		private Object val;
		private Node[] next = new Node[R];
	}
	
	public TrieST() {}
	
	/**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and {@code null} if the key is not in the symbol table
     * @throws NullPointerException if {@code key} is {@code null}
     */
	public Value get(String key) {
		Node x = get(root, key, 0);
		if (x == null) return null;
		return (Value) x.val;
	}

	/**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *     {@code false} otherwise
     * @throws NullPointerException if {@code key} is {@code null}
     */
	
	public boolean contains(String key) {
		return get(key) != null;
	}
	
	private Node get(Node x, String key, int d) {
		if (x == null) return null;
		if (d == key.length()) return x;
		char c = key.charAt(d);
		return get(x.next[c], key, d+1);
	}
	
	/**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param val the value
     * @throws NullPointerException if {@code key} is {@code null}
     */
	public void put(String key, Value val) {
		if (val == null) delete(key);
		else root = put(root, key, val, 0);
	}


	private Node put(Node x, String key, Value val, int d) {
		if (x == null) x = new Node();
		if (d == key.length()) {
			if (x.val == null) n++;
			x.val = val;
			return x;
		}
		char c = key.charAt(d);
		x.next[c] = put(x.next[c], key, val, d+1);
		return x;
	}
	
	/**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
	public int size() {
		return n;
	}

	/**
     * Is this symbol table empty?
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     * @return all keys in the symbol table as an {@code Iterable}
     */
	public Iterable<String> keys() {
		return keysWithPrefix("");//从根开始
	}
	
	/**
     * Returns all of the keys in the set that start with {@code prefix}.
     * @param prefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     *     as an iterable
     */
	public Iterable<String> keysWithPrefix(String prefix) {
		Queue<String> results = new Queue<String>();
		Node x = get(root, prefix, 0);
		collect(x, new StringBuilder(prefix), results);
		return results;
	}


	private void collect(Node x, StringBuilder prefix, Queue<String> results) {
		//不符合的将不会保存
		if (x == null) return;
		if (x.val != null) results.enqueue(prefix.toString());
		for (char c = 0; c < R; c++) {
			prefix.append(c);
			collect(x.next[c], prefix, results);
			prefix.deleteCharAt(prefix.length() - 1);
		}
	}

	/**
     * Returns all of the keys in the symbol table that match {@code pattern},
     * where . symbol is treated as a wildcard character.
     * @param pattern the pattern
     * @return all of the keys in the symbol table that match {@code pattern},
     *     as an iterable, where . is treated as a wildcard character.
     */
	public Iterable<String> keysThatMatch(String pattern) {
		Queue<String> results = new Queue<String>();
		collect(root, new StringBuilder(), pattern, results);
		return results;
	}

	private void collect(Node x, StringBuilder prefix, String pattern, Queue<String> results) {
		if (x == null) return;
		int d = prefix.length();
		if (d == pattern.length() && x.val != null)
			results.enqueue(prefix.toString());
		
		if (d == pattern.length())
			return;
		
		char c = pattern.charAt(d);
		if (c == '.'){
			for (char ch = 0; ch < R; ch++) {
				prefix.append(ch);
				collect(x.next[c], prefix, pattern, results);
				prefix.deleteCharAt(prefix.length() -1);
			}
		}
		else {
			prefix.append(c);
			collect(x.next[c], prefix, pattern, results);
			prefix.deleteCharAt(prefix.length()-1);
		}
	}
	
	/**
     * Returns the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null}, if no such string.
     * @param query the query string
     * @return the string in the symbol table that is the longest prefix of {@code query},
     *     or {@code null} if no such string
     * @throws NullPointerException if {@code query} is {@code null}
     */
	public String longestPrefixOf(String query) {
		int length = longestPrefixOf(root, query, 0, -1);
		if (length == -1) return null;
		else	return query.substring(0, length);
	}

    // returns the length of the longest string key in the subtrie
    // rooted at x that is a prefix of the query string,
    // assuming the first d character match and we have already
    // found a prefix match of given length (-1 if no such match)
	private int longestPrefixOf(Node x, String query, int d, int length) {
		if (x == null) return length;
		if (x.val != null) length = d;
		if (d == query.length()) return length;
		char c = query.charAt(d);
		return longestPrefixOf(x.next[c], query, d+1, length);
	}

	/**
     * Removes the key from the set if the key is present.
     * @param key the key
     * @throws NullPointerException if {@code key} is {@code null}
     */
	public void delete(String key) {
		root = delete(root, key, 0);
	}


	private Node delete(Node x, String key, int d) {
		if (x == null) return null;
		if (d == key.length()) {
			if (x.val != null) n--;
			x.val = null;
		}
		else {
			char c = key.charAt(d);
			x.next[c] = delete(x.next[c], key, d+1);
		}
		// remove subtrie rooted at x if it is completely 
		if (x.val != null) return x;
		//是否下面还有连接
		for (int c = 0; c < R; c++)
			if (x.next[c] != null)
				return x;
		//所有链接都为空 删除此节点
		return null;
	}
	
	public static void main(String[] args) {
		try {
			File[] files = new File[]{
					new File(".\\algs4-data\\shellsST.txt"),
					};
			for(File file : files) {
				In in = new In(file);
				TrieST<Integer> st = new TrieST<Integer>();
				for (int i = 0; !in.isEmpty(); i++) {
					String key = in.readString();
					st.put(key, i);
				}
				
				if (st.size() < 100) {
					System.out.println("keys(\"\"):");
					for (String key : st.keys()) 
						System.out.println(key + " " + st.get(key));
					System.out.println();
				}
				
				System.out.println("longestPrefixOf(\"shellsort\"):");
		        System.out.println(st.longestPrefixOf("shellsort"));
		        System.out.println();

		        System.out.println("longestPrefixOf(\"quicksort\"):");
		        System.out.println(st.longestPrefixOf("quicksort"));
		        System.out.println();

		        System.out.println("keysWithPrefix(\"shor\"):");
		        for (String s : st.keysWithPrefix("shor"))
		            System.out.println(s);
		        System.out.println();

		        System.out.println("keysThatMatch(\".he.l.\"):");
		        for (String s : st.keysThatMatch(".he.l."))
		            System.out.println(s);
			}
		} catch (FileSystemNotFoundException e) {
			e.printStackTrace();
		}
	}
}
