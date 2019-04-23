package com.hxd.strings.surstring;

/**
 *  Search for the string a^N b in the string  a^2N 
 *  where N = 2^n.
 * */

public class SystemSearch {
	public static void main(String[] args) {
		int n = 8;
		String text = "a";
		String query = "a";
		for(int i = 0; i < n ; i++){
			text = text + text;
			query = query + query;
		}
		text = text + text;
		query = query + "b";
		System.out.println(text.indexOf(query));
	}
}
