package com.melalex.bpp.gof.support;

public interface Provider<K, V> {

  V provide(K key);
}
