package vn.jpringboot.cinemaBooking.service.imlp;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.jpringboot.cinemaBooking.dto.request.MoviePatchDTO;
import vn.jpringboot.cinemaBooking.dto.request.MovieRequestDTO;
import vn.jpringboot.cinemaBooking.dto.request.MovieSearch;
import vn.jpringboot.cinemaBooking.dto.response.MovieDetailResponse;
import vn.jpringboot.cinemaBooking.dto.response.PageResponse;
import vn.jpringboot.cinemaBooking.exception.ResourceNotFoundException;
import vn.jpringboot.cinemaBooking.model.Movie;
import vn.jpringboot.cinemaBooking.repository.MovieRepository;
import vn.jpringboot.cinemaBooking.repository.MovieSpecification;
import vn.jpringboot.cinemaBooking.service.MovieService;
import vn.jpringboot.cinemaBooking.util.PageableUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    @Override
    public Long saveMovie(MovieRequestDTO request) {
        Movie movie = Movie.builder()
                .movieTitle(request.getMovieTitle())
                .movieDescription(request.getMovieDescription())
                .movieGenre(request.getMovieGenre())
                .movieDuration(request.getMovieDuration())
                .movieLanguage(request.getMovieLanguage())
                .baseShowtimePrice(request.getBaseShowtimePrice())
                .movieImageUrl(request.getMoviePosterUrl())
                .build();
        movieRepository.save(movie);
        log.info("Movie saved successfully with ID: {}", movie.getMovieId());
        return movie.getMovieId();
    }

    @Override
    public void updateMovie(Long movieId, MovieRequestDTO request) {
        Movie movie = getMovieById(movieId);
        movie.setMovieTitle(request.getMovieTitle());
        movie.setMovieDescription(request.getMovieDescription());
        movie.setMovieGenre(request.getMovieGenre());
        movie.setMovieDuration(request.getMovieDuration());
        movie.setMovieLanguage(request.getMovieLanguage());
        movie.setBaseShowtimePrice(request.getBaseShowtimePrice());
        movie.setMovieImageUrl(request.getMoviePosterUrl());
        movieRepository.save(movie);
        log.info("Movie updated successfully with ID: {}", movieId);
    }

    @Override
    public void updatePatchMovie(Long movieId, MoviePatchDTO request) {
        Movie movie = getMovieById(movieId);
        if (request.getMovieTitle() != null) {
            movie.setMovieTitle(request.getMovieTitle());
        }
        if (request.getMovieDescription() != null) {
            movie.setMovieDescription(request.getMovieDescription());
        }
        if (request.getMovieGenre() != null) {
            movie.setMovieGenre(request.getMovieGenre());
        }
        if (request.getMovieDuration() != null) {
            movie.setMovieDuration(request.getMovieDuration());
        }
        if (request.getMovieLanguage() != null) {
            movie.setMovieLanguage(request.getMovieLanguage());
        }
        if (request.getBaseShowtimePrice() != null) {
            movie.setBaseShowtimePrice(request.getBaseShowtimePrice());
        }
        if (request.getMoviePosterUrl() != null) {
            movie.setMovieImageUrl(request.getMoviePosterUrl());
        }
        movieRepository.save(movie);
        log.info("Movie updated successfully with ID: {}", movieId);
    }

    @Override
    public void deleteMovie(Long movieId) {
        movieRepository.deleteById(movieId);
        log.info("Movie deleted successfully with ID: {}", movieId);
    }

    @Override
    public MovieDetailResponse getMovie(Long movieId) {
        Movie movie = getMovieById(movieId);
        return MovieDetailResponse.builder()
                .movieId(movie.getMovieId())
                .movieTitle(movie.getMovieTitle())
                .movieDescription(movie.getMovieDescription())
                .movieGenre(movie.getMovieGenre())
                .movieDuration(movie.getMovieDuration())
                .movieLanguage(movie.getMovieLanguage())
                .baseShowtimePrice(movie.getBaseShowtimePrice())
                .moviePosterUrl(movie.getMovieImageUrl())
                .build();
    }

    @Override
    public PageResponse<?> getAllMovies(int pageNo, int pageSize, String... sortsBy) {
        Pageable pageable = PageableUtil.buildPageable(pageNo, pageSize, sortsBy);

        Page<Movie> movies = movieRepository.findAll(pageable);

        log.info("Total movies found:");
        List<MovieDetailResponse> response = movies.stream()
                .map(movie -> MovieDetailResponse.builder()
                        .movieId(movie.getMovieId())
                        .movieTitle(movie.getMovieTitle())
                        .movieDescription(movie.getMovieDescription())
                        .movieGenre(movie.getMovieGenre())
                        .movieDuration(movie.getMovieDuration())
                        .movieLanguage(movie.getMovieLanguage())
                        .baseShowtimePrice(movie.getBaseShowtimePrice())
                        .moviePosterUrl(movie.getMovieImageUrl())
                        .build())
                .toList();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(movies.getTotalPages())
                .items(response)
                .build();
    }

    @Override
    public Movie getMovieById(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not foundwith ID: " + movieId));
    }

    @Override
    public List<Movie> searchMovie(MovieSearch search) {
        MovieSpecification spec = new MovieSpecification(search);
        return movieRepository.findAll(spec);
    }
}
