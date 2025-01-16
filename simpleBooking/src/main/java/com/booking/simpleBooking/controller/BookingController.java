package com.booking.simpleBooking.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.booking.simpleBooking.model.Booking;
import com.booking.simpleBooking.model.Guests;
import com.booking.simpleBooking.model.Rooms;
import com.booking.simpleBooking.repository.BookingRepository;
import com.booking.simpleBooking.repository.GuestsRepository;
import com.booking.simpleBooking.repository.RoomsRepository;

@RestController
@RequestMapping("/")
class BookingController {

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private GuestsRepository guestsRepository;

  @Autowired
  private RoomsRepository roomsRepository;

  @PostMapping
  public ResponseEntity<String> createBooking(@RequestBody Booking bookingRequest) {
    Optional<Guests> guest = guestsRepository.findById(bookingRequest.getGuest().getPhoneNum());

    if (guest.isEmpty()) {
      return ResponseEntity.badRequest().body("Guest Not Found!");
    }

    Optional<Rooms> room = roomsRepository.findById(bookingRequest.getRoom().getRoomNumber());

    if (room.isEmpty()) {
      return ResponseEntity.badRequest().body("Room Not Found!");
    }

    if (room.get().getRoomStatus() != Rooms.RoomStatus.AVAILABLE) {
      return ResponseEntity.badRequest().body("Room not Available!");
    }

    // Create booking
    bookingRequest.setGuest(guest.get());
    bookingRequest.setRoom(room.get());
    bookingRequest.setIsActive(true);
    bookingRepository.save(bookingRequest);

    // Update status
    Rooms bookedRoom = room.get();
    bookedRoom.setRoomStatus(Rooms.RoomStatus.BOOKED);
    roomsRepository.save(bookedRoom);

    return ResponseEntity.ok("Booking Created Successfully!");
  }

  @PutMapping("/{bookingId}")
  public ResponseEntity<String> modifyBooking(@PathVariable Integer bookingId, @RequestBody Booking updatedBooking) {

    // Validations
    Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
    if (existingBooking.isEmpty()) {
      return ResponseEntity.badRequest().body("Booking Not Found!");
    }

    Optional<Guests> guest = guestsRepository.findById(updatedBooking.getGuest().getPhoneNum());
    if (guest.isEmpty()) {
      return ResponseEntity.badRequest().body("Guest Not Found!");
    }

    Optional<Rooms> room = roomsRepository.findById(updatedBooking.getRoom().getRoomNumber());
    if (room.isEmpty()) {
      return ResponseEntity.badRequest().body("Room Not Found!");
    }

    // Update booking details
    Booking booking = existingBooking.get();
    booking.setGuest(guest.get());
    booking.setRoom(room.get());
    booking.setCheckInDate(updatedBooking.getCheckInDate());
    booking.setCheckOutDate(updatedBooking.getCheckOutDate());
    booking.setIsActive(updatedBooking.getIsActive());
    bookingRepository.save(booking);

    return ResponseEntity.ok("Booking Updated!");
  }

  // Delete Booking
  @DeleteMapping("/{bookingId}")
  public ResponseEntity<String> deleteBooking(@PathVariable Integer bookingId) {
    Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
    if (existingBooking.isEmpty()) {
      return ResponseEntity.badRequest().body("Booking Not Found!");
    }

    Booking booking = existingBooking.get();

    // MArk Room as AVAILABLE
    Rooms room = booking.getRoom();
    room.setRoomStatus(Rooms.RoomStatus.AVAILABLE);
    roomsRepository.save(room);

    // Delete Booking
    bookingRepository.delete(booking);

    return ResponseEntity.ok("Booking deleted!");
  }
}
