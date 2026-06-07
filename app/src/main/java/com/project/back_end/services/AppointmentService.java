package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
public class AppointmentService {

	private final AppointmentRepository appointmentRepository;
	private final PatientRepository patientRepository;
	private final DoctorRepository doctorRepository;
	private final TokenService tokenService;

	public AppointmentService(
			AppointmentRepository appointmentRepository,
			PatientRepository patientRepository,
			DoctorRepository doctorRepository,
			TokenService tokenService) {
		this.appointmentRepository = appointmentRepository;
		this.patientRepository = patientRepository;
		this.doctorRepository = doctorRepository;
		this.tokenService = tokenService;
	}

	@Transactional
	public int bookAppointment(Appointment appointment) {
		try {
			hydrateReferences(appointment);
			appointmentRepository.save(appointment);
			return 1;
		} catch (Exception ex) {
			return 0;
		}
	}

	@Transactional
	public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment, String token) {
		Map<String, String> body = new HashMap<>();
		try {
			Optional<Appointment> existingOptional = appointmentRepository.findById(appointment.getId());
			if (existingOptional.isEmpty()) {
				body.put("message", "Appointment not found");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
			}

			Appointment existingAppointment = existingOptional.get();
			Patient tokenPatient = patientRepository.findByEmail(tokenService.extractIdentifier(token));
			if (tokenPatient == null || !existingAppointment.getPatient().getId().equals(tokenPatient.getId())) {
				body.put("message", "Unauthorized access");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
			}

			hydrateReferences(appointment);
			int validationStatus = validateAppointmentSlot(appointment, appointment.getId());
			if (validationStatus == -1) {
				body.put("message", "Doctor not found");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
			}
			if (validationStatus == 0) {
				body.put("message", "Appointment slot unavailable");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
			}

			appointmentRepository.save(appointment);
			body.put("message", "Appointment updated");
			return ResponseEntity.ok(body);
		} catch (Exception ex) {
			body.put("message", "Some internal error occurred");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
		}
	}

	@Transactional
	public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
		Map<String, String> body = new HashMap<>();
		try {
			Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);
			if (appointmentOptional.isEmpty()) {
				body.put("message", "Appointment not found");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
			}

			Appointment appointment = appointmentOptional.get();
			Patient patient = patientRepository.findByEmail(tokenService.extractIdentifier(token));
			if (patient == null || !appointment.getPatient().getId().equals(patient.getId())) {
				body.put("message", "Unauthorized access");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
			}

			appointmentRepository.delete(appointment);
			body.put("message", "Appointment cancelled successfully");
			return ResponseEntity.ok(body);
		} catch (Exception ex) {
			body.put("message", "Some internal error occurred");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
		}
	}

	@Transactional(readOnly = true)
	public Map<String, Object> getAppointment(String pname, LocalDate date, String token) {
		String doctorEmail = tokenService.extractIdentifier(token);
		Doctor doctor = doctorRepository.findByEmail(doctorEmail);
		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = date.plusDays(1).atStartOfDay().minusSeconds(1);

		List<Appointment> appointments = normalize(pname) == null
				? appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctor.getId(), start, end)
				: appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
						doctor.getId(), normalize(pname), start, end);

		Map<String, Object> body = new HashMap<>();
		body.put("appointments", appointments.stream().map(this::toDto).toList());
		return body;
	}

	@Transactional
	public void changeStatus(Long appointmentId, int status) {
		appointmentRepository.findById(appointmentId).ifPresent(appointment -> {
			appointment.setStatus(status);
			appointmentRepository.save(appointment);
		});
	}

	public int validateAppointmentSlot(Appointment appointment) {
		return validateAppointmentSlot(appointment, null);
	}

	private int validateAppointmentSlot(Appointment appointment, Long excludedAppointmentId) {
		if (appointment.getDoctor() == null || appointment.getDoctor().getId() == null) {
			return -1;
		}

		Optional<Doctor> doctorOptional = doctorRepository.findById(appointment.getDoctor().getId());
		if (doctorOptional.isEmpty()) {
			return -1;
		}

		Doctor doctor = doctorOptional.get();
		String requestedSlot = toTimeSlot(appointment.getAppointmentTime());
		if (doctor.getAvailableTimes() == null || !doctor.getAvailableTimes().contains(requestedSlot)) {
			return 0;
		}

		LocalDate appointmentDate = appointment.getAppointmentTime().toLocalDate();
		LocalDateTime start = appointmentDate.atStartOfDay();
		LocalDateTime end = appointmentDate.plusDays(1).atStartOfDay().minusSeconds(1);
		boolean slotTaken = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctor.getId(), start, end)
				.stream()
				.filter(existing -> excludedAppointmentId == null || !existing.getId().equals(excludedAppointmentId))
				.anyMatch(existing -> existing.getAppointmentTime().equals(appointment.getAppointmentTime()));

		return slotTaken ? 0 : 1;
	}

	private void hydrateReferences(Appointment appointment) {
		if (appointment.getDoctor() != null && appointment.getDoctor().getId() != null) {
			doctorRepository.findById(appointment.getDoctor().getId()).ifPresent(appointment::setDoctor);
		}
		if (appointment.getPatient() != null && appointment.getPatient().getId() != null) {
			patientRepository.findById(appointment.getPatient().getId()).ifPresent(appointment::setPatient);
		}
	}

	private AppointmentDTO toDto(Appointment appointment) {
		return new AppointmentDTO(
				appointment.getId(),
				appointment.getDoctor().getId(),
				appointment.getDoctor().getName(),
				appointment.getPatient().getId(),
				appointment.getPatient().getName(),
				appointment.getPatient().getEmail(),
				appointment.getPatient().getPhone(),
				appointment.getPatient().getAddress(),
				appointment.getAppointmentTime(),
				appointment.getStatus());
	}

	private String normalize(String value) {
		if (value == null || value.isBlank() || "null".equalsIgnoreCase(value)) {
			return null;
		}
		return value;
	}

	private String toTimeSlot(LocalDateTime appointmentTime) {
		LocalTime start = appointmentTime.toLocalTime();
		LocalTime end = start.plusHours(1);
		return start + "-" + end;
	}
}
