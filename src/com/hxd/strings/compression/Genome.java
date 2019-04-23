package com.hxd.strings.compression;

import java.nio.file.FileSystemNotFoundException;

import com.hxd.introcs.stdlib.BinaryIn;
import com.hxd.introcs.stdlib.BinaryOut;
import com.hxd.introcs.stdlib.BinaryStdIn;
import com.hxd.introcs.stdlib.BinaryStdOut;
import com.hxd.strings.Alphabet;

/**
 * 基因数据压缩;
 * {@code Genome}基因的一个简单性质是,它由4种不同的字符组成.这些字符可以用两个比特编码.尽管输入流是由字符组成,但是仍然可以使用
 * {@code BinaryStdIn}和{@code BinaryIn}来读取这些数据以和标准的数据压缩模型保持一致(从比特流到比特流)压缩后的文件是记
 * 录了被编码的字符数量,这样即使最后一位并没有和字节对齐,解码也能够顺利进行.因为它能够将一个8位的字符转换为一个双位编码,并附加32位用
 * 于记录长度,压缩会随着压缩字符的增多越来越接近25%.
 * @author houxu_000 20170314
 *
 */

public class Genome {
	
	private Genome() {}
	
	/**
	 * 
	 */
	public static void compress() {
		Alphabet DNA = Alphabet.DNA;
		String s = BinaryStdIn.readString();
		int n = s.length();
		BinaryStdOut.write(n);
		
		for (int i = 0; i < n; i++) {
			int d = DNA.toIndex(s.charAt(i));
			BinaryStdOut.write(d, 2);
		}
		
		BinaryStdOut.close();
	}
	
	/**
	 * 
	 * @param binaryIn
	 */
	public static void compress(BinaryIn binaryIn,String path) {
		Alphabet DNA = Alphabet.DNA;
		BinaryOut binaryOut = new BinaryOut(path);
		while (!binaryIn.isEmpty()) {
			String s = binaryIn.readString();
			int n = s.length();
			binaryOut.write(n);
			for (int i = 0; i < n; i++) {
				int  d = DNA.toIndex(s.charAt(i));
				binaryOut.write(d, 2);
			}
		}
		binaryOut.close();
	}
	
	/**
	 * 
	 */
	public static void expand() {
		Alphabet DNA = Alphabet.DNA;
		int n = BinaryStdIn.readInt();
		// Read two bits; write char. 
	    for (int i = 0; i < n; i++) {
	    char c = BinaryStdIn.readChar(2);
	    BinaryStdOut.write(DNA.toChar(c), 8);
	    }
	    BinaryStdOut.close();
	}
	
//	/**
//	 * 
//	 * @param binaryIn
//	 */
//	public static void expand(BinaryIn binaryIn) {
//		Alphabet DNA = Alphabet.DNA;
//		BinaryOut binaryOut = new BinaryOut();
//		while(!binaryIn.isEmpty()){
//			int n = binaryIn.readInt();
//			for (int i = 0; i < n ; i++) {
//			char c = BinaryStdIn.readChar(2);
//			binaryOut.write(DNA.toChar(c), 8);
//			}
//		}
//		binaryOut.close();
//	}
	
	public static void main(String[] args) {
		try {
			Genome.compress(new BinaryIn(".\\algs4-data\\genomeVirus.txt"), ".\\algs4-data\\genomeVirusCompress.txt");
			BinaryDump.dumpToBinary(64,new BinaryIn(".\\algs4-data\\genomeVirusCompress.txt"));
			
			BinaryDump.dumpToBinary(64, new BinaryIn(".\\algs4-data\\genomeVirusCompress.txt"), ".\\algs4-data\\genomeVirusToExpend.txt");
		} catch (FileSystemNotFoundException e) {
			// TODO: handle exception
		}
	}
}
