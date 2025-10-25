# UniTrack – Internal Team & Project Tracker 🚀🗂️

**UniTrack** is a lightweight web application designed to help small tech teams track their collaborators, manage their projects, and follow tasks with clarity and structure.

🛠️ Built with love in Java/Spring Boot by Maksym Zelinskyi — an 18-year-old developer looking to prove what a self-taught mind can deliver.

---

## 🎯 Features

### 👤 Collaborator Management
- Add, update, and remove team members
- Fields: name, email, role (intern, dev, etc.), skills (tags), availability %, join date
- Link collaborators to ongoing projects

### 📁 Project Management
- Create and manage projects
- Fields: title, description, client, status (planned, active, done), start/end date
- Assign multiple collaborators per project

### ✅ Task Tracking
- Tasks connected to projects and collaborators
- Fields: title, status, priority, deadline, description
- Full CRUD operations

### 📊 Dashboard Overview
- Project and team statistics
- View workload distribution
- Filter by status or person
- See overdue tasks and inactive projects

### 🖨️ PDF Export (optional)
- Generate printable project summaries
- Export team status reports
- Clean layout using iText

### 🤖 AI Assistant (optional bonus)
- Summarize project content
- Rephrase project notes
- Prioritize urgent tasks
- Powered by OpenAI or Mistral API
- Add collaborator based on their CV
---

## 🧰 Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Thymeleaf (Bootstrap UI)
- H2 / PostgreSQL
- iText PDF Generator
- OpenAI API (optional)

---

## 💻 Setup

```bash
git clone https://github.com/MaksymZelinskyi/unitrack
cd unitrack
docker-compose up
