import { getAllAppointments } from './services/appointmentRecordService.js';
import { createPatientRow } from './components/patientRows.js';

let selectedDate = new Date().toISOString().slice(0, 10);
const token = localStorage.getItem('token');
let patientName = 'null';

window.addEventListener('DOMContentLoaded', () => {
  const searchBar = document.getElementById('searchBar');
  const todayButton = document.getElementById('todayButton');
  const datePicker = document.getElementById('datePicker');

  if (searchBar) {
    searchBar.addEventListener('input', (event) => {
      const value = event.target.value.trim();
      patientName = value.length ? value : 'null';
      loadAppointments();
    });
  }

  if (todayButton) {
    todayButton.addEventListener('click', () => {
      selectedDate = new Date().toISOString().slice(0, 10);
      if (datePicker) datePicker.value = selectedDate;
      loadAppointments();
    });
  }

  if (datePicker) {
    datePicker.value = selectedDate;
    datePicker.addEventListener('change', (event) => {
      selectedDate = event.target.value;
      loadAppointments();
    });
  }

  loadAppointments();
});

export async function loadAppointments() {
  const patientTableBody = document.getElementById('patientTableBody');
  if (!patientTableBody) return;
  patientTableBody.innerHTML = '';

  try {
    const result = await getAllAppointments(selectedDate, patientName, token);
    const appointments = result?.appointments || [];

    if (!appointments.length) {
      patientTableBody.innerHTML = `<tr><td colspan="5">No Appointments found for today.</td></tr>`;
      return;
    }

    appointments.forEach((appointment) => {
      const patient = {
        id: appointment.patient?.id || appointment.patientId || 'N/A',
        name: appointment.patient?.name || appointment.patientName || 'N/A',
        phone: appointment.patient?.phone || appointment.patientPhone || 'N/A',
        email: appointment.patient?.email || appointment.patientEmail || 'N/A',
      };
      const row = createPatientRow(patient, appointment.id, appointment.doctorId || null);
      patientTableBody.appendChild(row);
    });
  } catch (error) {
    console.error('Error loading appointments:', error);
    patientTableBody.innerHTML = `<tr><td colspan="5">Error loading appointments. Try again later.</td></tr>`;
  }
}
