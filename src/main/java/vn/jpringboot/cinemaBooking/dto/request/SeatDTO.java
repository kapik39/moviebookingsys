package vn.jpringboot.cinemaBooking.dto.request;

import lombok.Builder;
import lombok.Getter;
import vn.jpringboot.cinemaBooking.util.SeatType;

@Getter
@Builder
public class SeatDTO {
    private String seatRow;
    private Integer seatNumber;
    private SeatType seatType;
}
