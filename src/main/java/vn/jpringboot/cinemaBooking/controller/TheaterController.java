package vn.jpringboot.cinemaBooking.controller;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.jpringboot.cinemaBooking.dto.request.TheaterRequestDTO;
import vn.jpringboot.cinemaBooking.dto.response.ResponseData;
import vn.jpringboot.cinemaBooking.dto.response.ResponseError;
import vn.jpringboot.cinemaBooking.service.TheaterService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping({ "/theater" })
@RequiredArgsConstructor
@Slf4j
@Validated
public class TheaterController {
    private final TheaterService theaterService;

    @Operation(summary = "Create a new theater", description = "This endpoint allows you to create a new theater entry in the system.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseData<?> addTheater(@Valid @RequestBody TheaterRequestDTO theater) {
        try {
            log.info("Creating theater: {}", theater);
            Long theaterId = theaterService.saveTheater(theater);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Theater created successfully", theaterId);
        } catch (Exception e) {
            log.error("Error creating theater: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error creating theater");
        }
    }

    @Operation(summary = "Update an existing theater", description = "This endpoint allows you to update an existing theater entry in the system.")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{theaterId}")
    public ResponseData<?> updateTheater(@PathVariable @Min(1) Long theaterId,
            @Valid @RequestBody TheaterRequestDTO theater) {
        try {
            log.info("Updating theater with ID: {}", theaterId);
            theaterService.updateTheater(theaterId, theater);
            return new ResponseData<>(HttpStatus.OK.value(), "Theater updated successfully");
        } catch (Exception e) {
            log.error("Error updating theater: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error updating theater");
        }
    }

    @Operation(summary = "Delete a theater", description = "This endpoint allows you to delete a theater entry from the system.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{theaterId}")
    public ResponseData<?> deleteTheater(@PathVariable @Min(1) Long theaterId) {
        try {
            log.info("Deleting theater with ID: {}", theaterId);
            theaterService.deleteTheater(theaterId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Theater deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting theater: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error deleting theater");
        }
    }

    @Operation(summary = "Get theater information", description = "This endpoint allows you to retrieve information about a specific theater.")
    @GetMapping("/{theaterId}")
    public ResponseData<?> getTheater(@PathVariable @Min(1) Long theaterId) {
        try {
            log.info("Retrieving theater with ID: {}", theaterId);
            return new ResponseData<>(HttpStatus.OK.value(), "Theater: ", theaterService.getTheater(theaterId));
        } catch (Exception e) {
            log.error("Error retrieving theater: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error retrieving theater");
        }
    }

    @Operation(summary = "Get all theaters", description = "This endpoint retrieves a paginated list of all theaters in the system.")
    @GetMapping("/list")
    public ResponseData<?> getAllTheaters(@RequestParam(defaultValue = "0", required = false) @Min(0) Integer page,
            @RequestParam(defaultValue = "10", required = false) @Min(10) Integer size,
            @RequestParam(defaultValue = "", required = false) String... sortsBy) {
        log.info("Retrieving all theaters");
        return new ResponseData<>(HttpStatus.OK.value(), "List theaters: ",
                theaterService.getAllTheaters(page, size, sortsBy));
    }
}