package com.melalex.bpp.strategy;

import lombok.Value;
import lombok.experimental.Delegate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class StrategyAnnotationProcessor implements BeanFactoryPostProcessor {

    @Autowired
    private StrategyProviderFactory strategyProviderFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (!(beanFactory instanceof BeanDefinitionRegistry)) {
            return;
        }

        final var strategyDefinition = Arrays
                .stream(beanFactory.getBeanNamesForAnnotation(Strategy.class))
                .map(n -> createStrategyDefinition(n, beanFactory))
                .collect(Collectors.groupingBy(this::classifyInterface));

        final var registry = (BeanDefinitionRegistry) beanFactory;

        strategyDefinition.forEach((metaData, beanDefinitions) -> {
            beanDefinitions.forEach(d -> d.setAutowireCandidate(false));

            final var resolverDefinition = createResolverDefinition(beanFactory, metaData, beanDefinitions);
            final var resolverName = BeanDefinitionReaderUtils.generateBeanName(resolverDefinition, registry);

            registry.registerBeanDefinition(resolverName, resolverDefinition);
        });
    }


    private ProviderMetaData classifyInterface(StrategyBeanDefinition definition) {
        Class<?> interfaceClass;

        if (definition.baseInterface() != DefaultBaseInterface.class) {
            interfaceClass = definition.baseInterface();
        } else {
            final var interfaces = definition.getType().getInterfaces();

            if (interfaces.length != 1) {
                throw new IllegalStateException(
                        "Bean [ " + definition.getName() + " ] has [ " + interfaces.length
                                + " ] super interface. Required [ 1 ].");
            }

            interfaceClass = interfaces[0];
        }

        return new ProviderMetaData(interfaceClass, definition.resolver());
    }

    private StrategyBeanDefinition createStrategyDefinition(
            String name,
            ConfigurableListableBeanFactory beanFactory
    ) {
        final var beanDefinition = beanFactory.getBeanDefinition(name);
        final var annotationOnBean = beanFactory.findAnnotationOnBean(name, Strategy.class);
        final var type = beanFactory.getType(name);

        Objects.requireNonNull(annotationOnBean,
                () -> "Can't find @Strategy annotation on bean [ " + name + " ]");
        Objects.requireNonNull(type, () -> "Can't determine type of [ " + name + " ] bean");

        return new StrategyBeanDefinition(beanDefinition, annotationOnBean, name, type);
    }

    private BeanDefinition createResolverDefinition(
            ConfigurableListableBeanFactory beanFactory,
            ProviderMetaData metaData,
            List<StrategyBeanDefinition> beanDefinitions
    ) {
        final var strategyNames = beanDefinitions.stream()
                .map(StrategyBeanDefinition::getName)
                .toArray(String[]::new);

        final Supplier<?> providerSupplier = () -> {
            final var resolverBean = (StrategyResolver) beanFactory.getBean(metaData.resolverClass);
            final var strategies = Arrays.stream(strategyNames)
                    .map(beanFactory::getBean)
                    .collect(Collectors.toList());

            resolverBean.setStrategies(strategies);

            return strategyProviderFactory
                    .createProviderForInterface(metaData.interfaceClass, resolverBean);
        };

        final var result = new GenericBeanDefinition();

        result.setBeanClass(metaData.interfaceClass);
        result.setInstanceSupplier(providerSupplier);
        result.setPrimary(true);
        result.setDependsOn(strategyNames);

        return result;
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

    @Value
    private static class ProviderMetaData {

        private Class<?> interfaceClass;
        private Class<?> resolverClass;
    }
}
