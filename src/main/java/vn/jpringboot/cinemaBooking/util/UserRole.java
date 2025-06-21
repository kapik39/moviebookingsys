package vn.jpringboot.cinemaBooking.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserRole {
    @JsonProperty("admin")
    ROLE_ADMIN,
    @JsonProperty("user")
    ROLE_USER,
    @JsonProperty("staff")
    ROLE_STAFF,
    @JsonProperty("manager")
    ROLE_MANAGER,
}