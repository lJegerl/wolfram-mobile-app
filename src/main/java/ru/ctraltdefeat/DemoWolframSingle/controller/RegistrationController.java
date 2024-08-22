package ru.ctraltdefeat.DemoWolframSingle.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ctraltdefeat.DemoWolframSingle.domain.*;
import ru.ctraltdefeat.DemoWolframSingle.repository.UserRepository;
import ru.ctraltdefeat.DemoWolframSingle.service.MailService;

import java.util.Collections;
import java.util.Random;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
@Api(description = "Controllers for registration")
public class RegistrationController {

    @Autowired
    private final UserRepository userRepository;
    private final int digitsForCode = 1000000;

    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private final MailService mailSender;

    @PostMapping
    @ApiOperation("Register user to a program")
    public ResponseEntity<UserDto> create(@RequestBody UserRequest userRequest) {
        Random random = new Random();
        String code = String.format("%06d", random.nextInt(digitsForCode));

        if (userRequest.getEmail().equals(from)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (userRepository.existsByLogin(userRequest.getLogin()) || userRepository.existsByEmail(userRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            User user = User.builder()
                    .login(userRequest.getLogin())
                    .password(userRequest.getPassword())
                    .email(userRequest.getEmail())
                    .activationCode(code)
                    .active(false)
                    .roles(Collections.singleton(Role.USER))
                    .build();

            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to WolframSingle. It's your code %s",
                    user.getLogin(),
                    user.getActivationCode()
            );

            userRepository.save(user);
            mailSender.send(user.getEmail(), "Activation code", message);

            UserDto userWrapper = new UserDto(user);
            return ResponseEntity.ok().body(userWrapper);
        }
    }

    @PostMapping("/activate/{code}")
    public ResponseEntity<UserDto> activate(@PathVariable String code, @RequestBody UserRequest userRequest) {
        User user = userRepository.findByLoginAndPassword(userRequest.getLogin(), userRequest.getPassword());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (!user.getActivationCode().equals(code)) {
            return ResponseEntity.notFound().build();
        }
        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);

        UserDto userWrapper = new UserDto(user);
        return ResponseEntity.ok().body(userWrapper);

    }
}
