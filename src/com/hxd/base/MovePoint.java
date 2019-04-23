package com.hxd.base;

/**
 * 一个小小的OJ
 * @author houxu_000
 *
 */
public class MovePoint {

	public static void main(String[] args) {
		
		/**
		 * 需要自定义求的个数 n
		 */
		Ball b1 = new Ball(1,4);
		Ball b2 = new Ball(1,6);
		Ball b3 = new Ball(1,8);
		/**
		 * upperBound相当于第一行要输入的L
		 */
		int upperBound = 10;//
		int lowerBound = 0;
		/**
		 * 第一行要输入的秒数t
		 */
		int t = 5;
		Ball[][] L = new Ball[2][upperBound + 1];

		L[0][b1.pos] = b1;
		L[0][b2.pos] = b2;
		L[0][b3.pos] = b3;
		
		//计算
		for(int ti = 1; ti <=t; ti++) {
			//正方向运动
			for(int i = upperBound; i >= lowerBound; i--) {
				if(L[0][i] != null ) {
					Ball tmp = L[0][i];
					tmp.pos ++;
					//遇到右边界进行反向运动
					if(tmp.pos > upperBound) {
						tmp.pos --;
						tmp.dir=0;
						L[1][upperBound] = tmp;
						L[0][i] = null;
					}else {
						L[0][i+1] = tmp;//前进一位
						L[0][i] = null;
					}
				}
			}//正向运动结束
			
			//反向运动
			for(int i = lowerBound ; i <= upperBound; i++) {
				if(L[1][i] != null ) {
					Ball tmp = L[1][i];
					tmp.pos --;
					//遇到左边界进行正向运动
					if(tmp.pos < lowerBound) {
						tmp.pos =lowerBound + 1;
						tmp.dir=1;
						L[0][lowerBound + 1] = tmp;
						L[1][i] = null;
					}else {
						L[1][i-1] = tmp;//前进一位
						L[1][i] = null;
					}
				}
			}
			
			//碰撞检测
			for(int i = lowerBound; i <= upperBound; i++) {
				if(L[0][i] != null && L[1][i] !=null) {//发成碰撞两球交换运动方向
					Ball tmp = L[0][i];
					tmp.dir=0;
					L[0][i] = L[1][i];
					L[1][i] = tmp;
					L[0][i].dir = 1;
				}
			}
		}
		
		//输出结果
		for(int i = lowerBound; i <= upperBound; i++) {
			if(L[0][i]!=null)				System.out.println(L[0][i].toString());
			if(L[1][i]!=null)				System.out.println(L[1][i].toString());
		}
	}
}
class Ball{
	int dir;//运动方向 0 为反,1为正
	int pos;//运动位置
	public Ball(int dir, int pos) {
		this.dir = dir;
		this.pos = pos;
	}
	public int getDir() {
		return dir;
	}
	public void setDir(int dir) {
		this.dir = dir;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	@Override
	public String toString() {
		return "dir: " + dir + " pos: " + pos;
	}
}

