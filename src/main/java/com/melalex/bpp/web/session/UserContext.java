package com.melalex.bpp.web.session;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import lombok.Data;

@Data
@Component
@SessionScope
public class UserContext {

  private boolean cassandra;
}
