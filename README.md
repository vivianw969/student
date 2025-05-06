# Student Achievement Management System

This project is a full-stack web application for managing student achievements, courses, and grades. It consists of a Vue.js frontend and a Spring Boot backend.

## Project Structure

- **Frontend**: Located in the `vue` directory, built with Vue 3, Vite, and Element Plus.
- **Backend**: Located in the `springboot` directory, built with Spring Boot and MyBatis.

## Features

- **User Authentication**: Login and registration functionality for both students and administrators.
- **Course Management**: Administrators can add, edit, and delete courses. Students can view and enroll in courses.
- **Student Management**: Administrators can manage student information, including adding, editing, and deleting student records.
- **Grade Management**: Administrators can assign grades and comments to students. Students can view their grades and provide feedback.
- **User Profile**: Students can update their personal information and profile picture.

## Frontend

The frontend is built with Vue 3 and uses Element Plus for UI components. It includes the following main views:

- **Login/Register**: User authentication pages.
- **Manager**: Main dashboard for both students and administrators.
- **Course Management**: Pages for managing courses and enrollment.
- **Student Management**: Pages for managing student information.
- **Grade Management**: Pages for managing grades and feedback.

## Backend

The backend is built with Spring Boot and uses MyBatis for database interactions. It includes the following main components:

- **Controllers**: Handle HTTP requests and responses.
- **Services**: Implement business logic.
- **Mappers**: Interact with the database using MyBatis.
- **Entities**: Represent database tables as Java objects.

## Configuration

- **Frontend**: Configured using Vite and includes environment-specific settings in `.env.development` and `.env.production`.
- **Backend**: Configured using `application.yml` for database connection, server port, and other settings.

## Getting Started

1. **Clone the repository**.
2. **Frontend Setup**:
   - Navigate to the `vue` directory.
   - Run `npm install` to install dependencies.
   - Run `npm run dev` to start the development server.
3. **Backend Setup**:
   - Navigate to the `springboot` directory.
   - Run `mvn spring-boot:run` to start the Spring Boot application.
4. **Access the application** at `http://localhost:9090`.

## Dependencies

- **Frontend**: Vue 3, Vite, Element Plus, Axios.
- **Backend**: Spring Boot, MyBatis, MySQL.

## License

This project is licensed under the MIT License. 