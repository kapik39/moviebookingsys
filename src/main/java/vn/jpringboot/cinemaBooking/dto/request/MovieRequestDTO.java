package vn.jpringboot.cinemaBooking.dto.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MovieRequestDTO implements Serializable {
    @NotBlank(message = "movieTitle must be not blank")
    private String movieTitle;

    @NotNull(message = "movieDescription must be not null")
    private String movieDescription;

    @NotBlank(message = "movieGenre must be not blank")
    private String movieGenre;

    @NotNull(message = "movieDuration must be not null")
    private Integer movieDuration;

    @NotBlank(message = "movieLanguage must be not blank")
    private String movieLanguage;

    @NotNull(message = "baseShowtimePrice must be not null")
    private Double baseShowtimePrice;

    @NotBlank(message = "moviePosterUrl must be not blank")
    private String moviePosterUrl;
}
