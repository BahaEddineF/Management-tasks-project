# 🗂️ Task Management Project


![image (2)](https://github.com/user-attachments/assets/0cfa9287-a09c-4fd2-9a0b-80bdefbdad02)
 
A full-stack web application for managing tasks and projects efficiently. Built using **Angular** for the frontend and **Spring Boot** for the backend, with DevOps practices using **GitLab CI/CD**, **Docker**, and **Docker Compose**.

---

## 🚀 Features

- ✅ User authentication and authorization
- 📝 Create, assign, and manage tasks
- 📁 Project management with deadlines and status tracking
- 🔔 Notifications for task updates
- 📊 Dashboard with task statistics
- ⚙️ RESTful API integration between front and back end

---

## 🛠️ Tech Stack

### 🔹 Frontend
- **Angular 15+**
- Angular Material
- RxJS & HTTP Client
- JWT Authentication

### 🔹 Backend
- **Spring Boot 3**
- Spring Security & JWT
- JPA & Hibernate
- MySQL or PostgreSQL

### 🔹 DevOps / Infrastructure
- **GitLab CI/CD** for automated builds and deployment
- **Docker** for containerizing the app
- **Docker Compose** for multi-container setup (frontend, backend, DB)
- NGINX (optional) for reverse proxy


🧪 How to Run Locally with Docker Compose
# Build and start all services
docker-compose up --build

⚙️ GitLab CI/CD
Lint, build, and test Angular & Spring Boot in separate stages

Build Docker images and push to GitLab container registry

Deploy via Docker Compose on a remote server (optional)

Example stages in .gitlab-ci.yml:

yaml
Copy
Edit
stages:
  - build
  - test
  - deploy

## 📌 Future Improvements
Unit & integration tests (Jest, JUnit)

CI/CD integration with cloud (AWS/GCP)

Task comments and attachments

Real-time updates with WebSockets
