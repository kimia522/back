package com.co2fp.backend.backend.service;

import com.co2fp.backend.backend.DTO.user.UserLoginDTO;
import com.co2fp.backend.backend.DTO.user.UserRegisterDTO;
import com.co2fp.backend.backend.DTO.user.UserResponseDTO;
import com.co2fp.backend.backend.entity.UserEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long User_Id);
    UserEntity addUser(UserRegisterDTO user);
    Map<String, Serializable> authenticateUser(UserLoginDTO user);
    List<UserResponseDTO> getAdmins();
    List<UserResponseDTO> getUsersforAdmin();
    Map<String,String> setUserforAdmin(UserResponseDTO userListDTO);
    Map<String, String> deleteUser(Long User_Id);
    Map<String, String> deleteUserCascade(Long User_Id);
    Map<String, String> deleteUserAndKeepAnswers(Long User_Id);

}
