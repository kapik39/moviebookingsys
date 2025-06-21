package vn.jpringboot.cinemaBooking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.jpringboot.cinemaBooking.dto.request.ScreenRequestDTO;
import vn.jpringboot.cinemaBooking.dto.request.SeatDTO;
import vn.jpringboot.cinemaBooking.dto.response.ResponseData;
import vn.jpringboot.cinemaBooking.dto.response.ResponseError;
import vn.jpringboot.cinemaBooking.service.ScreenService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping({ "/screen" })
@Slf4j
@Validated
@RequiredArgsConstructor
public class ScreensController {
    private final ScreenService screenService;

    @Operation(summary = "Create a new screen", description = "This endpoint allows you to add a new screen to the system.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/")
    public ResponseData<?> addScreen(@Valid @RequestBody ScreenRequestDTO screen) {
        try {
            log.info("Creating screen: {}", screen);
            Long screenId = screenService.saveScreen(screen);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Screen creating successfully", screenId);
        } catch (Exception e) {
            log.error("Error creating screen: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error creating screen");
        }
    }

    @Operation(summary = "Create a new seat", description = "This endpoint allows you to create a new seat for an existing screen.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/{screenId}/seat/")
    public ResponseData<?> addSeat(@PathVariable @Min(1) Long screenId, @Valid @RequestBody SeatDTO seat) {
        try {
            log.info("Creating seat for screenId: {}", screenId);
            screenService.addSeat(screenId, seat);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Seat created successfully for screendId", screenId);
        } catch (Exception e) {
            log.error("Error creating seat: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error creating seat");
        }
    }

    @Operation(summary = "Update an existing screen", description = "This endpoint allows you to update an existing screen in the system.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{screenId}")
    public ResponseData<?> updateScreen(@PathVariable @Min(1) Long screenId,
            @Valid @RequestBody ScreenRequestDTO screen) {
        try {
            log.info("Updating screen with ID: {}", screenId);
            screenService.updateScreen(screenId, screen);
            return new ResponseData<>(HttpStatus.OK.value(), "Screen updated successfully");
        } catch (Exception e) {
            log.error("Error updating screen: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error updating screen");
        }
    }

    @Operation(summary = "Update an existing seat", description = "This endpoint allows you to update an existing seat for an exiting screen.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{screenId}/seat/{seatId}")
    public ResponseData<?> updateSeat(@Min(1) @PathVariable Long screenId,
            @PathVariable @Min(1) Long seatId,
            @Valid @RequestBody SeatDTO seat) {
        try {
            log.info("Updating seat for screenId: {}", screenId);
            screenService.updateSeat(screenId, seatId, seat);
            return new ResponseData<>(HttpStatus.OK.value(), "Seat updated successfully");
        } catch (Exception e) {
            log.error("Error updating seat: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), "Error updating Seat");
        }
    }

    @Operation(summary = "Delete a screen", description = "This endpoint allows you to delete a screen from the system.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{screenId}/theater/{theaterId}")
    public ResponseData<?> deleteScreen(@PathVariable @Min(1) Long screenId, @PathVariable @Min(1) Long theaterId) {
        try {
            log.info("Deleting screen with ID: {}", screenId);
            screenService.deleteScreen(screenId, theaterId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Screen deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting screen: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error deleting screen");
        }
    }

    @Operation(summary = "Delete a seat", description = "This endpoint allows you to delete a seat from a screen.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{screenId}/seat{seatId}")
    public ResponseData<?> deleteSeat(@PathVariable @Min(1) Long screenId, @PathVariable @Min(1) Long seatId) {
        try {
            log.info("Deleting seat with ID: {}", seatId);
            screenService.deleteSeat(screenId, seatId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Seat deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting seat: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), "Error deleting seat");
        }
    }

    @Operation(summary = "Get screen details", description = "This endpoint allows you to retrieve details of a specific screen by screenId.")
    @GetMapping("/{screenId}")
    public ResponseData<?> getScreen(@PathVariable @Min(1) Long screenId) {
        try {
            log.info("Retrieving screen with ID: {}", screenId);
            return new ResponseData<>(HttpStatus.OK.value(), "Screen retrieved successfully",
                    screenService.getScreen(screenId));
        } catch (Exception e) {
            log.error("Error retrieving screen: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error retrieving screen");
        }
    }

    @Operation(summary = "Get all screens", description = "This endpoint allows you to retrieve a paginated list of all screens in the system.")
    @GetMapping("/list/theater/{theaterId}")
    public ResponseData<?> getAllScreens(@PathVariable @Min(1) Long theaterId,
            @RequestParam(defaultValue = "0", required = false) @Min(0) int page,
            @RequestParam(defaultValue = "10", required = false) @Min(10) int size,
            @RequestParam(defaultValue = "", required = false) String... sortsBy) {
        log.info("Retrieving all screens:");
        return new ResponseData<>(HttpStatus.OK.value(), "Screens retrieved successfully",
                screenService.getAllScreensByTheaterId(theaterId, page, size, sortsBy));
    }
}
