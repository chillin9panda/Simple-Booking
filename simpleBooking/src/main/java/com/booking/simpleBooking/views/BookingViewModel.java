package com.booking.simpleBooking.views;

public class BookingViewModel {
    private Integer bookingId;
    private String firstName;
    private String phoneNum;
    private String roomNumber;
    private String status;

    // Constructors
    public BookingViewModel(Integer bookingId, String firstName, String phoneNum, String roomNumber, String status) {
        this.bookingId = bookingId;
        this.firstName = firstName;
        this.phoneNum = phoneNum;
        this.roomNumber = roomNumber;
        this.status = status;
    }

    // Getters and setters
    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
