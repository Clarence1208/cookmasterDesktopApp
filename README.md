# CookMaster Desktop App

The CookMaster Desktop App is a Java application designed for managing and organizing recipes. It provides a user-friendly interface for creating, editing, and organizing recipes, as well as generating PDF versions of recipes.

## Prerequisites

- Java Development Kit (JDK) 17 or later
- Gradle build system

## Getting Started

1. Clone the repository to your local machine:


2. Navigate to the project directory:

cd cookmaster-desktop-app


3. Build the project using Gradle:

./gradlew build


4. Run the application:

./gradlew run


Alternatively, you can run the application using the generated distribution ZIP file:

- Extract the ZIP file located in `build/distributions`.
- Navigate to the extracted directory.
- Run the launcher script (`app` on Unix/Linux or `app.bat` on Windows).

## Features

- Login page for user authentication
- Recipe creation and editing
- Recipe organization and categorization
- PDF generation of recipes
- Integration with Keycloak for user management (dependencies commented out in the build file)

## Dependencies

The CookMaster Desktop App uses the following dependencies:

- ControlsFX 11.1.2
- FormsFX 11.6.0
- BootstrapFX 0.4.0
- JFreeChart 1.5.3
- PDFBox 2.0.24
- JSON 20210307

Please refer to the `build.gradle` file for the complete list of dependencies.
