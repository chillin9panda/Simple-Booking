package com.booking.simpleBooking.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.booking.simpleBooking.views.BookingViewModel;

@Repository
public class BookingViewRepository {
    private final JdbcTemplate jdbcTemplate;

    public BookingViewRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<BookingViewModel> getAllBookingViews() {
        String sql = "SELECT * FROM booking_view";

        return jdbcTemplate.query(sql, new RowMapper<BookingViewModel>() {
            @Override
            public BookingViewModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new BookingViewModel(
                        rs.getInt("booking_id"),
                        rs.getString("first_name"),
                        rs.getString("phone_num"),
                        rs.getString("room_number"),
                        rs.getString("status"));
            }
        });
    }
}