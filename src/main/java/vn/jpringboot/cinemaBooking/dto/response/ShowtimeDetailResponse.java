package vn.jpringboot.cinemaBooking.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShowtimeDetailResponse implements Serializable {
    private Long showtimeId;
    private LocalDateTime showtime;
    private Double baseShowtimePrice;
    private Long screenId;
    private Long movieId;
}
