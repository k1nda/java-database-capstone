# Smart Clinic Database Design

## MySQL Database Design

### Table: patients

* id: INT, Primary Key, Auto Increment
* first_name: VARCHAR(100), Not Null
* last_name: VARCHAR(100), Not Null
* email: VARCHAR(255), Unique
* phone: VARCHAR(20), Not Null
* date_of_birth: DATE
* address: VARCHAR(255)
* created_at: TIMESTAMP

### Table: doctors

* id: INT, Primary Key, Auto Increment
* first_name: VARCHAR(100), Not Null
* last_name: VARCHAR(100), Not Null
* specialty: VARCHAR(100), Not Null
* email: VARCHAR(255), Unique
* phone: VARCHAR(20)
* available_hours: VARCHAR(255)
* created_at: TIMESTAMP

### Table: appointments

* id: INT, Primary Key, Auto Increment
* patient_id: INT, Foreign Key → patients(id)
* doctor_id: INT, Foreign Key → doctors(id)
* appointment_time: DATETIME, Not Null
* status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)
* notes: VARCHAR(500)
* created_at: TIMESTAMP

### Table: admin

* id: INT, Primary Key, Auto Increment
* username: VARCHAR(100), Unique
* password_hash: VARCHAR(255), Not Null
* email: VARCHAR(255), Unique
* created_at: TIMESTAMP

### Table: clinic_locations

* id: INT, Primary Key, Auto Increment
* name: VARCHAR(100), Not Null
* address: VARCHAR(255), Not Null
* phone: VARCHAR(20)

### Relationships

* One patient can have many appointments.
* One doctor can have many appointments.
* Each appointment belongs to one patient and one doctor.
* Appointments should not be deleted when a patient is removed; historical records should be preserved.
* Doctors should not be allowed to have overlapping appointments.

## MongoDB Collection Design

### Collection: prescriptions

```json
{
  "_id": "ObjectId('64abc123456')",
  "appointmentId": 101,
  "patientId": 25,
  "doctorId": 8,
  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500mg",
      "frequency": "Every 6 hours"
    },
    {
      "name": "Vitamin C",
      "dosage": "1000mg",
      "frequency": "Once daily"
    }
  ],
  "doctorNotes": "Drink plenty of water and rest.",
  "tags": ["fever", "viral"],
  "refillCount": 2,
  "pharmacy": {
    "name": "Central Pharmacy",
    "location": "Main Street"
  },
  "createdAt": "2026-06-07T10:00:00Z"
}
```

### Design Notes

* MongoDB is used because prescription structures may change over time.
* Medication lists are stored as embedded documents.
* Patient, doctor, and appointment references are stored using IDs from MySQL.
* Additional fields can be added later without changing the database schema.
