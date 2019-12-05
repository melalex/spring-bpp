package com.melalex.bpp.strategy;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.stereotype.Component;

import lombok.Value;
import lombok.experimental.Delegate;

@Component
public class StrategyAnnotationProcessor implements BeanFactoryPostProcessor {

  @Autowired
  private StrategyProviderFactory strategyProviderFactory;

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
      throws BeansException {
    if (beanFactory instanceof BeanDefinitionRegistry) {
      final var strategyDefinition = Arrays
          .stream(beanFactory.getBeanNamesForAnnotation(Strategy.class))
          .map(n -> createDefinition(n, beanFactory))
          .collect(Collectors.groupingBy(this::classifyInterface));

      final var beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;

      strategyDefinition.forEach((metaData, beanDefinitions) -> {
        beanDefinitions.forEach(d -> d.setAutowireCandidate(false));

        BeanDefinitionBuilder.genericBeanDefinition(metaData.interfaceClass, () -> {
          final var resolverBean = (StrategyResolver) beanFactory.getBean(metaData.resolverClass);
          return metaData.interfaceClass.cast(strategyProviderFactory
              .createProviderForInterface(metaData.interfaceClass, resolverBean));
        }).addPropertyReference()


      });

    }
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
                + " ] super interface. Require [ 1 ].");
      }

      interfaceClass = interfaces[0];
    }

    return new ProviderMetaData(interfaceClass, definition.resolver());
  }

  private StrategyBeanDefinition createDefinition(String name,
      ConfigurableListableBeanFactory beanFactory) {
    final var beanDefinition = beanFactory.getBeanDefinition(name);
    final var annotationOnBean = beanFactory.findAnnotationOnBean(name, Strategy.class);
    final var type = beanFactory.getType(name);

    Objects.requireNonNull(annotationOnBean,
        () -> "Can't find @Strategy annotation on bean [ " + name + " ]");
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

  @Value
  private static class ProviderMetaData {

    private Class<?> interfaceClass;
    private Class<?> resolverClass;
  }
}
