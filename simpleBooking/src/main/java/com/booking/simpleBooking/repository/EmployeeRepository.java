package com.booking.simpleBooking.repository;

import com.booking.simpleBooking.model.Employee;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
  Optional<Employee> findByEmployeeId(String employeeId);
}
