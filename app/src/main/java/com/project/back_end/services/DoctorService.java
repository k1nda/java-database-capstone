package com.project.back_end.services;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
public class DoctorService {

	private final DoctorRepository doctorRepository;
	private final AppointmentRepository appointmentRepository;
	private final TokenService tokenService;

	public DoctorService(
			DoctorRepository doctorRepository,
			AppointmentRepository appointmentRepository,
			TokenService tokenService) {
		this.doctorRepository = doctorRepository;
		this.appointmentRepository = appointmentRepository;
		this.tokenService = tokenService;
	}

	@Transactional(readOnly = true)
	public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
		Optional<Doctor> doctorOptional = doctorRepository.findById(doctorId);
		if (doctorOptional.isEmpty()) {
			return List.of();
		}

		Doctor doctor = doctorOptional.get();
		List<String> availableTimes = doctor.getAvailableTimes() == null
				? new ArrayList<>()
				: new ArrayList<>(doctor.getAvailableTimes());

		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = date.plusDays(1).atStartOfDay().minusSeconds(1);
		Set<String> bookedSlots = appointmentRepository
				.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end)
				.stream()
				.map(this::toTimeSlot)
				.collect(Collectors.toSet());

		return availableTimes.stream()
				.filter(slot -> !bookedSlots.contains(slot))
				.toList();
	}

	public int saveDoctor(Doctor doctor) {
		try {
			if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
				return -1;
			}
			doctorRepository.save(doctor);
			return 1;
		} catch (Exception ex) {
			return 0;
		}
	}

	public int updateDoctor(Doctor doctor) {
		try {
			if (doctor.getId() == null || doctorRepository.findById(doctor.getId()).isEmpty()) {
				return -1;
			}
			doctorRepository.save(doctor);
			return 1;
		} catch (Exception ex) {
			return 0;
		}
	}

	@Transactional(readOnly = true)
	public List<Doctor> getDoctors() {
		return doctorRepository.findAll();
	}

	@Transactional
	public int deleteDoctor(long id) {
		try {
			Optional<Doctor> doctor = doctorRepository.findById(id);
			if (doctor.isEmpty()) {
				return -1;
			}
			appointmentRepository.deleteAllByDoctorId(id);
			doctorRepository.deleteById(id);
			return 1;
		} catch (Exception ex) {
			return 0;
		}
	}

	public ResponseEntity<Map<String, String>> validateDoctor(Login login) {
		Map<String, String> response = new HashMap<>();
		try {
			Doctor doctor = doctorRepository.findByEmail(login.getIdentifier());
			if (doctor == null || !doctor.getPassword().equals(login.getPassword())) {
				response.put("message", "Invalid credentials");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
			}

			response.put("token", tokenService.generateToken(doctor.getEmail()));
			response.put("message", "Login successful");
			return ResponseEntity.ok(response);
		} catch (Exception ex) {
			response.put("message", "Some internal error occurred");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@Transactional(readOnly = true)
	public Map<String, Object> findDoctorByName(String name) {
		return doctorsResponse(doctorRepository.findByNameLike(name));
	}

	@Transactional(readOnly = true)
	public Map<String, Object> filterDoctorsByNameSpecilityandTime(String name, String specialty, String amOrPm) {
		return doctorsResponse(filterDoctorByTime(
				doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty),
				amOrPm));
	}

	@Transactional(readOnly = true)
	public Map<String, Object> filterDoctorByNameAndTime(String name, String amOrPm) {
		return doctorsResponse(filterDoctorByTime(doctorRepository.findByNameLike(name), amOrPm));
	}

	@Transactional(readOnly = true)
	public Map<String, Object> filterDoctorByNameAndSpecility(String name, String specialty) {
		return doctorsResponse(doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty));
	}

	@Transactional(readOnly = true)
	public Map<String, Object> filterDoctorByTimeAndSpecility(String specialty, String amOrPm) {
		return doctorsResponse(filterDoctorByTime(doctorRepository.findBySpecialtyIgnoreCase(specialty), amOrPm));
	}

	@Transactional(readOnly = true)
	public Map<String, Object> filterDoctorBySpecility(String specialty) {
		return doctorsResponse(doctorRepository.findBySpecialtyIgnoreCase(specialty));
	}

	@Transactional(readOnly = true)
	public Map<String, Object> filterDoctorsByTime(String amOrPm) {
		return doctorsResponse(filterDoctorByTime(doctorRepository.findAll(), amOrPm));
	}

	private List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm) {
		if (amOrPm == null || amOrPm.isBlank()) {
			return doctors;
		}

		String normalized = amOrPm.trim().toLowerCase();
		return doctors.stream()
				.filter(doctor -> matchesTimeFilter(doctor.getAvailableTimes(), normalized))
				.toList();
	}

	private boolean matchesTimeFilter(List<String> availableTimes, String filter) {
		if (availableTimes == null || availableTimes.isEmpty()) {
			return false;
		}

		if ("am".equals(filter) || "pm".equals(filter)) {
			return availableTimes.stream().anyMatch(slot -> {
				LocalTime start = parseStartTime(slot);
				if (start == null) {
					return false;
				}
				return "am".equals(filter) ? start.isBefore(LocalTime.NOON) : !start.isBefore(LocalTime.NOON);
			});
		}

		return availableTimes.stream().anyMatch(slot -> slot.equalsIgnoreCase(filter));
	}

	private Map<String, Object> doctorsResponse(List<Doctor> doctors) {
		Map<String, Object> response = new HashMap<>();
		response.put("doctors", doctors);
		return response;
	}

	private String toTimeSlot(Appointment appointment) {
		LocalTime start = appointment.getAppointmentTime().toLocalTime();
		LocalTime end = start.plusHours(1);
		return start + "-" + end;
	}

	private LocalTime parseStartTime(String slot) {
		try {
			return LocalTime.parse(slot.split("-")[0]);
		} catch (Exception ex) {
			return null;
		}
	}
}
