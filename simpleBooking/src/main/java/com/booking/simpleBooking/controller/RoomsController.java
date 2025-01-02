package com.booking.simpleBooking.controller;

import com.booking.simpleBooking.model.Rooms;
import com.booking.simpleBooking.repository.RoomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomsController {

    @Autowired
    private RoomsRepository roomsRepository;

    @GetMapping
    public List<Rooms> getAllRooms() {
        return roomsRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Rooms> addRoom(@RequestBody Rooms room) {
        Rooms savedRoom = roomsRepository.save(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }

    @PutMapping("/{roomNumber}")
    public ResponseEntity<Rooms> updateRoom(@PathVariable int roomNumber, @RequestBody Rooms roomDetails) {
        Rooms room = roomsRepository.findById(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found with number: " + roomNumber));

        room.setRoomType(roomDetails.getRoomType());
        room.setRoomPrice(roomDetails.getRoomPrice());
        room.setRoomStatus(roomDetails.getRoomStatus());

        Rooms updatedRoom = roomsRepository.save(room);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{roomNumber}")
    public ResponseEntity<Void> deleteRoom(@PathVariable int roomNumber) {
        Rooms room = roomsRepository.findById(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found with number: " + roomNumber));

        roomsRepository.delete(room);
        return ResponseEntity.noContent().build();
    }
}
