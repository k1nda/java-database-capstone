package com.project.back_end.services;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@org.springframework.stereotype.Service
public class Service {

	private final TokenService tokenService;
	private final AdminRepository adminRepository;
	private final DoctorRepository doctorRepository;
	private final PatientRepository patientRepository;
	private final DoctorService doctorService;
	private final PatientService patientService;
	private final AppointmentService appointmentService;

	public Service(
			TokenService tokenService,
			AdminRepository adminRepository,
			DoctorRepository doctorRepository,
			PatientRepository patientRepository,
			DoctorService doctorService,
			PatientService patientService,
			AppointmentService appointmentService) {
		this.tokenService = tokenService;
		this.adminRepository = adminRepository;
		this.doctorRepository = doctorRepository;
		this.patientRepository = patientRepository;
		this.doctorService = doctorService;
		this.patientService = patientService;
		this.appointmentService = appointmentService;
	}

	public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
		if (!tokenService.validateToken(token, user)) {
			Map<String, String> body = new HashMap<>();
			body.put("message", "Invalid or expired token");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
		}
		return null;
	}

	public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
		Map<String, String> body = new HashMap<>();
		try {
			Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());
			if (admin == null || !admin.getPassword().equals(receivedAdmin.getPassword())) {
				body.put("message", "Invalid credentials");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
			}
			body.put("token", tokenService.generateToken(admin.getUsername()));
			body.put("message", "Login successful");
			return ResponseEntity.ok(body);
		} catch (Exception ex) {
			body.put("message", "Some internal error occurred");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
		}
	}

	public Map<String, Object> filterDoctor(String name, String specialty, String time) {
		String normalizedName = normalize(name);
		String normalizedSpecialty = normalize(specialty);
		String normalizedTime = normalize(time);

		if (normalizedName != null && normalizedSpecialty != null && normalizedTime != null) {
			return doctorService.filterDoctorsByNameSpecilityandTime(normalizedName, normalizedSpecialty, normalizedTime);
		}
		if (normalizedName != null && normalizedSpecialty != null) {
			return doctorService.filterDoctorByNameAndSpecility(normalizedName, normalizedSpecialty);
		}
		if (normalizedName != null && normalizedTime != null) {
			return doctorService.filterDoctorByNameAndTime(normalizedName, normalizedTime);
		}
		if (normalizedSpecialty != null && normalizedTime != null) {
			return doctorService.filterDoctorByTimeAndSpecility(normalizedSpecialty, normalizedTime);
		}
		if (normalizedName != null) {
			return doctorService.findDoctorByName(normalizedName);
		}
		if (normalizedSpecialty != null) {
			return doctorService.filterDoctorBySpecility(normalizedSpecialty);
		}
		if (normalizedTime != null) {
			return doctorService.filterDoctorsByTime(normalizedTime);
		}

		Map<String, Object> body = new HashMap<>();
		body.put("doctors", doctorService.getDoctors());
		return body;
	}

	public int validateAppointment(Appointment appointment) {
		if (appointment.getDoctor() == null || appointment.getDoctor().getId() == null) {
			return -1;
		}
		if (doctorRepository.findById(appointment.getDoctor().getId()).isEmpty()) {
			return -1;
		}

		List<String> availableSlots = doctorService.getDoctorAvailability(
				appointment.getDoctor().getId(),
				appointment.getAppointmentTime().toLocalDate());
		String requestedSlot = appointment.getAppointmentTime().toLocalTime() + "-"
				+ appointment.getAppointmentTime().toLocalTime().plusHours(1);
		return availableSlots.contains(requestedSlot) ? 1 : 0;
	}

	public boolean validatePatient(Patient patient) {
		return patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone()) == null;
	}

	public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
		Map<String, String> body = new HashMap<>();
		try {
			Patient patient = patientRepository.findByEmail(login.getIdentifier());
			if (patient == null || !patient.getPassword().equals(login.getPassword())) {
				body.put("message", "Invalid credentials");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
			}
			body.put("token", tokenService.generateToken(patient.getEmail()));
			body.put("message", "Login successful");
			return ResponseEntity.ok(body);
		} catch (Exception ex) {
			body.put("message", "Some internal error occurred");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
		}
	}

	public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {
		Patient patient = patientRepository.findByEmail(tokenService.extractIdentifier(token));
		if (patient == null) {
			Map<String, Object> body = new HashMap<>();
			body.put("message", "Patient not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
		}

		String normalizedCondition = normalize(condition);
		String normalizedName = normalize(name);

		if (normalizedCondition != null && normalizedName != null) {
			return patientService.filterByDoctorAndCondition(normalizedCondition, normalizedName, patient.getId());
		}
		if (normalizedCondition != null) {
			return patientService.filterByCondition(normalizedCondition, patient.getId());
		}
		if (normalizedName != null) {
			return patientService.filterByDoctor(normalizedName, patient.getId());
		}
		return patientService.getPatientAppointment(patient.getId(), token);
	}

	private String normalize(String value) {
		if (value == null || value.isBlank() || "null".equalsIgnoreCase(value)) {
			return null;
		}
		return value;
	}
}
