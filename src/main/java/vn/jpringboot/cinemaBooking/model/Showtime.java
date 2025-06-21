package vn.jpringboot.cinemaBooking.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tbl_showtime")
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "showtime_id")
    private Long showtimeId;

    @Column(name = "showtime")
    private LocalDateTime showtime;

    @Column(name = "base_showtime_price")
    private Double baseShowtimePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    public LocalDateTime getEndTime() {
        if (this.getShowtime() == null || this.getMovie() == null) {
            return null;
        }
        return this.getShowtime().plusMinutes(this.getMovie().getMovieDuration());
    }

    public double calculateSeatPrice(Seat seat) {
        double baseShowtimePrice = this.getBaseShowtimePrice();
        double seatTypeMultiplier = 1.0;

        switch (seat.getSeatType()) {
            case VIP:
                seatTypeMultiplier = 1.3;
                break;
            case COUPLE:
                seatTypeMultiplier = 1.8;
                break;
            case REGULAR:
            default:
                seatTypeMultiplier = 1.0;
                break;
        }
        return baseShowtimePrice * seatTypeMultiplier;
    }
}
