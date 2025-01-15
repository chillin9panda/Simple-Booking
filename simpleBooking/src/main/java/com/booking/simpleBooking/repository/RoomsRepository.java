package com.booking.simpleBooking.repository;

import com.booking.simpleBooking.model.Rooms;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomsRepository extends JpaRepository<Rooms, Integer> {
  List<Rooms> findByRoomStatus(Rooms.RoomStatus roomStatus);
}
