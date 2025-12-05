# Note App – Full Stack Application
A complete note-taking platform built with a Spring Boot backend and a React frontend.

---

## Quick Start

### Prerequisites
- Java 17+
- Node.js 16+
- MySQL 8+
- Git
- React

---

## Installation

### Clone and Configure
```bash
git clone https://github.com/hirelens-challenges/MARCOS-82fea5.git
cd noteapp
chmod +x start-app.sh
```

### Optional: Update Database Credentials
Edit values inside `start-app.sh` if needed:
```bash
export DB_USERNAME=root
export DB_PASSWORD=1234
```

### Start the Application
```bash
./start-app.sh
```

The script will:
- Build the Spring Boot backend  
- Install frontend dependencies  
- Start backend at: http://localhost:8080  
- Start frontend at: http://localhost:3000  

---

## API Endpoints

**Base URL:**  
https://notesapp-a6cuddbjdkeza7ft.canadacentral-01.azurewebsites.net/api

---

### Notes

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/notes` | Get all notes |
| GET | `/notes/active` | Get active notes |
| GET | `/notes/archived` | Get archived notes |
| POST | `/notes` | Create a note |
| PUT | `/notes/{id}` | Update a note |
| DELETE | `/notes/{id}` | Delete a note |
| POST | `/notes/{id}/archive` | Archive a note |
| POST | `/notes/{id}/unarchive` | Unarchive a note |
| GET | `/notes/category/{name}` | Get notes by category |

---

### Categories

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/categories` | Get all categories |
| POST | `/categories?name={name}` | Create a category |
| DELETE | `/categories/{id}` | Delete a category |

---

### Note–Category Relations

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/notes/{noteId}/categories/{categoryId}` | Add category to a note |
| DELETE | `/notes/{noteId}/categories/{categoryId}` | Remove category from a note |
| POST | `/notes/with-categories?categories={csv}` | Create a note with categories |

**Example:**  
GET https://notesapp-a6cuddbjdkeza7ft.canadacentral-01.azurewebsites.net/api/notes/active

---

## Authentication API

**Base URL:**  
https://notesapp-a6cuddbjdkeza7ft.canadacentral-01.azurewebsites.net/api

### Credentials
- **Username:** admin  
- **Password:** admin123  

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/login` | User login (requires username and password) |
