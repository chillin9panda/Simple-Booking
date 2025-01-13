package com.booking.simpleBooking.repository;

import com.booking.simpleBooking.model.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestsRepository extends JpaRepository <Rooms, Int> {

}