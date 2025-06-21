package vn.jpringboot.cinemaBooking.dto.request;

import java.io.Serializable;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ScreenRequestDTO implements Serializable {
    @NotBlank(message = "screenName must be not blank")
    private String screenName;

    @NotNull(message = "theaterId must be not null")
    private Long theaterId;

    private Set<SeatDTO> seats;
}
