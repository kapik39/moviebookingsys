package vn.jpringboot.cinemaBooking.service;

import vn.jpringboot.cinemaBooking.dto.request.AddressDTO;
import vn.jpringboot.cinemaBooking.dto.request.ChangePasswordRequest;
import vn.jpringboot.cinemaBooking.dto.request.UserDTO;
import vn.jpringboot.cinemaBooking.dto.request.UserRequestDTO;
import vn.jpringboot.cinemaBooking.dto.response.PageResponse;
import vn.jpringboot.cinemaBooking.dto.response.ResponseData;
import vn.jpringboot.cinemaBooking.dto.response.UserDetailResponse;
import vn.jpringboot.cinemaBooking.model.AppUser;

public interface UserService {
    long saveUser(UserRequestDTO request);

    void addAddress(Long userId, AddressDTO request);

    void updateUser(Long userId, UserDTO request);

    void updateAddress(Long userId, Long addressId, AddressDTO request);

    ResponseData<?> changeUserPassword(Long userId, ChangePasswordRequest request);

    void deleteUser(long userId);

    void deleteAddress(Long addressId);

    UserDetailResponse getUser(long userId);

    PageResponse<?> getAllUsersWithSortBy(Integer pageNo, Integer pageSize, String... sorts);

    AppUser getUserById(Long userId);
}
