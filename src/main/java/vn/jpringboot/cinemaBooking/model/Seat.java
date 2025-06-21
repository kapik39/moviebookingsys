package vn.jpringboot.cinemaBooking.model;

import jakarta.persistence.*;
import lombok.*;
import vn.jpringboot.cinemaBooking.util.SeatType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_seat", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "seat_row", "seat_number", "seat_type", "screen_id" })
})
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long seatId;

    @Column(nullable = false, length = 2)
    private String seatRow;

    @Column(nullable = false)
    private Integer seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatType seatType; // VIP, REGULAR, COUPLE

    @Column(name = "is_available")
    @Builder.Default
    private Boolean isAvailable = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id")
    private Screen screen;
}
