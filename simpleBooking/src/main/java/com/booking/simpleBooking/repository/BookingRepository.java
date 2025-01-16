package com.booking.simpleBooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.booking.simpleBooking.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

}
