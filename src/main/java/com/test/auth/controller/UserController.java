package com.test.auth.controller;

import java.security.interfaces.RSAPublicKey;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.auth.model.User;
import com.test.auth.payload.LoginRequest;
import com.test.auth.payload.SignupRequest;
import com.test.auth.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	RSAPublicKey publicKey;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
		return ResponseEntity.ok(userService.login(loginRequest));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		return userService.signup(signUpRequest);
	}

	@PutMapping({ "updateUser/{userId}" })
	public ResponseEntity<User> updateUser(@PathVariable("userId") int userId, @RequestBody User user) {
		userService.updateUser(userId, user);
		return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
	}

	@DeleteMapping({ "deleteUser/{userId}" })
	public ResponseEntity<String> deleteUser(@PathVariable("userId") int userId) {
		userService.deleteUser(userId);
		return new ResponseEntity<>("True", HttpStatus.OK);
	}

}