-- Stored procedures for reporting lab
DELIMITER $

CREATE PROCEDURE GetDailyAppointmentReportByDoctor(
    IN report_date DATE
)
BEGIN
    SELECT 
        d.name AS doctor_name,
        a.appointment_time,
        a.status,
        p.name AS patient_name,
        p.phone AS patient_phone
    FROM 
        appointment a
    JOIN 
        doctor d ON a.doctor_id = d.id
    JOIN 
        patient p ON a.patient_id = p.id
    WHERE 
        DATE(a.appointment_time) = report_date
    ORDER BY 
        d.name, a.appointment_time;
END$

CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(
    IN input_month INT,
    IN input_year INT
)
BEGIN
    SELECT
        d.id AS doctor_id,
        d.name AS doctor_name,
        COUNT(a.patient_id) AS patients_seen
    FROM
        appointment a
    JOIN doctor d ON a.doctor_id = d.id
    WHERE
        MONTH(a.appointment_time) = input_month
        AND YEAR(a.appointment_time) = input_year
    GROUP BY
        d.id, d.name
    ORDER BY
        patients_seen DESC
    LIMIT 1;
END$

CREATE PROCEDURE GetDoctorWithMostPatientsByYear(
    IN input_year INT
)
BEGIN
    SELECT
        d.id AS doctor_id,
        d.name AS doctor_name,
        COUNT(a.patient_id) AS patients_seen
    FROM
        appointment a
    JOIN doctor d ON a.doctor_id = d.id
    WHERE
        YEAR(a.appointment_time) = input_year
    GROUP BY
        d.id, d.name
    ORDER BY
        patients_seen DESC
    LIMIT 1;
END$

DELIMITER ;

-- Example calls (uncomment to run in a mysql client):
-- CALL GetDailyAppointmentReportByDoctor('2025-04-15');
-- CALL GetDoctorWithMostPatientsByMonth(4, 2025);
-- CALL GetDoctorWithMostPatientsByYear(2025);
