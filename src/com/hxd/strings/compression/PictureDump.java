package com.hxd.strings.compression;

import java.awt.Color;

import com.hxd.introcs.stdlib.BinaryIn;
import com.hxd.introcs.stdlib.Picture;

/**
 * {@code PictureDump}类提供了一个客户端，用于将二进制文件的内容显示为黑白图片。
 * @author houxu_000 20170310
 */
public class PictureDump {
	
	private PictureDump() {}
	/**
	 * 
	 * @param binaryIn
	 */
	public static void dumpToPic(BinaryIn binaryIn) {
		int width = 16;
		int height = 6;
		Picture picture = new Picture(width, height);
		for (int row = 0; row < height; row ++) {
			for (int col = 0; col < width; col++) {
				if (!binaryIn.isEmpty()) {
					boolean bit = binaryIn.readBoolean();
					if (bit)	picture.set(col, row, Color.BLACK);
					else		picture.set(col, row, Color.WHITE);
				}
				else
					picture.set(col, row, Color.RED);
			}
		}
		picture.show();
	}
	
	public static void main(String[] args) {
		dumpToPic(new BinaryIn(".\\algs4-data\\abra.txt"));
	}
}
