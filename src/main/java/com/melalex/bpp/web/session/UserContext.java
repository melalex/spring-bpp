package com.melalex.bpp.web.session;

public interface UserContext {

  boolean isCassandra();

  void setCassandra(final boolean cassandra);
}
