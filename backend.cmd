@echo off
REM Run Spring Boot backend using Maven Wrapper
cd /d %~dp0rush_solver
call mvnw spring-boot:run