# Smart Clinic Portal User Stories

## Admin User Stories

### Story 1

**Title:** Admin Login

*As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely.*

**Acceptance Criteria:**

1. Admin can enter a username and password.
2. Valid credentials grant access to the admin dashboard.
3. Invalid credentials display an error message.

**Priority:** High
**Story Points:** 3

**Notes:**

* Authentication must be secure.

---

### Story 2

**Title:** Admin Logout

*As an admin, I want to log out of the portal, so that I can protect system access.*

**Acceptance Criteria:**

1. Admin can click a logout button.
2. The session is terminated.
3. The user is redirected to the login page.

**Priority:** High
**Story Points:** 2

**Notes:**

* Logout should invalidate the active session.

---

### Story 3

**Title:** Add Doctor

*As an admin, I want to add doctors to the portal, so that patients can schedule appointments.*

**Acceptance Criteria:**

1. Admin can enter doctor details.
2. Doctor information is validated.
3. Doctor profile is saved successfully.

**Priority:** High
**Story Points:** 5

**Notes:**

* Required fields must not be empty.

---

### Story 4

**Title:** Delete Doctor Profile

*As an admin, I want to delete a doctor's profile from the portal, so that outdated records can be removed.*

**Acceptance Criteria:**

1. Admin can select a doctor profile.
2. Confirmation is required before deletion.
3. The profile is removed successfully.

**Priority:** Medium
**Story Points:** 3

**Notes:**

* Deletion actions should be logged.

---

### Story 5

**Title:** View Monthly Appointment Statistics

*As an admin, I want to run a stored procedure in MySQL CLI to get the number of appointments per month, so that I can track usage statistics.*

**Acceptance Criteria:**

1. The stored procedure executes successfully.
2. Monthly appointment counts are returned.
3. Results are displayed for review.

**Priority:** Medium
**Story Points:** 5

**Notes:**

* Requires database access permissions.

---

## Patient User Stories

### Story 1

**Title:** View Doctors Without Login

*As a patient, I want to view a list of doctors without logging in, so that I can explore options before registering.*

**Acceptance Criteria:**

1. Doctor listings are publicly accessible.
2. Doctor details are displayed.
3. No login is required.

**Priority:** High
**Story Points:** 2

**Notes:**

* Public users have read-only access.

---

### Story 2

**Title:** Patient Registration

*As a patient, I want to sign up using my email and password, so that I can book appointments.*

**Acceptance Criteria:**

1. Registration form accepts email and password.
2. A new account is created successfully.
3. Duplicate emails are rejected.

**Priority:** High
**Story Points:** 5

**Notes:**

* Email addresses must be unique.

---

### Story 3

**Title:** Patient Login

*As a patient, I want to log into the portal, so that I can manage my bookings.*

**Acceptance Criteria:**

1. Patient enters valid credentials.
2. Successful login redirects to the dashboard.
3. Invalid login displays an error message.

**Priority:** High
**Story Points:** 3

**Notes:**

* Authentication should be secure.

---

### Story 4

**Title:** Patient Logout

*As a patient, I want to log out of the portal, so that I can secure my account.*

**Acceptance Criteria:**

1. Logout button is available.
2. Session is terminated.
3. User is redirected to the login page.

**Priority:** High
**Story Points:** 2

**Notes:**

* Session data must be cleared.

---

### Story 5

**Title:** Book Appointment

*As a patient, I want to book an hour-long appointment with a doctor, so that I can receive medical consultation.*

**Acceptance Criteria:**

1. Patient can select a doctor.
2. Patient can choose an available one-hour time slot.
3. Appointment confirmation is displayed.

**Priority:** High
**Story Points:** 5

**Notes:**

* Double booking must be prevented.

---

### Story 6

**Title:** View Upcoming Appointments

*As a patient, I want to view my upcoming appointments, so that I can prepare accordingly.*

**Acceptance Criteria:**

1. Upcoming appointments are displayed.
2. Appointment details are visible.
3. Past appointments are excluded.

**Priority:** Medium
**Story Points:** 3

**Notes:**

* Appointment list should be updated automatically.

---

## Doctor User Stories

### Story 1

**Title:** Doctor Login

*As a doctor, I want to log into the portal, so that I can manage my appointments.*

**Acceptance Criteria:**

1. Doctor enters valid credentials.
2. Login succeeds.
3. Doctor dashboard is displayed.

**Priority:** High
**Story Points:** 3

**Notes:**

* Authentication should be secure.

---

### Story 2

**Title:** Doctor Logout

*As a doctor, I want to log out of the portal, so that I can protect my data.*

**Acceptance Criteria:**

1. Logout option is available.
2. Session is terminated.
3. Doctor is redirected to the login page.

**Priority:** High
**Story Points:** 2

**Notes:**

* All session information should be cleared.

---

### Story 3

**Title:** View Appointment Calendar

*As a doctor, I want to view my appointment calendar, so that I can stay organized.*

**Acceptance Criteria:**

1. Calendar displays upcoming appointments.
2. Appointment details are visible.
3. Calendar updates automatically.

**Priority:** High
**Story Points:** 5

**Notes:**

* Support daily and weekly views.

---

### Story 4

**Title:** Mark Unavailability

*As a doctor, I want to mark my unavailability, so that patients only see available appointment slots.*

**Acceptance Criteria:**

1. Doctor can specify unavailable dates and times.
2. Unavailable slots cannot be booked.
3. Changes are saved successfully.

**Priority:** High
**Story Points:** 5

**Notes:**

* Existing appointments must remain unchanged.

---

### Story 5

**Title:** Update Profile Information

*As a doctor, I want to update my specialization and contact information, so that patients have up-to-date information.*

**Acceptance Criteria:**

1. Doctor can edit profile information.
2. Changes are saved successfully.
3. Updated information is visible to patients.

**Priority:** Medium
**Story Points:** 3

**Notes:**

* Contact information must be validated.

---

### Story 6

**Title:** View Patient Details

*As a doctor, I want to view patient details for upcoming appointments, so that I can be prepared.*

**Acceptance Criteria:**

1. Doctor can access upcoming appointment information.
2. Patient details are displayed.
3. Access is restricted to scheduled appointments.

**Priority:** High
**Story Points:** 5

**Notes:**

* Patient privacy must be protected.
