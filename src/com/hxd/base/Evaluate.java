package com.hxd.base;

import java.util.Scanner;

import com.hxd.introcs.Stack;

/**
 * 候旭东 20161212 算数表达式
 * 用两个栈,一个用于保存运算符,每一个用于保存操作数
 * 表达式由括号,运算符和操作数(数字)组成,根据以下4种情况从左到右逐个将这些实体送入栈处理
 * 		将操作数压入操作数栈
 * 		将运算符压入运算符栈
 * 		忽略左括号
 * 		在遇到右括号时,弹出一个运算符,弹出所需数量的操作数,并将运算结果压入操作数栈
 * 在处理完最后一个右括号之后,操作数栈上只会有一个值,它就是表达式的值.
 * 理解: 每当算法遇到一个被括号包围并且并由一个运算符和两个操作数所组成的子表达式是,它都将
 * 元算符和操作数的计算结果压入操作数栈.这样的结果,就好像在输入中用这个值代替了该子表达式,
 * 因此用这个值代替子表达式得到的结果和原表达式相同.
 * */

public class Evaluate {
	public static void main(String[] args) throws InterruptedException {
		Stack<String> ops = new Stack<String>();
		Stack<Double> vals = new Stack<Double>();
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
		  //读取字符,如果是运算符则压入栈
			String s = scanner.next();
			if(s.equals("("))	;
			else if(s.equals("+")) ops.push(s);
			else if(s.equals("-")) ops.push(s);
			else if(s.equals("*")) ops.push(s);
			else if(s.equals("/")) ops.push(s);
			else if(s.equals("sqrt")) ops.push(s);
			else if(s.equals(")")){
				String op = ops.pop();
				double v = vals.pop();
				if(op.equals("+")) v=vals.pop()+v;
				else if(op.equals("-")) v=vals.pop()-v;
				else if(op.equals("*")) v=vals.pop()*v;
				else if(op.equals("/")) v=vals.pop()/v;
				else if(op.equals("sqrt")) v=Math.sqrt(v);
				vals.push(v);
			}
			else if(s.equals("=")) System.out.println(vals.pop());
			else if(s.equals("Q")) break;
			else vals.push(Double.parseDouble(s));
		}
		scanner.close();
		
	}
}
