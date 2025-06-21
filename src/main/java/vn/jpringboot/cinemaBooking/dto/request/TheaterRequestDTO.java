package vn.jpringboot.cinemaBooking.dto.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TheaterRequestDTO implements Serializable {
    @NotBlank(message = "theaterName must be not blank")
    private String theaterName;

    @NotBlank(message = "theaterLocation must be not blank")
    private String theaterLocation;
}
