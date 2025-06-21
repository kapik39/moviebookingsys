package vn.jpringboot.cinemaBooking.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
import vn.jpringboot.cinemaBooking.util.BookingStatus;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tbl_booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "booking_time")
    private LocalDateTime bookingTime;

    @Column(name = "number_of_tickets")
    private Integer numberOfTickets;

    @Column(name = "booking_status")
    private BookingStatus status;

    @Column(name = "total_amount")
    private double totalAmount;

    @ManyToMany
    @Builder.Default
    @JoinTable(name = "booking_seat", joinColumns = @JoinColumn(name = "booking_id"), inverseJoinColumns = @JoinColumn(name = "seat_id"))
    private Set<Seat> seats = new HashSet<>();
}
