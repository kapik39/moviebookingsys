package vn.jpringboot.cinemaBooking.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String username;
    private String email;
    private List<String> roles;
}
