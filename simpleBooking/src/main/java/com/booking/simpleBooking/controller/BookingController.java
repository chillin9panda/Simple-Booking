package com.booking.simpleBooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class BookingController {
  @GetMapping("/")
  public String BookingPage() {
    return "booking";
  }
}
