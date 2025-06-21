package vn.jpringboot.cinemaBooking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.jpringboot.cinemaBooking.dto.request.ShowtimeRequestDTO;
import vn.jpringboot.cinemaBooking.dto.response.ResponseData;
import vn.jpringboot.cinemaBooking.dto.response.ResponseError;
import vn.jpringboot.cinemaBooking.service.ShowtimeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping({ "/showtime" })
@Slf4j
@Validated
@RequiredArgsConstructor
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    @Operation(summary = "Create a new showtime", description = "This endpoint allows you to add a new showtime to the system.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/")
    public ResponseData<?> addShowtime(@Valid @RequestBody ShowtimeRequestDTO showtime) {
        try {
            log.info("Adding showtime: {}", showtime);
            Long showtimeId = showtimeService.saveShowtime(showtime);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Showtime added successfully", showtimeId);
        } catch (Exception e) {
            log.error("Error adding showtime", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error adding showtime");
        }
    }

    @Operation(summary = "Update an existing showtime", description = "This endpoint allows you to update an existing showtime for an exiting screen.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{showtimeId}")
    public ResponseData<?> updateShowTime(@PathVariable @Min(1) Long showtimeId,
            @Valid @RequestBody ShowtimeRequestDTO showtime) {
        try {
            log.info("Updating showtime with ID: {}", showtimeId);
            showtimeService.updateShowtime(showtimeId, showtime);
            return new ResponseData<>(HttpStatus.OK.value(), "Showtime updated successfully");
        } catch (Exception e) {
            log.error("Error updating showtime: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error updating showtime");
        }
    }

    @Operation(summary = "Delete a showtime", description = "This endpoint allows you to delete a showtime from the system.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{showtimeId}")
    public ResponseData<?> deleteShowtime(@PathVariable @Min(1) Long showtimeId) {
        try {
            log.info("Deleting showtime with ID: {}", showtimeId);
            showtimeService.deleteShowtime(showtimeId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Showtime deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting showtime: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error deleting showtime");
        }
    }

    @Operation(summary = "Get showtime details", description = "This endpoint allows you to retrieve details of a specific screen by showtimeId.")
    @GetMapping("/{showtimeId}")
    public ResponseData<?> getShowTime(@PathVariable @Min(1) Long showtimeId) {
        try {
            log.info("Retrieving showtime with ID: {}", showtimeId);
            return new ResponseData<>(HttpStatus.OK.value(), "Showtime: ",
                    showtimeService.getShowtime(showtimeId));
        } catch (Exception e) {
            log.error("Error retrieving showtime: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error retrieving showtime");
        }
    }

    @Operation(summary = "Get all showtimes by screen", description = "This endpoint retrieves a paginated list of all showtimes of a screen in the system.")
    @GetMapping("/listbyscreen/{screenId}")
    public ResponseData<?> getAllShowtimesByScreenId(@PathVariable @Min(1) Long screenId,
            @RequestParam(defaultValue = "0", required = false) @Min(0) Integer page,
            @RequestParam(defaultValue = "10", required = false) @Min(10) Integer size,
            @RequestParam(defaultValue = "", required = false) String... sortsBy) {
        log.info("Retrieving all showtime by screenId");
        return new ResponseData<>(HttpStatus.OK.value(), "List showtime: ",
                showtimeService.getAllShowtimesByScreenId(screenId, page, size, sortsBy));
    }

    @Operation(summary = "Get all showtimes by movie", description = "This endpoint retrieves a paginated list of all showtimes of a movie in the system.")
    @GetMapping("/listbymoive/{movieId}")
    public ResponseData<?> getAllShowtimesByMovieId(@PathVariable @Min(1) Long movieId,
            @RequestParam(defaultValue = "0", required = false) @Min(0) Integer page,
            @RequestParam(defaultValue = "10", required = false) @Min(10) Integer size,
            @RequestParam(defaultValue = "", required = false) String... sortsBy) {
        log.info("Retrieving all showtime by movieId");
        return new ResponseData<>(HttpStatus.OK.value(), "List showtime: ",
                showtimeService.getAllShowtimesByMovieId(movieId, page, size, sortsBy));
    }
}
