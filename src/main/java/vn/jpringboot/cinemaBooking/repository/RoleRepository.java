package vn.jpringboot.cinemaBooking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;

import vn.jpringboot.cinemaBooking.model.Role;
import vn.jpringboot.cinemaBooking.util.UserRole;

public interface RoleRepository extends JpaRepository<Role, Long> {
    // @Query(value = "Select * from tbl_roles r where r.role =:role", nativeQuery =
    // true)
    Optional<Role> findByRole(UserRole role);
}