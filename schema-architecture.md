# Smart Clinic Management System Architecture

## Architecture Summary

The Smart Clinic Management System is built using Spring Boot and follows a three-tier architecture. The presentation layer consists of Thymeleaf-based dashboards for administrators and doctors, as well as REST API clients for appointments and patient-related modules. The application layer contains controllers, services, and business logic responsible for processing requests and enforcing business rules.

The data layer uses two databases. MySQL stores structured data such as patients, doctors, appointments, and administrators. MongoDB stores prescription records in a document-based format. Controllers communicate with the service layer, which delegates data operations to the appropriate repositories. Spring Data JPA is used for MySQL entities, while Spring Data MongoDB manages document models.

## Numbered Flow of Data and Control

1. Users access the application through Thymeleaf dashboards or REST API clients.
2. Requests are routed to either MVC controllers or REST controllers based on the endpoint.
3. Controllers validate requests and delegate processing to the service layer.
4. The service layer applies business rules and coordinates application workflows.
5. Services communicate with MySQL and MongoDB repositories to retrieve or store data.
6. Database records are mapped into Java entities or document models.
7. The processed data is returned either as rendered HTML pages or JSON responses.
