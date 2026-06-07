// createDoctorCard: builds a DOM card for a doctor and returns it
export async function createDoctorCard(doctor) {
  const card = document.createElement('div');
  card.classList.add('doctor-card');

  const role = localStorage.getItem('userRole');

  // Info section
  const infoDiv = document.createElement('div');
  infoDiv.classList.add('doctor-info');

  const name = document.createElement('h3');
  name.textContent = doctor.name || 'Unknown Doctor';

  const specialization = document.createElement('p');
  specialization.textContent = doctor.specialty || doctor.specialization || '';

  const email = document.createElement('p');
  email.textContent = doctor.email || '';

  const availability = document.createElement('p');
  if (Array.isArray(doctor.available_times)) {
    availability.textContent = 'Available: ' + doctor.available_times.join(', ');
  } else if (typeof doctor.available_times === 'string') {
    availability.textContent = 'Available: ' + doctor.available_times;
  } else {
    availability.textContent = '';
  }

  infoDiv.appendChild(name);
  if (specialization.textContent) infoDiv.appendChild(specialization);
  if (email.textContent) infoDiv.appendChild(email);
  if (availability.textContent) infoDiv.appendChild(availability);

  // Actions
  const actionsDiv = document.createElement('div');
  actionsDiv.classList.add('card-actions');

  if (role === 'admin') {
    const removeBtn = document.createElement('button');
    removeBtn.textContent = 'Delete';
    removeBtn.classList.add('danger');
    removeBtn.addEventListener('click', async () => {
      if (!confirm('Delete this doctor?')) return;
      const token = localStorage.getItem('token');
      try {
        if (typeof window.deleteDoctor === 'function') {
          await window.deleteDoctor(doctor.id, token);
          card.remove();
        } else if (typeof deleteDoctor === 'function') {
          await deleteDoctor(doctor.id, token);
          card.remove();
        } else {
          alert('Delete functionality not available in this environment.');
        }
      } catch (err) {
        console.error(err);
        alert('Failed to delete doctor');
      }
    });
    actionsDiv.appendChild(removeBtn);
  } else if (role === 'patient') {
    const bookNow = document.createElement('button');
    bookNow.textContent = 'Book Now';
    bookNow.addEventListener('click', () => { alert('Please log in to book an appointment.'); });
    actionsDiv.appendChild(bookNow);
  } else if (role === 'loggedPatient') {
    const bookNow = document.createElement('button');
    bookNow.textContent = 'Book Now';
    bookNow.addEventListener('click', async (e) => {
      const token = localStorage.getItem('token');
      if (!token) { alert('Please log in.'); window.location.href = '/login'; return; }
      try {
        let patientData = null;
        if (typeof window.getPatientData === 'function') patientData = await window.getPatientData(token);
        else if (typeof getPatientData === 'function') patientData = await getPatientData(token);
        // showBookingOverlay could be provided by the page
        if (typeof window.showBookingOverlay === 'function') {
          window.showBookingOverlay(e, doctor, patientData);
        } else {
          alert('Booking UI not available in this environment.');
        }
      } catch (err) {
        console.error(err);
        alert('Unable to fetch patient data.');
      }
    });
    actionsDiv.appendChild(bookNow);
  }

  card.appendChild(infoDiv);
  card.appendChild(actionsDiv);

  return card;
}

// Export default for easy imports
export default createDoctorCard;
