package com.hxd.introcs;

import com.hxd.introcs.stdlib.In;
import com.hxd.introcs.stdlib.StdIn;
import com.hxd.introcs.stdlib.StdOut;

/******************************************************************************
 *  Compilation:  javac Lookup.java
 *  Execution:    java Lookup file.csv keyField valField
 *  Dependencies: ST.java In.java StdIn.java
 *  Data files:   http://www.cs.princeton.edu/introcs/44st/amino.csv
 *                http://www.cs.princeton.edu/introcs/44st/ip.csv
 *                http://www.cs.princeton.edu/introcs/44st/DJIA.csv
 *                http://www.cs.princeton.edu/introcs/44st/morse.csv
 *                http://www.cs.princeton.edu/introcs/44st/elements.csv
 *                http://www.cs.princeton.edu/introcs/44st/mktsymbols.csv
 *                http://www.cs.princeton.edu/introcs/44st/toplevel-domain.csv
 *  
 *  Reads in a set of key-value pairs from a two-column CSV file
 *  specified on the command line; then, reads in keys from standard
 *  input and prints out corresponding values.
 * 
 *  % java Lookup amino.csv 0 3      % java Lookup ip.csv 0 1 
 *  TTA                              www.google.com 
 *  Leucine                          216.239.41.99 
 *  ABC                               
 *  Not found                        % java Lookup ip.csv 1 0 
 *  TCT                              216.239.41.99 
 *  Serine                           www.google.com 
 *                                 
 *  % java Lookup amino.csv 3 0      java Lookup DJIA.csv 0 1 
 *  Glycine                          29-Oct-29 
 *  GGG                              252.38 
 *                                   20-Oct-87 
 *                                   1738.74
 *
 ******************************************************************************/

public class Lookup {
    public static void main(String[] args) {
        In in = new In(args[0]);
        int keyField = Integer.parseInt(args[1]);
        int valField = Integer.parseInt(args[2]);
        String[] database = in.readAllLines();
        ST<String, String> st = new ST<String, String>();
        for (int i = 0; i < database.length; i++) {
            String[] tokens = database[i].split(",");
            String key = tokens[keyField];
            String val = tokens[valField];
            st.put(key, val);
        }

        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (st.contains(s)) StdOut.println(st.get(s));
            else                StdOut.println("Not found");
        }
    }
}