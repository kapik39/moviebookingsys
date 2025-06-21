package vn.jpringboot.cinemaBooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.jpringboot.cinemaBooking.model.Screen;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {

    Page<Screen> findByTheater_TheaterId(Long theaterId, Pageable pageable);

}
