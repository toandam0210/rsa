package com.test.auth.service;

import org.springframework.http.ResponseEntity;

import com.test.auth.model.User;
import com.test.auth.payload.JwtResponse;
import com.test.auth.payload.LoginRequest;
import com.test.auth.payload.SignupRequest;


public interface UserService {
	 void updateUser(int id,User user);
	 void deleteUser(int id);
	 User getUserById(int id);
	 JwtResponse login(LoginRequest loginRequest) throws Exception;
	 ResponseEntity<?> signup(SignupRequest signUpRequest);
}
