package com.co2fp.backend.backend.controller;

import com.co2fp.backend.backend.DTO.transport.TransportRegisterDTO;
import com.co2fp.backend.backend.DTO.transport.TransportResponseDTO;
import com.co2fp.backend.backend.DTO.user.UserLoginDTO;
import com.co2fp.backend.backend.DTO.user.UserRegisterDTO;
import com.co2fp.backend.backend.DTO.user.UserResponseDTO;
import com.co2fp.backend.backend.entity.UserEntity;
import com.co2fp.backend.backend.repository.UserRepository;
import com.co2fp.backend.backend.service.AnswerServiceImplement;
import com.co2fp.backend.backend.service.TransportServiceImplement;
import com.co2fp.backend.backend.service.UserServiceImplement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserServiceImplement userService;
    private final TransportServiceImplement transportService;
    private final AnswerServiceImplement answerService;
    private final UserRepository userRepository;
//    private final ResulService

    public UserController(UserServiceImplement userservice, TransportServiceImplement transportService, AnswerServiceImplement answerservice, UserRepository userRepository) {
        this.userService = userservice;
        this.transportService = transportService;
        this.answerService = answerservice;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> userResponseDTOList = userService.getAllUsers();
        return new ResponseEntity<>(userResponseDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") Long userId) {
        UserResponseDTO userResponseDTO = userService.getUserById(userId);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> addUser(@RequestBody UserRegisterDTO User) {
        try {
            UserEntity registeredUser = userService.addUser(User);
            Map<String, Object> response = new HashMap<>();
            response.put("userId", registeredUser.getUserId());
            response.put("message", "User registered successfully");
            return ResponseEntity.ok().body(response); //
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Serializable>> signUser(@RequestBody UserLoginDTO userLoginDTO) {
        Map<String, Serializable> jwtToken = userService.authenticateUser(userLoginDTO);
        if (jwtToken != null) {
            return ResponseEntity.ok(jwtToken);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }
    }

    @GetMapping("/admins")
    public ResponseEntity<?> getAdmins() {
        List<UserResponseDTO> userResponseDTOList = userService.getAdmins();
        if (userResponseDTOList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "No admin users found."));
        }
        return ResponseEntity.ok(userResponseDTOList);
    }

    @GetMapping("/admins/getusers")
    public ResponseEntity<?> getUsersforAdmin() {
        List<UserResponseDTO> userResponseDTOList = userService.getUsersforAdmin();
        if (userResponseDTOList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "No Users Found"));
        }
        return ResponseEntity.ok(userResponseDTOList);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        Map<String, String> response = userService.deleteUser(id);

        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/cascade/{id}")
    public ResponseEntity<Map<String, String>> deleteUserCascade(@PathVariable Long id) {
        Map<String, String> response = userService.deleteUserCascade(id);

        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/setadmin")
    public ResponseEntity<Map<String, String>> setAdmin(@RequestBody UserResponseDTO userResponseDTO) {
        Map<String, String> response = userService.setUserforAdmin(userResponseDTO);
        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.ok(response);
    }

}
