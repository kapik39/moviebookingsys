package vn.jpringboot.cinemaBooking.service.imlp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vn.jpringboot.cinemaBooking.model.AppUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.jpringboot.cinemaBooking.dto.request.BookingRequestDTO;
import vn.jpringboot.cinemaBooking.dto.request.SeatDTO;
import vn.jpringboot.cinemaBooking.dto.response.BookignDetailResponse;
import vn.jpringboot.cinemaBooking.dto.response.PageResponse;
import vn.jpringboot.cinemaBooking.model.Booking;
import vn.jpringboot.cinemaBooking.model.Seat;
import vn.jpringboot.cinemaBooking.model.Showtime;
import vn.jpringboot.cinemaBooking.repository.BookingRepository;
import vn.jpringboot.cinemaBooking.repository.SeatRepository;
import vn.jpringboot.cinemaBooking.service.BookingService;
import vn.jpringboot.cinemaBooking.service.ShowtimeService;
import vn.jpringboot.cinemaBooking.service.UserService;
import vn.jpringboot.cinemaBooking.util.BookingStatus;
import vn.jpringboot.cinemaBooking.util.PageableUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ShowtimeService showtimeService;
    private final SeatRepository seatRepository;

    @Override
    public Long createBooking(BookingRequestDTO request) {
        Showtime showtime = showtimeService.getShowtimeById(request.getShowtimeId());
        AppUser user = userService.getUserById(request.getUserId());
        Set<Seat> selectSeats = new HashSet<>();
        double totalAmount = 0;

        for (Long seatId : request.getSeatsId()) {
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("Seat not found: " + seatId));
            if (!seat.getIsAvailable()) {
                throw new RuntimeException("Seat " + seat.getSeatRow() + seat.getSeatNumber() + " is not available.");
            }
            seat.setIsAvailable(false);
            seatRepository.save(seat);
            selectSeats.add(seat);
            totalAmount += showtime.calculateSeatPrice(seat);
        }

        Booking booking = Booking.builder()
                .showtime(showtime)
                .user(user)
                .seats(selectSeats)
                .numberOfTickets(request.getSeatsId().size())
                .totalAmount(totalAmount)
                .bookingTime(LocalDateTime.now())
                .status(BookingStatus.PENDING) // Can be changed after payment
                .build();

        bookingRepository.save(booking);
        log.info("Booking saved successfully with ID: {}", booking.getBookingId());
        return booking.getBookingId();
    }

    @Override
    public void deleteBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
        log.info("Booking deleted successfully with ID: {}", bookingId);
    }

    @Override
    public PageResponse<?> getBookingByUser(Long userId, int pageNo, int pageSize, String... sortsBy) {
        Pageable pageable = PageableUtil.buildPageable(pageNo, pageSize, sortsBy);

        Page<Booking> bookingPage = bookingRepository.findByUser_UserId(userId, pageable);

        log.info("Total booking found: ");
        List<BookignDetailResponse> bookingDetails = bookingPage.stream()
                .map(booking -> BookignDetailResponse.builder()
                        .bookingId(booking.getBookingId())
                        .userId(booking.getUser().getUserId())
                        .showtimeId(booking.getShowtime().getShowtimeId())
                        .bookingTime(booking.getBookingTime())
                        .numberOfTickets(booking.getNumberOfTickets())
                        .totalAmout(booking.getTotalAmount())
                        .seats(convertToSeat(booking.getSeats()))
                        .build())
                .toList();
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(bookingPage.getTotalPages())
                .items(bookingDetails)
                .build();
    }

    private Set<SeatDTO> convertToSeat(Set<Seat> seat) {
        Set<SeatDTO> result = new HashSet<>();
        seat.forEach(a -> result.add(SeatDTO.builder()
                .seatNumber(a.getSeatNumber())
                .seatRow(a.getSeatRow())
                .seatType(a.getSeatType())
                .build()));
        return result;
    }
}
