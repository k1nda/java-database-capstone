package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public PatientService(
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository,
            TokenService tokenService) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long id, String token) {
        Patient tokenPatient = patientRepository.findByEmail(tokenService.extractIdentifier(token));
        if (tokenPatient == null || !tokenPatient.getId().equals(id)) {
            return response(HttpStatus.UNAUTHORIZED, "message", "Unauthorized access");
        }
        return appointmentsResponse(appointmentRepository.findByPatientId(id));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getPatientAppointmentForDoctor(Long id) {
        return appointmentsResponse(appointmentRepository.findByPatientId(id));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByCondition(String condition, Long id) {
        Integer status = mapCondition(condition);
        if (status == null) {
            return response(HttpStatus.BAD_REQUEST, "message", "Invalid condition");
        }
        return appointmentsResponse(appointmentRepository.findByPatient_IdAndStatusOrderByAppointmentTimeAsc(id, status));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByDoctor(String name, Long patientId) {
        return appointmentsResponse(appointmentRepository.filterByDoctorNameAndPatientId(name, patientId));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(String condition, String name, long patientId) {
        Integer status = mapCondition(condition);
        if (status == null) {
            return response(HttpStatus.BAD_REQUEST, "message", "Invalid condition");
        }
        return appointmentsResponse(appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(name, patientId, status));
    }

    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {
        try {
            Patient patient = patientRepository.findByEmail(tokenService.extractIdentifier(token));
            if (patient == null) {
                return response(HttpStatus.NOT_FOUND, "message", "Patient not found");
            }
            Map<String, Object> body = new HashMap<>();
            body.put("patient", patient);
            return ResponseEntity.ok(body);
        } catch (Exception ex) {
            return response(HttpStatus.INTERNAL_SERVER_ERROR, "message", "Some internal error occurred");
        }
    }

    private ResponseEntity<Map<String, Object>> appointmentsResponse(List<Appointment> appointments) {
        Map<String, Object> body = new HashMap<>();
        body.put("appointments", appointments.stream().map(this::toDto).toList());
        return ResponseEntity.ok(body);
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

    private Integer mapCondition(String condition) {
        if (condition == null) {
            return null;
        }
        return switch (condition.trim().toLowerCase()) {
            case "future", "upcoming" -> 0;
            case "past", "completed" -> 1;
            default -> null;
        };
    }

    private ResponseEntity<Map<String, Object>> response(HttpStatus status, String key, Object value) {
        Map<String, Object> body = new HashMap<>();
        body.put(key, value);
        return ResponseEntity.status(status).body(body);
    }
}
