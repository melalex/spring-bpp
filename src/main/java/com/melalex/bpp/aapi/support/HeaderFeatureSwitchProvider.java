package com.melalex.bpp.aapi.support;

import org.springframework.stereotype.Component;

import com.melalex.bpp.web.session.UserContext;

@Component
public class HeaderFeatureSwitchProvider implements FeatureSwitchProvider {

  private final UserContext userContext;

  public HeaderFeatureSwitchProvider(final UserContext userContext) {
    this.userContext = userContext;
  }

  @Override
  public boolean getSwitchFeatureValue(final String featureName) {
    return this.userContext.isCassandra();
  }
}
