package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.path}patient")
public class PatientController {

	private final PatientService patientService;
	private final Service service;

	public PatientController(PatientService patientService, Service service) {
		this.patientService = patientService;
		this.service = service;
	}

	@GetMapping("/{token}")
	public ResponseEntity<Map<String, Object>> getPatient(@PathVariable String token) {
		ResponseEntity<Map<String, String>> validation = service.validateToken(token, "patient");
		if (validation != null) {
			return ResponseEntity.status(validation.getStatusCode()).body(new HashMap<>(validation.getBody()));
		}
		return patientService.getPatientDetails(token);
	}

	@PostMapping
	public ResponseEntity<Map<String, String>> createPatient(@Valid @RequestBody Patient patient) {
		if (!service.validatePatient(patient)) {
			return response(HttpStatus.CONFLICT, "Patient with email id or phone no already exist");
		}
		int result = patientService.createPatient(patient);
		return result == 1
				? response(HttpStatus.CREATED, "Signup successful")
				: response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody Login login) {
		return service.validatePatientLogin(login);
	}

	@GetMapping("/{id}/{user}/{token}")
	public ResponseEntity<Map<String, Object>> getPatientAppointment(
			@PathVariable Long id,
			@PathVariable String user,
			@PathVariable String token) {
		ResponseEntity<Map<String, String>> validation = service.validateToken(token, user);
		if (validation != null) {
			return ResponseEntity.status(validation.getStatusCode()).body(new HashMap<>(validation.getBody()));
		}

		if ("patient".equalsIgnoreCase(user)) {
			return patientService.getPatientAppointment(id, token);
		}
		return patientService.getPatientAppointmentForDoctor(id);
	}

	@GetMapping("/filter/{condition}/{name}/{token}")
	public ResponseEntity<Map<String, Object>> filterPatientAppointment(
			@PathVariable String condition,
			@PathVariable String name,
			@PathVariable String token) {
		ResponseEntity<Map<String, String>> validation = service.validateToken(token, "patient");
		if (validation != null) {
			return ResponseEntity.status(validation.getStatusCode()).body(new HashMap<>(validation.getBody()));
		}
		return service.filterPatient(condition, name, token);
	}

	private ResponseEntity<Map<String, String>> response(HttpStatus status, String message) {
		Map<String, String> body = new HashMap<>();
		body.put("message", message);
		return ResponseEntity.status(status).body(body);
	}
}


