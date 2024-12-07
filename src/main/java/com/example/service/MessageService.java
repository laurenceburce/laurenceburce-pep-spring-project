package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AccountRepository accountRepository;

    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Invalid message text");
        }
        if (!accountRepository.existsById(message.getPostedBy())) {
            throw new IllegalArgumentException("User does not exist");
        }
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    public int updateMessageText(Integer messageId, String newText) {
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            throw new IllegalArgumentException("Invalid message text");
        }
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isEmpty()) {
            throw new IllegalArgumentException("Message not found");
        }
        Message message = messageOpt.get();
        message.setMessageText(newText);
        messageRepository.save(message);
        return 1;
    }

    public int deleteMessage(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return 1;
        }
        return 0;
    }

    public List<Message> getMessagesByUser(Integer accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new IllegalArgumentException("User not found");
        }
        return messageRepository.findByPostedBy(accountId);
    }
}