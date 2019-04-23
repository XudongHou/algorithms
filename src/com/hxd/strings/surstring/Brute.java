package com.hxd.strings.surstring;

/**
 * 暴力字符串查找方法;
 * 在最坏的情况下,暴力字符串查找方法在长度为N的文本中查抄长度为M的模式需要的~NM次字符比较
 * @author houxu_000 20170304
 * */

public class Brute {
	
	private Brute() {};
	
	// return offset of first match or n if no match
	public static int search1(String pat, String txt) {
		int m = pat.length();
        int n = txt.length();

        for (int i = 0; i <= n - m; i++) {
            int j;
            for (j = 0; j < m; j++) {
                if (txt.charAt(i+j) != pat.charAt(j))
                    break;
            }
            if (j == m) return i;            // found at offset i
        }
        return n;                            // not found
	}
	
	// return offset of first match or n if no match
	public static int search2(String pat, String txt) {
		int m = pat.length();
		int n = txt.length();
		int i,j;
		for (i = 0, j=0; i < n&& j < m; i++) {
			if (txt.charAt(i) == pat.charAt(j))	j++;
			else {
				i -=j;
				j = 0;
			}
		}
		if (j == m) return i - m;	//found
		else		return n;
	}
	
	// return offset of first match or n if no match
	public static int search1(char[] pattern, char[] text) {
		int m = pattern.length;
		int n = text.length;
		
		for (int i = 0; i <= n-m; i++) {
			int j;
			for (j = 0; j < m; j++) 
				if (text[i+j] != pattern[j])
					break;
			if (j == m)
				return i;
		}
		return n;
	}
	
	// return offset of first match or n if no match
	public static int search2(char[] pattern, char[] text) {
		int m = pattern.length;
		int n = text.length;
		int i,j;
		for (i = 0, j = 0; i < n && j < m; i++) {
			if (text[i] == pattern[j])	j++;
			else {
				i -= j;
				j=0;
			}
		}
		if (j == m)	return i-m;
		else		return n; 
	}
	
	
	public static void main(String[] args) {
		String pat = "abracadabr";
		String txt = "abacadabrabracabracadabrabrabracad";
		
		int offset1a = search1(pat, txt);
        int offset2a = search2(pat, txt);
        

        // print results
        System.out.println("text:    " + txt);

        // from brute force search method 1a
        System.out.print("pattern: ");
        for (int i = 0; i < offset1a; i++)
            System.out.print(" ");  
        System.out.println(pat);
        System.out.println(offset1a);

        // from brute force search method 2a
        System.out.print("pattern: ");
        for (int i = 0; i < offset2a; i++)
            System.out.print(" ");
        System.out.println(pat);
        System.out.println(offset2a);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
