package ru.ctraltdefeat.DemoWolframSingle.domain;

public class UserDto {
    private User user;

    public UserDto(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

