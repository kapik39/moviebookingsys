package vn.jpringboot.cinemaBooking.service;

import vn.jpringboot.cinemaBooking.dto.request.BookingRequestDTO;
import vn.jpringboot.cinemaBooking.dto.response.PageResponse;

public interface BookingService {
    Long createBooking(BookingRequestDTO request);

    void deleteBooking(Long bookingId);

    PageResponse<?> getBookingByUser(Long userId, int pageNo, int pageSize, String... sortsBy);
}
