package com.melalex.bpp.annotation.support.reflection.wrapers;

import java.lang.reflect.Method;

import lombok.Value;

@Value
public class CommonMethodWrapper implements MethodWrapper {

  private Method method;
}
