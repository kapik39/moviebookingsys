package vn.jpringboot.cinemaBooking.dto.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import vn.jpringboot.cinemaBooking.util.Gender;
import vn.jpringboot.cinemaBooking.util.GenderSubset;

@Getter
@Setter
public class RegisterRequestDTO {
    @NotBlank(message = "firstName must be not blank")
    private String firstName;

    @NotNull(message = "lastName must be not null")
    private String lastName;

    @NotNull(message = "dateOfBirth must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date dateOfBirth;

    @NotNull(message = "gender must be not null")
    @GenderSubset(anyOf = { Gender.MALE, Gender.FEMALE, Gender.OTHER })
    private Gender gender;

    @NotNull(message = "email must be not null")
    @Email(message = "email invalid format")
    @Size(max = 50)
    private String email;

    @NotNull(message = "username must be not null")
    @Size(min = 3, max = 20)
    private String username;

    @NotNull(message = "password must be not null")
    @Size(min = 6, max = 30)
    private String password;
}
