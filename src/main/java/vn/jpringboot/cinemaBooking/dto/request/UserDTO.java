package vn.jpringboot.cinemaBooking.dto.request;

import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import vn.jpringboot.cinemaBooking.util.Gender;
import vn.jpringboot.cinemaBooking.util.GenderSubset;
import vn.jpringboot.cinemaBooking.util.PhoneNumber;

@Getter
public class UserDTO {
    @NotBlank(message = "firstName must be not blank")
    private String firstName;
    @NotNull(message = "lastname must be not null")
    private String lastName;

    @Email(message = "email invalid format")
    private String email;

    @PhoneNumber(message = "phone invalid format")
    private String phoneNumber;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date dateOfBirth;

    @GenderSubset(anyOf = { Gender.MALE, Gender.FEMALE, Gender.OTHER })
    private Gender gender;

    private Set<AddressDTO> addresses;
}
