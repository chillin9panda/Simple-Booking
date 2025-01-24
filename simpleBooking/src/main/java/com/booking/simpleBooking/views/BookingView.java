package com.booking.simpleBooking.views;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;

@Component
public class BookingView {
  @Autowired
  private DataSource dataSource;

  @PostConstruct
  public void createBookingView() {
    try (Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement()) {
      String createViewQuery = "CREATE OR REPLACE VIEW booking_view AS " +
          "SELECT b.booking_id, g.first_name, g.phone_num, b.room_number, " +
          "CASE WHEN b.is_active = 1 THEN 'Active' ELSE 'Inactive' END AS status " +
          "FROM booking b " +
          "JOIN guests g ON b.phone_num = g.phone_num";

      statement.executeUpdate(createViewQuery);
    } catch (SQLException e) {
      System.err.println("Error Creating View: " + e.getMessage());
    }
  }
}
