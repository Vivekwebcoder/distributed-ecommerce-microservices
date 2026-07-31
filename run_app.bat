@echo off
title Payment Service Microservice Runner
echo ========================================================
echo   Starting Payment Service Microservice (Port 8086)
echo ========================================================
echo.

set JAVA_HOME=C:\Users\Hiii\.vscode\extensions\redhat.java-1.55.0-win32-x64\jre\21.0.11-win32-x86_64
set PATH=%JAVA_HOME%\bin;%~dp0maven\bin;%PATH%

echo Java 21 Environment Detected:
"%JAVA_HOME%\bin\java.exe" -version
echo.

if not exist "%~dp0maven\bin\mvn.cmd" (
    echo [INFO] Maven binaries setting up, please wait a few seconds...
    pause
    exit /b
)

echo Starting Spring Boot Application...
call "%~dp0maven\bin\mvn.cmd" spring-boot:run
pause
