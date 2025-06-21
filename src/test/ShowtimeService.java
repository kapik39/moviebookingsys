package com.example.moviebooking.service;

import com.example.moviebooking.model.Movie;
import com.example.moviebooking.model.Screen;
import com.example.moviebooking.model.Showtime;
import com.example.moviebooking.repository.MovieRepository;
import com.example.moviebooking.repository.ScreenRepository;
import com.example.moviebooking.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ScreenRepository screenRepository;

    // --- 1. Tạo lịch chiếu mới ---
    @Transactional
    public Showtime createShowtime(Long movieId, Long screenId, LocalDateTime startTime, double price) {
        // Lấy thông tin phim và phòng chiếu để liên kết
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + movieId));
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new RuntimeException("Screen not found with ID: " + screenId));

        // Tính toán thời gian kết thúc
        LocalDateTime endTime = startTime.plusMinutes(movie.getDurationMinutes());

        // TODO: Thêm logic kiểm tra trùng lịch chiếu
        // Ví dụ: Không cho phép tạo lịch chiếu mới nếu nó chồng chéo với lịch chiếu đã
        // có
        // trong cùng một phòng chiếu.
        if (isOverlap(screen, startTime, endTime)) {
            throw new RuntimeException("Showtime overlaps with existing showtimes in this screen.");
        }

        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setScreen(screen);
        showtime.setStartTime(startTime);
        showtime.setEndTime(endTime); // Gán thời gian kết thúc
        showtime.setPrice(price);
        showtime.setStatus("ACTIVE"); // Mặc định là ACTIVE

        return showtimeRepository.save(showtime);
    }

    // --- 2. Lấy lịch chiếu theo ID ---
    public Optional<Showtime> getShowtimeById(Long id) {
        return showtimeRepository.findById(id);
    }

    // --- 3. Lấy tất cả lịch chiếu ---
    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    // --- 4. Lấy lịch chiếu theo phim, rạp, ngày cụ thể ---
    public List<Showtime> getShowtimesByMovieAndScreenAndDate(Long movieId, Long screenId, LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);
        return showtimeRepository.findByMovieIdAndScreenIdAndStartTimeBetween(movieId, screenId, startOfDay, endOfDay);
    }

    // --- 5. Lấy lịch chiếu theo rạp và ngày ---
    public List<Showtime> getShowtimesByScreenAndDate(Long screenId, LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);
        return showtimeRepository.findByScreenIdAndStartTimeBetween(screenId, startOfDay, endOfDay);
    }

    // --- 6. Cập nhật lịch chiếu ---
    @Transactional
    public Showtime updateShowtime(Long id, LocalDateTime newStartTime, double newPrice) {
        Showtime existingShowtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found with ID: " + id));

        // Cập nhật thông tin
        existingShowtime.setStartTime(newStartTime);
        existingShowtime.setPrice(newPrice);
        // Cập nhật lại endTime nếu startTime thay đổi
        existingShowtime.setEndTime(newStartTime.plusMinutes(existingShowtime.getMovie().getDurationMinutes()));

        // TODO: Kiểm tra trùng lịch chiếu sau khi cập nhật
        if (isOverlap(existingShowtime.getScreen(), newStartTime, existingShowtime.getEndTime(),
                existingShowtime.getId())) {
            throw new RuntimeException("Updated showtime overlaps with existing showtimes in this screen.");
        }

        return showtimeRepository.save(existingShowtime);
    }

    // --- 7. Xóa lịch chiếu ---
    public void deleteShowtime(Long id) {
        showtimeRepository.deleteById(id);
    }

    // --- 8. Hàm kiểm tra trùng lịch (ví dụ) ---
    private boolean isOverlap(Screen screen, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        return isOverlap(screen, newStartTime, newEndTime, null); // Cho trường hợp tạo mới
    }

    private boolean isOverlap(Screen screen, LocalDateTime newStartTime, LocalDateTime newEndTime,
            Long currentShowtimeId) {
        List<Showtime> existingShowtimes = showtimeRepository.findByScreenId(screen.getId());
        for (Showtime existing : existingShowtimes) {
            // Bỏ qua chính lịch chiếu đang cập nhật nếu có
            if (currentShowtimeId != null && existing.getId().equals(currentShowtimeId)) {
                continue;
            }

            // Kiểm tra xem có chồng chéo thời gian không
            boolean overlap = (newStartTime.isBefore(existing.getEndTime())
                    && newEndTime.isAfter(existing.getStartTime()));
            if (overlap) {
                return true;
            }
        }
        return false;
    }
}