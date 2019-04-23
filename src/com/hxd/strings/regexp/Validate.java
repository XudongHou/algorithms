package com.hxd.strings.regexp;

import com.hxd.introcs.stdlib.StdOut;

/******************************************************************************
 *  Compilation:  javac Validate.java
 *  Execution:    java Validate pattern text
 *  Dependencies: StdOut.java
 *  
 *  Reads in a regular expression and a text input string from the
 *  command line and prints true or false depending on whether
 *  the pattern matches the text.
 *
 *  % java Validate "..oo..oo." bloodroot
 *  true
 *
 *  % java Validate "..oo..oo." nincompoophood
 *  false
 *
 *  % java Validate "[^aeiou]{6}" rhythm
 *  true
 *
 *  % java Validate "[^aeiou]{6}" rhythms
 *  false
 *
 ******************************************************************************/

/**
 * 
 * @author houxu_000 20170404
 *
 */

public class Validate {
	public static void main(String[] args) { 
        String regexp = args[0];
        String text   = args[1];
        StdOut.println(text.matches(regexp));
    }
}
