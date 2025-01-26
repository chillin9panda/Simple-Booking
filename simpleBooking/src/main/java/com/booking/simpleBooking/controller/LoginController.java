package com.booking.simpleBooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
  @GetMapping("/login")
  public String LoginPage() {
    return "login";
  }

  @GetMapping("/favicon.ico")
  @ResponseBody
  public void favicon() {

  }
}
