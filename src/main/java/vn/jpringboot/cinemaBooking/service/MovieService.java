package vn.jpringboot.cinemaBooking.service;

import java.util.List;

import vn.jpringboot.cinemaBooking.dto.request.MoviePatchDTO;
import vn.jpringboot.cinemaBooking.dto.request.MovieRequestDTO;
import vn.jpringboot.cinemaBooking.dto.request.MovieSearch;
import vn.jpringboot.cinemaBooking.dto.response.MovieDetailResponse;
import vn.jpringboot.cinemaBooking.dto.response.PageResponse;
import vn.jpringboot.cinemaBooking.model.Movie;

public interface MovieService {

    Long saveMovie(MovieRequestDTO request);

    void updateMovie(Long movieId, MovieRequestDTO request);

    void updatePatchMovie(Long movieId, MoviePatchDTO request);

    void deleteMovie(Long movieId);

    MovieDetailResponse getMovie(Long movieId);

    PageResponse<?> getAllMovies(int pageNo, int pageSize, String... sortsBy);

    Movie getMovieById(Long movieId);

    List<Movie> searchMovie(MovieSearch search);
}
