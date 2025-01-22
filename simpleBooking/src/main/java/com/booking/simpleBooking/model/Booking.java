package com.booking.simpleBooking.model;

import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Booking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer bookingId;

  @ManyToOne
  @JoinColumn(name = "phoneNum", referencedColumnName = "phoneNum")
  private Guests guest;

  @OneToOne
  @JoinColumn(name = "roomNumber", referencedColumnName = "roomNumber")
  private Rooms room;

  private Date checkInDate;
  private Date checkOutDate;

  private Boolean isActive;

  // getter and setters
  public void setGuest(Guests guest) {
    this.guest = guest;
  }

  public Guests getGuest() {
    return guest;
  }

  public void setRoom(Rooms room) {
    this.room = room;
  }

  public Rooms getRoom() {
    return room;
  }

  public Integer getBookingId() {
    return bookingId;
  }

  public void setCheckInDate(Date checkInDate) {
    this.checkInDate = checkInDate;
  }

  public Date getCheckInDate() {
    return checkInDate;
  }

  public void setCheckOutDate(Date checkOutDate) {
    this.checkOutDate = checkOutDate;
  }

  public Date getCheckOutDate() {
    return checkOutDate;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public Boolean getIsActive() {
    return isActive;
  }
}
