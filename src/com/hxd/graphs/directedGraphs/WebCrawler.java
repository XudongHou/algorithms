package com.hxd.graphs.directedGraphs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hxd.base.Queue;
import com.hxd.introcs.SET;
import com.hxd.introcs.stdlib.In;

/**
 * 网络爬虫。编写一个程序WebCrawler.java，它使用广度优先搜索从给定的网页开始抓取网络图。不要明确地构建web图。
 * @author houxu_000 2017024
 * */


public class WebCrawler {
	public static void main(String[] args) {
		System.setProperty("sun.net.client.defaultConnectTimeout", "500");
		System.setProperty("sun.net.client.defaultReadTimeout", "1000");
		
		String s = "http://www.cs.princeton.edu";
		Queue<String> queue = new Queue<String>();
		queue.enqueue(s);
		
		SET<String> marked = new SET<String>();
		marked.add(s);
		
		while (!queue.isEmpty()) {
			String v = queue.dequeue();
			System.out.println(v);
			String input = null;
			try{
				In in = new In(v);
				input = in.readAll();
			}
			catch (IllegalArgumentException e) {
                System.out.println("[could not open " + v + "]");
                continue;
            }
			
			String regexep = "http://(\\w+\\.)+(\\v+)";
			Pattern pattern = Pattern.compile(regexep);
			
			Matcher matcher = pattern.matcher(input);
			while (matcher.find()) {
				String w = matcher.group();
				if (!marked.contains(w)) {
					queue.enqueue(w);
					marked.add(w);
				}
			}
		}
	}
}
