import { API_BASE_URL } from '../config/config.js';
const PATIENT_API = `${API_BASE_URL}/patient`;

export async function patientSignup(data) {
  try {
    const response = await fetch(`${PATIENT_API}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    const result = await response.json();
    if (!response.ok) {
      return { success: false, message: result.message || 'Signup failed' };
    }
    return { success: true, message: result.message || 'Patient created successfully' };
  } catch (error) {
    console.error('Error :: patientSignup :: ', error);
    return { success: false, message: error.message };
  }
}

export async function patientLogin(data) {
  try {
    return await fetch(`${PATIENT_API}/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
  } catch (error) {
    console.error('Error :: patientLogin :: ', error);
    return { ok: false, json: async () => ({ message: error.message }) };
  }
}

export async function getPatientData(token) {
  try {
    const response = await fetch(`${PATIENT_API}/${token}`);
    const data = await response.json();
    if (response.ok) return data.patient || null;
    return null;
  } catch (error) {
    console.error('Error fetching patient details:', error);
    return null;
  }
}

export async function getPatientAppointments(id, token, user) {
  try {
    const response = await fetch(`${PATIENT_API}/${id}/${user}/${token}`);
    const data = await response.json();
    if (response.ok) {
      return data.appointments || [];
    }
    return [];
  } catch (error) {
    console.error('Error fetching patient appointments:', error);
    return [];
  }
}

export async function filterAppointments(condition, name, token) {
  try {
    const response = await fetch(`${PATIENT_API}/filter/${condition}/${name}/${token}`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    });

    if (response.ok) {
      const data = await response.json();
      return data.appointments || [];
    }

    console.error('Failed to filter appointments:', response.statusText);
    return [];
  } catch (error) {
    console.error('Error filtering appointments:', error);
    alert('Something went wrong');
    return [];
  }
}
