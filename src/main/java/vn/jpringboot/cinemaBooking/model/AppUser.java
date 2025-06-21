package vn.jpringboot.cinemaBooking.model;

import jakarta.persistence.*;
import lombok.*;
import vn.jpringboot.cinemaBooking.util.Gender;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity(name = "tbl_users")
public class AppUser extends AbstractEntity {
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "user")
    @Builder.Default
    private Set<AddressUser> addresses = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    public void saveAddress(AddressUser address) {
        if (address != null) {
            if (addresses == null) {
                addresses = new HashSet<>();
            }
            this.addresses.add(address);
            address.setUser(this);
        }
    }

    public AppUser(String firstName, String lastName, Date dateOfBirth, Gender gender, String email, String username,
            String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
