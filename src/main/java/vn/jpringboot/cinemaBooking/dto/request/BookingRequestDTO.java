package vn.jpringboot.cinemaBooking.dto.request;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequestDTO {
    private Long userId;
    @NotNull(message = "showtimeId must be not null")
    private Long showtimeId;
    @NotEmpty(message = "seatId must be not empty")
    private Set<Long> seatsId;
}
