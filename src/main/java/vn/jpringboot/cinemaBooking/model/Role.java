package vn.jpringboot.cinemaBooking.model;

import jakarta.persistence.*;
import lombok.*;
import vn.jpringboot.cinemaBooking.util.UserRole;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity(name = "tbl_roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", unique = true, nullable = false)
    private UserRole role;
}
