package vn.jpringboot.cinemaBooking.dto.response;

import java.io.Serializable;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import vn.jpringboot.cinemaBooking.dto.request.SeatDTO;

@Getter
@Builder
public class ScreenDetailResponse implements Serializable {
    private Long screenId;
    private Long theaterId;
    private String screenName;
    private Integer screenCapacity;
    private Set<SeatDTO> seats;
}
