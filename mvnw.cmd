@REM ----------------------------------------------------------------------------
@REM Maven Wrapper startup batch script
@REM ----------------------------------------------------------------------------

@if "%DEBUG%" == "" @echo off
@classworlds.conf exec %*

setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_DIR=%DIRNAME%
set APP_HOME=%DIRNAME%

set MAVEN_PROJECT_BASEDIR=%APP_BASE_DIR%

set WRAPPER_JAR="%APP_BASE_DIR%\.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

set JAVA_EXE=java.exe
if not "%JAVA_HOME%" == "" (
  set JAVA_EXE="%JAVA_HOME%\bin\java.exe"
)

%JAVA_EXE% -version >NUL 2>&1
if ERRORLEVEL 1 (
  if exist "C:\Users\Hiii\.vscode\extensions\redhat.java-1.55.0-win32-x64\jre\21.0.11-win32-x86_64\bin\java.exe" (
    set JAVA_EXE="C:\Users\Hiii\.vscode\extensions\redhat.java-1.55.0-win32-x64\jre\21.0.11-win32-x86_64\bin\java.exe"
  )
)

if not exist %WRAPPER_JAR% (
    echo Downloading Maven Wrapper...
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object Net.WebClient).DownloadFile('https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.0/maven-wrapper-3.3.0.jar', '%WRAPPER_JAR%')"
)

%JAVA_EXE% -classpath %WRAPPER_JAR% "-Dmaven.home=%APP_BASE_DIR%\.mvn\wrapper" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECT_BASEDIR%" %WRAPPER_LAUNCHER% %*
