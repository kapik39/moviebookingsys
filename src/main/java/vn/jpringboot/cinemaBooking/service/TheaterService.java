package vn.jpringboot.cinemaBooking.service;

import vn.jpringboot.cinemaBooking.dto.request.TheaterPatchDTO;
import vn.jpringboot.cinemaBooking.dto.request.TheaterRequestDTO;
import vn.jpringboot.cinemaBooking.dto.response.PageResponse;
import vn.jpringboot.cinemaBooking.dto.response.TheaterDetailResponse;
import vn.jpringboot.cinemaBooking.model.Theater;

public interface TheaterService {
    Long saveTheater(TheaterRequestDTO request);

    void updateTheater(Long theaterId, TheaterRequestDTO request);

    void updatePatchTheater(Long theaterId, TheaterPatchDTO request);

    void deleteTheater(Long theaterId);

    TheaterDetailResponse getTheater(Long theaterId);

    PageResponse<?> getAllTheaters(int pageNo, int pageSize, String... sortsBy);

    Theater getTheaterById(Long theaterId);

}
