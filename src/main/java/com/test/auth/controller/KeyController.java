package com.test.auth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.jwk.JWKSet;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class KeyController {

	@Autowired
	private JWKSet jwkSet;

	@GetMapping("/.well-known/jwks.json")
	public Map<String, Object> keys() {
		return this.jwkSet.toJSONObject();
	}

}