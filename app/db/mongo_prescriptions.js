// Use this file with the mongo shell inside the mongo-clinic container
use prescriptions;

db.prescriptions.insertMany([
  {
  "_id": ObjectId("6807dd712725f013281e7201"),
  "patientName": "John Smith",
  "appointmentId": 51,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 6 hours.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7202"),
  "patientName": "Emily Rose",
  "appointmentId": 52,
  "medication": "Aspirin",
  "dosage": "300mg",
  "doctorNotes": "Take 1 tablet after meals.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7203"),
  "patientName": "Michael Jordan",
  "appointmentId": 53,
  "medication": "Ibuprofen",
  "dosage": "400mg",
  "doctorNotes": "Take 1 tablet every 8 hours.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7204"),
  "patientName": "Olivia Moon",
  "appointmentId": 54,
  "medication": "Antihistamine",
  "dosage": "10mg",
  "doctorNotes": "Take 1 tablet daily before bed.",
  "_class": "com.project.back_end.models.Prescription"
},
{
  "_id": ObjectId("6807dd712725f013281e7205"),
  "patientName": "Liam King",
  "appointmentId": 55,
  "medication": "Vitamin C",
  "dosage": "1000mg",
  "doctorNotes": "Take 1 tablet daily.",
  "_class": "com.project.back_end.models.Prescription"
}
// (truncated here for brevity) — full list available in lab instructions
]);
