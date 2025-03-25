package com.example.demo.security;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.dto.SignUpRequestDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.SessionService;
import com.example.demo.service.UserService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final SessionService sessionService;
    private final UserService userService;

    // public UserDto signUp(SignUpRequestDto signUpRequestDto) {

    // User user =
    // userRepository.findByEmail(signUpRequestDto.getEmail()).orElse(null);

    // if (user != null) {
    // throw new RuntimeException("User is already present with same email id");
    // }

    // User newUser = modelMapper.map(signUpRequestDto, User.class);
    // newUser.setRoles(Set.of(Role.ADMIN));
    // newUser.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
    // newUser = userRepository.save(newUser);

    // return modelMapper.map(newUser, UserDto.class);
    // }
    public UserDto signUp(SignUpRequestDto signUpRequestDto) {
        User user = userRepository.findByEmail(signUpRequestDto.getEmail());

        if (user != null) {
            throw new RuntimeException("User is already present with same email id");
        }

        // Map DTO to User entity, excluding roles initially
        User newUser = modelMapper.map(signUpRequestDto, User.class);

        // // Manually set the roles as a Set<Role>
        // try {
        // Role role = Role.valueOf(signUpRequestDto.getRole().toUpperCase());
        // // Validate that the role is either INSTRUCTOR or STUDENT

        // if (role == Role.INSTRUCTOR) {
        // newUser.setRoles(Set.of(Role.ADMIN));
        // }
        // if (role == Role.STUDENT) {
        // newUser.setRoles(Set.of(Role.STUDENT));
        // }

        // // newUser.setRoles(Set.of(role)); // Set as a single-element Set
        // } catch (IllegalArgumentException e) {
        // throw new RuntimeException("Invalid role specified: " +
        // signUpRequestDto.getRole());
        // }

        // Encode password and save
        newUser.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        newUser = userRepository.save(newUser);

        return modelMapper.map(newUser, UserDto.class);
    }

    public LoginResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        sessionService.generateNewSession(user, refreshToken);

        return new LoginResponseDto(user.getId(), accessToken, refreshToken);
    }

    public LoginResponseDto refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        sessionService.validateSession(refreshToken);
        User user = userService.getUserById(userId);

        String accessToken = jwtService.generateAccessToken(user);
        return new LoginResponseDto(user.getId(), accessToken, refreshToken);
    }
}
