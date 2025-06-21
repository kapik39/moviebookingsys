package vn.jpringboot.cinemaBooking.service.imlp;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.jpringboot.cinemaBooking.dto.request.ShowtimeRequestDTO;
import vn.jpringboot.cinemaBooking.dto.response.PageResponse;
import vn.jpringboot.cinemaBooking.dto.response.ShowtimeDetailResponse;
import vn.jpringboot.cinemaBooking.exception.ResourceNotFoundException;
import vn.jpringboot.cinemaBooking.model.Movie;
import vn.jpringboot.cinemaBooking.model.Screen;
import vn.jpringboot.cinemaBooking.model.Showtime;
import vn.jpringboot.cinemaBooking.repository.ShowtimeRepository;
import vn.jpringboot.cinemaBooking.service.MovieService;
import vn.jpringboot.cinemaBooking.service.ScreenService;
import vn.jpringboot.cinemaBooking.service.ShowtimeService;
import vn.jpringboot.cinemaBooking.util.PageableUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final ScreenService screenService;
    private final MovieService movieService;

    @Override
    public Long saveShowtime(ShowtimeRequestDTO request) {
        Screen screen = screenService.getScreenById(request.getScreenId());
        Movie movie = movieService.getMovieById(request.getMovieId());

        Showtime showtime = Showtime.builder()
                .showtime(request.getShowtime())
                .baseShowtimePrice(movie.getBaseShowtimePrice())
                .screen(screen)
                .movie(movie)
                .build();

        LocalDateTime endTime = request.getShowtime().plusMinutes(movie.getMovieDuration());
        if (isOverlap(screen, request.getShowtime(), endTime)) {
            throw new ResourceNotFoundException("Showtime overlaps with existing showtimes in this screen.");
        }

        showtimeRepository.save(showtime);
        log.info("Showtime saved successfully with ID: {}", showtime.getShowtimeId());
        return showtime.getShowtimeId();
    }

    @Override
    public void updateShowtime(Long showtimeId, ShowtimeRequestDTO request) {
        Screen screen = screenService.getScreenById(request.getScreenId());
        Movie movie = movieService.getMovieById(request.getMovieId());
        Showtime showtime = getShowtimeById(showtimeId);
        showtime.setShowtime(request.getShowtime());
        showtime.setMovie(movie);
        showtime.setScreen(screen);

        if (isOverlap(screen, request.getShowtime(), showtime.getEndTime(), showtimeId)) {
            throw new ResourceNotFoundException("Updated showtime overlaps with existing showtimes in this screen.");
        }
        showtimeRepository.save(showtime);
        log.info("Showtime updated successfully with ID: {}", showtimeId);
    }

    @Override
    public void deleteShowtime(Long showtimeId) {
        showtimeRepository.deleteById(showtimeId);
        log.info("Showtime deleted successfully with ID: {}", showtimeId);
    }

    @Override
    public ShowtimeDetailResponse getShowtime(Long showtimeId) {
        Showtime showtime = getShowtimeById(showtimeId);
        return ShowtimeDetailResponse.builder()
                .showtimeId(showtime.getShowtimeId())
                .showtime(showtime.getShowtime())
                .baseShowtimePrice(showtime.getBaseShowtimePrice())
                .movieId(showtime.getMovie().getMovieId())
                .screenId(showtime.getScreen().getScreenId())
                .build();
    }

    @Override
    public PageResponse<?> getAllShowtimesByScreenId(Long screenId, int pageNo, int pageSize, String... sortsBy) {
        Pageable pageable = PageableUtil.buildPageable(pageNo, pageSize, sortsBy);

        Page<Showtime> showtimePage = showtimeRepository.findByScreen_ScreenId(screenId, pageable);

        log.info("Total Showtime by Screen found:");
        List<ShowtimeDetailResponse> showtimeDetails = showtimePage.stream()
                .map(showtime -> ShowtimeDetailResponse.builder()
                        .showtimeId(showtime.getShowtimeId())
                        .showtime(showtime.getShowtime())
                        .baseShowtimePrice(showtime.getBaseShowtimePrice())
                        .screenId(showtime.getScreen().getScreenId())
                        .movieId(showtime.getMovie().getMovieId())
                        .build())
                .toList();
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(showtimePage.getTotalPages())
                .items(showtimeDetails)
                .build();
    }

    @Override
    public PageResponse<?> getAllShowtimesByMovieId(Long movieId, int pageNo, int pageSize, String... sortsBy) {
        Pageable pageable = PageableUtil.buildPageable(pageNo, pageSize, sortsBy);

        Page<Showtime> showtimePage = showtimeRepository.findByMovie_MovieId(movieId, pageable);

        log.info("Total ShowTime by Screen found:");
        List<ShowtimeDetailResponse> showtimeDetails = showtimePage.stream()
                .map(showtime -> ShowtimeDetailResponse.builder()
                        .showtimeId(showtime.getShowtimeId())
                        .showtime(showtime.getShowtime())
                        .baseShowtimePrice(showtime.getBaseShowtimePrice())
                        .screenId(showtime.getScreen().getScreenId())
                        .movieId(showtime.getMovie().getMovieId())
                        .build())
                .toList();
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(showtimePage.getTotalPages())
                .items(showtimeDetails)
                .build();
    }

    @Override
    public Showtime getShowtimeById(Long showtimeId) {
        return showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Screen not found with ID: " + showtimeId));
    }

    // Check Overlap
    private boolean isOverlap(Screen screen, LocalDateTime StartTime, LocalDateTime EndTime) {
        return isOverlap(screen, StartTime, EndTime, null);
    }

    private boolean isOverlap(Screen screen, LocalDateTime newStartTime, LocalDateTime newEndTime,
            Long currentShowtimeId) {
        List<Showtime> existingShowtimes = showtimeRepository.findByScreen_ScreenId(screen.getScreenId());
        for (Showtime existing : existingShowtimes) {
            if (currentShowtimeId != null && existing.getShowtimeId().equals(currentShowtimeId)) {
                continue;
            }

            boolean overlap = (newStartTime.isBefore(existing.getEndTime())
                    && newEndTime.isAfter(existing.getShowtime()));
            if (overlap) {
                return true;
            }
        }
        return false;
    }
}
