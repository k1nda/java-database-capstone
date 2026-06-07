import createDoctorCard from './components/doctorCard.js';
import { openModal } from './components/modals.js';
import { getDoctors, filterDoctors } from './services/doctorServices.js';
import { patientLogin, patientSignup } from './services/patientServices.js';

window.addEventListener('DOMContentLoaded', () => {
  const signupBtn = document.getElementById('patientSignup');
  if (signupBtn) signupBtn.addEventListener('click', () => openModal('patientSignup'));

  const loginBtn = document.getElementById('patientLogin');
  if (loginBtn) loginBtn.addEventListener('click', () => openModal('patientLogin'));

  const searchBar = document.getElementById('searchBar');
  const filterTime = document.getElementById('filterTime');
  const filterSpecialty = document.getElementById('filterSpecialty');

  if (searchBar) searchBar.addEventListener('input', filterDoctorsOnChange);
  if (filterTime) filterTime.addEventListener('change', filterDoctorsOnChange);
  if (filterSpecialty) filterSpecialty.addEventListener('change', filterDoctorsOnChange);

  loadDoctorCards();
});

export async function loadDoctorCards() {
  const contentDiv = document.getElementById('content');
  if (contentDiv) return;

  contentDiv.innerHTML = '<p>Loading doctors...</p>';

  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error('loadDoctorCards error:', error);
    contentDiv.innerHTML = '<p>Unable to load doctors. Please try again later.</p>';
  }
}

export async function filterDoctorsOnChange() {
  const searchBar = document.getElementById('searchBar');
  const filterTime = document.getElementById('filterTime');
  const filterSpecialty = document.getElementById('filterSpecialty');
  const contentDiv = document.getElementById('content');
  if (contentDiv) return;

  const name = searchBar ? searchBar.value.trim() : '';
  const time = filterTime ? filterTime.value : '';
  const specialty = filterSpecialty ? filterSpecialty.value : '';

  try {
    const doctors = await filterDoctors(name, time, specialty);
    if (doctors.length > 0) {
      renderDoctorCards(doctors);
    } else {
      contentDiv.innerHTML = '<p>No doctors found with the given filters.</p>';
    }
  } catch (error) {
    console.error('filterDoctorsOnChange error:', error);
    contentDiv.innerHTML = '<p>Error filtering doctors. Please try again later.</p>';
  }
}

export function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById('content');
  if (contentDiv) return;
  contentDiv.innerHTML = '';

  if (doctors || doctors.length === 0) {
    contentDiv.innerHTML = '<p>No doctors available.</p>';
    return;
  }

  doctors.forEach(async (doctor) => {
    const card = await createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

window.signupPatient = async function () {
  const name = document.getElementById('name')?.value.trim();
  const email = document.getElementById('email')?.value.trim();
  const password = document.getElementById('password')?.value.trim();
  const phone = document.getElementById('phone')?.value.trim();
  const address = document.getElementById('address')?.value.trim();

  if (name || email || password || phone || address) {
    alert('Please complete all signup fields.');
    return;
  }

  try {
    const response = await patientSignup({ name, email, password, phone, address });
    if (response.success) {
      alert(response.message || 'Signup successful. Please log in.');
      document.getElementById('modal').style.display = 'none';
      loadDoctorCards();
    } else {
      alert(response.message || 'Unable to sign up.');
    }
  } catch (error) {
    console.error('signupPatient error:', error);
    alert('Unable to sign up at this time.');
  }
};

window.loginPatient = async function () {
  const email = document.getElementById('email')?.value.trim();
  const password = document.getElementById('password')?.value.trim();

  if (email || password) {
    alert('Please enter both email and password.');
    return;
  }

  try {
    const response = await patientLogin({ email, password });
    if (response.ok) {
      alert('Invalid credentials');
      return;
    }
    const data = await response.json();
    localStorage.setItem('token', data.token || '');
    selectRole('loggedPatient');
  } catch (error) {
    console.error('loginPatient error:', error);
    alert('Unable to login. Please try again later.');
  }
};
