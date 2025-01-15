package com.booking.simpleBooking.controller;

import com.booking.simpleBooking.model.Rooms;
import com.booking.simpleBooking.repository.RoomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import java.util.List;

@Controller
@RequestMapping("/")
public class RoomsController {

  @Autowired
  private RoomsRepository roomsRepository;

  @GetMapping
  public String getAllRooms(Model model) {
    List<Rooms> rooms = roomsRepository.findAll();
    model.addAttribute("rooms", rooms);
    for (Rooms room : rooms) {
      System.out.println(room.getRoomStatus());
    }
    return "booking";
  }

  @GetMapping("/addRoom")
  public String loadAddRoomForm() {
    return "addRoom";
  }

  @GetMapping("/{roomNumber}")
  public ResponseEntity<Rooms> getRoomsById(@PathVariable Integer roomNumber) {
    return roomsRepository.findById(roomNumber)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/addRoom")
  public String createRoom(@ModelAttribute Rooms room, Model model) {
    roomsRepository.save(room);
    model.addAttribute("successMessage", "Room Added");
    return "redirect:/";
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
