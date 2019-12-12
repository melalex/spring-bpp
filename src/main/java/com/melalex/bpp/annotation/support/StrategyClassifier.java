package com.melalex.bpp.annotation.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface StrategyClassifier<K, A extends Annotation> {

  K classifyKey(Method method, Object[] args);

  K classifyMatcher(A annotation);

  Class<A> getAnnotationType();
}
