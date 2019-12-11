package com.melalex.bpp.strategy;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.annotation.AnnotationUtils;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Delegate;

import com.melalex.bpp.util.SupplierUtils;

@AllArgsConstructor
public class StrategyAnnotationProcessor implements BeanFactoryPostProcessor {

  private final StrategyProviderFactory strategyProviderFactory;

  @Override
  public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory)
      throws BeansException {
    if (!(beanFactory instanceof BeanDefinitionRegistry)) {
      return;
    }

    final var strategyDefinition = Arrays
        .stream(beanFactory.getBeanNamesForAnnotation(Strategy.class))
        .map(n -> this.createStrategyDefinition(n, beanFactory))
        .collect(Collectors.groupingBy(this::classifyInterface));

    final var resolvers = SupplierUtils
        .memoize(() -> Arrays.stream(beanFactory.getBeanNamesForType(StrategyResolver.class))
            .map(beanFactory::getBean)
            .map(StrategyResolver.class::cast)
            .map(b -> Map.entry(b.getAnnotationType(), b))
            .collect(Collectors.toUnmodifiableList())
        );

    final var registry = (BeanDefinitionRegistry) beanFactory;

    strategyDefinition.forEach((metaData, beanDefinitions) -> {
      beanDefinitions.forEach(d -> d.setAutowireCandidate(false));

      final var resolverDefinition = this.createProviderDefinition(
          beanFactory,
          metaData,
          beanDefinitions,
          resolvers
      );

      final var resolverName = BeanDefinitionReaderUtils
          .generateBeanName(resolverDefinition, registry);

      registry.registerBeanDefinition(resolverName, resolverDefinition);
    });
  }


  private Class<?> classifyInterface(final StrategyBeanDefinition definition) {
    final Class<?> result;

    if (definition.baseInterface() != DefaultBaseInterface.class) {
      result = definition.baseInterface();
    } else {
      final var interfaces = definition.getType().getInterfaces();

      if (interfaces.length != 1) {
        throw new IllegalStateException(
            "Bean [ " + definition.getName() + " ] has [ " + interfaces.length
                + " ] super interface. Required [ 1 ].");
      }

      result = interfaces[0];
    }

    return result;
  }

  private StrategyBeanDefinition createStrategyDefinition(
      final String name,
      final ConfigurableListableBeanFactory beanFactory
  ) {
    final var beanDefinition = beanFactory.getBeanDefinition(name);
    final var annotationOnBean = beanFactory.findAnnotationOnBean(name, Strategy.class);
    final var type = beanFactory.getType(name);

    Objects.requireNonNull(annotationOnBean,
        () -> "Can't find @Strategy annotation on bean [ " + name + " ]");
    Objects.requireNonNull(type, () -> "Can't determine type of [ " + name + " ] bean");

    return new StrategyBeanDefinition(beanDefinition, annotationOnBean, name, type);
  }

  private BeanDefinition createProviderDefinition(
      final ConfigurableListableBeanFactory beanFactory,
      final Class<?> interfaceClass,
      final List<StrategyBeanDefinition> beanDefinitions,
      final Supplier<List<Entry<Class, StrategyResolver>>> resolvers
  ) {
    final var strategyNames = beanDefinitions.stream()
        .map(StrategyBeanDefinition::getName)
        .toArray(String[]::new);

    final Supplier<?> providerSupplier = () -> {
      final var resolverBean = resolvers.get()
          .stream()
          .filter(e -> beanDefinitions.stream()
              .map(StrategyBeanDefinition::getType)
              .allMatch(c -> AnnotationUtils.getAnnotation(c, e.getKey()) != null)
          )
          .map(Entry::getValue)
          .findFirst()
          .orElseThrow(() -> new IllegalStateException(
              "Can't find matching resolver for strategies: " + beanDefinitions)
          );

      final var strategies = Arrays.stream(strategyNames)
          .map(beanFactory::getBean)
          .collect(Collectors.toUnmodifiableList());

      resolverBean.setStrategies(strategies);

      return this.strategyProviderFactory
          .createProviderForInterface(interfaceClass, resolverBean);
    };

    final var result = new GenericBeanDefinition();

    result.setBeanClass(interfaceClass);
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
}
