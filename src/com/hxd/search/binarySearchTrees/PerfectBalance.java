package com.hxd.search.binarySearchTrees;

import java.util.Arrays;

/**
 * 完美平衡.用一组键构造一颗和二分查找等价的二叉查找树.在这棵树种查找任意键所产生的比较序列和在这组键
 * 中使用二分查找所产生的比较序列完全相同.
 * 注: 这个程序有问题
 * */
@SuppressWarnings("rawtypes")
public class PerfectBalance {
    // precondition: a[] has no duplicates
	private static void perfect(BST bst, String[] a) {
        Arrays.sort(a);
        System.out.println(Arrays.toString(a));
        perfect(bst, a, 0, a.length - 1);
        System.out.println();
    }

    // precondition: a[lo..hi] is sorted
    @SuppressWarnings("unchecked")
	private static void perfect(BST bst, String[] a, int lo, int hi) {
        if (hi < lo) return;
        int mid = lo + (hi - lo) / 2;
        bst.put(a[mid], mid);
        System.out.print(a[mid] + " ");
        perfect(bst, a, lo, mid-1);
        perfect(bst, a, mid+1, hi);
    }
    
	
	public static void main(String[] args) {
		BST<String, Integer> st = new BST<String,Integer>();
		String[] strings = new String[]{"S","E","A","R","C","H","E","X","A","M","P","L","E"};
		perfect(st, strings);
		for (String s: st.keys())
			System.out.println(s+" "+st.get(s));
		System.out.println("size "+st.size());
		System.out.print("print key ");st.printLeveOrder();
		System.out.println("min is " + st.min());
		System.out.println("max is " + st.max());
	}
}