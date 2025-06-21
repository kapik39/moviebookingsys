package vn.jpringboot.cinemaBooking.configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;
import vn.jpringboot.cinemaBooking.model.AppUser;
import vn.jpringboot.cinemaBooking.model.Role;
import vn.jpringboot.cinemaBooking.repository.RoleRepository;
import vn.jpringboot.cinemaBooking.repository.UserRepository;
import vn.jpringboot.cinemaBooking.util.UserRole;

@Configuration
@Slf4j
public class DataInitializer {
    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository, UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            if (roleRepository.findByRole(UserRole.ROLE_ADMIN).isEmpty()) {
                roleRepository.save(new Role(null, UserRole.ROLE_ADMIN));
            }
            if (roleRepository.findByRole(UserRole.ROLE_USER).isEmpty()) {
                roleRepository.save(new Role(null, UserRole.ROLE_USER));
            }
            if (roleRepository.findByRole(UserRole.ROLE_MANAGER).isEmpty()) {
                roleRepository.save(new Role(null, UserRole.ROLE_MANAGER));
            }
            if (roleRepository.findByRole(UserRole.ROLE_STAFF).isEmpty()) {
                roleRepository.save(new Role(null, UserRole.ROLE_STAFF));
            }

            if (userRepository.findByUsername("admin").isEmpty()) {
                Optional<Role> adminRole = roleRepository.findByRole(UserRole.ROLE_ADMIN);
                AppUser adminUser = new AppUser();
                adminUser.setUsername("admin");
                adminUser.setEmail("admin@example.com");
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                adminUser.setRoles(new HashSet<>(Collections.singletonList(adminRole.get())));

                userRepository.save(adminUser);
                log.info("AdminUser created successfully");
            }

            if (userRepository.findByUsername("manager").isEmpty()) {
                Optional<Role> adminRole = roleRepository.findByRole(UserRole.ROLE_MANAGER);
                AppUser managerUser = new AppUser();
                managerUser.setUsername("manager");
                managerUser.setEmail("manager@example.com");
                managerUser.setPassword(passwordEncoder.encode("manager123"));
                managerUser.setRoles(new HashSet<>(Collections.singletonList(adminRole.get())));

                userRepository.save(managerUser);
                log.info("ManagerUser created successfully");
            }
        };
    }
}
