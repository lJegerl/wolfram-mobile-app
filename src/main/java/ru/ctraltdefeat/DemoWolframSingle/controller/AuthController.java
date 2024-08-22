package ru.ctraltdefeat.DemoWolframSingle.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ctraltdefeat.DemoWolframSingle.domain.JwtRequest;
import ru.ctraltdefeat.DemoWolframSingle.domain.JwtResponse;
import ru.ctraltdefeat.DemoWolframSingle.domain.RefreshJwtRequest;
import ru.ctraltdefeat.DemoWolframSingle.exception.AuthException;
import ru.ctraltdefeat.DemoWolframSingle.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Api(description = "Controllers for login")
public class AuthController {

    @Autowired
    private final AuthService authService;

    @PostMapping("login")
    @ApiOperation("login to program")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        if (!authService.isUserActive(authRequest)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            final JwtResponse token = authService.login(authRequest);
            return ResponseEntity.ok(token);
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("token")
    @ApiOperation("get access token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    @ApiOperation("get refresh token")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

}
