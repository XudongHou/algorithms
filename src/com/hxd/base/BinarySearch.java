package com.hxd.base;

import java.util.Arrays;

/**
 * 候旭东 20161206 折半查找,用于经过排序的数列
 * 二分查找
 * 如果存在于数组(经过排序的)中则返回它的索引,否则返回-1.算法使用两个变量lo和hi,并保
 * 证如果键在数组中则它一定在a[lo,hi]中,然后方法进入一个循环,不断将数组的中间键(索引
 * 为mid)和被查找的键比较,如果被查找的键等于mid,返回mid;否则算法将查找范围缩小一半,
 * 算法找到被查询的键或是查找范围为空时该过程结束.二分查找之所以快是因为它只需检查很少几个
 * 条目(相对于数组的大小)就能够找到目标元素(或者确认目标元素不存在)
 * <p>
 * 应用场景白名单过滤
 */

/**
 * 方法的性质 方法的参数按值传递,可以被重载,只能返回一个值,但可以包含多个返回语句,方法可
 * 以产生副作用
 * */
public class BinarySearch {
    public static int rank(int key, int[] a) {
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (key < a[mid]) hi = mid - 1;
            else if (key > a[mid]) lo = mid + 1;
            else return mid;
        }
        return -1;
    }

    /**
     * 编写递归代码时最重要的有三点
     * 1.递归总有一个最简单的情况--方法的第一条语句总是一个包含return的条件语句
     * 2.递归调用总是去尝试解决一个规模更小的子问题,这样递归才能收敛到最简单的情况.
     * 3.递归调用的父问题和尝试解决的子问题之间不应该有交集
     * */
    public static int rank(int key, int[] a, int lo, int hi) {
        if (lo > hi) return -1;
        int mid = lo + (hi - lo) / 2;
        if (key < a[mid]) return rank(key, a, lo, mid - 1);
        if (key > a[mid]) return rank(key, a, mid + 1, hi);
        else return mid;
    }

    public static void main(String[] args) {
        int a[] = new int[]{1, 3, 7, 4, 88};
        Arrays.sort(a);
        System.out.println(Arrays.toString(a));
        System.out.println(rank(4, a));
        System.out.println(rank(4, a, 0, a.length - 1));
    }

}
