package com.hxd.base;
/**
 * 使用位运算的四则运算 
 * @author houxu_000
 */

public class QuickCompute {
	
	public static int add(int a, int b){
//		return b ? add(a^b,(a&b)<<1):a;
		int ans;
		while(b != 0) {
			ans = a^b;	//无进位的加法
			b = (a&b) << 1;	//进位
			a = ans;
		}
		return a;
	}
	
	/**
	 * 取反操作
	 */
	private static int negetive(int a) {
		return add(~a, 1);
	}
	
	public static int subtract(int a, int b) {
		return add(a, negetive(b));
	}
	
	public static int pos_multiply(int a, int b) {
		int ans = 0;
		while(b != 0) {
			int c = b&1;		
			if(c != 0)	ans = add(ans, a);
			a = (a << 1);
			b = (b >> 1);
		}
		return ans;
	}
	/**
	 * 除法就是由乘法的过程逆推，依次减掉（如果x够减的）y^(2^31),y^(2^30),...y^8,y^4,y^2,y^1。
	 * 减掉相应数量的y就在结果加上相应的数量。  
	 */
	public static int pos_divide(int a, int b) {
		int ans = 0;
		for(int i = 31; i >= 0; i--) {
			//比较x是否大于y的(1<<i)次方，避免将x与(y<<i)比较，因为不确定y的(1<<i)次方是否溢出
			if((a>>i) >= b) {
				ans += (1<<i);
				a-=(b<<i);
			}
		}
		return ans;
	}
	
	public static int divideBy3(int a) {
		int ans = 0;
		while(a > 3) {
			ans = add(a >> 2, ans);
			a = add(a>>2, ans&3);
		}
		if(a == 3)
			ans = add(ans, 1);
		return ans;
	}
	
	public static void main(String[] args) {
		System.out.println("add(2,3): "+add(2,3));
		System.out.println("subtract(4, 10): "+subtract(4, 10));
		System.out.println("pos_divide(30, 6): "+pos_divide(30, 6));
		System.out.println("pos_multiply(4, 6): "+pos_multiply(4, 6));
		System.out.println("divideBy3(15): "+divideBy3(15));
	}
}
