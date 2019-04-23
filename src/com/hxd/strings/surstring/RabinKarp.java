package com.hxd.strings.surstring;

import java.math.BigInteger;
import java.util.Random;

/**
 * 指纹字符串查找算法
 * 基本思想: 长度为M的字符串对应着一个R进制的M位数.为了用一张大小为Q的散列表来保存这种类型的键,
 * 需要一个能够将R进制的M位数转化为一个0到Q-1之间的int值散列函数.除留余数法是一个很好的选择:
 * 将该树除以Q并取余.在实际应用中会使用一个随机的素数Q,在不溢出的情况下悬着一个尽可能大的值.
 * <p>
 * Rabin-karp算法的基础是对与所有位置i,高效计算文本中i+1的位置的子字符串散列值.这可以由一个
 * 简单的数学公式得到.构造函数为模式字符串计算了散列值patHash并在变量RM中保存了R^(M-1) mod Q
 * 的值.hashSearch()方法开头计算了文本的前M个字母的散列表并将它和模式字符串的散列值进行比较.
 * 如果未能匹配,将会在文本中继续前进,用讨论的方法计算由位置i开始的M个字符的散列值,将它保存在txtHash
 * 变量中并将每新的散列值和patHash进行比较
 * @author houxu_000 20170309
 * */

public class RabinKarp {
	private String pat;		
	private long patHash;	//pattern hash value 
	private int m;			//pattern length
	private long q;			//a large prime, small enough to avoid long overflow
	private int R;			//radix
	private long RM;		//R^(M-1)&Q
	
	/**
     * Preprocesses the pattern string.
     *
     * @param pattern the pattern string
     * @param R the alphabet size
     */
	public RabinKarp(char[] pattern, int R) {
		this.pat = String.valueOf(pattern);
		this.R = R;
		throw new UnsupportedOperationException("Operation not supported yet");
	}
	
	/**
     * Preprocesses the pattern string.
     *
     * @param pat the pattern string
     */
	public RabinKarp(String pat) {
		this.pat = pat;
		R = 256;
		m = pat.length();
		q = longRandomPrime();
		RM = 1;
		for (int i = 1; i <= m-1; i++) 
			RM = (R * RM) % q;
		patHash = hash(pat, m);
	}
	
	private long hash(String key, int m) {
		long h = 0;
		for (int j = 0; j < m; j++)
			h = (R*h + key.charAt(j)) % q;
		return h;
	}

	
	private boolean check(String txt, int i) {
		for (int j = 0; j < m; j++)
			if (pat.charAt(j) != txt.charAt(i + j))
				return false;
		return true;
		
	}
	
	/**
     * Returns the index of the first occurrrence of the pattern string
     * in the text string.
     *
     * @param  txt the text string
     * @return the index of the first occurrence of the pattern string
     *         in the text string; n if no such match
     */
	public int search(String txt) {
		int n = txt.length();
		if (n < m)	return n;
		long txtHash = hash(txt, m);
		
		if (patHash == txtHash && check(txt, 0))
			return 0;
		
		for (int i = m; i < n; i++) {
			txtHash = (txtHash + q - RM*txt.charAt(i-m) % q) % q;
			txtHash = (txtHash*R + txt.charAt(i)) % q;
			
			int offset = i - m + 1;
			if ((patHash == txtHash) && check(txt, offset))
				return offset;
		}
		
		return n;
	}
	
	// a random 31-bit prime
	private long longRandomPrime() {
		BigInteger prime = BigInteger.probablePrime(31, new Random());
		return prime.longValue();
	}
	
	public static void main(String[] args) {
		String pats[] = new String[]{ "ababab","aaa","aab","bra"};
		String txt = "abacadabrabababracabracadabrabraaabracad";
		
		for (String pat : pats) {
			RabinKarp kmp = new RabinKarp(pat);
			int offset = kmp.search(txt);
			
			System.out.println("text:    "+txt);
			System.out.print("pattern: ");
			for (int i = 0; i < offset; i ++)
				System.out.print(" ");
			System.out.println(pat);
			
			System.out.println();
		}
		
	}
}
