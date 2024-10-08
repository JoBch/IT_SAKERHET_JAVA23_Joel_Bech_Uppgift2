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
    public String createTimeCapsule(@RequestHeader("Authorization") String token, @RequestBody MessageDTO messageDTO)
            throws Exception {
        //Check if the token is present
        if (token == null || !token.startsWith("Bearer ")) {
            return "No token provided";
        }

        //Extract the token from the header and validate it
        String jwtToken = token.substring(7);
        if (jwtUtil.validateToken(jwtToken)) {
            return "Invalid token";
        }

        //Check if the token has expired
        if (jwtUtil.isTokenExpired(jwtToken)) {
            return "Token has expired";
        }

        String username = jwtUtil.extractUsername(jwtToken);
        UserEntity user = userRepository.findByEmail(username);
        if (user == null) {
            return "User not found";
        }

        String encryptedMessage = aesUtil.encryptMessage(messageDTO.getMessage());

        //Create and save the message entity
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMessageContent(encryptedMessage);
        messageEntity.setUser(user);

        messageRepository.save(messageEntity);

        return "Time capsule created!";
    }


    @GetMapping("/view")
    public List<String> viewTimeCapsules(@RequestHeader("Authorization") String token) throws Exception {
        String jwtToken = token.substring(7);
        if (jwtUtil.validateToken(jwtToken)) {
            return List.of("Invalid token");
        }

        String username = jwtUtil.extractUsername(jwtToken);
        UserEntity user = userRepository.findByEmail(username);

        //Fetch all time capsules for the user
        List<MessageEntity> messages = user.getMessages();

        //Decrypt each message before returning
        return messages.stream()
                .map(msg -> {
                    try {
                        return aesUtil.decryptMessage(msg.getMessageContent());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "Error decrypting message";
                    }
                }).toList();
    }
}
