package com.hxd.context.anagram;
/******************************************************************************
 *  Compilation:  AnagramInQuick3X.java
 *  Execution:    java AnagramInQuick3X str_a, str_b
 *
 *  A anagram algorithm to math sub-anagram string in English and Chinese
 *
 ******************************************************************************/

import com.hxd.sort.quick.Quick3Way;

import java.util.Arrays;

/**
 * The {@code AnagramInQuick3X} class represents a  algrithms to solve the sub-anagram
 * match problem with Quick3XSort
 * <p>
 * This implementation uses the <em>Quick-3-way</em> algorithm to ensure entropy-optimization
 * <p>
 * Dijkstra’s solution to this problem leads to the remarkably simple partition code
 * shown on the next page. It is based on a single left-to-right pass through the array that
 * maintains a pointer lt such that a[lo..lt-1] is less than v, a pointer gt such that
 * a[gt+1, hi] is greater than v, and a pointer i such that a[lt..i-1] are equal to v and
 * a[i..gt] are not yet examined. Starting with i equal to lo, we process a[i] using the
 * 3-way comparison given us by the Comparable interface (instead of using less()) to
 * directly handle the three possible cases:
 * <p>
 *   a[i] less than v: exchange a[lt] with a[i] and increment both lt and i
 * <p>
 *   a[i] greater than v: exchange a[i] with a[gt] and decrement gt
 * <p>
 *   a[i] equal to v: increment i
 * <p>
 *
 * @author 候旭东
 */
public class AnagramInQuick3X {

    /**
     * 将String按内部包含的元素建立字符串数组
     * @param str
     * @return {@code String[]} 长度为str.length()的String[]
     */
    private static String[] string2StringArray(String str){
        String[] str_array = new String[str.length()];
        for(int j = 0; j < str.length(); j++)
            str_array[j] = str.substring(j,j+1);
        return str_array;
    }

    /**
     *
     * @param str_a string_A is the string to be
     * @param str_b
     * @return
     */
    public static boolean isAnagram(String str_a, String str_b){
        int b_length = str_b.length();
        String[] str_b_array = string2StringArray(str_b);
        Quick3Way.sort(str_b_array);    // sort str_b
        for(int i = 0; i <= str_a.length() - b_length; i++){
            String[] sub_a_array = string2StringArray(str_a.substring(i, i + b_length));
            Quick3Way.sort(sub_a_array);    //sort str_a
            if (Arrays.equals(sub_a_array, str_b_array))
                return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String str_a = args[0];
        String str_b = args[1];
//        String str_a = "helloworld你好, 世界";
//        String str_b = "olworld你好";
        System.out.println(isAnagram(str_a, str_b));
    }
}
