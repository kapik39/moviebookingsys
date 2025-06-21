package vn.jpringboot.cinemaBooking.service;

import vn.jpringboot.cinemaBooking.dto.request.ShowtimeRequestDTO;
import vn.jpringboot.cinemaBooking.dto.response.PageResponse;
import vn.jpringboot.cinemaBooking.dto.response.ShowtimeDetailResponse;
import vn.jpringboot.cinemaBooking.model.Showtime;

public interface ShowtimeService {
    Long saveShowtime(ShowtimeRequestDTO request);

    void updateShowtime(Long showtimeId, ShowtimeRequestDTO request);

    void deleteShowtime(Long showtimeId);

    ShowtimeDetailResponse getShowtime(Long showtimeId);

    PageResponse<?> getAllShowtimesByScreenId(Long showtimeId, int pageNo, int pageSize, String... sortsBy);

    PageResponse<?> getAllShowtimesByMovieId(Long movieId, int pageNo, int pageSize, String... sortsBy);

    Showtime getShowtimeById(Long showtimeId);
}
