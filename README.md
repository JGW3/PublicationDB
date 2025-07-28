# PublicationDB - Secure Retail Database

A comprehensive retail-oriented database application built with Java and SQLite, featuring robust data management capabilities, secure input validation, and a professional user interface designed for handling thousands of records efficiently.

## 🚀 Features

### Core Functionality
- **Comprehensive Data Management**: Efficiently handle thousands of publication records with full CRUD operations
- **Advanced Input Validation**: Multi-layer validation system preventing invalid data entry and SQL injection
- **Robust Error Handling**: Professional-grade exception handling with user-friendly error messages
- **Search & Filter**: Advanced query capabilities for quick record retrieval
- **Data Integrity**: Enforced referential integrity and data consistency checks

### Security & Performance
- **SQL Injection Protection**: Parameterized queries and input sanitization
- **Data Validation**: Client-side and server-side validation for all inputs
- **Transaction Management**: Atomic operations ensuring data consistency
- **Optimized Performance**: Efficient database queries and connection management
- **Error Recovery**: Graceful handling of database connection issues and data conflicts

### User Experience
- **Intuitive Interface**: Clean, professional Java Swing/AWT interface
- **Responsive Design**: Smooth user interactions with immediate feedback
- **Professional Layout**: Well-organized forms and data display tables
- **User-Friendly Navigation**: Logical workflow for database operations

## 🛠️ Technology Stack

- **Frontend**: Java Swing/AWT
- **Backend**: Java SE
- **Database**: SQLite 3.x
- **Architecture**: MVC (Model-View-Controller) pattern
- **Build System**: Standard Java compilation

## 📋 Prerequisites

- Java Development Kit (JDK) 8 or higher
- SQLite JDBC driver (included in project)
- Any Java IDE (Eclipse, IntelliJ IDEA, NetBeans) or command line tools

## 🚀 Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/JGW3/PublicationDB.git
   cd PublicationDB
   ```

2. **Compile the application**
   ```bash
   javac -cp ".:sqlite-jdbc-3.47.1.0.jar" src/publications/*.java
   ```

3. **Run the application**
   ```bash
   java -cp ".:sqlite-jdbc-3.47.1.0.jar" publications.Main
   ```

## 💡 Usage

1. **Launch the Application**: Start the program to initialize the database connection
2. **Add Records**: Use the intuitive forms to input publication data
3. **Search & Browse**: Utilize the search functionality to find specific records
4. **Edit & Update**: Modify existing records with validation checks
5. **Data Management**: Perform bulk operations and database maintenance

## 📁 Project Structure

```
PublicationDB/
├── src/
│   ├── publications/       # Main application package
│   │   ├── DBConnect.java  # Database connection management
│   │   ├── DBLoader.java   # Data loading utilities
│   │   ├── DBOps.java      # Database operations
│   │   ├── Main.java       # Application entry point
│   │   └── SubscriptionUtils.java  # Subscription management
│   ├── project.db          # SQLite database file
│   └── preload.txt         # Initial data file
├── sqlite-jdbc-3.47.1.0.jar  # SQLite JDBC driver
├── PublicationDB.iml       # IntelliJ IDEA module file
└── README.md
```

## 🔧 Key Implementation Highlights

- **Input Validation**: Comprehensive validation preventing data corruption and security vulnerabilities
- **Error Handling**: Professional exception management with meaningful user feedback
- **Database Design**: Normalized schema ensuring data integrity and optimal performance
- **Code Architecture**: Clean separation of concerns following MVC principles
- **Resource Management**: Proper handling of database connections and memory usage

## 🎯 Development Focus

This project demonstrates proficiency in:
- Java desktop application development
- Database design and management
- Secure coding practices
- User interface design
- Error handling and data validation
- Professional software architecture

## 📈 Performance

- Handles thousands of records efficiently
- Optimized database queries for quick response times
- Memory-efficient design for extended usage
- Robust error recovery mechanisms

---

**Note**: This application was developed as a demonstration of secure database application development practices, emphasizing data integrity, user experience, and professional-grade error handling.