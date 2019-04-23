package com.hxd.strings.surstring;

/**
 * 
 * @author houxu_000 20170308
 * */

public class KMPplus {
	private String pattern;
	private int[] next;
	
	public KMPplus(String pattern) {
		this.pattern = pattern;
		int m = pattern.length();
		next = new int[m];
		int j = -1;
		for (int i = 0; i < m; i++) {
			if (i == 0)										next[i] = -1;
			else if (pattern.charAt(i)!=pattern.charAt(j))	next[i] = j;
			else											next[i] = next[j];
			while (j >= 0 && pattern.charAt(i) != pattern.charAt(j))//保证j一直是-1
				j = next[j];
			j++ ;
		}
		
		for (int i = 0; i < m; i++)
			System.out.println("next["+ i + "]" + next[i]);
	}
	
	public int search(String text) {
		int m = pattern.length();
		int n = text.length();
		int i ,j;
		for (i = 0, j = 0; i < n && j < m; i++) {
			while (j >= 0 && text.charAt(i) != pattern.charAt(j))
				j = next[j];
			j++;
		}
		if (j == m) return i - m;
		else		return n;
	}
	
	public static void main(String[] args) {
		String pats[] = new String[]{ "ababab","aaa","aab","bra"};
		String txt = "abacadabrabababracabracadabrabraaabracad";
		
		for (String pat : pats) {
			KMPplus kmp = new KMPplus(pat);
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
