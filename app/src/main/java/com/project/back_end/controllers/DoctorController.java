package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

	private final DoctorService doctorService;
	private final Service service;

	public DoctorController(DoctorService doctorService, Service service) {
		this.doctorService = doctorService;
		this.service = service;
	}

	@GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
	public ResponseEntity<Map<String, Object>> getDoctorAvailability(
			@PathVariable String user,
			@PathVariable Long doctorId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@PathVariable String token) {
		ResponseEntity<Map<String, String>> validation = service.validateToken(token, user);
		if (validation != null) {
			return ResponseEntity.status(validation.getStatusCode()).body(new HashMap<>(validation.getBody()));
		}
		Map<String, Object> body = new HashMap<>();
		body.put("availability", doctorService.getDoctorAvailability(doctorId, date));
		return ResponseEntity.ok(body);
	}

	@GetMapping
	public ResponseEntity<Map<String, Object>> getDoctor() {
		Map<String, Object> body = new HashMap<>();
		body.put("doctors", doctorService.getDoctors());
		return ResponseEntity.ok(body);
	}

	@PostMapping("/{token}")
	public ResponseEntity<Map<String, String>> saveDoctor(@Valid @RequestBody Doctor doctor, @PathVariable String token) {
		ResponseEntity<Map<String, String>> validation = service.validateToken(token, "admin");
		if (validation != null) {
			return validation;
		}

		int result = doctorService.saveDoctor(doctor);
		return switch (result) {
			case 1 -> response(HttpStatus.CREATED, "Doctor added to db");
			case -1 -> response(HttpStatus.CONFLICT, "Doctor already exists");
			default -> response(HttpStatus.INTERNAL_SERVER_ERROR, "Some internal error occurred");
		};
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody Login login) {
		return doctorService.validateDoctor(login);
	}

	@PutMapping("/{token}")
	public ResponseEntity<Map<String, String>> updateDoctor(@Valid @RequestBody Doctor doctor, @PathVariable String token) {
		ResponseEntity<Map<String, String>> validation = service.validateToken(token, "admin");
		if (validation != null) {
			return validation;
		}

		int result = doctorService.updateDoctor(doctor);
		return switch (result) {
			case 1 -> response(HttpStatus.OK, "Doctor updated");
			case -1 -> response(HttpStatus.NOT_FOUND, "Doctor not found");
			default -> response(HttpStatus.INTERNAL_SERVER_ERROR, "Some internal error occurred");
		};
	}

	@DeleteMapping("/{id}/{token}")
	public ResponseEntity<Map<String, String>> deleteDoctor(@PathVariable long id, @PathVariable String token) {
		ResponseEntity<Map<String, String>> validation = service.validateToken(token, "admin");
		if (validation != null) {
			return validation;
		}

		int result = doctorService.deleteDoctor(id);
		return switch (result) {
			case 1 -> response(HttpStatus.OK, "Doctor deleted successfully");
			case -1 -> response(HttpStatus.NOT_FOUND, "Doctor not found with id");
			default -> response(HttpStatus.INTERNAL_SERVER_ERROR, "Some internal error occurred");
		};
	}

	@GetMapping("/filter/{name}/{time}/{speciality}")
	public ResponseEntity<Map<String, Object>> filter(
			@PathVariable String name,
			@PathVariable String time,
			@PathVariable String speciality) {
		return ResponseEntity.ok(service.filterDoctor(name, speciality, time));
	}

	private ResponseEntity<Map<String, String>> response(HttpStatus status, String message) {
		Map<String, String> body = new HashMap<>();
		body.put("message", message);
		return ResponseEntity.status(status).body(body);
	}
}
