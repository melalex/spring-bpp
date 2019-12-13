package com.melalex.bpp.web.session.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import lombok.Data;

import com.melalex.bpp.web.session.UserContext;

@Data
@Component
@SessionScope
public class WebUserContext implements UserContext {

  private boolean cassandra;
}
