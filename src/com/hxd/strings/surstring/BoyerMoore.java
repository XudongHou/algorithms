package com.hxd.strings.surstring;

/**
 * {@code BoyerMoore}类在文本字符串中查找模式字符串的第一次出现。 此实现使用Boyer-Moore算法
 * （具有坏字符规则，但不是强好后缀规则）。
 * 当可以在文本字符串中回退时,如果可以从右向左扫描模式字符串并将它和文本匹配,那么就能得到一种非常快的字符串
 * 查找算法.
 * @author houxu_000 20170308
 * */

public class BoyerMoore {
	private final int R;
	private int[] right;
	
	private char[] pattern;
	private String pat;
	
	/**
	 * 要实现启发式的处理不匹配的字符串,使用数组right[]记录字母表中的每个字符在模式中出现的最靠有的地方(如果字符在模式中不存在则
	 * 表示为-1).这个值揭示了如果改字符串出现在文本中且查找时造成了一次匹配失败,应该向右跳跃多远.要将right[]数组初始化,首先将
	 * 所有元素的值设为-1,然后对于0到M-1的j,将right[pat.charAt(j)]设为j.
	 * 在计算完right[]数组之后,算法的实现就很简单了,用一个索引为i在文本中从左向右移动,用另一个索引j在模式中从右向左移动.内循环
	 * 会检查正文和模式字符串在位置i时候一致.如果从M-1到0的所有j,txt.charAt(i+j) 都和pat.chatAt(j)相等,那么就找到
	 * 了一个匹配.否则匹配失败,就会遇到一下三种情况.
	 * 		如果造成匹配失败的字符串不包含在模式字符串中,将模式字符串向右移动j+1个位置(即将i添加j+1);
	 * 		如果造成匹配失败的字符包含在模式字符串中,那就可以使用right[]数组来将模式字符串和文本对齐,使得该字符和模式中字符串
	 * 		中出现的最有位置相匹配;
	 * 		如果这种方式无法增大i,直接将i加1来保证模式字符串至少向右移动了一个位置;
	 * 
	 * 在一般情况下,对于长度为N的文本和长度为M的模式字符串,使用了Boyer-Moore的子字符串查找方法通过启发式处理不匹配的字符串需要
	 * ~NM次字符比较
	 */
	
	/**
     * Preprocesses the pattern string.
     *
     * @param pat the pattern string
     */
	public BoyerMoore(String pat) {
		this.R=256;
		this.pat = pat;
		
		right = new int[R];
		for (int c = 0; c < R; c++)
			right[c] = -1;
		for (int j = 0; j < pat.length(); j++)
			right[pat.charAt(j)] = j;
	}
	
	/**
     * Preprocesses the pattern string.
     *
     * @param pattern the pattern string
     * @param R the alphabet size
     */
	public BoyerMoore(char[] pattern, int R) {
		this.R = R;
		this.pattern = new char[pattern.length];
		for (int j = 0; j < pattern.length; j++)
			this.pattern[j] = pattern[j];
		
		right = new int[R];
		for (int c = 0;  c < R; c++)
			right[c] = -1;
		for (int j = 0 ; j < pattern.length; j++)
			right[pattern[j]] = j;
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
		int m = pat.length();
		int n = txt.length();
		int skip ;
		
		for (int i = 0; i <=n - m; i += skip) {
			skip = 0;
			for (int j = m-1; j >= 0; j--) {
				if (pat.charAt(j) != txt.charAt(i+j)) {
					skip = Math.max(1, j-right[txt.charAt(j+i)]);
					break;
				}
			}
			if (skip == 0) return i;
		}
		
		return n;
	}
	
	/**
     * Returns the index of the first occurrrence of the pattern string
     * in the text string.
     *
     * @param  text the text string
     * @return the index of the first occurrence of the pattern string
     *         in the text string; n if no such match
     */
	public int search (char[] text) {
		int m = pattern.length;
		int n = text.length;
		int skip;
		
		for (int i = 0; i <= n-m; i += skip){
			skip = 0;
			for (int j = m-1; j >= 0; j--) {
				if (pattern[j] != text[j+i]) { 
					skip = Math.max(1, j - right[text[j+i]]);
					break;
				}
			}
			if (skip == 0) return i;
		}
		return n;
	}
	
	public static void main(String[] args) {
		String pats[] = new String[]{ "ababab","aaa","aab","bra"};
		String txt = "abacadabrabababracabracadabrabraaabracad";
		
		for (String pat : pats) {
			BoyerMoore bm = new BoyerMoore(pat);
			int offset = bm.search(txt);
			
			System.out.println("text:    "+txt);
			System.out.print("pattern: ");
			for (int i = 0; i < offset; i ++)
				System.out.print(" ");
			System.out.println(pat);
			
			System.out.println();
			
			char[] pattern = pat.toCharArray();
			char[] text = txt.toCharArray();
			
			bm = new BoyerMoore(pattern, 256);
			offset = bm.search(text);
			
			System.out.println("text:    "+txt);
			System.out.print("pattern: ");
			for (int i = 0; i < offset; i ++)
				System.out.print(" ");
			System.out.println(pat);
			
			System.out.println();
		}
	}
}