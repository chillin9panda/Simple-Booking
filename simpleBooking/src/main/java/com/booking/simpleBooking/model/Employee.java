package com.booking.simpleBooking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Employee {
  @Id
  private int employeeId;
  private String firstName;
  private String lastName;

  public void setEmployeeId(int employeeId) {
    this.employeeId = employeeId;
  }

  public int getEmployeeId() {
    return employeeId;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getLastName() {
    return lastName;
  }

}
