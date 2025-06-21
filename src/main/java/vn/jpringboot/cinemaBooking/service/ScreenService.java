package vn.jpringboot.cinemaBooking.service;

import vn.jpringboot.cinemaBooking.dto.request.ScreenRequestDTO;
import vn.jpringboot.cinemaBooking.dto.request.SeatDTO;
import vn.jpringboot.cinemaBooking.dto.response.PageResponse;
import vn.jpringboot.cinemaBooking.dto.response.ScreenDetailResponse;
import vn.jpringboot.cinemaBooking.model.Screen;

public interface ScreenService {
    Long saveScreen(ScreenRequestDTO request);

    void addSeat(Long screenId, SeatDTO request);

    void updateScreen(Long screenId, ScreenRequestDTO request);

    void updateSeat(Long screenId, Long seatId, SeatDTO request);

    void deleteScreen(Long screenId, Long theaterId);

    void deleteSeat(Long screenId, Long seatId);

    ScreenDetailResponse getScreen(Long screenId);

    PageResponse<?> getAllScreensByTheaterId(Long theaterId, int pageNo, int pageSize, String... sortsBy);

    Screen getScreenById(Long screenId);
}
