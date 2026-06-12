import { openModal } from './components/modals.js';
import { getDoctors, filterDoctors, saveDoctor } from './services/doctorServices.js';
import createDoctorCard from './components/doctorCard.js';

window.addEventListener('DOMContentLoaded', () => {
  const addDocBtn = document.getElementById('addDocBtn');
  if (addDocBtn) {
    addDocBtn.addEventListener('click', () => openModal('addDoctor'));
  }

  const searchBar = document.getElementById('searchBar');
  const filterTime = document.getElementById('filterTime');
  const filterSpecialty = document.getElementById('filterSpecialty');

  if (searchBar) searchBar.addEventListener('input', filterDoctorsOnChange);
  if (filterTime) filterTime.addEventListener('change', filterDoctorsOnChange);
  if (filterSpecialty) filterSpecialty.addEventListener('change', filterDoctorsOnChange);

  loadDoctorCards();
});

export async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error('loadDoctorCards error:', error);
    const contentDiv = document.getElementById('content');
    if (contentDiv) contentDiv.innerHTML = '<p>Unable to load doctors. Please try again later.</p>';
  }
}

export async function filterDoctorsOnChange() {
  const searchBar = document.getElementById('searchBar');
  const filterTime = document.getElementById('filterTime');
  const filterSpecialty = document.getElementById('filterSpecialty');

  const name = searchBar ? searchBar.value.trim() : '';
  const time = filterTime ? filterTime.value : '';
  const specialty = filterSpecialty ? filterSpecialty.value : '';

  try {
    const doctors = await filterDoctors(name, time, specialty);
    if (doctors.length > 0) {
      renderDoctorCards(doctors);
    } else {
      const contentDiv = document.getElementById('content');
      if (contentDiv) contentDiv.innerHTML = '<p>No doctors found with the given filters.</p>';
    }
  } catch (error) {
    console.error('filterDoctorsOnChange error:', error);
    alert('Unable to filter doctors. Please try again later.');
  }
}

export function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById('content');
  if (!contentDiv) return;

  contentDiv.innerHTML = '';

  if (!doctors || doctors.length === 0) {
    contentDiv.innerHTML = '<p>No doctors found.</p>';
    return;
  }

  doctors.forEach(async (doctor) => {
    const card = await createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

export async function adminAddDoctor() {
  const name = document.getElementById('doctorName')?.value.trim();
  const specialty = document.getElementById('specialization')?.value.trim();
  const email = document.getElementById('doctorEmail')?.value.trim();
  const password = document.getElementById('doctorPassword')?.value.trim();
  const phone = document.getElementById('doctorPhone')?.value.trim();
  const availabilityEls = document.querySelectorAll('input[name="availability"]:checked');

  if (!name || !specialty || !email || !password || !phone) {
    alert('Please complete all doctor fields.');
    return;
  }

  const available_times = Array.from(availabilityEls).map((input) => input.value);
  const doctor = { name, specialty, email, password, phone, available_times };
  const token = localStorage.getItem('token');

  if (!token) {
    alert('Admin authentication required. Please log in.');
    return;
  }

  try {
    const response = await saveDoctor(doctor, token);
    if (response.success) {
      alert(response.message || 'Doctor saved successfully.');
      document.getElementById('modal').style.display = 'none';
      loadDoctorCards();
    } else {
      alert(response.message || 'Failed to save doctor.');
    }
  } catch (error) {
    console.error('adminAddDoctor error:', error);
    alert('Unable to save doctor at this time.');
  }
}

window.adminAddDoctor = adminAddDoctor;
