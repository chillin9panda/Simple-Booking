package com.booking.simpleBooking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {

  @GetMapping("/")
  public String home() {
    return "login";
  }
}
