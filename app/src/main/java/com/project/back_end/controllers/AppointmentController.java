package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
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
@RequestMapping("/appointments")
public class AppointmentController {

	private final AppointmentService appointmentService;
	private final Service service;

	public AppointmentController(AppointmentService appointmentService, Service service) {
		this.appointmentService = appointmentService;
		this.service = service;
	}

	@GetMapping("/{date}/{patientName}/{token}")
	public ResponseEntity<Map<String, Object>> getAppointments(
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@PathVariable String patientName,
			@PathVariable String token) {
		ResponseEntity<Map<String, String>> validation = service.validateToken(token, "doctor");
		if (validation != null) {
			return ResponseEntity.status(validation.getStatusCode()).body(new HashMap<>(validation.getBody()));
		}
		return ResponseEntity.ok(appointmentService.getAppointment(patientName, date, token));
	}

	@PostMapping("/{token}")
	public ResponseEntity<Map<String, String>> bookAppointment(
			@Valid @RequestBody Appointment appointment,
			@PathVariable String token) {
		ResponseEntity<Map<String, String>> validation = service.validateToken(token, "patient");
		if (validation != null) {
			return validation;
		}

		int appointmentValidation = service.validateAppointment(appointment);
		if (appointmentValidation == -1) {
			return response(HttpStatus.NOT_FOUND, "Doctor not found");
		}
		if (appointmentValidation == 0) {
			return response(HttpStatus.CONFLICT, "Appointment slot unavailable");
		}

		int result = appointmentService.bookAppointment(appointment);
		return result == 1
				? response(HttpStatus.CREATED, "Appointment booked successfully")
				: response(HttpStatus.INTERNAL_SERVER_ERROR, "Some internal error occurred");
	}

	@PutMapping("/{token}")
	public ResponseEntity<Map<String, String>> updateAppointment(
			@Valid @RequestBody Appointment appointment,
			@PathVariable String token) {
		ResponseEntity<Map<String, String>> validation = service.validateToken(token, "patient");
		if (validation != null) {
			return validation;
		}
		return appointmentService.updateAppointment(appointment, token);
	}

	@DeleteMapping("/{id}/{token}")
	public ResponseEntity<Map<String, String>> cancelAppointment(@PathVariable long id, @PathVariable String token) {
		ResponseEntity<Map<String, String>> validation = service.validateToken(token, "patient");
		if (validation != null) {
			return validation;
		}
		return appointmentService.cancelAppointment(id, token);
	}

	private ResponseEntity<Map<String, String>> response(HttpStatus status, String message) {
		Map<String, String> body = new HashMap<>();
		body.put("message", message);
		return ResponseEntity.status(status).body(body);
	}
}
