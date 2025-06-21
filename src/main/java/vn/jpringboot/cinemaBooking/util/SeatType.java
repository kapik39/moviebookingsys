package vn.jpringboot.cinemaBooking.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SeatType {
    @JsonProperty("vip")
    VIP,
    @JsonProperty("regular")
    REGULAR,
    @JsonProperty("couple")
    COUPLE;
}
