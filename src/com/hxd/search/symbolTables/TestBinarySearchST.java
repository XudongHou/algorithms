package com.hxd.search.symbolTables;

/**
 * @author 候旭东 20161227
 * */

public class TestBinarySearchST {
	public static void main(String[] args) {
		String test = "S E A R C H E X A M P L E";
		String[] keys = test.split(" ");
		int n = keys.length;
		
		BinarySearchST<String, Integer> st = new BinarySearchST<String,Integer>();
		for (int i = 0; i < n; i++)
			st.put(keys[i],i);
		System.out.println("size = " + st.size());
		System.out.println("min  = " + st.min());
		System.out.println("max  = " + st.max());
		System.out.println();
		
		System.out.println("Testing keys()");
		System.out.println("-----------------------");
		for (String string : st.keys())
			System.out.println(string+" "+st.get(string));
		System.out.println();
		
		System.out.println("Testing select");
		System.out.println("-----------------------");
		for (int i = 0;i<=st.size();i++) 
			System.out.println(i + " " +st.select(i));
		
		System.out.println("Key rank floor cell");
		System.out.println("-----------------------");
		for(char i ='A';i<='Z';i++){
			String s = i+"";
			System.out.printf("%2s %4d %4s %4s\n",s,st.rank(s),st.floor(s),st.ceiling(s));
		}
		System.out.println();
		for (int i = 0; i < st.size() / 2; i++) {
            st.deleteMin();
        }
        System.out.println("After deleting the smallest " + st.size() / 2 + " keys");
       System.out.println("------------------------");
       for(String s : st.keys())
    	   System.out.println(s+" "+st.get(s));
       System.out.println();
       while(!st.isEmpty())
    	   st.delete(st.select(st.size()/2));
       
       System.out.println("After deleting the remaining keys");
       System.out.println("--------------------------------");
       System.out.println("st is Empty? "+st.isEmpty());
       System.out.println();
       System.out.println("After adding back N keys");
       System.out.println("--------------------------------");
       for (int i = 0; i < n; i++) 
           st.put(keys[i], i); 
       for (String s : st.keys()) 
           System.out.println(s + " " + st.get(s)); 
       System.out.println();
	}
}
