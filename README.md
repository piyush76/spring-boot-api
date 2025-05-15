# Spring Boot User Pages API

A Spring Boot API project with jOOQ and OpenAPI integration for managing user pages based on a database schema.

## Project Structure

The project follows a standard Spring Boot architecture with the following components:

- **Models**: `UserPage`, `CompanyPage`, and `UserCompany` representing the database entities
- **Repository**: `UserPageRepository` using jOOQ for database operations
- **Service**: `UserPageService` implementing business logic
- **Controller**: `UserPageController` exposing REST endpoints with OpenAPI documentation
- **Configuration**: `JooqConfig` for setting up jOOQ with Spring Boot

## Database

For this proof of concept, an H2 in-memory database is used with schema and sample data defined in:
- `schema.sql`: Creates the database tables based on the provided schema
- `data.sql`: Populates the tables with sample data for testing

## API Endpoints

The following endpoints are implemented:

1. **GET /users/{userId}/companies/{companyId}/pages**
   - Retrieves all pages for a specific user in a specific company

2. **GET /users/{userId}/companies/{companyId}/pages/{pageId}**
   - Retrieves a specific page for a user in a company

3. **PUT /users/{userId}/companies/{companyId}/pages/{pageId}**
   - Creates or updates a page for a user in a company

4. **DELETE /users/{userId}/companies/{companyId}/pages/{pageId}**
   - Deletes a specific page for a user in a company

5. **DELETE /users/{userId}/companies/{companyId}/pages**
   - Deletes all pages for a user in a company

## Running the Application

To run the application:

```bash
cd spring-boot-user-pages-api
mvn spring-boot:run
```

The application will start on port 8080 and the H2 console will be available at http://localhost:8080/h2-console with the following connection details:
- JDBC URL: jdbc:h2:mem:userpagesdb
- Username: sa
- Password: password

## API Documentation

OpenAPI documentation is available at:
- http://localhost:8080/swagger-ui.html
- http://localhost:8080/api-docs

## Testing the API

You can test the API using curl commands:

```bash
# Get all pages for a user in a company
curl -X GET http://localhost:8080/users/1001/companies/COMP001/pages

# Get a specific page
curl -X GET http://localhost:8080/users/1001/companies/COMP001/pages/PAGE001

# Create or update a page
curl -X PUT http://localhost:8080/users/1001/companies/COMP001/pages/PAGE001 \
  -H "Content-Type: application/json" \
  -d '{"companyId":"COMP001","personnelId":1001,"pageId":"PAGE001","adminRole":"ADMIN"}'

# Delete a specific page
curl -X DELETE http://localhost:8080/users/1001/companies/COMP001/pages/PAGE001

# Delete all pages for a user in a company
curl -X DELETE http://localhost:8080/users/1001/companies/COMP001/pages
```

## Production Considerations

For a production environment, you would need to:
1. Configure an Oracle database connection instead of H2
2. Generate jOOQ classes from the Oracle database schema
3. Implement proper authentication and authorization
4. Add validation and error handling
5. Configure logging and monitoring
