package vn.jpringboot.cinemaBooking.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressDTO {
    private String apartmentNumber;
    private String floor;
    private String building;
    private String streetNumber;
    private String street;
    private String city;
    private String country;
}