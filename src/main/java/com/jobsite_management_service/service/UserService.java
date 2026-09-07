package com.jobsite_management_service.service;

import com.jobsite_management_service.repository.UserRepository;

public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //grab a userId by email
    public Long getUserIdByEmail(String email) {
        return userRepository.getIdByEmail(email);
    }
}
