# Car Parking Management System

A **Java-based Car Parking Management System** developed as a college mini project. The system allows users to view and book available parking slots, check their parking status, and make digital payments for their parking.

The project simulates a real-world parking management system using **dummy data and simulated payments**. No real money or real payment gateway is involved.

## Features

* User registration and login
* View available parking slots
* Book a parking slot
* Check current parking/booking status
* Store vehicle details
* Calculate parking charges
* Simulated digital payment
* View parking/booking details
* Admin login and parking management
* Manage parking slot availability

## Tech Stack

* **Programming Language:** Java
* **Database:** PostgreSQL
* **Database Connectivity:** JDBC
* **JDBC Driver:** PostgreSQL JDBC Driver
* **Development Environment:** VS Code
* **Version Control:** Git & GitHub

## Algorithms and Concepts Used

The project uses basic programming and data-management concepts learned in Java:

* **Searching** – To find available parking slots and user/booking records.
* **Conditional Logic** – To determine slot availability, parking status, and payment status.
* **Sorting** – Used where required for displaying parking/booking records.
* **CRUD Operations** – Create, Read, Update, and Delete operations for managing users, vehicles, bookings, and parking slots.
* **Slot Allocation Logic** – Allocates an available parking slot to a user during booking.
* **Fee Calculation** – Calculates the parking amount based on the defined parking duration/rules.
* **Input Validation** – Validates user inputs such as vehicle and booking information.

## Main Modules

### User Module

Users can:

* Register and log in
* View available slots
* Book a slot
* Check their parking status
* View booking details
* Make a simulated digital payment

### Admin Module

The administrator can:

* Log in using admin credentials
* Manage parking slots
* View booking information
* Check parking availability
* Manage parking-related records

### Payment Module

The payment module simulates digital payment. It is included only for demonstration purposes and **does not process real financial transactions**.


## Database

The system uses **PostgreSQL** to store project-related information such as:

* User details
* Vehicle details
* Parking slots
* Booking information
* Payment status

The Java application connects to PostgreSQL using **JDBC**.


## Project Disclaimer

This project is developed **for educational purposes as a college mini project**.

* All user and parking data is dummy/sample data.
* The payment system is simulated.
* No real money is transferred.
* No real payment gateway or banking system is connected.
* The project should not be considered a production-ready parking or payment system.

## Project Structure

```text
## Project Structure

```text
Car-Parking-Management-System/
│
├── frontend/
│   └── User interface files
│
├── backend/
│   └── Java source files
│
├── database/
│   └── PostgreSQL SQL files
│
├── docs/
│   └── Project documentation
│
├── README.md
└── .gitignore
```


## Purpose

This project was created to apply concepts of **Java programming, Object-Oriented Programming, database management, JDBC, and basic algorithms** to a real-world inspired problem.

---

