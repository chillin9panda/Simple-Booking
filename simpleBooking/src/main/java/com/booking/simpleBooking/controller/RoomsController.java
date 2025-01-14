package com.booking.simpleBooking.controller;

import com.booking.simpleBooking.model.Rooms;
import com.booking.simpleBooking.repository.RoomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/addRooms")
public class RoomsController {

  @Autowired
  private RoomsRepository roomsRepository;

  @GetMapping
  public List<Rooms> getAllRooms() {
    return roomsRepository.findAll();
  }

  @GetMapping("/add")
  public String loadAddRoomForm() {
    return "addRoom";
  }

  @GetMapping("/{roomNumber}")
  public ResponseEntity<Rooms> getRoomsById(@PathVariable Integer roomNumber) {
    return roomsRepository.findById(roomNumber)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Rooms createRoom(@RequestBody Rooms room) {
    return roomsRepository.save(room);
  }

  @PutMapping("/{roomNumber}")
  public ResponseEntity<Rooms> updateRoom(@PathVariable Integer roomNumber, @RequestBody Rooms roomDetails) {
    return roomsRepository.findById(roomNumber)
        .map(room -> {
          room.setRoomPrice(roomDetails.getRoomPrice());
          room.setRoomType(roomDetails.getRoomType());
          Rooms updateRoom = roomsRepository.save(roomDetails);
          return ResponseEntity.ok(updateRoom);
        })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{roomNumber}")
  public ResponseEntity<Void> deleteRoom(@PathVariable Integer roomNumber) {
    if (roomsRepository.existsById(roomNumber)) {
      roomsRepository.deleteById(roomNumber);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
