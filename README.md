# 📝 To-Do List Application  

![Java](https://img.shields.io/badge/Java-11+-red?logo=java)  
![JavaFX](https://img.shields.io/badge/UI-JavaFX-blue?logo=java)  
![CSV](https://img.shields.io/badge/Storage-CSV-orange)  
![License](https://img.shields.io/badge/License-Educational-green)  
![Repo Size](https://img.shields.io/github/repo-size/CL26005/todo)  

A **Java-based To-Do List Application** developed for **WIX1002 Fundamentals of Programming – Project 3**.  
This project demonstrates fundamental programming concepts, object-oriented design, data persistence, and GUI building.  

---

## 📂 Project Structure

├── AnalyticsDashboard.java # Provides task statistics & analytics
├── GUI.java # JavaFX-based graphical user interface
├── SimpleTaskManager.java # Command-line interface & task manager
├── Task.java # Task model class
├── TaskStorage.java # Handles saving/loading tasks from CSV
├── tasks.csv # Stores task data
└── README.md # Project documentation

markdown
Copy code

---

## ✨ Features

### ✅ Core Features
- **Task Creation** – Add tasks with:
  - Title  
  - Description  
  - Due Date  
  - Category  
  - Priority (Low / Medium / High)  
  - Completion Status  
- **Task Management** – View tasks, mark as complete/incomplete  
- **Task Deletion** – Remove tasks permanently  
- **Task Sorting** – Sort tasks by due date or priority (ascending/descending)  
- **Task Searching** – Search tasks by keyword in title/description  
- **Recurring Tasks** – Daily, Weekly, or Monthly recurring tasks  
- **Task Dependencies** – Link tasks to enforce order of completion  
- **Edit Tasks** – Modify existing task attributes  
- **Data Persistence** – Save to / load from a CSV file  

### 🌟 Extra Features
- **Graphical User Interface (GUI)** built with **JavaFX**  
- **Email Notification System** – Send reminders for upcoming tasks due within 24 hours (using JavaMail)  
- **Analytics Dashboard** – Displays:
  - Completed vs pending tasks  
  - Completion rate over time  
  - Task breakdown by category  

---

## 🛠 Tech Stack

- **Language:** Java  
- **UI Framework:** JavaFX  
- **Storage:** CSV files  
- **Email Service:** JavaMail API  

---

## 🚀 Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/CL26005/todo.git
cd todo
2. Prerequisites
Java JDK 11 or higher

JavaFX SDK configured in your IDE

JavaMail libraries (for email notifications)

(Optional) Internet access for email features

3. Build & Run
Run GUI Version
bash
Copy code
javac *.java
java GUI
Run Command-Line Version
bash
Copy code
javac *.java
java SimpleTaskManager
Note: The tasks.csv file in the repo will be used to load existing tasks.
Make sure the file is in the working directory or adjust path accordingly.

🧪 Example Analytics Output
yaml
Copy code
Total Tasks: 10  
Completed Tasks: 6  
Pending Tasks: 4  
Completion Rate: 60%  
Category Breakdown:  
- Homework: 3  
- Work: 5  
- Personal: 2  
👥 Contributors
Lai Zhi Hang (24066429)

Gianina Lazaroo (24066775)

Kwek Chee Ling (23080328)

Lee Yu Xuan (23098783)

Mah Kah Mun (24004590)

📜 License
This project is for educational purposes under the course WIX1002 Fundamentals of Programming.
You may use it as a reference or for learning, but please do not use it commercially without permission.
