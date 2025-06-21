package vn.jpringboot.cinemaBooking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.jpringboot.cinemaBooking.dto.request.BookingRequestDTO;
import vn.jpringboot.cinemaBooking.dto.response.ResponseData;
import vn.jpringboot.cinemaBooking.dto.response.ResponseError;
import vn.jpringboot.cinemaBooking.security.UserDetailsImpl;
import vn.jpringboot.cinemaBooking.service.BookingService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping({ "/booking" })
@Slf4j
@Validated
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @Operation(summary = "Create a new booking", description = "This endpoint allows you to create a new booking entry in the system.")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    @PostMapping("/")
    public ResponseData<?> createBooking(@Valid @RequestBody BookingRequestDTO booking, Authentication authentication) {
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"))) {
            if (booking.getUserId() != null && !booking.getUserId().equals(currentUser.getUserId())) {
                log.warn("Warning: Trying to create a booking for anorther userId");
            }
            booking.setUserId(currentUser.getUserId());
        }

        try {
            log.info("Creating a booking: {}", booking);
            Long bookingId = bookingService.createBooking(booking);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Booking created successfully", bookingId);
        } catch (Exception e) {
            log.error("Error creating booking", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error creating booking");
        }
    }

    @Operation(summary = "Delete a booking", description = "This endpoint allows you to delete a booking entry from the system.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{bookingId}")
    public ResponseData<?> deleteBooking(@PathVariable @Min(1) Long bookingId) {
        try {
            log.info("Deleting booking with ID: {}", bookingId);
            bookingService.deleteBooking(bookingId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Booking deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting booking: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error deleting booking");
        }
    }

    @Operation(summary = "Get all booking of a user", description = "This endpoint retrieves a paginated list of all bookings of a user in the system.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or #userId == authentication.principal.userId")
    @GetMapping("/list/user/{userId}")
    public ResponseData<?> getBookingByUser(@PathVariable @Min(1) Long userId,
            @RequestParam(defaultValue = "0", required = false) @Min(0) Integer page,
            @RequestParam(defaultValue = "10", required = false) @Min(10) Integer size,
            @RequestParam(defaultValue = "", required = false) String... sortsBy) {
        log.info("Retrieving all bookings");
        return new ResponseData<>(HttpStatus.OK.value(), "List booking: ",
                bookingService.getBookingByUser(userId, page, size, sortsBy));
    }

    @Operation(summary = "Get all booking of user", description = "This endpoint retrieves a paginated list of all bookings of current user in the system.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mylist")
    public ResponseData<?> getMyBooking(Authentication authentication,
            @RequestParam(defaultValue = "0", required = false) @Min(0) Integer page,
            @RequestParam(defaultValue = "10", required = false) @Min(10) Integer size,
            @RequestParam(defaultValue = "", required = false) String... sortsBy) {
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        log.info("Retrieving all bookings");
        return new ResponseData<>(HttpStatus.OK.value(), "List booking: ",
                bookingService.getBookingByUser(currentUser.getUserId(), page, size, sortsBy));
    }
}
