# Use Maven image to build the app
FROM  maven:3.9.9-amazoncorretto-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests

FROM  amazoncorretto:21
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
#You can use versionning for ci/cd
#ARG APP_VERSION=1.0.0
# Expose port and set entry point
EXPOSE 8080
ENV DB_URL=jdbc:mysql://mysql-sql-bsn:3306/project_management_version_deploy
#ENV JAR_version=${APP_version}
#The entry point will be
#ENTRYPOINT ["java", "-jar", "-Dspring.datasource.url=${DB_URL}","app-${JAR_VERSION}.jar"]
ENTRYPOINT ["java", "-jar", "-Dspring.datasource.url=${DB_URL}","app.jar"]

