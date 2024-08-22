package ru.ctraltdefeat.DemoWolframSingle.service;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.ctraltdefeat.DemoWolframSingle.domain.JwtAuthentication;
import ru.ctraltdefeat.DemoWolframSingle.domain.JwtRequest;
import ru.ctraltdefeat.DemoWolframSingle.domain.JwtResponse;
import ru.ctraltdefeat.DemoWolframSingle.domain.User;
import ru.ctraltdefeat.DemoWolframSingle.exception.AuthException;
import ru.ctraltdefeat.DemoWolframSingle.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    public boolean isUserActive(JwtRequest authRequest) {
        User user = userRepository.findByLoginAndPassword(authRequest.getLogin(), authRequest.getPassword());
        return user != null && user.isActive();
    }

    public JwtResponse login(@NonNull JwtRequest authRequest) {
        final User user = (User) userRepository.getByLogin(authRequest.getLogin())
                .orElseThrow(() -> new AuthException("User not found"));
        if (user.getPassword().equals(authRequest.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getLogin(), refreshToken);
            return new JwtResponse(accessToken, refreshToken, user);
        } else {
            throw new AuthException("Incorrect password");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = (User) userRepository.getByLogin(login)
                        .orElseThrow(() -> new AuthException("User not found"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null, user);
            }
        }
        return new JwtResponse(null, null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = (User) userRepository.getByLogin(login)
                        .orElseThrow(() -> new AuthException("User not found"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getLogin(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken, user);
            }
        }
        throw new AuthException("Invalid JWT token");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
