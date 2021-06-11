package com.test.auth.security.jwt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;

@Configuration
public class RSAConfiguration{
	@Bean
	public RSAPublicKey getPublicKey()
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, URISyntaxException {
		String publicKeyContent = new String(
				Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("public_key.pem").toURI())));
		publicKeyContent = publicKeyContent.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "")
				.replace("-----END PUBLIC KEY-----", "");
		KeyFactory kf = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getMimeDecoder().decode(publicKeyContent));
		RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
		return pubKey;
	}
	@Bean
	public PrivateKey getPrivateKey()
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, URISyntaxException {
		String privateKeyContent = new String(
				Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("private_key_pkcs8.pem").toURI())));
		privateKeyContent = privateKeyContent.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "")
				.replace("-----END PRIVATE KEY-----", "");
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getMimeDecoder().decode(privateKeyContent));
		PrivateKey priKey = kf.generatePrivate(keySpecPKCS8);
		return priKey;
	}
	@Bean
	public JWKSet jwkSet(RSAPublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, URISyntaxException {
	    RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) publicKey)
	      .keyUse(KeyUse.SIGNATURE)
	      .algorithm(JWSAlgorithm.RS512)
	      .keyID("bael-key-id");
	    return new JWKSet(builder.build());
	}
	

}