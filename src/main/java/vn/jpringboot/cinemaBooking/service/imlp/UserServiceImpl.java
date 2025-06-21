package vn.jpringboot.cinemaBooking.service.imlp;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import vn.jpringboot.cinemaBooking.model.AppUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.jpringboot.cinemaBooking.dto.request.AddressDTO;
import vn.jpringboot.cinemaBooking.dto.request.ChangePasswordRequest;
import vn.jpringboot.cinemaBooking.dto.request.UserDTO;
import vn.jpringboot.cinemaBooking.dto.request.UserRequestDTO;
import vn.jpringboot.cinemaBooking.dto.response.PageResponse;
import vn.jpringboot.cinemaBooking.dto.response.ResponseData;
import vn.jpringboot.cinemaBooking.dto.response.UserDetailResponse;
import vn.jpringboot.cinemaBooking.exception.ResourceNotFoundException;
import vn.jpringboot.cinemaBooking.model.AddressUser;
import vn.jpringboot.cinemaBooking.repository.AddressRepository;
import vn.jpringboot.cinemaBooking.repository.UserRepository;
import vn.jpringboot.cinemaBooking.service.UserService;
import vn.jpringboot.cinemaBooking.util.PageableUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public long saveUser(UserRequestDTO request) {
        AppUser user = AppUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .username(request.getUsername())
                .password(request.getPassword())
                .roles(Collections.singleton(request.getRoles()))
                .build();

        request.getAddresses().forEach(a -> user.saveAddress(AddressUser.builder()
                .apartmentNumber(a.getApartmentNumber())
                .floor(a.getFloor())
                .building(a.getBuilding())
                .streetNumber(a.getStreetNumber())
                .street(a.getStreet())
                .city(a.getCity())
                .country(a.getCountry())
                .build()));

        userRepository.save(user);
        log.info("User saved successfully with ID: {}", user.getUserId());
        return user.getUserId();
    }

    @Override
    public void addAddress(Long userId, AddressDTO request) {
        AppUser user = getUserById(userId);
        AddressUser newAddress = AddressUser.builder()
                .apartmentNumber(request.getApartmentNumber())
                .floor(request.getFloor())
                .building(request.getBuilding())
                .streetNumber(request.getStreetNumber())
                .street(request.getStreet())
                .city(request.getCity())
                .country(request.getCountry())
                .build();
        user.saveAddress(newAddress);
        userRepository.save(user);
        log.info("Address added successfully for userId: {}", userId);
    }

    @Override
    public void updateUser(Long userId, UserDTO request) {
        AppUser user = getUserById(userId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setGender(request.getGender());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);
        log.info("User updated successfully for userId: {}", user.getUserId());
    }

    @Override
    public void updateAddress(Long userId, Long addressId, AddressDTO request) {
        AppUser user = getUserById(userId);
        AddressUser addressToUpdate = user.getAddresses().stream()
                .filter(address -> address.getAddressId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + addressId));

        addressToUpdate.setApartmentNumber(request.getApartmentNumber());
        addressToUpdate.setFloor(request.getFloor());
        addressToUpdate.setBuilding(request.getBuilding());
        addressToUpdate.setStreetNumber(request.getStreetNumber());
        addressToUpdate.setStreet(request.getStreet());
        addressToUpdate.setCity(request.getCity());
        addressToUpdate.setCountry(request.getCountry());

        userRepository.save(user);
        log.info("Address updated successfully for userId: {}", userId);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
        log.info("User deleted successfully with ID: {}", userId);
    }

    @Override
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
        log.info("Address deleted successfully with ID: {}", addressId);
    }

    @Override
    public UserDetailResponse getUser(long userId) {
        AppUser user = getUserById(userId);
        return UserDetailResponse.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .username(user.getUsername())
                .addresses(convertToAddress(user.getAddresses()))
                .build();
    }

    @Override
    public PageResponse<?> getAllUsersWithSortBy(Integer pageNo, Integer pageSize, String... sorts) {
        Pageable pageable = PageableUtil.buildPageable(pageNo, pageSize, sorts);

        Page<AppUser> users = userRepository.findAll(pageable);

        log.info("Total users found: ");
        List<UserDetailResponse> response = users.stream()
                .map(user -> UserDetailResponse.builder()
                        .userId(user.getUserId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .dateOfBirth(user.getDateOfBirth())
                        .gender(user.getGender())
                        .username(user.getUsername())
                        .build())
                .toList();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(users.getTotalPages())
                .items(response)
                .build();
    }

    private Set<AddressDTO> convertToAddress(Set<AddressUser> address) {
        Set<AddressDTO> result = new HashSet<>();
        address.forEach(a -> result.add(AddressDTO.builder()
                .apartmentNumber(a.getApartmentNumber())
                .floor(a.getFloor())
                .building(a.getBuilding())
                .streetNumber(a.getStreetNumber())
                .street(a.getStreet())
                .city(a.getCity())
                .country(a.getCountry())
                .build()));
        return result;
    }

    @Override
    public AppUser getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    @Override
    public ResponseData<?> changeUserPassword(Long userId, ChangePasswordRequest request) {
        AppUser user = getUserById(userId);

        if (!passwordEncoder.matches(request.getConfirmPassword(), request.getNewPassword())) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Password do not match");
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Incorrect password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Password has be changed");
    }
}
