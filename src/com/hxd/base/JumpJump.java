package com.hxd.base;

/**
 * 跳一跳
 * @author houxu_000
 *
 */
public class JumpJump {
	public static void main(String[] args) {
		
		/**
		 * 修改这个即可
		 */
		int[] jumpPoint = {1,1,2,2,2,1,1,2,2,0};
		
		int score = 0;
		int last = 1;
		int add = 4;
		
		for(int i = 0; i< jumpPoint.length-1; i++) {
			if(jumpPoint[i] == 1) {
				score += 1;
				last = 1;
				add = 4;
			}else {
				if(last == 1) {
					score += 2;
					last = 2;
				}else {
					score = score + add;
					add = add + 2;
				}
			}
		}
		
		System.out.println(score);
	}
}
