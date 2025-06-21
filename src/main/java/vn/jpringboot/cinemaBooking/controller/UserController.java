package vn.jpringboot.cinemaBooking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.jpringboot.cinemaBooking.dto.request.AddressDTO;
import vn.jpringboot.cinemaBooking.dto.request.ChangePasswordRequest;
import vn.jpringboot.cinemaBooking.dto.request.UserDTO;
import vn.jpringboot.cinemaBooking.dto.request.UserRequestDTO;
import vn.jpringboot.cinemaBooking.dto.response.ResponseData;
import vn.jpringboot.cinemaBooking.dto.response.ResponseError;
import vn.jpringboot.cinemaBooking.security.UserDetailsImpl;
import vn.jpringboot.cinemaBooking.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping({ "/user" })
@Validated
@Tag(name = "User Controller")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @Operation(summary = "Create a new user", description = "This endpoint allows you to create a new user entry in the system.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseData<?> addUser(@Valid @RequestBody UserRequestDTO user) {
        try {
            log.info("Creating user: {}", user);
            long userId = userService.saveUser(user);
            return new ResponseData<>(HttpStatus.CREATED.value(), "User created successfully", userId);
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error creating user");
        }
    }

    @Operation(summary = "Create a new address", description = "This endpoint allows you to create a new address for an existing user.")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    @PostMapping("/{userId}/adress/")
    public ResponseData<?> addAddress(@PathVariable @Min(1) Long userId,
            @Valid @RequestBody AddressDTO address) {
        try {
            log.info("Creating address for userId: {}", userId);
            userService.addAddress(userId, address);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Address created successfully for userId: ", userId);
        } catch (Exception e) {
            log.error("Error creating address: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Error creating address");
        }
    }

    @Operation(summary = "Update an existing user", description = "This endpoint allows you to update an existing user entry in the system.")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@Min(1) @PathVariable Long userId,
            @Valid @RequestBody UserDTO user) {
        try {
            log.info("Updating user with ID: {}", userId);
            userService.updateUser(userId, user);
            return new ResponseData<>(HttpStatus.OK.value(), "User updated successfully");
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), "Error updating user");
        }
    }

    @Operation(summary = "Change password", description = "This endpoint allows you to change password of user entry in the system.")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PostMapping("/{userId}/changepassword")
    public ResponseData<?> changeUserPassword(@PathVariable @Min(1) Long userId,
            @Valid @RequestBody ChangePasswordRequest request) {
        try {
            log.info("Change user password of userId: {}", userId);
            return userService.changeUserPassword(userId, request);
        } catch (Exception e) {
            log.error(("Error change password"), e.getMessage());
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error change password");
        }
    }

    @Operation(summary = "Update an existing address", description = "This endpoint allows you to update an existing address for an exiting user.")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    @PutMapping("/{userId}/address/{addressId}")
    public ResponseData<?> updateAddress(@Min(1) @PathVariable Long userId,
            @PathVariable @Min(1) Long addressId,
            @Valid @RequestBody AddressDTO address) {
        try {
            log.info("Updating address for userId: {}", userId);
            userService.updateAddress(userId, addressId, address);
            return new ResponseData<>(HttpStatus.OK.value(), "Address updated successfully");
        } catch (Exception e) {
            log.error("Error updating address: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), "Error updating address");
        }
    }

    @Operation(summary = "Delete a user", description = "This endpoint allows you to delete a user entry from the system.")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(@PathVariable @Min(1) Long userId) {
        try {
            log.info("Deleting user with ID: {}", userId);
            userService.deleteUser(userId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), "Error deleting user");
        }
    }

    @Operation(summary = "Delete an address", description = "This endpoint allows you to delete an address from a user.")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    @DeleteMapping("/{userId}/address/{addressId}")
    public ResponseData<?> deleteAddress(@Min(1) @PathVariable Long userId,
            @PathVariable @Min(1) Long addressId) {
        try {
            log.info("Deleting address with ID: {}", addressId);
            userService.deleteAddress(addressId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Address deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting address: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), "Error deleting address");
        }
    }

    @Operation(summary = "Get User Infomation", description = "This endpoint allows you to retrieve information about a specific user.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/user{userId}")
    public ResponseData<?> getUserById(@PathVariable @Min(1) Long userId) {
        try {
            log.info("Retrieving user with ID: {}", userId);
            return new ResponseData<>(HttpStatus.OK.value(), "User:", userService.getUser(userId));
        } catch (Exception e) {
            log.error("Error retrieving user: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), "Error retrieving user");
        }

    }

    @Operation(summary = "Get User Infomation", description = "This endpoint allows you to retrieve information about a specific user.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myuser")
    public ResponseData<?> getMyUser(Authentication authentication) {
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        try {
            log.info("Retrieving user with ID: {}", currentUser.getUserId());
            return new ResponseData<>(HttpStatus.OK.value(), "User:", userService.getUser(currentUser.getUserId()));
        } catch (Exception e) {
            log.error("Error retrieving user: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), "Error retrieving user");
        }

    }

    @Operation(summary = "Get Infomation All User", description = "This endpoint retrieves a paginated list of all users.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/list")
    public ResponseData<?> getAllUsersWithSortBy(
            @RequestParam(defaultValue = "0", required = false) @Min(0) Integer pageNo,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(defaultValue = "", required = false) String... sorts) {
        log.info("Retrieving all users");
        return new ResponseData<>(HttpStatus.OK.value(), "List users:",
                userService.getAllUsersWithSortBy(pageNo, pageSize, sorts));
    }
}
