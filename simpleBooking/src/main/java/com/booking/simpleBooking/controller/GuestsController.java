package com.booking.simpleBooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.booking.simpleBooking.model.Guests;
import com.booking.simpleBooking.repository.GuestsRepository;

@RestController
@RequestMapping("/api/guests")
public class GuestsController {
    @Autowired
    private GuestsRepository guestsRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addGuest(@RequestBody Guests guests) {
        guestsRepository.save(guests);
        return ResponseEntity.status(HttpStatus.CREATED).body("Guest Recorded");

    }
} 
