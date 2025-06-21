package vn.jpringboot.cinemaBooking.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tbl_theater")
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id")
    private Long theaterId;

    @Column(name = "theater_name")
    private String theaterName;

    @Column(name = "theater_location")
    private String theaterLocation;

    @Column(name = "number_of_screens")
    private Integer numberOfScreens;
}