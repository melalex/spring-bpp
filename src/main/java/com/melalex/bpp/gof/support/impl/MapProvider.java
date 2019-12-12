package com.melalex.bpp.gof.support.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.BeanNameAware;

import lombok.extern.slf4j.Slf4j;

import com.melalex.bpp.gof.support.Provider;

@Slf4j
public final class MapProvider<K, V> implements Provider<K, V>, BeanNameAware {

  private final Map<K, V> map;
  private final V defaultValue;

  private String beanName = "UNKNOWN";

  private MapProvider(final Map<K, V> map, final V defaultValue) {
    this.map = Map.copyOf(map);
    this.defaultValue = defaultValue;
  }

  public static <K, V> MapProviderBuilder<K, V> builder() {
    return new MapProviderBuilder<>();
  }

  @Override
  public void setBeanName(final String beanName) {
    this.beanName = beanName;
  }

  @Override
  public V provide(final K key) {
    final var value = this.map.get(key);

    if (value == null && this.defaultValue != null) {
      log.warn("( {} ) has no value for key [ {} ]. Returning [ {} ].", this.beanName, key,
          this.defaultValue);

      return this.defaultValue;
    } else if (value == null) {
      throw new IllegalArgumentException(
          "[ " + this.beanName + " ] has no value for key [ " + key + " ]");
    } else {
      return value;
    }
  }

  public static final class MapProviderBuilder<K, V> {

    private final Map<K, V> mapBuilder = new HashMap<>();
    private V defaultValue;

    private MapProviderBuilder() {
    }

    public MapProviderBuilder<K, V> with(final K key, final V value) {
      this.mapBuilder.put(key, value);
      return this;
    }

    public MapProviderBuilder<K, V> with(final Map<K, V> map) {
      this.mapBuilder.putAll(map);
      return this;
    }

    public MapProviderBuilder<K, V> defaultValue(final V defaultValue) {
      this.defaultValue = defaultValue;
      return this;
    }

    public MapProvider<K, V> build() {
      return new MapProvider<>(this.mapBuilder, this.defaultValue);
    }
  }
}
