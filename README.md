# TaskGenie

A JavaFX-based task management application that helps users organize and track their tasks with intuitive categorization, priority filtering, and visual progress tracking.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [Running the Application](#running-the-application)
- [Usage Guide](#usage-guide)
- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)
- [Contributors](#contributors)

---

## Overview

**TaskGenie** is a desktop task management application built with JavaFX that provides users with a clean, intuitive interface for managing tasks across various categories and priority levels. The application offers real-time statistics, powerful filtering capabilities, and direct task editing, helping users stay organized and focused on what matters most.

---

## Features

- **Task Management:** Add, edit, and delete tasks with details including name, description, deadline, priority, and category.
- **Priority Classification:** Categorize tasks as High, Medium, or Low priority with visual color coding.
- **Category Organization:** Group related tasks into predefined categories (Work, Personal, Study, Health, Finance).
- **Visual Statistics:** View task completion rates, overdue counts, and priority breakdowns through an intuitive dashboard.
- **Dynamic Filtering:** Filter tasks by priority, category, completion status, and due date.
- **Responsive UI:** Experience real-time updates when tasks are modified, with automatic statistics recalculation.

---

## Prerequisites

To run TaskGenie, you will need:

- Java JDK 11 or higher
- JavaFX SDK 11 or higher (if not included in your JDK)
- IntelliJ IDEA (Community or Ultimate)

---

## Setup Instructions

### Option 1: Clone from GitHub Using IntelliJ IDEA

1. **Open IntelliJ IDEA.**
2. On the Welcome screen, click **"Get from Version Control..."**.
3. Paste the repository URL: https://github.com/nihal-majumder/TaskGenie.git

4. Choose your desired directory and click **Clone**.
5. IntelliJ will automatically open the project. If prompted, select the appropriate JDK (Java 11 or higher).

### Option 2: Download ZIP and Open in IntelliJ IDEA

1. Download the ZIP file from [GitHub](https://github.com/nihal-majumder/TaskGenie).
2. Extract the ZIP file to your desired location.
3. Open IntelliJ IDEA and select **"Open"** from the Welcome screen.
4. Browse to the extracted folder and select it.
5. IntelliJ will open the project. If prompted, select the appropriate JDK (Java 11 or higher).

---

## Running the Application

1. **Configure JavaFX SDK (if needed):**
- Download JavaFX SDK from [openjfx.io](https://openjfx.io/).
- In IntelliJ, go to **File > Project Structure > Libraries** and add the JavaFX SDK `lib` directory.
2. **Set VM Options for JavaFX:**
- Go to **Run > Edit Configurations...**
- In the **VM options** field, add (adjust the path as needed):
  ```
  --module-path "C:/path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
  ```
3. In the Project view, navigate to `src/edu/northeastern/Main.java`.
4. Right-click `Main.java` and select **Run 'Main.main()'**.

---

## Usage Guide

### Adding a Task

- Click the **Add Task** button.
- Fill in the task details (name, description, priority, category, deadline).
- Click **OK** to create the task.

### Editing a Task

- Double-click on any cell in the task table to edit that property.
- For priority and category, a dropdown will appear with available options.
- For the deadline, a date picker will be displayed.

### Completing a Task

- Click the checkbox in the **Done** column to mark a task as completed.

### Filtering Tasks

- Use the priority dropdown to filter by High, Medium, or Low priority.
- Use the category dropdown to filter by task category.
- Toggle **Show Completed** to hide or show completed tasks.
- Toggle **Show Overdue Only** to focus on overdue tasks.

### Deleting a Task

- Select a task in the table.
- Click the **Delete Task** button.

---

## Project Structure

src/edu/northeastern/ # Java source files
├── Main.java # Application entry point
├── Task.java # Abstract base class for tasks
├── RegularTask.java # Concrete implementation of Task
├── Priority.java # Enum for task priority levels
├── Category.java # Class representing task categories
├── TaskService.java # Service class for task operations
└── TaskManagerController.java # Main UI controller

src/resources/ # FXML layouts and CSS files
├── main.fxml # Main application layout
├── task_dialog.fxml # Task creation dialog layout
└── styles.css # CSS styling for the application


---

## Technologies Used

- Java 11
- JavaFX
- FXML
- CSS
- IntelliJ IDEA

---

## Contributors

- **Nafisa Anzum** - Information Systems, Northeastern University
- **Nihal Majumder** - Software Engineering Systems, Northeastern University
