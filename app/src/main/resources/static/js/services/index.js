import { openModal } from '../components/modals.js';
import { API_BASE_URL } from '../config/config.js';
import { patientLogin } from './patientServices.js';

const ADMIN_API = `${API_BASE_URL}/admin`;
const DOCTOR_API = `${API_BASE_URL}/doctor/login`;

window.onload = function () {
  const adminBtn = document.getElementById('adminLogin');
  if (adminBtn) {
    adminBtn.addEventListener('click', () => openModal('adminLogin'));
  }

  const doctorBtn = document.getElementById('doctorLogin');
  if (doctorBtn) {
    doctorBtn.addEventListener('click', () => openModal('doctorLogin'));
  }

  const patientBtn = document.getElementById('patientLogin');
  if (patientBtn) {
    patientBtn.addEventListener('click', () => openModal('patientLogin'));
  }
};

window.adminLoginHandler = async function () {
  const usernameInput = document.getElementById('username');
  const passwordInput = document.getElementById('password');

  const username = usernameInput ? usernameInput.value.trim() : '';
  const password = passwordInput ? passwordInput.value.trim() : '';

  if (!username || !password) {
    alert('Please enter both username and password.');
    return;
  }

  const admin = { username, password };

  try {
    const response = await fetch(ADMIN_API, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(admin),
    });

    if (!response.ok) {
      alert('Invalid credentials');
      return;
    }

    const data = await response.json();
    localStorage.setItem('token', data.token || '');
    selectRole('admin');
  } catch (error) {
    console.error('adminLoginHandler error:', error);
    alert('Unable to login. Please try again later.');
  }
};

window.doctorLoginHandler = async function () {
  const emailInput = document.getElementById('email');
  const passwordInput = document.getElementById('password');

  const email = emailInput ? emailInput.value.trim() : '';
  const password = passwordInput ? passwordInput.value.trim() : '';

  if (!email || !password) {
    alert('Please enter both email and password.');
    return;
  }

  const doctor = { email, password };

  try {
    const response = await fetch(DOCTOR_API, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(doctor),
    });

    if (!response.ok) {
      alert('Invalid credentials');
      return;
    }

    const data = await response.json();
    localStorage.setItem('token', data.token || '');
    selectRole('doctor');
  } catch (error) {
    console.error('doctorLoginHandler error:', error);
    alert('Unable to login. Please try again later.');
  }
};

window.loginPatient = async function () {
  const email = document.getElementById('email')?.value.trim();
  const password = document.getElementById('password')?.value.trim();

  if (!email || !password) {
    alert('Please enter both email and password.');
    return;
  }

  try {
    const response = await patientLogin({ email, password });
    if (!response || !response.ok) {
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
