package com.melalex.bpp.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import com.melalex.bpp.annotation.service.AnnotatedPingService;
import com.melalex.bpp.annotation.service.AnnotatedWithAbstractAncestorPingService;
import com.melalex.bpp.gof.service.GofPingService;
import com.melalex.bpp.spring.service.SpringPingService;
import com.melalex.bpp.web.dto.PingDto;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/ping")
public class PingController {

  private final AnnotatedWithAbstractAncestorPingService annotatedWithAbstractAncestorPingService;
  private final AnnotatedPingService annotatedPingService;
  private final GofPingService gofPingService;
  private final SpringPingService springPingService;

  @GetMapping("/annotation/withAbstractAncestor")
  public PingDto pingAnnotatedWithAbstractAncestorPingService() {
    return this.annotatedWithAbstractAncestorPingService.ping();
  }

  @GetMapping("/annotation")
  public PingDto pingAnnotatedPingService() {
    return this.annotatedPingService.ping();
  }

  @GetMapping("/gof")
  public PingDto pingGofPingService() {
    return this.gofPingService.ping();
  }

  @GetMapping("/spring")
  public PingDto pingSpringPingService() {
    return this.springPingService.ping();
  }
}
