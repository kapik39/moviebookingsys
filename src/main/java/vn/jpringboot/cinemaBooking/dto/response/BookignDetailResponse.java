package vn.jpringboot.cinemaBooking.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import vn.jpringboot.cinemaBooking.dto.request.SeatDTO;

@Getter
@Builder
public class BookignDetailResponse implements Serializable {
    private Long bookingId;
    private Long showtimeId;
    private Long userId;
    private LocalDateTime bookingTime;
    private Integer numberOfTickets;
    private Double totalAmout;
    private Set<SeatDTO> seats;
}
