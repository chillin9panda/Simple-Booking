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

  @GetMapping("/addEmployee")
  public String addEmployee() {
    // Add Employee
    Employee employee = new Employee();
    employee.setEmployeeId("emp001");
    employee.setFirstName("John");
    employee.setLastName("Doe");
    employee.setPhoneNum("0915253640");

    employee.setPassword("john001");

    employeeRepository.save(employee);
    return "Employee Saved";
  }

}
