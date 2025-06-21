package vn.jpringboot.cinemaBooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.jpringboot.cinemaBooking.model.Theater;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {

}
