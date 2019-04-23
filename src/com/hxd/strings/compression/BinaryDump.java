package com.hxd.strings.compression;


import com.hxd.introcs.stdlib.BinaryIn;
import com.hxd.introcs.stdlib.BinaryOut;
import com.hxd.introcs.stdlib.BinaryStdIn;

/**
 * 将标准输入中的比特按照0和1的形式打印出来.
 * 
 * @author houxu_000 20170314
 *
 */

public class BinaryDump {
	
	private BinaryDump() {}
	
	public static void dumpToBinary(int bitsPerLine ,BinaryIn in) {
		int count =0;
		while (!in.isEmpty()) {
			if (bitsPerLine <= 0)
				throw new IllegalArgumentException("bitsPerLine cannot less than 0");
			else if (count != 0 && count % bitsPerLine == 0)
				System.out.println();
			if (in.readBoolean())
				System.out.print(1);
			else
				System.out.print(0);
			count++;
		}
		System.out.print("\n\n");
		System.out.println(count + "bits");
	}
	
	public static void dumpToBinary(int bitsPerLine, BinaryIn in, String path) {
		BinaryOut binaryOut = new BinaryOut(path);
		int count = 0;
		while (!in.isEmpty()) {
			if (bitsPerLine <= 0)
				throw new IllegalArgumentException("bitsPerLine cannot less than 0");
			else if (count != 0 && count % bitsPerLine == 0)
				binaryOut.write("\n");
			if (in.readBoolean())
				binaryOut.write("1");
			else
				binaryOut.write("0");
			count++;
		}
		binaryOut.close();
	}
	
	public static void main(String[] args) {
		int bitsPerLine = 16;
		if (args.length == 1)
			bitsPerLine = Integer.parseInt(args[0]);
		int count;
		for (count = 0;!BinaryStdIn.isEmpty(); count++) {
			if (bitsPerLine == 0) {
				BinaryStdIn.readBoolean();
				continue;
			}
			else if (count != 0 && count % bitsPerLine ==0)
				System.out.println();
			if (BinaryStdIn.readBoolean())
				System.out.print(1);
			else
				System.err.print(0);
			System.out.println(count + "bits");
		}
;	}
}
