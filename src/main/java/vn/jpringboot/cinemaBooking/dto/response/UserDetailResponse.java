package vn.jpringboot.cinemaBooking.dto.response;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import vn.jpringboot.cinemaBooking.dto.request.AddressDTO;
import vn.jpringboot.cinemaBooking.util.Gender;

@Getter
@Builder
public class UserDetailResponse implements Serializable {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Date dateOfBirth;
    private Gender gender;
    private String username;
    private Set<AddressDTO> addresses;
}
