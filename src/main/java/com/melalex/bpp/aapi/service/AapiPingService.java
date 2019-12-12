package com.melalex.bpp.aapi.service;

import com.melalex.bpp.aapi.service.impl.AapiPingCassandraService;
import com.melalex.bpp.aapi.support.Switch;
import com.melalex.bpp.web.dto.PingDto;

@Switch(beanClass = AapiPingCassandraService.class, featureName = "CassandraSwitch")
public interface AapiPingService {

  PingDto ping();
}
