package vn.jpringboot.cinemaBooking.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.jpringboot.cinemaBooking.model.Showtime;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    Page<Showtime> findByScreen_ScreenId(Long screenId, Pageable pageable);

    Page<Showtime> findByMovie_MovieId(Long movieId, Pageable pageable);

    List<Showtime> findByScreen_ScreenId(Long screenId);

}
