package com.melalex.bpp.web.filter;


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

import com.melalex.bpp.web.session.UserContext;

@Component
@AllArgsConstructor
public class CassandraSwitchFilter implements Filter {

  private final UserContext userContext;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;

    final boolean cassandraSwitch = Boolean.parseBoolean(req.getHeader("CassandraSwitch"));

    userContext.setCassandra(cassandraSwitch);

    chain.doFilter(request, response);
  }
}
