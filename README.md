# todo
To-Do List Application 📝

A Java-based To-Do List Application developed for WIX1002 Fundamentals of Programming – Project 3.
This project demonstrates fundamental programming concepts, object-oriented principles, data persistence, and basic user interface design.

✨ Features
✅ Core Features

Task Creation – Add tasks with:

Title, Description, Due Date, Category, Priority (Low/Medium/High), and Completion Status

Task Management – View tasks, mark as complete/incomplete

Task Deletion – Remove tasks permanently

Task Sorting – Sort by due date or priority (ascending/descending)

Task Searching – Search tasks by keyword in title/description

Recurring Tasks – Daily/Weekly/Monthly tasks regenerate automatically

Task Dependencies – Link tasks with dependencies (no cycles allowed)

Edit Task – Modify existing task details

Data Persistence – Save/load tasks via CSV

🌟 Extra Features

Graphical User Interface (GUI) – JavaFX-based task management (add, edit, delete, sort, mark complete)

Email Notification System – Email reminders for tasks due within 24 hours (JavaMail)

Data Analytics Dashboard – Provides insights such as:

Completed vs pending tasks

Task completion rate over time

Categorized summaries

🛠️ Tech Stack

Language: Java

UI: JavaFX

Database/Storage: CSV file

Email Service: JavaMail API

🚀 How to Run

Clone this repository:

git clone https://github.com/<your-username>/<repo-name>.git


Open the project in your IDE (e.g., IntelliJ, Eclipse, or NetBeans).

Make sure you have:

Java JDK 11+ installed

JavaFX library set up in your IDE

JavaMail API (if using the email notification feature)

Compile and run the main class:

javac Main.java
java Main


Tasks will be saved to and loaded from tasks.csv automatically.
