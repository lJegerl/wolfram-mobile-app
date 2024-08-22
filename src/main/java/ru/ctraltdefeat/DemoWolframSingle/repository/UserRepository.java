package ru.ctraltdefeat.DemoWolframSingle.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ctraltdefeat.DemoWolframSingle.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);

    User findByLoginAndPassword(String login, String password);
    User findByActivationCode(String code);

    Optional<Object> getByLogin(String login);
}
