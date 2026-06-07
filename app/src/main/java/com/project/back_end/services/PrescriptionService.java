package com.project.back_end.services;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@org.springframework.stereotype.Service
public class PrescriptionService {

	private final PrescriptionRepository prescriptionRepository;

	public PrescriptionService(PrescriptionRepository prescriptionRepository) {
		this.prescriptionRepository = prescriptionRepository;
	}

	public ResponseEntity<Map<String, String>> savePrescription(Prescription prescription) {
		Map<String, String> body = new HashMap<>();
		try {
			if (!prescriptionRepository.findByAppointmentId(prescription.getAppointmentId()).isEmpty()) {
				body.put("message", "Prescription already exists for this appointment");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
			}
			prescriptionRepository.save(prescription);
			body.put("message", "Prescription saved");
			return ResponseEntity.status(HttpStatus.CREATED).body(body);
		} catch (Exception ex) {
			body.put("message", "Some internal error occurred");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
		}
	}

	public ResponseEntity<Map<String, Object>> getPrescription(Long appointmentId) {
		try {
			Map<String, Object> body = new HashMap<>();
			body.put("prescription", prescriptionRepository.findByAppointmentId(appointmentId));
			return ResponseEntity.ok(body);
		} catch (Exception ex) {
			Map<String, Object> body = new HashMap<>();
			body.put("message", "Some internal error occurred");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
		}
	}
}
