package vn.jpringboot.cinemaBooking.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tbl_movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "movie_title")
    private String movieTitle;

    @Column(name = "movie_description")
    private String movieDescription;

    @Column(name = "movie_duration")
    private Integer movieDuration;

    @Column(name = "movie_genre")
    private String movieGenre;

    @Column(name = "movie_language")
    private String movieLanguage;

    @Column(name = "base_showtime_price")
    private Double baseShowtimePrice;

    @Column(name = "movie_image_url")
    private String movieImageUrl;
}
