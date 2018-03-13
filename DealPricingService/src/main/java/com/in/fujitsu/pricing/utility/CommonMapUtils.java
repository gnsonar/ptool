package com.in.fujitsu.pricing.utility;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommonMapUtils {

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueAsc(Map<K, V> unsortMap) {

		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(unsortMap.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;

	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDesc(Map<K, V> unsortMap) {

		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(unsortMap.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;

	}

	public static <T> T getFirstElement(final Iterable<T> elements) {
		if (elements == null)
			return null;

		return elements.iterator().next();
	}

	public static <T> T getLastElement(final Iterable<T> elements) {
		final Iterator<T> itr = elements.iterator();
		T lastElement = itr.next();

		while (itr.hasNext()) {
			lastElement = itr.next();
		}

		return lastElement;
	}

	public static Map<Long, BigDecimal> getFirstThreeEntries(int max, Map<Long, BigDecimal> source) {
		int count = 0;
		Map<Long, BigDecimal> target = new HashMap<Long, BigDecimal>();
		for (Map.Entry<Long, BigDecimal> entry : source.entrySet()) {
			if (count >= max) {
				return target;
			}
			target.put(entry.getKey(), entry.getValue());
			count++;
		}
		return target;
	}

}