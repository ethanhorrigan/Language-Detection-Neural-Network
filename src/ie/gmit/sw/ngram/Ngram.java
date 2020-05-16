package ie.gmit.sw.ngram;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Ngram splits a string in ngrams of size n.
 * 
 * In the beginning, I was creating ngrams with an offset of 1. But, offsetting
 * by the size of the ngram produced better results.
 * 
 * Example for ngram of size 2: Hello, World! [He], [ll], [o,], [ w], [or], [ld]
 * 
 * @author Ethan Horrigan
 *
 */
public class Ngram {

	private Ngram() {
	}

	/*
	 * public static List<String> ngrams(int n, String str) { List<String> ngrams =
	 * new ArrayList<String>(); for (int i = 0; i < str.length() - n + 1; i++) {
	 * ngrams.add(str.substring(i, i + n));
	 * 
	 * } return ngrams; }
	 */

	public static Collection<String> ngrams(int n, String s) {
		AtomicInteger splitCounter = new AtomicInteger(0);
		return s.chars().mapToObj(c -> String.valueOf((char) c))
				.collect(Collectors.groupingBy(stringChar -> splitCounter.getAndIncrement() / n, Collectors.joining()))
				.values();
	}

}
