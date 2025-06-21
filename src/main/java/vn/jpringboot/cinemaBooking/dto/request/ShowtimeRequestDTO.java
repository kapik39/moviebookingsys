package vn.jpringboot.cinemaBooking.dto.request;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ShowtimeRequestDTO implements Serializable {
    @NotNull(message = "Showtime must be not null")
    private LocalDateTime showtime;

    @NotNull(message = "ScreenId must be not null")
    private Long screenId;

    @NotNull(message = "MovieId must be not null")
    private Long movieId;
}
