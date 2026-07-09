````markdown
## Application Configuration

```properties
# ===============================
# Server
# ===============================
server.port=${SERVER_PORT:8080}

# ===============================
# Database - PostgreSQL
# ===============================
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/your_database_name}
spring.datasource.username=${DATABASE_USERNAME:your_database_username}
spring.datasource.password=${DATABASE_PASSWORD:your_database_password}
spring.datasource.driver-class-name=org.postgresql.Driver

# ===============================
# JPA / Hibernate
# ===============================
spring.jpa.hibernate.ddl-auto=${JPA_DDL_AUTO:update}
spring.jpa.show-sql=${JPA_SHOW_SQL:false}
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# ===============================
# Frontend
# ===============================
frontend.url=${FRONTEND_URL:http://localhost:4200}

# ===============================
# Mail - Gmail SMTP
# ===============================
spring.mail.host=${MAIL_HOST:smtp.gmail.com}
spring.mail.port=${MAIL_PORT:587}
spring.mail.username=${MAIL_USERNAME:your_email@gmail.com}
spring.mail.password=${MAIL_PASSWORD:your_gmail_app_password}

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# ===============================
# Python Service
# ===============================
ekyc.python.base-url=${EKYC_PYTHON_BASE_URL:http://localhost:8000}
ekyc.python.timeout-seconds=${EKYC_PYTHON_TIMEOUT_SECONDS:600}

# ===============================
# Multipart / File Upload
# ===============================
spring.servlet.multipart.location=${MULTIPART_TEMP_LOCATION:/tmp/spring-multipart}
spring.servlet.multipart.max-file-size=${MULTIPART_MAX_FILE_SIZE:10MB}
spring.servlet.multipart.max-request-size=${MULTIPART_MAX_REQUEST_SIZE:20MB}
server.tomcat.max-swallow-size=${TOMCAT_MAX_SWALLOW_SIZE:20MB}

# Allow larger Authorization headers, such as JWT tokens
server.max-http-request-header-size=${MAX_HTTP_REQUEST_HEADER_SIZE:64KB}

# ===============================
# Google OAuth2
# ===============================
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID:your_google_client_id}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET:your_google_client_secret}
```

## Environment Variables

For security, do not commit real credentials to GitHub.  
Use environment variables instead.

Example `.env` file:

```env
SERVER_PORT=8080

DATABASE_URL=jdbc:postgresql://localhost:5432/your_database_name
DATABASE_USERNAME=your_database_username
DATABASE_PASSWORD=your_database_password

FRONTEND_URL=http://localhost:4200

MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_gmail_app_password

EKYC_PYTHON_BASE_URL=http://localhost:8000
EKYC_PYTHON_TIMEOUT_SECONDS=600

MULTIPART_TEMP_LOCATION=/tmp/spring-multipart

GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
```

> **Important:** Never push real passwords, Gmail app passwords, database credentials, or Google OAuth secrets to GitHub.
````
