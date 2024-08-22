package ru.ctraltdefeat.DemoWolframSingle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ctraltdefeat.DemoWolframSingle.domain.Message;
import ru.ctraltdefeat.DemoWolframSingle.repository.MessageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getLastMessages(int limit) {
        List<Message> allMessages = messageRepository.findAllByOrderByTimestampDesc();
        return allMessages.stream().limit(limit).collect(Collectors.toList());
    }


    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    public void deleteMessage(Long id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Message with id " + id + " not found");
        }
    }

    public boolean existsById(Long id) {
        return messageRepository.existsById(id);
    }
}
