@echo off
set DIR=%~dp0
set APP_BASE_NAME=%~n0
set APP_HOME=%DIR%
set DEFAULT_JVM_OPTS=
set GRADLE_WRAPPER_JAR=%APP_HOME%gradle\wrapper\gradle-wrapper.jar
if exist "%GRADLE_WRAPPER_JAR%" (
  java %DEFAULT_JVM_OPTS% -classpath "%GRADLE_WRAPPER_JAR%" org.gradle.wrapper.GradleWrapperMain %*
) else (
  echo Gradle wrapper jar not found, downloading...
  powershell -Command "Invoke-WebRequest -Uri 'https://github.com/gradle/gradle/raw/master/gradle/wrapper/gradle-wrapper.jar' -OutFile '%GRADLE_WRAPPER_JAR%'"
  java -classpath "%GRADLE_WRAPPER_JAR%" org.gradle.wrapper.GradleWrapperMain %*
)
