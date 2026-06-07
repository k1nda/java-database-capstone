package com.project.back_end.services;

import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenService {

	private static final long TOKEN_EXPIRATION_MS = 7L * 24 * 60 * 60 * 1000;

	private final AdminRepository adminRepository;
	private final DoctorRepository doctorRepository;
	private final PatientRepository patientRepository;

	@Value("${jwt.secret}")
	private String secret;

	public TokenService(
			AdminRepository adminRepository,
			DoctorRepository doctorRepository,
			PatientRepository patientRepository) {
		this.adminRepository = adminRepository;
		this.doctorRepository = doctorRepository;
		this.patientRepository = patientRepository;
	}

	public String generateToken(String identifier) {
		Date issuedAt = new Date();
		Date expiration = new Date(issuedAt.getTime() + TOKEN_EXPIRATION_MS);

		return Jwts.builder()
				.subject(identifier)
				.issuedAt(issuedAt)
				.expiration(expiration)
				.signWith(getSigningKey())
				.compact();
	}

	public String extractIdentifier(String token) {
		String sanitizedToken = sanitizeToken(token);
		return Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(sanitizedToken)
				.getPayload()
				.getSubject();
	}

	public boolean validateToken(String token, String user) {
		try {
			String identifier = extractIdentifier(token);
			if (identifier == null || user == null) {
				return false;
			}

			return switch (user.trim().toLowerCase()) {
				case "admin" -> adminRepository.findByUsername(identifier) != null;
				case "doctor" -> doctorRepository.findByEmail(identifier) != null;
				case "patient" -> patientRepository.findByEmail(identifier) != null;
				default -> false;
			};
		} catch (Exception ex) {
			return false;
		}
	}

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	private String sanitizeToken(String token) {
		if (token == null) {
			return null;
		}
		if (token.startsWith("Bearer ")) {
			return token.substring(7);
		}
		return token;
	}
}
