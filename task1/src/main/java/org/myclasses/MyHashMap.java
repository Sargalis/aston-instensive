package org.myclasses;

import java.util.*;

public class MyHashMap<K, V> implements Map<K, V> {
	private static class Entry<K,V> implements Map.Entry<K,V> {
		K key;
		V value;

		Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() { return key; }

		@Override
		public V getValue() { return value; }

		@Override
		public V setValue(V value) {
			V old = this.value;
			this.value = value;
			return old;
		}
	}
	private LinkedList<Entry<K, V>>[] buckets;
	private int size = 0;
	private static final int DEFAULT_CAPACITY = 16;

	public MyHashMap() {
		buckets = new LinkedList[DEFAULT_CAPACITY];
	}

	private int hash(Object key) {
		if (key == null) return 0;
		return Math.abs(key.hashCode()) % buckets.length;
	}

	@Override
	public V put(K key, V value) {
		int index = hash(key);
		if (buckets[index] == null) {
			buckets[index] = new LinkedList<Entry<K, V>>();
		}
		Iterator<Entry<K, V>> iter = buckets[index].iterator();
		while (iter.hasNext()) {
			Entry<K, V> entry = iter.next();
			if (Objects.equals(key, entry.key)) {
				V old_value = entry.value;
				entry.value = value;
				return old_value;
			}
		}

		buckets[index].add(new Entry<>(key, value));
		size++;
		return null;
	}

	@Override
	public V get(Object key) {
		int index = hash(key);
		if (buckets[index] == null) { return null; }
		Iterator<Entry<K, V>> iter = buckets[index].iterator();
		while (iter.hasNext()) {
			Entry<K, V> entry = iter.next();
			if (Objects.equals(key, entry.key)) {
				return entry.value;
			}
		}
		return null;
	}

	@Override
	public V remove(Object key) {
		int index = hash(key);
		if (buckets[index] == null) { return null; }

		Iterator<Entry<K, V>> iter = buckets[index].iterator();
		while (iter.hasNext()) {
			Entry<K, V> entry = iter.next();
			if (Objects.equals(key, entry.key)) {
				V old_value = entry.value;
				iter.remove();
				size--;
				return old_value;
			}
		}
		return null;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	// Заглушка
	@Override
	public boolean containsKey(Object key) {
		return false;
	}

	// Заглушка
	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	// Заглушка
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {

	}

	// Заглушка
	@Override
	public void clear() {

	}

	// Заглушка
	@Override
	public Set<K> keySet() {
		return null;
	}

	// Заглушка
	@Override
	public Collection<V> values() {
		return null;
	}

	// Заглушка
	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return null;
	}

}
