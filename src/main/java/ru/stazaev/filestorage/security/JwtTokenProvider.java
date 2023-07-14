package ru.stazaev.filestorage.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import ru.stazaev.filestorage.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.access-validity}")
    private long accessValidity;
    @Value("${jwt.refresh-validity}")
    private long refreshValidity;

    public String generateAccessToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessValidity))
                .withClaim("id", user.getId())
                .withClaim("authority", user.getRole().getAuthority())
                .sign(algorithm);
    }

    public String generateRefreshToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshValidity))
                .sign(algorithm);
    }

    public UsernamePasswordAuthenticationToken getAuthTokenFromJwt(String jwtToken) {
        DecodedJWT decodedJWT = decodeJWT(jwtToken);
        String username = decodedJWT.getSubject();
        String authority = decodedJWT.getClaim("authority").asString();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(authority));
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    public String getUsernameFromJwt(String jwtToken) {
        DecodedJWT decodedJWT = decodeJWT(jwtToken);
        return decodedJWT.getSubject();
    }

    private DecodedJWT decodeJWT(String jwtToken) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(jwtToken);
    }
}
