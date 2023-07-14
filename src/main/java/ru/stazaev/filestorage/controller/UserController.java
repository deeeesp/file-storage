package ru.stazaev.filestorage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.stazaev.filestorage.dto.request.UserLoginDto;
import ru.stazaev.filestorage.dto.request.UserRegistrationDto;
import ru.stazaev.filestorage.dto.response.JwtTokensDto;
import ru.stazaev.filestorage.services.AuthorizationService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user/")
public class UserController {
    private final AuthorizationService authorizationService;
    @GetMapping("a")
    public String vo(@RequestBody UserLoginDto userLoginDto){
        return "ss";
    }

    @PostMapping("register")
    public JwtTokensDto registerNewUser(@RequestBody UserRegistrationDto userRegistrationDto){
        return authorizationService.registerUser(userRegistrationDto);
    }

    @GetMapping("login")
    public  JwtTokensDto loginUser(@RequestBody UserLoginDto userLoginDto){
        return authorizationService.loginUser(userLoginDto);
    }
}
