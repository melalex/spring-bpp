package com.melalex.bpp.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import com.melalex.bpp.web.dto.PingDto;
import com.melalex.bpp.service.PingService;

@RestController
@AllArgsConstructor
@RequestMapping("/v1")
public class PingController {

  private final PingService pingService;

  @GetMapping("/ping")
  public PingDto ping() {
    return pingService.ping();
  }
}
