#!/bin/sh
DIR=dirname ""
exec java -classpath "/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
