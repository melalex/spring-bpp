package com.melalex.bpp.strategy;

import lombok.Value;
import lombok.experimental.Delegate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.ValidationUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class StrategyAnnotationProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        final var beans = Arrays.stream(beanFactory.getBeanNamesForAnnotation(Strategy.class))
                .map(n -> createDefinition(n, beanFactory))
                .collect(Collectors.groupingBy(this::classifyInterface));

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

    }

    private Class<?> classifyInterface(StrategyBeanDefinition definition) {
        if (definition.baseInterface() != DefaultBaseInterface.class) {
            return definition.baseInterface();
        }

        final var interfaces = definition.getType().getInterfaces();

        if (interfaces.length != 1) {
            throw new IllegalStateException("Bean [ " + definition.getName() + " ] has [ " + interfaces.length + " ] super interface. Require [ 1 ].");
        }

        return interfaces[0];
    }

    private StrategyBeanDefinition createDefinition(String name, ConfigurableListableBeanFactory beanFactory) {
        final var beanDefinition = beanFactory.getBeanDefinition(name);
        final var annotationOnBean = beanFactory.findAnnotationOnBean(name, Strategy.class);
        final var type = beanFactory.getType(name);

        Objects.requireNonNull(annotationOnBean, () -> "Can't find @Strategy annotation on bean [ " + name + " ]");
        Objects.requireNonNull(type, () -> "Can't determine type of [ " + name + " ] bean");

        return new StrategyBeanDefinition(beanDefinition, annotationOnBean, name, type);
    }

    @Value
    private static class StrategyBeanDefinition {

        @Delegate
        private BeanDefinition beanDefinition;

        @Delegate
        private Strategy strategyAnnotation;

        private String name;
        private Class<?> type;
    }
}
