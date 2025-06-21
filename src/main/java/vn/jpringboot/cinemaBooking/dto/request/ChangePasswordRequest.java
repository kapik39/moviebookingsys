package vn.jpringboot.cinemaBooking.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    @NotNull(message = "old password must be not null")
    private String oldPassword;
    @NotNull(message = "new password must be not null")
    @Size(min = 6, max = 30, message = "new password must be between 6 to 30 characters")
    private String newPassword;

    @NotNull(message = "confirm password must be not null")
    @Size(min = 6, max = 30, message = "confirm password must be between 6 to 30 characters")
    private String confirmPassword;
}
