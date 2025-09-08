# InternQuest Project

**InternQuest**: a web application where users can track their internship progress through "activities" within companies, helping them stay motivated as they work towards securing their internship. This project uses the **Spring Framework** for backend development, offering a seamless platform to manage user profiles, company activities, and achievements, all aimed at boosting the internship experience.

## Brief Summary

InternQuest is a platform designed for users to manage and track their progress within an organization. The application allows users to:

- Create, delete and edit their profile
- Manage companies and their associated activities
- Earn achievements based on user actions
- Update and delete activities and companies

## Functions

### User Management
- **Create User**: Register a new user profile and set basic user information.
- **Login**: Secure authentication system allowing users to access their account.
- **Edit Account**: Modify user details such as name, email, and password.
- **Delete Account**: Delete the user account when needed.

### Company Management
- **Create Company**: Allows users to create a new company, assign industries, and manage company details.
- **Edit Company**: Modify the company information.
- **Delete Company**: Remove a company from the platform.
- **List Companies**: View all companies associated with the current user.

### Activity Management
- **Create Activity**: Users can create new activities, assign them to companies, and track deadlines.
- **Update Activity**: Modify the details of an existing activity.
- **Delete Activity**: Remove an activity from the system.
- **List Activities**: View a list of activities associated with a user.

### Achievement System
- **Earn Achievements**: Users can earn achievements based on actions such as creating an account or a company.
- **List Achievements**: View the list of earned achievements.

## Technologies

- **Backend**: 
    - **Spring Web** for models, views and controllers 
    - **Spring Security** for user authentication and authorization (BCryptPasswordEncoder is used for hashing and salting passwords)
    - **JPA/Hibernate** for database interaction

- **Frontend**:
    - **Thymeleaf** for rendering dynamic HTML pages
    - **Bootstrap** for responsive design and UI components
    

- **Database**:
    - **MySQL** for storing all the data

## Extra

- [Entity Relationship Diagram (ERD)](https://imgur.com/a/ys9xwuu) 🗺️
- [Trello Kanban Board](https://trello.com/b/v5k4sJ6D/kanban-java-advanced-internquest) 📋
- [Wikimedia Commons](https://commons.wikimedia.org/wiki/Main_Page)🆓


---
