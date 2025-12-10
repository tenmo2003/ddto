# DynamicDTO

A Java library for annotation and interface-based serialization of POJOs with Jackson, enabling dynamic type handling during JSON serialization and deserialization.

## Overview

DynamicDTO provides a flexible solution for serializing and deserializing Java objects where the exact type needs to be preserved and restored during JSON processing. This is particularly useful in scenarios where you need to handle polymorphic objects or when the concrete type information must be maintained across serialization boundaries.

## Key Features

- **Dynamic Type Preservation**: Automatically includes type information in JSON output
- **Annotation-Based Configuration**: Use `@TypeName` to customize type names and aliases
- **Jackson Integration**: Seamless integration with Jackson ObjectMapper
- **Reflection-Based Discovery**: Automatically discovers and registers DTO classes
- **Flexible Type Mapping**: Support for custom type names and multiple aliases

## How It Works

The library works by:

1. **Registration**: Scans a base package for classes annotated with `@TypeName` and implementing `DynamicDTO`
2. **Serialization**: Adds a `__type` field to JSON output containing the registered type name
3. **Deserialization**: Reads the `__type` field to determine the correct class for instantiation

## Quick Start

### 1. Add Dependencies

```xml
<dependency>
    <groupId>com.tenmo2003</groupId>
    <artifactId>dynamicdto</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### 2. Create Your DTO Classes

```java
import com.tenmo2003.dynamicdto.dto.abstraction.DynamicDTO;
import com.tenmo2003.dynamicdto.registry.TypeName;

@TypeName("user")
public class UserDTO implements DynamicDTO {
    private String name;
    private String email;

    // constructors, getters, setters...
}

@TypeName(value = "admin", aliases = {"administrator", "admin_user"})
public class AdminDTO implements DynamicDTO {
    private String name;
    private String permissions;

    // constructors, getters, setters...
}
```

### 3. Configure Jackson ObjectMapper

```java
import com.tenmo2003.dynamicdto.jackson.DynamicDTOModule;
import com.tenmo2003.dynamicdto.registry.DynamicDTORegistry;
import tools.jackson.databind.ObjectMapper;

// Create registry for your DTO package
DynamicDTORegistry registry = new DynamicDTORegistry("com.yourpackage.dto");

// Configure ObjectMapper
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new DynamicDTOModule(registry));

// Use the mapper
UserDTO user = new UserDTO("John", "john@example.com");
String json = mapper.writeValueAsString(user);
// Output: {"__type":"user","name":"John","email":"john@example.com"}

DynamicDTO restored = mapper.readValue(json, DynamicDTO.class);
// restored will be an instance of UserDTO
```

## Core Components

### DynamicDTO Interface

The marker interface that all dynamic DTOs must implement:

```java
public interface DynamicDTO {
}
```

### @TypeName Annotation

Configures type names and aliases for DTO classes:

```java
@TypeName(value = "custom_name", aliases = {"alias1", "alias2"})
public class MyDTO implements DynamicDTO {
    // class implementation
}
```

- `value`: Custom type name (defaults to class simple name if empty)
- `aliases`: Additional names that can be used for deserialization

### DynamicDTORegistry

Manages the mapping between type names and classes:

```java
DynamicDTORegistry registry = new DynamicDTORegistry("com.example.dto");
Class<?> clazz = registry.getClass("user");
String typeName = registry.getName(UserDTO.class);
```

### DynamicDTOModule

Jackson module that provides serialization and deserialization support:

```java
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new DynamicDTOModule(registry));
```

## JSON Format

Serialized objects include a special `__type` field:

```json
{
  "__type": "user",
  "name": "John Doe",
  "email": "john@example.com"
}
```

During deserialization, this field is used to determine the correct class and is automatically removed from the final object.

## Requirements

- Java 17 or higher
- Jackson 3.0.3
- Reflections 0.10.2

## Dependencies

The library uses the following dependencies:

- `tools.jackson.core:jackson-core:3.0.3`
- `tools.jackson.core:jackson-databind:3.0.3`
- `org.reflections:reflections:0.10.2`
- `org.projectlombok:lombok:1.18.42` (optional, for development)

## Building

```bash
# Build the project
./mvnw clean compile

# Run tests
./mvnw test

# Create JAR
./mvnw package
```

## Author

- **anhvn** - Initial work

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## Support

For questions, issues, or contributions, please use the GitHub issue tracker.
