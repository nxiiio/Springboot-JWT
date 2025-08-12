# Springboot-JWT
Example project made with Spring Boot with JWT authentication



## Auth Endpoints
### 🗝️ Register
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register your user |

#### JSON Example:
```json
{
    "username": "santiago",
    "password": "1234",
    "roleRequest":{
        "roles": [
            "USER"
        ]
    }
}
```

### 🗝️ Login
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/login` | Login to the application |

#### JSON Example:
```json
{
    "username": "santiago",
    "password": "1234"
}
```
### 🏅 When accessing one of the endpoints, it will return a JWT token.

