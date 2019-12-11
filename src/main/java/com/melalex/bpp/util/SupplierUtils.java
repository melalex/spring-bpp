package com.melalex.bpp.util;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SupplierUtils {

  public <T> Supplier<T> memoize(final Supplier<T> supplier) {
    return (supplier instanceof MemoizingSupplier)
        ? supplier
        : new MemoizingSupplier<>(Objects.requireNonNull(supplier));
  }

  private static final class MemoizingSupplier<T> implements Supplier<T>, Serializable {

    private static final long serialVersionUID = 5151750905364552759L;

    private final Supplier<T> delegate;
    private transient volatile boolean initialized;
    private transient T value;

    private MemoizingSupplier(final Supplier<T> delegate) {
      this.delegate = delegate;
    }

    @Override
    @SuppressWarnings("findbugs:IS2_INCONSISTENT_SYNC")
    public T get() {
      if (!this.initialized) {
        synchronized (this) {
          if (!this.initialized) {
            final T t = this.delegate.get();
            this.value = t;
            this.initialized = true;
            return t;
          }
        }
      }
      return this.value;
    }

    @Override
    public String toString() {
      return "SupplierUtils.memoize(" + this.delegate + ")";
    }
  }
}

