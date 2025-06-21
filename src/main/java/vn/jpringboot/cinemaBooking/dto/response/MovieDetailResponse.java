package vn.jpringboot.cinemaBooking.dto.response;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MovieDetailResponse implements Serializable {
    private Long movieId;
    private String movieTitle;
    private String movieDescription;
    private String movieGenre;
    private Integer movieDuration;
    private String movieLanguage;
    private Double baseShowtimePrice;
    private String moviePosterUrl;
}
