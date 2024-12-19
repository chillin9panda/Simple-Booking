package com.booking.simpleBooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.booking.simpleBooking")
public class SimpleBookingApplication {

  public static void main(String[] args) {
    SpringApplication.run(SimpleBookingApplication.class, args);
  }

}
