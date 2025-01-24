package com.booking.simpleBooking.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.booking.simpleBooking.model.Booking;
import com.booking.simpleBooking.model.Guests;
import com.booking.simpleBooking.model.Rooms;
import com.booking.simpleBooking.repository.BookingRepository;
import com.booking.simpleBooking.repository.BookingViewRepository;
import com.booking.simpleBooking.repository.GuestsRepository;
import com.booking.simpleBooking.repository.RoomsRepository;
import com.booking.simpleBooking.views.BookingViewModel;

import org.springframework.ui.Model;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/")
class BookingController {

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private GuestsRepository guestsRepository;

  @Autowired
  private RoomsRepository roomsRepository;

  @Autowired
  private BookingViewRepository bookingViewRepository;

  @GetMapping("/")
  public String showHomePage(Model model, @RequestParam(required = false) String view) {
    List<BookingViewModel> bookings = bookingViewRepository.getAllBookingViews();
    model.addAttribute("bookings", bookings);

    List<Rooms> rooms = roomsRepository.findAll();
    model.addAttribute("rooms", rooms);

    List<Rooms> availableRooms = roomsRepository.findByRoomStatus(Rooms.RoomStatus.AVAILABLE);
    model.addAttribute("availableRooms", availableRooms);

    return "booking";
  }

  @PostMapping("/newBooking")
  public ResponseEntity<String> createBooking(
      @RequestParam String firstName,
      @RequestParam String lastName,
      @RequestParam String phoneNum,
      @RequestParam(required = false) String email,
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkInDate,
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkOutDate,
      @RequestParam Integer roomNumber) {

    Optional<Guests> guestOptional = guestsRepository.findById(phoneNum);

    Guests guest;

    if (guestOptional.isEmpty()) {
      guest = new Guests();
      guest.setFirstName(firstName);
      guest.setLastName(lastName);
      guest.setPhoneNum(phoneNum);
      guest.setEmail(email);
      guestsRepository.save(guest);
    } else {
      guest = guestOptional.get();
    }

    Optional<Rooms> roomOptional = roomsRepository.findById(roomNumber);

    if (roomOptional.isEmpty()) {
      return ResponseEntity.badRequest().body("Room Not Found!");
    }

    Rooms room = roomOptional.get();

    if (room.getRoomStatus() != Rooms.RoomStatus.AVAILABLE) {
      return ResponseEntity.badRequest().body("Room not Available!");
    }

    // Create booking
    Booking booking = new Booking();
    booking.setGuest(guest);
    booking.setRoom(room);
    booking.setCheckInDate(checkInDate);
    booking.setCheckOutDate(checkOutDate);
    booking.setIsActive(true);
    bookingRepository.save(booking);

    // Update status
    room.setRoomStatus(Rooms.RoomStatus.BOOKED);
    roomsRepository.save(room);

    return ResponseEntity.ok("Booking Created Successfully!");
  }

  @Transactional
  @PutMapping("/booking/{bookingId}")
  public ResponseEntity<String> modifyBooking(@PathVariable Integer bookingId, @RequestBody Booking updatedBooking) {

    // Validations
    Optional<Booking> existingBookingOptional = bookingRepository.findById(bookingId);
    if (existingBookingOptional.isEmpty()) {
      return ResponseEntity.badRequest().body("Booking Not Found!");
    }

    Booking existingBooking = existingBookingOptional.get();

    Optional<Guests> guestOptional = guestsRepository.findById(updatedBooking.getGuest().getPhoneNum());
    if (guestOptional.isEmpty()) {
      return ResponseEntity.badRequest().body("Guest Not Found!");
    }

    Guests guest = guestOptional.get();

    Optional<Rooms> roomOptional = roomsRepository.findById(updatedBooking.getRoom().getRoomNumber());
    if (roomOptional.isEmpty()) {
      return ResponseEntity.badRequest().body("Room Not Found!");
    }

    Rooms room = roomOptional.get();

    // check availablity before change'
    if (!room.equals(existingBooking.getRoom()) ||
        !updatedBooking.getCheckInDate().equals(existingBooking.getCheckInDate()) ||
        !updatedBooking.getCheckOutDate().equals(existingBooking.getCheckOutDate())) {
      if (room.getRoomStatus() != Rooms.RoomStatus.AVAILABLE) {
        return ResponseEntity.badRequest().body("Room Not Available!");
      }
    }

    if (updatedBooking.getCheckInDate().after(updatedBooking.getCheckOutDate())) {
      return ResponseEntity.badRequest().body("Check-in date must be before Check-out date");
    }

    // Update booking details
    existingBooking.setGuest(guest);
    existingBooking.setRoom(room);
    existingBooking.setCheckInDate(updatedBooking.getCheckInDate());
    existingBooking.setCheckOutDate(updatedBooking.getCheckOutDate());
    existingBooking.setIsActive(updatedBooking.getIsActive());
    bookingRepository.save(existingBooking);

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
