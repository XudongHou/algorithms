package com.hxd.strings.compression;


import java.util.Arrays;

import com.hxd.introcs.stdlib.BinaryIn;

/**
 * {@code HexDump}类提供了一个以十六进制显示二进制文件内容的客户端。将数据组织成8位的字节并将它打印为
 * 各表示4为的两个十六进制数
 * 转储(dump)这个词从计算机的早期一直沿用下来,表示的是比特流的一种可供人类阅读的形式.
 * @author houxu_000 20170310
 * */

public class HexDump {
	
	private HexDump() {}
	
	/**
	 * 
	 * @param binaryIn
	 * @return
	 */
	public static char[] dumpTochars(BinaryIn binaryIn){
		StringBuilder st = new StringBuilder();
		while (!binaryIn.isEmpty()) {
			char c = binaryIn.readChar();
			st.append(c);
		}
		return st.toString().toCharArray();
	}
	
	/**
     * Reads in a sequence of bytes from standard input and writes
     * them to standard output using hexademical notation, k hex digits
     * per line, where k is given as a command-line integer (defaults
     * to 16 if no integer is specified); also writes the number
     * of bits.
     *
     * @param args the command-line arguments
     */
	public static void main(String[] args) {
		int bytesPerline = 16;
		int i;
		BinaryIn binaryIn = new BinaryIn(".\\algs4-data\\abra.txt");
		for (i = 0; !binaryIn.isEmpty(); i++) {
			if (bytesPerline == 0) {
				binaryIn.readChar();
				continue;
			}
			if (i == 0) System.out.printf("");
			else if (i % bytesPerline == 0) System.out.printf("\n", i);
			else System.out.print(" ");
			char c = binaryIn.readChar();
			System.out.print(c);
			System.out.printf("%02x", c & 0xff);
		}
		if (bytesPerline != 0)	System.out.println();
		System.out.println((i*8)+ "bits");
		System.out.println(Arrays.toString(dumpTochars(new BinaryIn(".\\algs4-data\\abra.txt"))));
	}
}
