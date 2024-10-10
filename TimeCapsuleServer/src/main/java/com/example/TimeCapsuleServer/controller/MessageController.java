package com.example.TimeCapsuleServer.controller;

import com.example.TimeCapsuleServer.dtos.MessageDTO;
import com.example.TimeCapsuleServer.entities.MessageEntity;
import com.example.TimeCapsuleServer.entities.UserEntity;
import com.example.TimeCapsuleServer.repositories.MessageRepository;
import com.example.TimeCapsuleServer.repositories.UserRepository;
import com.example.TimeCapsuleServer.utils.AESUtil;
import com.example.TimeCapsuleServer.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timecapsule")
public class MessageController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AESUtil aesUtil;

    @PostMapping("/create")
    public String createTimeCapsule(@RequestHeader("Authorization") String token, @RequestBody MessageDTO messageDTO) throws Exception {

        if (token == null) {
            return "No token provided";
        }

        String jwtToken = token.substring(7);

        if (!jwtUtil.validateToken(jwtToken)) {
            return "Invalid token";
        }

        if (jwtUtil.isTokenExpired(jwtToken)) {
            return "Token has expired";
        }

        String userEmail = jwtUtil.extractEmail(jwtToken);
        UserEntity user = userRepository.findByEmail(userEmail);
        if (user == null) {
            return "User not found";
        }

        //Generate a salt for this message
        String salt = aesUtil.generateSalt();

        //Encrypt the message using the users password and the generated salt stored in the db
        String encryptedMessage = aesUtil.encryptMessage(messageDTO.getMessage(), user.getPassword(), salt);

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMessageContent(encryptedMessage);
        messageEntity.setSalt(salt);
        messageEntity.setUser(user);

        messageRepository.save(messageEntity);

        return "Time capsule created!";
    }

    @GetMapping("/view")
    public List<String> viewTimeCapsules(@RequestHeader("Authorization") String token) throws Exception {

        if (token == null) {
            return List.of("No token provided");
        }

        String jwtToken = token.substring(7);

        if (!jwtUtil.validateToken(jwtToken)) {
            return List.of("Invalid token");
        }

        if (jwtUtil.isTokenExpired(jwtToken)) {
            return List.of("Token has expired");
        }
        String username = jwtUtil.extractEmail(jwtToken);
        UserEntity user = userRepository.findByEmail(username);

        //Fetch all time capsules for the user
        List<MessageEntity> messages = user.getMessages();

        return messages.stream()
                .map(msg -> {
                    try {
                        return aesUtil.decryptMessage(msg.getMessageContent(), user.getPassword(), msg.getSalt());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "Error decrypting message";
                    }
                }).toList();
    }
}