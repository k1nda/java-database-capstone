import { API_BASE_URL } from '../config/config.js';
const DOCTOR_API = `${API_BASE_URL}/doctor`;

export async function getDoctors() {
  try {
    const response = await fetch(DOCTOR_API);
    if (!response.ok) {
      console.error('getDoctors failed', response.statusText);
      return [];
    }
    const data = await response.json();
    return Array.isArray(data.doctors) ? data.doctors : data;
  } catch (error) {
    console.error('Error fetching doctors:', error);
    return [];
  }
}

export async function deleteDoctor(id, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/${id}/${token}`, {
      method: 'DELETE',
    });
    const data = await response.json();
    return {
      success: response.ok,
      message: data.message || (response.ok ? 'Doctor deleted successfully' : 'Failed to delete doctor'),
    };
  } catch (error) {
    console.error('Error deleting doctor:', error);
    return { success: false, message: error.message };
  }
}

export async function saveDoctor(doctor, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/${token}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(doctor),
    });
    const data = await response.json();
    return {
      success: response.ok,
      message: data.message || (response.ok ? 'Doctor saved successfully' : 'Failed to save doctor'),
    };
  } catch (error) {
    console.error('Error saving doctor:', error);
    return { success: false, message: error.message };
  }
}

export async function filterDoctors(name, time, specialty) {
  const safeName = name && name.trim().length ? encodeURIComponent(name.trim()) : 'null';
  const safeTime = time && time.trim().length ? encodeURIComponent(time.trim()) : 'null';
  const safeSpecialty = specialty && specialty.trim().length ? encodeURIComponent(specialty.trim()) : 'null';

  try {
    const response = await fetch(`${DOCTOR_API}/filter/${safeName}/${safeTime}/${safeSpecialty}`);
    if (!response.ok) {
      console.error('filterDoctors failed', response.statusText);
      return [];
    }
    const data = await response.json();
    return Array.isArray(data.doctors) ? data.doctors : data;
  } catch (error) {
    console.error('Error filtering doctors:', error);
    alert('Something went wrong while filtering doctors');
    return [];
  }
}
