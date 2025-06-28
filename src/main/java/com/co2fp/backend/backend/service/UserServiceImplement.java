package com.co2fp.backend.backend.service;

import com.co2fp.backend.backend.DTO.user.UserLoginDTO;
import com.co2fp.backend.backend.DTO.user.UserRegisterDTO;
import com.co2fp.backend.backend.DTO.user.UserResponseDTO;
import com.co2fp.backend.backend.entity.UserEntity;
import com.co2fp.backend.backend.repository.UserRepository;
import com.co2fp.backend.backend.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImplement implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserServiceImplement(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public List<UserResponseDTO> getAllUsers() {
        log.info("Fetching All Users");
        List<UserEntity> userEntityList = userRepository.findAll();
        List<UserResponseDTO> userResponseDTOList =userEntityList.stream()
                .map(this::convertToUserResponseDTO)
                .collect(Collectors.toList());
        log.info("Fetched ALl Users");
        return userResponseDTOList;
    }

    @Override
    public UserResponseDTO getUserById(Long User_Id) {
        log.info("Fetching User By Id: {}",User_Id);
        UserEntity userEntity=userRepository.findById(User_Id)
                .orElseThrow(()->new RuntimeException("User with given doesnt exists"));
        UserResponseDTO userResponseDTO= convertToUserResponseDTO(userEntity);
        log.info("Fetched User by id: {}",User_Id);
        return userResponseDTO;
    }

    @Override
    public UserEntity addUser(UserRegisterDTO user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("User with this username or email already exists.");
        }
        // Encrypt password before saving
        String hashedPassword = passwordEncoder.encode(user.getPassword());

        log.info("request came");
        String currentUsername = user.getUsername();
        boolean isAdmin = false;
        if (currentUsername != null && currentUsername.toLowerCase().contains("admin")) {
            isAdmin = true;
        }
        // Create user object
        UserEntity newUser = UserEntity.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(hashedPassword)
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .admin(isAdmin)
                .build();
        return userRepository.save(newUser);
    }

    @Override
    public Map<String, Serializable> authenticateUser(UserLoginDTO user) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(user.getUsername());
        if(userEntityOptional.isPresent()){
            UserEntity User = userEntityOptional.get();
            if(passwordEncoder.matches(user.getPassword(),User.getPassword())){
                String token = jwtUtil.generateToken(user.getUsername());
                String lastname = User.getLastname();
                Long id = User.getUserId();
                Boolean admin = User.getAdmin();

                return new HashMap<>() {{
                    put("token", token);
                    put("lastname", lastname);
                    put("id", id);
                    put("admin", admin != null ? admin : false);
                }};
            }
        }
        return null;
    }

    @Override
    public List<UserResponseDTO> getAdmins() {
        log.info("Fetching All Admins");
        //Fetch Users
        List<UserEntity> userEntityList = userRepository.findByAdminTrue();
        List<UserResponseDTO> userResponseDTOList =userEntityList.stream()
                .map(this::convertToUserResponseDTO)
                .collect(Collectors.toList());
        log.info("Fetched ALl Admins");
        return userResponseDTOList;
    }

    @Override
    public List<UserResponseDTO> getUsersforAdmin() {
        log.info("Fetching All Users");
        //Fetch Users
        List<UserEntity> userEntityList = userRepository.findAll();
        List<UserResponseDTO> userResponseDTOList =userEntityList.stream()
                .map(this::convertToUserResponseDTO)
                .collect(Collectors.toList());
        log.info("Fetched ALl Users");
        return userResponseDTOList;
    }

    @Override
    public Map<String, String> setUserforAdmin(UserResponseDTO userListDTO) {
        log.info("Updating admin status for user: " + userListDTO.getUsername());

        try {
            Optional<UserEntity> userEntity = userRepository.findByUsername(userListDTO.getUsername());

            if (userEntity.isPresent()) {
                UserEntity user = userEntity.get();
                user.setAdmin(userListDTO.isAdmin());
                userRepository.save(user);

                return Map.of("message", "User updated as " + (user.getAdmin() ? "admin" : "not admin"));
            } else {
                return Map.of("error", "User not found");
            }
        } catch (Exception e) {
            log.error("Error setting user as admin: " + e.getMessage());
            return Map.of("error", "Failed to update user as admin");
        }
    }

    @Override
    public Map<String, String> deleteUser(Long User_Id) {
        log.info("Request to delete User with ID: " + User_Id);

        if (!userRepository.existsById(User_Id)) {
            log.warn("User not found.");
            return Map.of("error", "User not found.");
        }

        UserEntity user = userRepository.findById(User_Id).orElseThrow();

        if (!user.getAnswer().isEmpty()) {
            log.warn("Cannot delete user with ID " + User_Id + " because it has linked answers.");
            return Map.of("error", "Cannot delete: User is linked to existing answers.");
        }

        userRepository.deleteById(User_Id);
        log.info("User deleted successfully.");

        return Map.of("message", "User deleted successfully.");
    }

    @Override
    public Map<String, String> deleteUserCascade(Long User_Id) {
        log.info("Request to cascade delete User with ID: " + User_Id);

        if (!userRepository.existsById(User_Id)) {
            log.warn("User not found.");
            return Map.of("error", "User not found.");
        }

        userRepository.deleteById(User_Id);
        log.info("user and linked answers deleted successfully.");

        return Map.of("message", "User and all linked answers deleted successfully.");
    }

    @Override
    public Map<String, String> deleteUserAndKeepAnswers(Long User_Id) {
        log.info("Request to delete User with ID: " + User_Id + " but keep answers.");

        if (!userRepository.existsById(User_Id)) {
            log.warn("User not found.");
            return Map.of("error", "User not found.");
        }

        UserEntity User = userRepository.findById(User_Id).orElseThrow();

        // Set transport reference in linked answers to NULL
        User.getAnswer().forEach(answer -> answer.setUser(null));

        userRepository.deleteById(User_Id);

        log.info("User deleted, but linked answers were kept.");
        return Map.of("message", "User deleted, but linked answers were kept.");
    }

    private UserResponseDTO convertToUserResponseDTO(UserEntity userEntity) {

        return UserResponseDTO.builder()
                .user_id(userEntity.getUserId())
                .email(userEntity.getEmail())
                .firstname(userEntity.getFirstname())
                .lastname(userEntity.getLastname())
                .admin(userEntity.getAdmin())
                .build();
    }

}
