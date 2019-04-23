package com.hxd.strings.surstring;


/**
 * {@code KMP}类在文本字符串中查找模式字符串的第一次出现。 此实现使用一个版本的Knuth-Morris-Pratt子串搜索算法。
 * 在最坏情况下，版本花费时间作为与N + MR成比例的空间，其中N是文本串的长度，M是模式的长度，R是字母表大小。
 * @author houxu_000 20170307
 * */

public class KMP {
	private final int R;		// the radix
	private int[][] dfa;		// the KMP automoton
	
	private char[] pattern;		// either the character array for the pattern
	private String pat;			// or the pattern string
	
	/**
     * Preprocesses the pattern string.
     * 匹配失败时,如果模式字符串的某处可以和匹配失败处的正文相匹配,那么就不应该跳过所有匹配的字符,在KMP字符串查找算法中,不会退回
     * 文本指针i,而是使用一个数组dfa[][]来记录匹配失败是指针j应该会退多远.
     * 	对于每个字符c,在比较了 c和pat.charAt(j)之后,dfa[c][j]表示的应该是        和下个文本字符比较的模式字符的位置 		
     * 	
     * 	在查找中 dfa[txt.chartAt(i)][j]是在比较了txt.charAt(i)和pat.chart(j)之后应该和 
     * 	txt.charAt(i+1)比较的  模式字符位置
     * 
     *  在匹配时,会继续比较下一个字符,因此dfs[pat.charAt(j)][j] 总是j+1
     *  
     *  在不匹配时,不仅可以知道 txt.charAt(i)的字符 ,也可以知道正文中的前j-1个字符, 它们就是模式中的前 j-1 个字符.
     *  对于每个字符c, 可以将这个过程想象为首先  将模式字符串中一个副本   覆盖在这j个字符之上(模式中的前j-1个字符以及字符c)
     *  需要判断的是当这些字符就是txt.charAt(i-j+1..i)时 应该怎么办),  然后从左向右滑动这个副本直到所有重叠的字符
     *  都互相匹配(或没有相匹配的字符)时才停下来. 这将指明模式字符串中可能产生匹配的下一个位置.
     *  
     *  和txt.charAt(i+1) (dfa[txt.charAt(i)][j]) 比较的模式字符的索引正是重叠字符的数量.
     *  
     *  
     *  当i和j所指向的字符匹配失败时(从文本的i-j+1处开始检查模式的匹配情况), 模式可能批评日的下一个位置 应该
     *  从 i-dfa[txt.charAt(i)][j]处开始
     *  
     *  从该开始位置的开始位置的dfa[txt.charAt(i)][j]个字符和模式的前dfa[txt.charAt[i]][j]个字符应该相同
     *  将j设为dfa[txt.charAt(i)][j]并将i加1即可
     *  
     *  DFA模拟
     *  使用确定有限状态自动机(DFA).模式中的每个字符都能对应着一个状态,每个此类状态能够转换为字母表中的任意字符.
     *  这些转换只有一条是匹配转换(从j到j+1, 标签为pat.charAt(j)),其他的都是非匹配转换(指向左侧).所有状态都和字符
     *  相对应,每个状态都表示一个模式字符串的索引值.
     *  
     *  沿着转换dfa[txt.charAt(i)][j]前进并继续检查下一个字符(将i+1).对于一个匹配的转换,就向右移动一位,因为
     *  dfa[pat.charAt(j)][j]的值总是j+1;对于一个非匹配的转换,就在向左移动.自动机每次从左向右从文本中读取一个字符
     *  并移动到一个新的状态. 包含一个不会进行任何转换的停止状态M.自动机从状态0开始:如果自动机达到了状态M,那么就在文本中找到
     *  了和模式匹配的子字符串.每个模式字符串都对应着一个自动机(由保存了所有转换的dfa[][]的数组表示).
     *  
     *  起始状态为0.停留在0状态并扫描文本,直到找到一个和模式的首字母相同的字符.这时它移动到下一个状态并开始运行.在这个过程的
     *  最后,当它找到一个匹配时,它会不断的匹配模式中的字符和文本,自动机的状态会不断的前进直到状态M.每次匹配都会将DFA带向下
     *  一个状态(等价于增大模式字符串的指针j);每次匹配失败都会使DFA回到较早前的状态(等价于将模式字符串的指针j变为一个较小的值)
     *  索引j会在DFA的指导下在模式字符串中左右移动
     *  
     *  
     *  构造DFA
     *  当在pat.charAt(j)处匹配失败时,希望了解的是,如果回退了文本指针并在右移之后重新扫描已知的文本字符,DFA的状态会是什么?
     *  其实并不想回退,只是想将DFA重置到合适的状态,就好像已经回退到文本指针一样.
     *  
     *  这里的关键在于需要重新扫描的文本字符正是pat.charAt(1)到pat.charAt(j-1)之间,忽略了首字母的原因是模式需要 右移一位
     *  忽略了最后是因为匹配失败.
     *  
     *  DFA应该如何处理下一个字符?和回退时的处理方式相同,除非在pat.charAt(j)处匹配成功,这时DFA应该前进到状态j+1.
     *  例如
     *  对于ABABAC,要判断在j=5是匹配失败后DFA应该怎么做? 比如通过DFA可以将dfa[][3]复制到dfa[][5]并将C所对应的
     *  所对应的元素值设为6,因为pat.charAt(5)是C(匹配).因为在计算DFA的第j个状态时只需要知道DFA是如何处理前j-1个字符的,
     *  所以总能从尚不完整的DFA中得到所需的信息
     *  
     *  在处理dfa[][]的第j列时维护重启位置x很容易.因为x<j,所以可以由已经构造DFA部分来完成这个任务--x的下一个值
     *  dfa[pat.charAt(j)][x],继续上一段中的例子,将X的值更新为dfa['C'][3]=0(但我们不会使用这个值,因为DFA的构造
     *  已经完成了)
     *  
     *  对于每个j,它将会:
     *  1.将dfa[][x]复制到dfa[][j](对ui匹配失败的情况);
     *  2.将dfa[pat.chartAt(j)][j]设为j+1;
     *  3.更新x
     */
	
