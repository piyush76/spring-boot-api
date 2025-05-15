# Converting to Stoplight API-First Approach

This guide explains how to convert the existing Spring Boot User Pages API to follow Stoplight API-first principles.

## What is API-First Development?

API-first development is an approach where you design and document your API before implementing it. This ensures that:

1. The API is well-designed and consistent
2. Documentation is accurate and up-to-date
3. Multiple teams can work in parallel (frontend, backend, etc.)
4. Testing can begin earlier in the development cycle

## Steps to Convert to API-First with Stoplight

### 1. Create OpenAPI Specification

We've already created an initial OpenAPI specification based on our existing implementation:

```
src/main/resources/api/openapi.yaml
```

This file defines all endpoints, request/response schemas, and parameters for our User Pages API.

### 2. Import into Stoplight Studio

1. Download and install [Stoplight Studio](https://stoplight.io/studio/)
2. Create a new project or open an existing one
3. Import the `openapi.yaml` file into your project
4. Use the visual editor to refine the API design

### 3. Design and Validate API

With Stoplight Studio, you can:

- Visually design endpoints, models, and parameters
- Add detailed descriptions and examples
- Set up mock servers for testing
- Validate your API against best practices
- Collaborate with team members

### 4. Generate Server Code

Stoplight can generate server code stubs based on your OpenAPI specification:

1. In Stoplight Studio, go to your API
2. Click on "Export" or "Generate Code"
3. Select "Spring Boot" as the target framework
4. Download the generated code

### 5. Integrate Generated Code with Existing Project

The generated code will include:

- Controller interfaces with method signatures
- Model classes
- API documentation

To integrate with our existing project:

1. Replace our current controller interfaces with the generated ones
2. Implement the controller interfaces with our existing business logic
3. Update model classes as needed
4. Keep our existing repository and service layers

### 6. Update Build Configuration

Add the following dependencies to your `pom.xml`:

```xml
<!-- OpenAPI Generator -->
<dependency>
    <groupId>org.openapitools</groupId>
    <artifactId>openapi-generator-maven-plugin</artifactId>
    <version>6.6.0</version>
    <scope>provided</scope>
</dependency>

<!-- Validation API -->
<dependency>
    <groupId>jakarta.validation</groupId>
    <artifactId>jakarta.validation-api</artifactId>
</dependency>
```

Add the OpenAPI Generator plugin:

```xml
<plugin>
    <groupId>org.openapitools</groupId>
    <artifactId>openapi-generator-maven-plugin</artifactId>
    <version>6.6.0</version>
    <executions>
        <execution>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <inputSpec>${project.basedir}/src/main/resources/api/openapi.yaml</inputSpec>
                <generatorName>spring</generatorName>
                <apiPackage>com.example.userpages.api</apiPackage>
                <modelPackage>com.example.userpages.model</modelPackage>
                <supportingFilesToGenerate>ApiUtil.java</supportingFilesToGenerate>
                <configOptions>
                    <delegatePattern>true</delegatePattern>
                    <interfaceOnly>true</interfaceOnly>
                    <useSpringBoot3>true</useSpringBoot3>
                </configOptions>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### 7. Implement the Generated Interfaces

Create implementation classes for the generated interfaces:

```java
@Service
public class UserPageApiImpl implements UserPageApi {

    private final UserPageService userPageService;

    public UserPageApiImpl(UserPageService userPageService) {
        this.userPageService = userPageService;
    }

    @Override
    public ResponseEntity<List<UserPage>> getUserPages(Long userId, String companyId) {
        // Implement using existing service
        List<UserPage> userPages = userPageService.getUserPages(userId, companyId);
        
        if (userPages.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(userPages);
    }

    // Implement other methods...
}
```

### 8. Continuous Integration with API-First

1. Add API linting to your CI pipeline using Spectral:
   ```bash
   npm install -g @stoplight/spectral-cli
   spectral lint src/main/resources/api/openapi.yaml
   ```

2. Generate documentation on each build:
   ```bash
   mvn clean compile
   ```

3. Run API tests against the specification

## Benefits of API-First with Stoplight

- **Improved Design**: Visual editor helps create better APIs
- **Consistency**: Enforces standards across your API
- **Documentation**: Always up-to-date with implementation
- **Testing**: Generate mock servers and tests from the spec
- **Client SDKs**: Generate client libraries for multiple languages
- **Collaboration**: Team members can review and contribute to API design

## API-First Development Workflow

1. **Design**: Create/update OpenAPI spec in Stoplight Studio
2. **Mock**: Test with mock servers before implementation
3. **Implement**: Generate server code and implement business logic
4. **Test**: Validate implementation against the spec
5. **Deploy**: Release the API with documentation
6. **Iterate**: Update the spec for new features/changes

## Resources

- [Stoplight Documentation](https://meta.stoplight.io/docs/platform/ZG9jOjIwNjk2MQ-welcome-to-stoplight-docs)
- [OpenAPI Specification](https://spec.openapis.org/oas/latest.html)
- [OpenAPI Generator](https://openapi-generator.tech/)
- [Spectral API Linter](https://stoplight.io/open-source/spectral)
