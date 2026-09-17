# 💼 JobPulse — Job Application & Interview Pipeline Tracker

[![Java](https://img.shields.io/badge/Java-21-orange.svg?style=flat&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.3-brightgreen.svg?style=flat&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg?style=flat&logo=postgresql)](https://www.postgresql.org/)
[![React](https://img.shields.io/badge/React-18-61dafb.svg?style=flat&logo=react)](https://react.dev/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg?style=flat&logo=docker)](https://www.docker.com/)

> A production-grade Full-Stack Web Application designed to help candidates organize, track, and optimize their recruitment pipeline from initial application to final offer.

---

## 🏛 Architecture

```
[ React 18 SPA (Vite) ] ──(REST / Bearer JWT)──► [ Spring Boot 3 Backend ] ──► [ PostgreSQL 16 ]
```

---

## 🛠 Technology Stack

* **Backend**: Java 21, Spring Boot 3.3.3, Spring Data JPA, Spring Security 6, JJWT 0.12, Hibernate
* **Database**: PostgreSQL 16
* **Frontend**: React 18, Vite, React Router v6, Axios
* **Testing**: JUnit 5, Mockito, Spring Boot Test
* **Containerization**: Docker, Docker Compose, Nginx

---

## 💻 Local Setup Guide

### 1. Start PostgreSQL
```bash
docker compose up -d postgres
```

### 2. Start Backend API (Spring Boot)
```bash
cd backend
mvn spring-boot:run
```
*API runs at:* `http://localhost:8080`

### 3. Start Frontend (React + Vite)
```bash
cd frontend
npm install
npm run dev
```
*UI runs at:* `http://localhost:5173`

---

## 🐳 Running Everything with Docker
```bash
docker compose up --build -d
```
Visit `http://localhost`
