package com.esatus.ssi.bkamt.controller.verification.web.rest;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import io.github.jhipster.config.JHipsterConstants;

@Profile(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
@Controller
@RequestMapping("/demo")
public class DemoVerification {
  @GetMapping("/")
  public String demo(Model model) {
    return "demo.html";
  }
}
