# Build frontend
FROM node:20-alpine AS frontend-build
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# Build backend
FROM maven:3.9-eclipse-temurin-24-alpine AS backend-build
WORKDIR /app
COPY rush_solver/pom.xml ./
COPY rush_solver/src ./src
RUN mvn package -DskipTests

# Final stage
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app

# Copy built artifacts
COPY --from=backend-build /app/target/*.jar app.jar
COPY --from=frontend-build /app/frontend/dist ./static

# Expose port
EXPOSE 10005

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application
CMD ["java", "-jar", "app.jar"]