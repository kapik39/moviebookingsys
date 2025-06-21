package vn.jpringboot.cinemaBooking.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tbl_screen")
public class Screen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screen_id")
    private Long screenId;

    @Column(name = "screen_name")
    private String screenName;

    @Column(name = "screen_capacity")
    private Integer screenCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "screen")
    @Builder.Default
    private Set<Seat> seats = new HashSet<>();

    public void saveSeat(Seat seat) {
        if (seat != null) {
            if (seats == null) {
                seats = new HashSet<>();
            }
            this.seats.add(seat);
            seat.setScreen(this);
        }
    }
}