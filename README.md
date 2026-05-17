# University Management System

Created by: [Tynyshtyk Alis](https://github.com/App1epli), [Kobylandy000](https://github.com/Kobylandy000),[Ruslan Myrzabayev](https://github.com/Rusikkooo)

A console-based Java project for Object-Oriented Programming that models a university information system.

The project demonstrates user roles, academic courses, grading, transcripts, news management, research activities, and several core design patterns.

## About the Project

The system includes several university roles:

- `Student` — registers for courses, views grades and transcript, and can participate in research scenarios.
- `Teacher` — manages courses, works with students, assigns grades, and has research capabilities depending on academic status.
- `Manager` — approves students, assigns teachers to courses, generates reports, and publishes news.
- `Admin` — manages users and system logs.

The application runs in the console through role-based menus and demonstrates the main interactions between system entities.

## Main Features

- User authentication by email and password
- Separate menus for different user roles
- Student course registration
- Teacher assignment to courses
- Grade submission with automatic GPA recalculation
- Transcript viewing
- Teacher rating by students
- Student and teacher reporting
- News management
- Research demo features: papers, projects, subscriptions, and publications

## OOP Concepts Used

- Inheritance: `User -> Employee -> Admin / Manager / Teacher / TechSupporter`
- Polymorphism: different roles are handled through the shared `User` type
- Encapsulation: data and behavior are grouped inside domain classes
- Interfaces: research behavior is abstracted through `Researcher`
- Composition: `Student` contains `Transcript`, `Teacher` contains courses and students, `Course` contains instructors and students

## Design Patterns Used

- `Singleton` — `Admin`
- `Factory` — `UserFactory`
- `Template Method` — `ProcessingOrders`
- `Observer` — simplified implementation through `ResearchPaperPublisher`

## Project Structure

```text
src/
  project/
    Main.java
    enums/
    exceptions/
    factory/
    interfaces/
    models/
    processing/
    publisher/
    users/
```

### Key Packages

- `users` — system user roles
- `models` — domain entities such as course, mark, transcript, paper, project, and news
- `enums` — status and type enumerations
- `interfaces` — shared contracts such as `Researcher`
- `factory` — user creation through a factory
- `publisher` — research publication logic
- `processing` — base structure for order/request processing

## Demo Accounts

The following accounts are preconfigured in `Main` and can be used for login:

| Role | Email | Password |
|---|---|---|
| Admin | `admin@university.com` | `admin123` |
| Teacher | `aibek@uni.kz` | `teach123` |
| Teacher | `zarina@uni.kz` | `teach456` |
| Student | `arman@uni.kz` | `stud123` |
| Student | `dana@uni.kz` | `stud456` |
| Manager | `gulnara@uni.kz` | `man123` |

## How to Run

### Requirements

- JDK 8+  
  The project was verified with `javac 25.0.2`.

### Run in PowerShell

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out (Get-ChildItem -Recurse -Filter *.java -Path src | ForEach-Object { $_.FullName })
java -cp out project.Main
```

### Run in an IDE

1. Open the project in IntelliJ IDEA or any other Java IDE.
2. Make sure the `src` folder is configured as a source root.
3. Run the `project.Main` class.

## Example Scenarios

### Student

- enroll in a course
- view registered courses
- view grades
- open transcript
- rate a teacher
- run the research demo

### Teacher

- view assigned courses
- view student list
- assign grades
- check average rating
- run the research demo

### Manager

- approve students
- assign a course to a teacher
- sort students by GPA or name
- generate a report
- add news

### Admin

- view all users
- search for a user by name
- remove a user by ID
- view system logs

## Recent Improvements

- Fixed administrator authentication so the admin account can log in correctly.
- Improved researcher state handling:
  - `Teacher` research status is now derived from `TeacherStatus`
  - `Student` research activation is handled through a dedicated method
