# SWIFT Codes Application

A fullstack application for managing SWIFT codes (Bank Identifier Codes) used in international banking transactions. The application provides a RESTful API and a web interface to search, view, add, and delete SWIFT codes.

## Features

- Parse and import SWIFT codes from Excel files
- Identify headquarter and branch relationships among SWIFT codes
- Search for SWIFT codes by code or country
- View detailed information about banks and their branches
- Add new SWIFT codes
- Delete existing SWIFT codes
- REST API with documented endpoints

## Technology Stack

### Backend
- Java 17
- Spring Boot 3.x
- Spring Data MongoDB
- MongoDB (document database)
- Apache POI (for Excel processing)
- Maven

### Frontend
- React 18
- React Router
- React Bootstrap
- Axios for API communication

### DevOps
- Docker & Docker Compose
- Nginx (for serving frontend)


## Setup and Running the Application

### Prerequisites
- Docker and Docker Compose installed on your system
- Java 17 JDK (for local development)
- Node.js and npm (for local frontend development)

### Running with Docker Compose

1. Clone the repository:
   ```bash
   git clone https://github.com/pcygan01/Remitly_swift_codes.git
   cd swift-codes-app
   ```

2. Place the `Interns_2025_SWIFT_CODES.xlsx` file in the `backend/src/main/resources/` directory

3. Build and start the containers:
   ```bash
   docker-compose up -d
   ```

4. Access the application at http://localhost

### Running Locally (Development)

#### Backend

1. Navigate to the backend directory:
   ```bash
   cd swift-codes-app/backend
   ```

2. Build and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. The backend will be available at http://localhost:8080

#### Frontend

1. Navigate to the frontend directory:
   ```bash
   cd swift-codes-app/frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

4. The frontend will be available at http://localhost:3000

## API Endpoints

### 1. Get SWIFT Code Details

- **URL**: `/v1/swift-codes/{swift-code}`
- **Method**: `GET`
- **Response**: Details of the specific SWIFT code (with branches if it's a headquarter)

### 2. Get SWIFT Codes by Country

- **URL**: `/v1/swift-codes/country/{countryISO2code}`
- **Method**: `GET`
- **Response**: All SWIFT codes for the specified country

### 3. Create SWIFT Code

- **URL**: `/v1/swift-codes`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "swiftCode": "EXAMPLECODE",
    "bankName": "EXAMPLE BANK",
    "countryISO2": "US",
    "countryName": "UNITED STATES",
    "address": "123 Main St",
    "isHeadquarter": true
  }
  ```
- **Response**: Success message

### 4. Delete SWIFT Code

- **URL**: `/v1/swift-codes/{swift-code}`
- **Method**: `DELETE`
- **Response**: Success message

## Testing

### Backend Tests

Navigate to the backend directory and run:
```bash
mvn test
```

## Data Model

The application implements the following data model for SWIFT codes:

- **SWIFT Code**: The unique identifier (e.g., `AAISITRAXXX`)
- **Bank Name**: The name of the bank
- **Country ISO2**: Two-letter country code (e.g., `US`)
- **Country Name**: Full name of the country
- **Address**: Physical address of the bank
- **Is Headquarter**: Boolean flag indicating if the code represents a headquarter

Headquarter-branch relationships are determined as follows:
- Codes ending with `XXX` represent a bank's headquarters
- Branches are associated with a headquarters if their first 8 characters match