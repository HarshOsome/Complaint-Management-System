# Complaint Management System (CMS)
 
A full-stack web application built with **Spring Boot 3** for managing complaints across three user roles — Admin, Employee, and Customer.
 
Built as part of the Intermediate Java Backend assignment at **HCL Technologies**.
 
---
 
## Tech Stack
 
- Java 17
- Spring Boot 3
- Spring Data JPA
- H2 In-Memory Database
- Thymeleaf (server-side rendering)
- Bean Validation (jakarta.validation)
- Lombok
- Maven
 
---
 
## Architecture
 
Clean 4-layer MVC monolith:- **Model** — `User.java`, `Complaint.java` (JPA entities)
- **Repository** — Spring Data JPA interfaces (zero SQL written)
- **Service** — `ComplaintService`, `UserService`, `AssignationService` (all business logic)
- **Controller** — `AuthController`, `ComplaintController`, `AssignationController`, `DashboardController`, `ReportController`
- **View** — 12 Thymeleaf templates
 
---
 
## Features
 
### Login Module
- Role-based authentication — Admin, Employee, Customer
- Session-based login with role-based dashboard routing
- Customer self-registration
- Forgot password / reset flow
 
### Complaint Module (Admin)
- Add, View, Update, Delete complaints
- Status management — PENDING → ASSIGNED → COMPLETED
- Filter complaints by status
 
### Assignation Module
- Manual assignment — Admin picks a specific employee
- **Auto-assignment algorithm** — assigns to the least-busy available employee automatically (workload balancing)
- Employees can update status and add remarks
 
### Customer Module
- Raise new complaints
- View own complaints only
- Add/update comments on complaints
- Give feedback (only unlocks when complaint is COMPLETED)
 
### Report Module
- Pending complaints report
- Solved complaints report
- Date range report
- User-wise complaint report
 
### Exception Handling
- Custom exceptions — `ComplaintNotFoundException`, `UserNotFoundException`, `InvalidRequestException`
- Global `@ControllerAdvice` handler — clean 404/400/500 error pages
 
---
 
## How to Run
 
**Prerequisites:** Java 17, Maven
 
**Clone the repo:**
```bash
git clone https://github.com/HarshOsome/Complaint-Management-System.git
cd Complaint-Management-System
 