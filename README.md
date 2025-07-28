# PublicationDB

A Java-based database application for managing publication records, developed for Graduate Database Systems coursework.

## Overview

This project implements a retail-oriented database system with a Java front-end and SQLite back-end. Features include comprehensive input validation, robust error handling, and efficient management of publication records.

## Technologies

- **Language**: Java
- **Database**: SQLite 3.x
- **JDBC Driver**: sqlite-jdbc-3.47.1.0.jar

## Setup & Running

1. **Compile the application**
   ```bash
   javac -cp ".:sqlite-jdbc-3.47.1.0.jar" src/publications/*.java
   ```

2. **Run the application**
   ```bash
   java -cp ".:sqlite-jdbc-3.47.1.0.jar" publications.Main
   ```

## Features

- CRUD operations for publication records
- Input validation and error handling
- SQLite database integration
- Java Swing user interface