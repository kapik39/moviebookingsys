package vn.jpringboot.cinemaBooking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.jpringboot.cinemaBooking.dto.request.MoviePatchDTO;
import vn.jpringboot.cinemaBooking.dto.request.MovieRequestDTO;
import vn.jpringboot.cinemaBooking.dto.request.MovieSearch;
import vn.jpringboot.cinemaBooking.dto.response.ResponseData;
import vn.jpringboot.cinemaBooking.dto.response.ResponseError;
import vn.jpringboot.cinemaBooking.model.Movie;
import vn.jpringboot.cinemaBooking.service.MovieService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping({ "/movie" })
@Validated
@RequiredArgsConstructor
@Slf4j
public class MovieController {
    private final MovieService movieService;

    @Operation(summary = "Create a new movie", description = "This endpoint allows you to create a new movie entry in the system.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseData<?> addMovie(@Valid @RequestBody MovieRequestDTO movie) {
        try {
            log.info("Creating movie: {}", movie);
            Long movieId = movieService.saveMovie(movie);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Movie created successfully", movieId);
        } catch (Exception e) {
            log.error("Error creating movie: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error creating movie");
        }
    }

    @Operation(summary = "Update an existing movie", description = "This endpoint allows you to update an existing movie entry in the system.")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{movieId}")
    public ResponseData<?> updateMovie(@PathVariable @Min(1) Long movieId, @Valid @RequestBody MovieRequestDTO movie) {
        try {
            log.info("Updating movie: {}", movie);
            movieService.updateMovie(movieId, movie);
            return new ResponseData<>(HttpStatus.OK.value(), "Movie updated successfully");
        } catch (Exception e) {
            log.error("Error updating movie: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error updating movie");
        }
    }

    @Operation(summary = "Update a patch of an existing movie", description = "This endpoint allows you to update a patch of an existing movie entry in the system.")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{movieId}")
    public ResponseData<?> patchMovie(@PathVariable @Min(1) Long movieId, @Valid @RequestBody MoviePatchDTO movie) {
        try {
            log.info("Patching movie with ID: {}", movieId);
            movieService.updatePatchMovie(movieId, movie);
            return new ResponseData<>(HttpStatus.OK.value(), "Movie patched successfully");
        } catch (Exception e) {
            log.error("Error patching movie: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error patching movie");
        }
    }

    @Operation(summary = "Delete a movie", description = "This endpoint allows you to delete a movie entry from the system.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{movieId}")
    public ResponseData<?> deleteMovie(@PathVariable @Min(1) Long movieId) {
        try {
            log.info("Deleting movie with ID: {}", movieId);
            movieService.deleteMovie(movieId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Movie deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting movie: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error deleting movie");
        }
    }

    @Operation(summary = "Get movie details", description = "This endpoint retrieves the details of a specific movie by movieId.")
    @GetMapping("/{movieId}")
    public ResponseData<?> getMovie(@PathVariable @Min(1) Long movieId) {
        try {
            log.info("Retrieving movie with ID: {}", movieId);
            return new ResponseData<>(HttpStatus.OK.value(), "Movie: ",
                    movieService.getMovie(movieId));
        } catch (Exception e) {
            log.error("Error retrieving movie: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), "Error retrieving movie");
        }
    }

    @Operation(summary = "Get all movies", description = "This endpoint retrieves a paginated list of all movies in the system.")
    @GetMapping("/list")
    public ResponseData<?> getAllMovies(@RequestParam(defaultValue = "0", required = false) @Min(0) Integer pageNo,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(defaultValue = "", required = false) String... sortsBy) {
        log.info("Retrieving all movies");
        return new ResponseData<>(HttpStatus.OK.value(), "List movies: ",
                movieService.getAllMovies(pageNo, pageSize, sortsBy));
    }

    @Operation(summary = "Search movies", description = "This endpoint retrieves a list of movies being searched in the system.")
    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMovie(@ModelAttribute MovieSearch search) {
        List<Movie> movies = movieService.searchMovie(search);
        if (movies.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

}
