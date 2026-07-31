@echo off
echo Setting up Java 21 environment...
set JAVA_HOME=C:\Users\Hiii\.vscode\extensions\redhat.java-1.55.0-win32-x64\jre\21.0.11-win32-x86_64
set PATH=%JAVA_HOME%\bin;%PATH%

echo Java Version:
"%JAVA_HOME%\bin\java.exe" -version

echo Running Spring Boot Application...
"%JAVA_HOME%\bin\java.exe" -jar target/payment-service-1.0.0-SNAPSHOT.jar 2>nul || echo Please use VS Code Run button or compile project first.
pause
