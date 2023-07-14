package ru.stazaev.filestorage.services;

import ru.stazaev.filestorage.dto.request.UserLoginDto;
import ru.stazaev.filestorage.dto.request.UserRegistrationDto;
import ru.stazaev.filestorage.dto.response.JwtTokensDto;
import ru.stazaev.filestorage.entity.User;

public interface AuthorizationService {

    public JwtTokensDto registerUser(UserRegistrationDto userRegistrationDto);

    public JwtTokensDto loginUser(UserLoginDto user);

    public JwtTokensDto refreshToken(String refreshToken);

}
