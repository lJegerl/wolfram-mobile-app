package ru.ctraltdefeat.DemoWolframSingle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ctraltdefeat.DemoWolframSingle.domain.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByOrderByTimestampDesc();
}
