package com.hxd.sort.base;

/**
 * 候旭东 20161219 插入排序的哨兵.再插入排序的实现中先找出最小的元素并将其置于数组的最左边,
 * 这样就能去掉内循环的判断条件j>0
 * 能够省略判断条件的元素通常被称为哨兵
 * */
@SuppressWarnings("rawtypes")
public class InsertionX extends Example {
    private InsertionX() { }

    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    
	public static void sort(Comparable[] a) {
        int n = a.length;

        // 先找出最小的元素并将其置于数组的最左边
        int exchanges = 0;
        for (int i = n-1; i > 0; i--) {
            if (less(a[i], a[i-1])) {
                exch(a, i, i-1);
                exchanges++;
            }
        }
        if (exchanges == 0) return;

        // insertion sort with half-exchanges
        for (int i = 2; i < n; i++) {
            Comparable v = a[i];
            int j = i;
            while (less(v, a[j-1])) {
                a[j] = a[j-1];
                j--;
            }
            a[j] = v;
        }

        assert isSorted(a);
    }
	
	public static void main(String[] args) {
		String[] a = new String[]{"bed","bug","dad","yes","zoo","now","for"
				,"tip","ilk","dim","tag","jot","sob","nob","sky","hut","men","egg","few","jay","owl",
				"joy","rap","gig","wee","was","wad","fee","tap","tar","dug","jam","all","bad","yet"};
		sort(a);
		assert isSorted(a);
		show(a);
	}
}
