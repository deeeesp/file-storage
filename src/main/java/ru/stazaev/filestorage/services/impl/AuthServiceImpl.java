package ru.stazaev.filestorage.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.stazaev.filestorage.dto.request.UserLoginDto;
import ru.stazaev.filestorage.dto.request.UserRegistrationDto;
import ru.stazaev.filestorage.dto.response.JwtTokensDto;
import ru.stazaev.filestorage.entity.User;
import ru.stazaev.filestorage.entity.enums.Role;
import ru.stazaev.filestorage.repository.UserRepository;
import ru.stazaev.filestorage.security.JwtTokenProvider;
import ru.stazaev.filestorage.services.AuthorizationService;

import java.util.NoSuchElementException;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthorizationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final ModelMapper modelMapper;


    public JwtTokensDto registerUser(UserRegistrationDto userRegistrationDto) {
        if (userRepository.findByUsername(userRegistrationDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким именем не существует");
        }
        User user = modelMapper.map(userRegistrationDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole(Role.USER);
        user = userRepository.save(user);

        log.info("A new user has registered " + user);
        return createTokensForUser(user);
    }

    public JwtTokensDto loginUser(UserLoginDto user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword()));
        User dbUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new NoSuchElementException("Пользователь не существует"));

        return createTokensForUser(dbUser);
    }

    public JwtTokensDto refreshToken(String refreshToken) {
        String nickname = tokenProvider.getUsernameFromJwt(refreshToken);
        User dbUser = userRepository.findByUsername(nickname)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не существует"));
        return createTokensForUser(dbUser);
    }

    private JwtTokensDto createTokensForUser(User user) {
//        if (user.getStatus().equals(Status.DELETED)) {
//            throw new AccessDeniedException("Пользователь заблокирован");
//        }

        return new JwtTokensDto(tokenProvider.generateAccessToken(user), tokenProvider.generateRefreshToken(user));
    }
}
