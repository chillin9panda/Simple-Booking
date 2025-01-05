package com.booking.simpleBooking.model;

import java.time.temporal.ChronoUnit;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer paymentId;

  @OneToOne
  @JoinColumn(name = "booking_id", referencedColumnName = "bookingId")
  private Booking booking;

  private Integer daysStayed;
  private Double totalAmount;
  private String paymentMethod;
  private boolean isPaid;

  // calculate days stayed and total totalAmount
  private void calculatePaymentDetails() {
    if (booking != null && booking.getCheckInDate() != null && booking.getCheckOutDate() != null) {
      this.daysStayed = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());

      Double roomPrice = booking.getRoom().getPrice();
      this.totalAmount = this.daysStayed * roomPrice;
    }
  }

  public void setPaymentID(int paymentId) {
    this.paymentId = paymentId;
  }

  public Integer getPaymentId() {
    return paymentId;
    calculatePaymentDetails();
  }

  public void setBooking(Booking booking) {
    this.booking = booking;
  }

  public Booking getBooking() {
    return Booking;
  }

  public Integer getDaysStayed() {
    return daysStayed;
  }

  public Double getTotalAmount() {
    return totalAmount;
  }

}
