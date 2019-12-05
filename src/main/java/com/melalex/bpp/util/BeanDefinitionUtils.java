package com.melalex.bpp.util;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import java.util.function.Supplier;

@UtilityClass
public class BeanDefinitionUtils {

    public static BeanDefinitionBuilder builder(Class<?> beanClass, Supplier<?> instanceSupplier) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
        builder.getRawBeanDefinition().setBeanClass(beanClass);
        builder.getRawBeanDefinition().setInstanceSupplier(instanceSupplier);
        return builder;
    }
}
