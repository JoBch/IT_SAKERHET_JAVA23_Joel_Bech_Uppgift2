package com.example.TimeCapsuleServer.services;

import com.example.TimeCapsuleServer.dtos.UserDTO;
import com.example.TimeCapsuleServer.entities.UserEntity;
import com.example.TimeCapsuleServer.repositories.UserRepository;
import com.example.TimeCapsuleServer.security.Hashing;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private final Hashing hashing;
    @Autowired
    private UserRepository userRepository;

    public UserService() {
        this.hashing = new Hashing();
    }

    public UserEntity findUserById(int id) {
        return userRepository.findById((long) id).orElse(null);
    }

    //Adding user to db
    public void registerUser(UserEntity user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Error!");
        }
        String hashedPassword = hashing.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    //Checking db to validate login
    public boolean loginUser(String email, String password, HttpSession session) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null || !hashing.verifyPassword(password, userEntity.getPassword())) {
            return false;
        }
        UserDTO userDTO = new UserDTO(userEntity.getId(), userEntity.getPassword(), userEntity.getUsername());
        session.setAttribute("user", userDTO);
        return true;
    }
}
