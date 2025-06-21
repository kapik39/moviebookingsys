package vn.jpringboot.cinemaBooking.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.jpringboot.cinemaBooking.dto.request.LoginRequestDTO;
import vn.jpringboot.cinemaBooking.dto.request.RegisterRequestDTO;
import vn.jpringboot.cinemaBooking.dto.response.JwtResponse;
import vn.jpringboot.cinemaBooking.model.AppUser;
import vn.jpringboot.cinemaBooking.model.Role;
import vn.jpringboot.cinemaBooking.repository.RoleRepository;
import vn.jpringboot.cinemaBooking.repository.UserRepository;
import vn.jpringboot.cinemaBooking.security.JwtBlacklistService;
import vn.jpringboot.cinemaBooking.security.JwtUtil;
import vn.jpringboot.cinemaBooking.security.UserDetailsImpl;
import vn.jpringboot.cinemaBooking.util.UserRole;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Login", description = "This endpoint allows you to login a exist user entry in the system.")
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO LoginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(LoginRequest.getUsername(), LoginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtil.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer",
                userDetails.getUserId(), userDetails.getUsername(),
                userDetails.getEmail(), roles));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = parseJwt(request);

        if (token != null && jwtUtil.validateJwtToken(token)) {

            Claims claims = jwtUtil.getClaimsFromToken(token);
            long expirationTimeMs = claims.getExpiration().getTime();

            jwtBlacklistService.blacklistToken(token, expirationTimeMs);
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok("Logout successfully");
        }
        return new ResponseEntity<>("Can not logout, token invalid", HttpStatus.BAD_REQUEST);

    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    @Operation(summary = "Register", description = "This endpoint allows you to register a new user entry in the system.")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>("Username has existed", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>("Email has existed", HttpStatus.BAD_REQUEST);
        }

        AppUser user = new AppUser();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setDateOfBirth(registerRequest.getDateOfBirth());
        user.setGender(registerRequest.getGender());
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        Role userRole = roleRepository.findByRole(UserRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Not found UserRole"));
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
        return new ResponseEntity<>("Register Successfully", HttpStatus.OK);
    }

}
