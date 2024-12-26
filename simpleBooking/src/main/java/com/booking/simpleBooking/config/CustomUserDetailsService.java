package com.booking.simpleBooking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.booking.simpleBooking.model.Employee;
import com.booking.simpleBooking.repository.EmployeeRepository;

@Service
class CustomUserDetailsService implements UserDetailsService {

  private final EmployeeRepository employeeRepository;

  @Autowired
  public CustomUserDetailsService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String employeeId) throws UsernameNotFoundException {
    Employee employee = employeeRepository.findByEmployeeId(employeeId)
        .orElseThrow(() -> new UsernameNotFoundException("Employee not found with Id: " + employeeId));

    return User.builder()
        .username(employee.getEmployeeId())
        .password(employee.getPassword())
        .build();
  }

}
