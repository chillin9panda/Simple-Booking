package com.booking.simpleBooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.booking.simpleBooking.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
