package com.booking.simpleBooking.repository;

import com.booking.simpleBooking.model.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomsRepository extends JpaRepository<Rooms, Integer> {

}
