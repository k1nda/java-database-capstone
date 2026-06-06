# User Story Template

## Admin Story 1

**Title:** Admin Login

*As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely.*

**Acceptance Criteria:**

1. Admin can enter username and password.
2. Valid credentials grant access.
3. Invalid credentials display an error message.

**Priority:** High
**Story Points:** 3

**Notes:**

* Authentication should be secure.

---

## Admin Story 2

**Title:** Admin Logout

*As an admin, I want to log out of the portal, so that I can protect system access.*

**Acceptance Criteria:**

1. Admin can click a logout button.
2. Session is terminated.
3. User is redirected to the login page.

**Priority:** High
**Story Points:** 2

**Notes:**

* Logout should invalidate all active sessions.

---

## Admin Story 3

**Title:** Add Doctor

*As an admin, I want to add doctors to the portal, so that patients can schedule appointments.*

**Acceptance Criteria:**

1. Admin can enter doctor details.
2. Doctor profile is saved successfully.
3. Doctor appears in the doctor listing.

**Priority:** High
**Story Points:** 5

**Notes:**

* Required fields must be validated.

---

## Admin Story 4

**Title:** Delete Doctor Profile

*As an admin, I want to delete a doctor's profile, so that outdated records can be removed.*

**Acceptance Criteria:**

1. Admin can select a doctor profile.
2. Confirmation is required before deletion.
3. Profile is removed from the system.

**Priority:** Medium
**Story Points:** 3

**Notes:**

* Deletion should be logged.

---

## Admin Story 5

**Title:** View Monthly Appointment Statistics

*As an admin, I want to run a stored procedure in MySQL CLI to get the number of appointments per month, so that I can track usage statistics.*

**Acceptance Criteria:**

1. Stored procedure executes successfully.
2. Monthly appointment counts are returned.
3. Results can be reviewed by the admin.

**Priority:** Medium
**Story Points:** 5

**Notes:**

* Requires database access.

---

## Patient Story 1

**Title:** View Doctors Without Login

*As a patient, I want to view a list of doctors without logging in, so that I can explore options before registering.*

**Acceptance Criteria:**

1. Doctor list is publicly accessible.
2. Doctor details are displayed.
3. No authentication is required.

**Priority:** High
**Story Points:** 2

**Notes:**

* Public users can only view information.

---

## Patient Story 2

**Title:** Patient Registration

*As a patient, I want to sign up using my email and password, so that I can book appointments.*

**Acceptance Criteria:**

1. Registration form accepts email and password.
2. Account is created successfully.
3. User receives confirmation.

**Priority:** High
**Story Points:** 5

**Notes:**

* Email must be unique.

---

## Patient Story 3

**Title:** Patient Login

*As a patient, I want to log into the portal, so that I can manage my bookings.*

**Acceptance Criteria:**

1. Login form accepts credentials.
2. Valid credentials grant access.
3. Invalid credentials display an error.

**Priority:** High
**Story Points:** 3

**Notes:**

* Authentication should be secure.

---

## Patient Story 4

**Title:** Patient Logout

*As a patient, I want to log out of the portal, so that I can secure my account.*

**Acceptance Criteria:**

1. Logout button is available.
2. Session ends successfully.
3. User is redirected to login.

**Priority:** High
**Story Points:** 2

**Notes:**

* Session data should be cleared.

---

## Patient Story 5

**Title:** Book Appointment

*As a patient, I want to book an hour-long appointment with a doctor, so that I can receive medical consultation.*

**Acceptance Criteria:**

1. Patient can select a doctor.
2. Patient can choose an available time slot.
3. Appointment is confirmed after booking.

**Priority:** High
**Story Points:** 5

**Notes:**

* Double-booking must be prevented.

---

## Patient Story 6

**Title:** View Upcoming Appointments

*As a patient, I want to view my upcoming appointments, so that I can prepare accordingly.*

**Acceptance Criteria:**

1. Upcoming appointments are displayed.
2. Appointment details are visible.
3. List is updated automatically.

**Priority:** Medium
**Story Points:** 3

**Notes:**

* Past appointments should be excluded.

---

## Doctor Story 1

**Title:** Doctor Login

*As a doctor, I want to log into the portal, so that I can manage my appointments.*

**Acceptance Criteria:**

1. Doctor enters valid credentials.
2. Login succeeds.
3. Dashboard is displayed.

**Priority:** High
**Story Points:** 3

**Notes:**

* Authentication should be secure.

---

## Doctor Story 2

**Title:** Doctor Logout

*As a doctor, I want to log out of the portal, so that I can protect my data.*

**Acceptance Criteria:**

1. Logout button is available.
2. Session is terminated.
3. User is redirected to login.

**Priority:** High
**Story Points:** 2

**Notes:**

* All session information should be cleared.

---

## Doctor Story 3

**Title:** View Appointment Calendar

*As a doctor, I want to view my appointment calendar, so that I can stay organized.*

**Acceptance Criteria:**

1. Calendar displays upcoming appointments.
2. Appointment times are visible.
3. Calendar updates automatically.

**Priority:** High
**Story Points:** 5

**Notes:**

* Calendar should support daily and weekly views.

---

## Doctor Story 4

**Title:** Mark Unavailability

*As a doctor, I want to mark my unavailability, so that patients only see available appointment slots.*

**Acceptance Criteria:**

1. Doctor can specify unavailable dates and times.
2. Unavailable slots cannot be booked.
3. Changes are saved immediately.

**Priority:** High
**Story Points:** 5

**Notes:**

* Existing appointments must remain unaffected.

---

## Doctor Story 5

**Title:** Update Profile Information

*As a doctor, I want to update my specialization and contact information, so that patients have up-to-date information.*

**Acceptance Criteria:**

1. Doctor can edit profile details.
2. Changes are saved successfully.
3. Updated information is displayed to patients.

**Priority:** Medium
**Story Points:** 3

**Notes:**

* Validation is required for contact information.

---

## Doctor Story 6

**Title:** View Patient Details

*As a doctor, I want to view patient details for upcoming appointments, so that I can be prepared.*

**Acceptance Criteria:**

1. Doctor can access appointment details.
2. Patient information is displayed.
3. Access is limited to scheduled appointments.

**Priority:** High
**Story Points:** 5

**Notes:**

* Patient privacy must be protected.