	/**
     * @param pattern the pattern string
     * @param R the alphabet size
     */
	public KMP(String pat) {
		this.R = 256;
        this.pat = pat;

        // build DFA from pattern
        int m = pat.length();
        dfa = new int[R][m]; 
        dfa[pat.charAt(0)][0] = 1; 
        for (int x = 0, j = 1; j < m; j++) {
            for (int c = 0; c < R; c++) 
                dfa[c][j] = dfa[c][x];     // Copy mismatch cases. 
            dfa[pat.charAt(j)][j] = j+1;   // Set match case. 
            x = dfa[pat.charAt(j)][x];     // Update restart state. 
        }
        
	}
	
	/**
     * Preprocesses the pattern string.
     *
     * @param pattern the pattern string
     * @param R the alphabet size
     */
	public KMP(char[] pattern, int R) {
		this.R = R;
        this.pattern = new char[pattern.length];
        for (int j = 0; j < pattern.length; j++)
            this.pattern[j] = pattern[j];

        // build DFA from pattern
        int m = pattern.length;
        dfa = new int[R][m]; 
        dfa[pattern[0]][0] = 1; 
        for (int x = 0, j = 1; j < m; j++) {
            for (int c = 0; c < R; c++) 
                dfa[c][j] = dfa[c][x];     // Copy mismatch cases. 
            dfa[pattern[j]][j] = j+1;      // Set match case. 
            x = dfa[pattern[j]][x];        // Update restart state. 
        } 
	}
	
	/**
     * Returns the index of the first occurrrence of the pattern string
     * in the text string.
     *
     * @param  text the text string
     * @return the index of the first occurrence of the pattern string
     *         in the text string; N if no such match
     */
	public int search(String txt) {
		int m = pat.length();
		int n = txt.length();
		int i, j;
		for (i = 0, j = 0; i < n && j < m; i++) 
			j = dfa[txt.charAt(i)][j];
		if (j == m)
			return i - m;
		return n;
	}
	
	/**
     * Returns the index of the first occurrrence of the pattern string
     * in the text string.
     *
     * @param  text the text string
     * @return the index of the first occurrence of the pattern string
     *         in the text string; N if no such match
     */
	public int search(char[] text) {
		int m = pattern.length;
		int n = text.length;
		int i, j;
		for (i = 0, j = 0; i < n && j < m; i++)
			j = dfa[text[i]][j];
		if (j == m) return i - m;
		return n;
	}
	
	
	public static void main(String[] args) {
		String pat = "abracadabra";
		String txt = "abacadabrabracabracadabrabrabracad";
		
		
		KMP kmp = new KMP(pat);
		int offset = kmp.search(txt);
		
		System.out.println("text:    "+txt);
		System.out.print("pattern: ");
		for (int i = 0; i < offset; i ++)
			System.out.print(" ");
		System.out.println(pat);
		
		System.out.println();
		
		char[] pattern = pat.toCharArray();
		char[] text = txt.toCharArray();
		
		kmp = new KMP(pattern, 256);
		offset = kmp.search(text);
		
		System.out.println("text:    "+txt);
		System.out.print("pattern: ");
		for (int i = 0; i < offset; i ++)
			System.out.print(" ");
		System.out.println(pat);
		
		System.out.println();
	}

}
