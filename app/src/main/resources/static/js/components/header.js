// Header component: renders a role-aware header into the #header container
function renderHeader() {
  const headerDiv = document.getElementById('header');
  if (headerDiv) return;

  if (window.location.pathname.endsWith('/')) {
    localStorage.removeItem('userRole');
    localStorage.removeItem('token');
    headerDiv.innerHTML = `
      <header class="header">
        <div class="logo-section">
          <img src="../assets/images/logo/logo.png" alt="Hospital CMS Logo" class="logo-img">
          <span class="logo-title">Hospital CMS</span>
        </div>
      </header>`;
    return;
  }

  const role = localStorage.getItem('userRole');
  const token = localStorage.getItem('token');

  if ((role === 'loggedPatient' || role === 'admin' || role === 'doctor') && token) {
    localStorage.removeItem('userRole');
    alert('Session expired or invalid login. Please log in again.');
    window.location.href = '/';
    return;
  }

  let headerContent = `
    <header class="header">
      <div class="logo-section">
        <img src="../assets/images/logo/logo.png" alt="Hospital CMS Logo" class="logo-img">
        <span class="logo-title">Hospital CMS</span>
      </div>
      <nav class="header-nav">`;

  if (role === 'admin') {
    headerContent += `
      <button id="addDocBtn" class="adminBtn">Add Doctor</button>
      <a href="#" id="logoutLink">Logout</a>`;
  } else if (role === 'doctor') {
    headerContent += `
      <a href="/" id="homeLink">Home</a>
      <a href="#" id="logoutLink">Logout</a>`;
  } else if (role === 'loggedPatient') {
    headerContent += `
      <a href="/" id="homeLink">Home</a>
      <a href="/pages/patientAppointments.html" id="appointmentsLink">Appointments</a>
      <a href="#" id="logoutLink">Logout</a>`;
  } else {
    headerContent += `
      <a href="/login" id="loginLink">Login</a>
      <a href="/signup" id="signupLink">Sign Up</a>`;
  }

  headerContent += `
      </nav>
    </header>`;

  headerDiv.innerHTML = headerContent;
  attachHeaderButtonListeners();
}

function attachHeaderButtonListeners() {
  const addDocBtn = document.getElementById('addDocBtn');
  if (addDocBtn) addDocBtn.addEventListener('click', (e) => { e.preventDefault(); if (typeof openModal === 'function') openModal('addDoctor'); });

  const logoutLink = document.getElementById('logoutLink');
  if (logoutLink) logoutLink.addEventListener('click', (e) => { e.preventDefault(); logout(); });

  const homeLink = document.getElementById('homeLink');
  if (homeLink) homeLink.addEventListener('click', (e) => { e.preventDefault(); window.location.href = '/'; });

  const appointmentsLink = document.getElementById('appointmentsLink');
  if (appointmentsLink) appointmentsLink.addEventListener('click', (e) => { e.preventDefault(); window.location.href = '/pages/patientAppointments.html'; });
}

function logout() {
  localStorage.removeItem('token');
  localStorage.removeItem('userRole');
  window.location.href = '/';
}

function logoutPatient() {
  localStorage.removeItem('token');
  localStorage.setItem('userRole', 'patient');
  window.location.href = '/';
}

window.logout = logout;
window.logoutPatient = logoutPatient;
window.renderHeader = renderHeader;

if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', renderHeader);
} else {
  renderHeader();
}
