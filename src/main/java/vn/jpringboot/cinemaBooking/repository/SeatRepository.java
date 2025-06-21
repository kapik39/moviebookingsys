package vn.jpringboot.cinemaBooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.jpringboot.cinemaBooking.model.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

}
