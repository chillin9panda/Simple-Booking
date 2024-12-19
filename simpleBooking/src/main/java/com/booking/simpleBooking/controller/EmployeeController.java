package com.booking.simpleBooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.booking.simpleBooking.model.Employee;
import com.booking.simpleBooking.repository.EmployeeRepository;

@RestController
public class EmployeeController {
  @Autowired
  private EmployeeRepository employeeRepository;

  @GetMapping("/add-employee")
  public String addEmployee() {
    Employee employee = new Employee();
    employee.setEmployeeId(001);
    employee.setFirstName("John");
    employee.setLastName("Doe");

    employeeRepository.save(employee);
    return "Employee Saved";
  }

}
