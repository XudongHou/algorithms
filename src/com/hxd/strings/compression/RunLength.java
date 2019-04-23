package com.hxd.strings.compression;

import com.hxd.introcs.stdlib.BinaryIn;
import com.hxd.introcs.stdlib.BinaryStdIn;
import com.hxd.introcs.stdlib.BinaryStdOut;

/**
 * 游程编码,比特流中最简单的冗余形式就是一长串重复的比特.;
 * 我们需要解决以下问题:
 * 应该使用多少比特来记录游程的长度?
 * 当某个游程的长度超过了能够记录的最大长度是怎么办?
 * 当游程的长度所需的比特数小于记录长度的比特数时怎么办?
 * 我们感兴趣的主要是含有的短游程相对较少的长比特流,因此这些问题的回答是:
 * 游程长度应该在0到255之间,使用8位编码;
 * 在需要的情况下使用0的游程来保证所有游程的长度小于256;
 * 我们也会将较短的游程编码,虽然这样做有可能是输出变得更长.
 * 不适用于含有大量短游程的输入--只有在游程的长度大于将它们用二进制表示的所需的长度是才能节省空间
 * <p>
 * {@code Run Length}类提供了使用具有8位游程长度的游程长度编码来压缩和扩展二进制输入的静态方法。
 * @author houxu_000 20170314
 *
 */

public class RunLength {
	private static final int R = 256;
	private static final int LG_R = 8;
	
	private RunLength() {}
	
	/**
     * Reads a sequence of bits from standard input (that are encoded
     * using run-length encoding with 8-bit run lengths); decodes them;
     * and writes the results to standard output.
     */
	public static void expand() {
		boolean b = false;
		while (!BinaryStdIn.isEmpty()) {
			int run = BinaryStdIn.readInt(LG_R);
			for (int i = 0; i < run; i++)
				BinaryStdOut.write(b);
			b = !b;
		}
		BinaryStdOut.close();
	}
	
	public static void expand(BinaryIn binaryIn) {
		boolean b = false;
		while (!binaryIn.isEmpty()) {
			int run = binaryIn.readInt(LG_R);
			for (int i = 0; i < run; i++)
				BinaryStdOut.write(b);
			b = !b;
		}
		BinaryStdOut.close();
	}
	
	/**
	 * 读取一个比特;
	 * 如果它和上一个比特不同,写入当前的计数值并将计数器归0;
	 * 如果它和上一个比特相同且计数器达到了最大值,则写入计数值,再写入一个0计数值,然后将计数器归0;
	 * 增加计数器的值;
	 * 
     * Reads a sequence of bits from standard input; compresses
     * them using run-length coding with 8-bit run lengths; and writes the
     * results to standard output.
     */
	public static void compress() {
		char run = 0;
		boolean old = false;
		while (!BinaryStdIn.isEmpty()) {
			boolean b = BinaryStdIn.readBoolean();
			if (b != old) {
				BinaryStdOut.write(run, LG_R);
				run = 1;
				old = !old;
			}
			else {
				if (run == R - 1) {
					BinaryStdOut.write(run, LG_R);
					run = 0;
					BinaryStdOut.write(run, LG_R);
				}
				run++;
			}
		}
		BinaryStdOut.write(run, LG_R);
		BinaryStdOut.close();
	}
	
	
	public static void compress(BinaryIn binaryIn) {
		char run = 0;
		boolean old = false;
		while (!binaryIn.isEmpty()) {
			boolean b = BinaryStdIn.readBoolean();
			if (b != old) {
				BinaryStdOut.write(run, LG_R);
				run = 1;
				old = !old;
			}
			else {
				if (run == R - 1) {
					BinaryStdOut.write(run, LG_R);
					run = 0;
					BinaryStdOut.write(run, LG_R);
				}
				run++;
			}
		}
		BinaryStdOut.write(run, LG_R);
		BinaryStdOut.close();
	}
	 /**
     * Sample client that calls {@code compress()} if the command-line
     * argument is "-" an {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
