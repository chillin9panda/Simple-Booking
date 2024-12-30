package com.booking.simpleBooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.booking.simpleBooking.model.Guests;

public interface GuestsRepository extends JpaRepository<Guests, String> {

}
