package com.hxd.base;

/**
 * 候旭东 20161205 经典静态例子实现
 * */

public class SimpleMath {
	
	//绝对值
	public static int abs(int x){
		if(x<0)return -x;
		else return x;
	}
	public static double abs(double x){
		if(x<0.0)return -x;
		else return x;
	}
	
	//是否为素数
	public static boolean isPrime(int N){
		if(N<2)return false;
		for (int i = 2; i*i <=N ; i++)
			if(N%i==0)return false;
		return true;
	}
	
	//计算平方根(牛顿迭代法)
	//public static final double NaN = 0.0d / 0.0;
	public static double sqrt(double c){
		if(c<0)
			return Double.NaN;
		double err=1e-15;
		double t=c;
		while(Math.abs(t-c/t)>err*t)
			t=(c/t+t)/2.0;
		return t;
	}
	
	//计算直角三角形的斜边
	public static double hypotenuse(double a,double b){
		return Math.sqrt(a*a+b*b);
	}
	
	//计算调和级别
	public static double H(int N){
		double sum=0.0;
		for (int i = 1; i <= N; i++)
			sum+=1.0/i;
		return sum;
	}
	
	public static void main(String[] args) {
		System.out.println(abs(-6.6));
		System.out.println(isPrime(13));
		System.out.println(sqrt(16));
		System.out.println(hypotenuse(3.0,4.0));
		System.out.println(H(3));
	}
}
