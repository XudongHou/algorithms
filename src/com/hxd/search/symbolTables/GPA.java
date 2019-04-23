package com.hxd.search.symbolTables;

/**
 * @author 候旭东
 * 编写一段程序,创建一张符号表并建立字母成绩和数值分组的对应关系,
 * */

public class GPA {
	public static void main(String[] args) {
	// create symbol table of grades and values
		BinarySearchST<String, Double> grades = new BinarySearchST<String, Double>();
		grades.put("A",  4.00);
        grades.put("B",  3.00);
        grades.put("C",  2.00);
        grades.put("D",  1.00);
        grades.put("F",  0.00);
        grades.put("A+", 4.33);
        grades.put("B+", 3.33);
        grades.put("C+", 2.33);
        grades.put("A-", 3.67);
        grades.put("B-", 2.67);
        
     // read grades from standard input and compute gpa
        int n = 0;
        double total = 0.0;
        String[] strings = new String[]{"A-","B+","A+","C"};
        for (String grade : strings) {
			double value = grades.get(grade);
			System.out.print(grade+" : "+value+" ");
			n++;
			total+=value;
        }
        double gpa = total / n;
        System.out.println();
        System.out.println("GPA = " + gpa);
	}
}
