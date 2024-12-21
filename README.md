# Student Data Management System

## Problem Statement
A school wants to digitize its student data management process. Currently, the student data is stored manually in a register with the following structure:

| Student ID | Student Name | Roll No | Class | Address (Area, Block, District, State, Country) |

This project aims to create a Spring Boot application to manage and store student data in a relational database. The application should include the following features and requirements:

---

## Database Structure
The application will use a relational database with the following tables:

1. **Country Table**
   - **Fields**: `id`, `name`
   - **Constraint**: Country names must be unique.

2. **State Table**
   - **Fields**: `id`, `name`, `country_id`
   - **Constraint**: State names must be unique across all countries.

3. **District Table**
   - **Fields**: `id`, `name`, `state_id`
   - **Constraint**: District names must be unique within a particular state.

4. **Block Table**
   - **Fields**: `id`, `name`, `district_id`
   - **Constraint**: Block names must be unique within a particular district.

5. **Area Table**
   - **Fields**: `id`, `name`, `block_id`
   - **Constraint**: Area names must be unique within a particular block.

6. **Student Table**
   - **Fields**: `id`, `student_name`, `roll_no`, `class`, `address`, `country_id`, `state_id`, `district_id`, `block_id`, `area_id`
   - **Constraints**:
     - Roll numbers must be unique within the same class.
     - A combination of `roll_no` and `class` must be unique.

---

## Features and Functionalities

### 1. Bulk Upload from Excel
The application provides functionality to upload an Excel file containing up to 1000 student records in the following format:

| Student Name | Roll No | Class | Address | Area | Block | District | State | Country |
|--------------|---------|-------|---------|------|-------|----------|-------|---------|
| Student 1    | 1       | 4     | Barahi  | Chari Ali | Urban Area | Soanri | Charaideo | Assam | India |

#### Workflow:
1. **Validate Input Data**
   - Ensure all required fields are present.
   - Verify the uniqueness of:
     - Country, state, district, block, and area names.
     - Roll numbers within each class.
2. **Save Data**
   - Insert validated data into the respective database tables (Country, State, District, Block, Area, and Student).
3. **Handle Duplicates and Errors**
   - Check for existing entries based on uniqueness constraints.
   - Provide detailed error reporting for invalid or duplicate rows in the uploaded file.

### 2. Generate Excel Report
Generate an Excel report in the same format as the manual register. The report will include:

| Student ID | Student Name | Roll No | Class | Address (Area, Block, District, State, Country) |

---

## Technologies Used
- **Java**: For backend development.
- **Spring Boot**: Framework for creating the application.
- **MySQL/PostgreSQL**: Relational database to store data.
- **Apache POI**: For handling Excel file uploads and report generation.
- **Maven/Gradle**: Build automation tools.

---

## Installation and Setup
1. Clone the repository.
   ```bash
   git clone <repository_url>
   cd <repository_name>
   ```
2. Set up the database:
   - Create the required tables in your relational database using the schema defined above.
3. Configure the application properties:
   - Update the `application.properties` file with your database credentials.
4. Build and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
5. Access the application APIs via Postman or your preferred REST client.

---

## API Endpoints
### 1. Bulk Upload API
- **Endpoint**: `/api/upload`
- **Method**: `POST`
- **Description**: Accepts an Excel file and uploads student data to the database.
- **Validation**: Ensures data adheres to the specified constraints.

### 2. Generate Report API
- **Endpoint**: `/api/export`
- **Method**: `GET`
- **Description**: Generates an Excel report in the manual register format.

---

## Error Handling
- Proper error messages will be returned for:
  - Duplicate entries.
  - Invalid or missing data in the uploaded Excel file.

---

## Future Enhancements
- Add additional APIs for CRUD operations on student data.
- Implement authentication and authorization using Spring Security.
- Create a frontend interface for easier user interaction.
- Optimize bulk upload performance for larger datasets.


---

## Contributing
Contributions are welcome! Please open an issue or submit a pull request for any improvements or bug fixes.
