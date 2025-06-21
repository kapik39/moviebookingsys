package vn.jpringboot.cinemaBooking.dto.request;

import lombok.Getter;

@Getter
public class MoviePatchDTO {
    private String movieTitle;
    private String movieDescription;
    private String movieGenre;
    private Integer movieDuration;
    private String movieLanguage;
    private Double baseShowtimePrice;
    private String moviePosterUrl;
}
