package com.booking.simpleBooking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class Rooms {
  @Id
  private int roomNumber;

  @Enumerated(EnumType.STRING)
  private RoomType roomType;

  private double roomPrice;

  @Enumerated(EnumType.STRING)
  private RoomStatus roomStatus;

  // Getters and Setters
  public int getRoomNumber() {
    return roomNumber;
  }

  public void setRoomNumber(int roomNumber) {
    this.roomNumber = roomNumber;
  }

  public RoomType getRoomType() {
    return roomType;
  }

  public void setRoomType(RoomType roomType) {
    this.roomType = roomType;
  }

  public double getRoomPrice() {
    return roomPrice;
  }

  public void setRoomPrice(double roomPrice) {
    this.roomPrice = roomPrice;
  }

  public RoomStatus getRoomStatus() {
    return roomStatus;
  }

  public void setRoomStatus(RoomStatus roomStatus) {
    this.roomStatus = roomStatus;
  }

  public enum RoomType {
    SINGLE,
    DOUBLE
  }

  public enum RoomStatus {
    AVAILABLE,
    BOOKED,
    UNAVAILABLE
  }
}
