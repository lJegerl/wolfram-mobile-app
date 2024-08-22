package ru.ctraltdefeat.DemoWolframSingle.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String login;
    private String password;
    private String email;
}
