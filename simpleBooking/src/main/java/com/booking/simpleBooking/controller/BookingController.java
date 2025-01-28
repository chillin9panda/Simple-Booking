package com.booking.simpleBooking.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import com.booking.simpleBooking.model.Booking;
import com.booking.simpleBooking.model.Employee;
import com.booking.simpleBooking.model.Guests;
import com.booking.simpleBooking.model.Rooms;
import com.booking.simpleBooking.repository.BookingRepository;
import com.booking.simpleBooking.repository.BookingViewRepository;
import com.booking.simpleBooking.repository.EmployeeRepository;
import com.booking.simpleBooking.repository.GuestsRepository;
import com.booking.simpleBooking.repository.RoomsRepository;
import com.booking.simpleBooking.views.BookingViewModel;
import org.springframework.ui.Model;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

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

  @Autowired
  EmployeeRepository employeeRepository;

  @GetMapping("/")
  public String showHomePage(Model model, @RequestParam(required = false) String phoneNum) {
    // Booking
    List<BookingViewModel> bookings = bookingViewRepository.getAllBookingViews();
    model.addAttribute("bookings", bookings);

    // Rooms
    List<Rooms> rooms = roomsRepository.findAll();
    model.addAttribute("rooms", rooms);
    model.addAttribute("roomsCount", rooms.size());

    List<Rooms> availableRooms = roomsRepository.findByRoomStatus(Rooms.RoomStatus.AVAILABLE);
    model.addAttribute("availableRooms", availableRooms);
    model.addAttribute("availableRoomsCount", availableRooms.size());

    List<Rooms> bookedRooms = roomsRepository.findByRoomStatus(Rooms.RoomStatus.BOOKED);
    model.addAttribute("bookedRoomsCount", bookedRooms.size());

    // User
    User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    String employeeId = loggedInUser.getUsername();

    Employee loggedInEmployee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new RuntimeException("User not found!"));

    String firstName = loggedInEmployee.getFirstName();

    model.addAttribute("firstName", firstName);

    // Search for booking with guest phone number
    List<Booking> bookingFound = bookingRepository.findByGuestPhoneNum(phoneNum);

    if (bookingFound.isEmpty()) {
      model.addAttribute("message", "No booking found for Phone Number: " + phoneNum);
    } else {
      model.addAttribute("bookingFound", bookingFound);
    }

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

  @GetMapping("/booking/json/{bookingId}")
  public ResponseEntity<Booking> getBooking(@PathVariable Integer bookingId) {
    Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);

    if (bookingOptional.isEmpty()) {
      return ResponseEntity.status(404).body(null);
    }

    return ResponseEntity.ok(bookingOptional.get());
  }

  @GetMapping("/booking/edit/{bookingId}")
  public String editBookingPage(@PathVariable Integer bookingId, Model model) {
    Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);

    if (bookingOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not Found");
    }

    Booking booking = bookingOptional.get();
    model.addAttribute("booking", booking);

    List<Rooms> availableRooms = roomsRepository.findByRoomStatus(Rooms.RoomStatus.AVAILABLE);
    model.addAttribute("availableRooms", availableRooms);

    return "editBooking";
  }

  @Transactional
  @PostMapping("/booking/{bookingId}")
  public ResponseEntity<String> modifyBooking(@PathVariable Integer bookingId, @ModelAttribute Booking updatedBooking) {

    // Validate and fetch existing booking
    Optional<Booking> existingBookingOptional = bookingRepository.findById(bookingId);
    if (existingBookingOptional.isEmpty()) {
      return ResponseEntity.badRequest().body("Booking Not Found!");
    }

    Booking existingBooking = existingBookingOptional.get();

    // Validate and fetch new room (if provided)
    Rooms newRoom = null;
    if (updatedBooking.getRoom() != null) {
      Optional<Rooms> roomOptional = roomsRepository.findById(updatedBooking.getRoom().getRoomNumber());
      if (roomOptional.isEmpty()) {
        return ResponseEntity.badRequest().body("Room Not Found!");
      }
      newRoom = roomOptional.get();

      // Check availability of the new room
      if (!newRoom.equals(existingBooking.getRoom()) && newRoom.getRoomStatus() != Rooms.RoomStatus.AVAILABLE) {
        return ResponseEntity.badRequest().body("New Room Not Available!");
      }
    }

    // Update room if it has changed
    if (newRoom != null && !newRoom.equals(existingBooking.getRoom())) {
      // Set previous room's status to AVAILABLE
      existingBooking.getRoom().setRoomStatus(Rooms.RoomStatus.AVAILABLE);
      roomsRepository.save(existingBooking.getRoom());

      newRoom.setRoomStatus(Rooms.RoomStatus.BOOKED);

      // Update booking with new room
      existingBooking.setRoom(newRoom);
    }

    // Update check-out date if provided
    if (updatedBooking.getCheckOutDate() != null) {
      if (updatedBooking.getCheckInDate() != null
          && updatedBooking.getCheckInDate().after(updatedBooking.getCheckOutDate())) {
        return ResponseEntity.badRequest().body("Check-in date must be before Check-out date");
      }
      existingBooking.setCheckOutDate(updatedBooking.getCheckOutDate());
    }

    // Save updated booking
    bookingRepository.save(existingBooking);

    return ResponseEntity.ok("Booking Updated!");
  }

  // Check-out
  @PostMapping("/booking/checkout/{bookingId}")
  public ResponseEntity<String> checkOutBooking(@PathVariable Integer bookingId) {
    Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
    if (existingBooking.isEmpty()) {
      return ResponseEntity.badRequest().body("Booking is not found!");
    }

    Booking booking = existingBooking.get();

    Date today = new Date();

    LocalDate bookingCheckOutLocalDate = booking.getCheckOutDate().toInstant().atZone(ZoneId.systemDefault())
        .toLocalDate();

    LocalDate todayLocalDate = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    if (!bookingCheckOutLocalDate.equals(todayLocalDate)) {
      booking.setCheckOutDate(today);
    }

    booking.setIsActive(false);

    Rooms room = booking.getRoom();
    room.setRoomStatus(Rooms.RoomStatus.AVAILABLE);
    bookingRepository.save(booking);

    return ResponseEntity.ok("Check-out Successfull");
  }

  // Delete Booking
  @DeleteMapping("booking/delete/{bookingId}")
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
