package vn.jpringboot.cinemaBooking.dto.response;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TheaterDetailResponse implements Serializable {
    private Long theaterId;
    private String theaterName;
    private String theaterLocation;
    private Integer numberOfScreens;
}
