package com.hxd.base;

/**
 * 候旭东20161203 欧几里得算法
 * 自然语言描述,计算两个非负整数p和q的最大公约数:若q=0,则最大公约数为p.否则p除以q得到余数r
 * ,p和q的最大公约数即为q和r的最大公约数
 * */

public class Euclid {
	public static  int  gcd(int p,int q){
		if(q==0)return p;
		int r = p%q;
		return gcd(q, r);
	}
}
