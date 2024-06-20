# Stage 1: Maven Build
FROM maven:3.8.4-openjdk-21 as build

# Transfer the pom.xml file
COPY ./pom.xml ./pom.xml

# Fetch all dependencies
RUN mvn dependency:go-offline -B

# Copy your source code
COPY ./src ./src

# Build the application
RUN mvn clean package


# Stage 2: Build Docker Image with built .jar
FROM openjdk:21-jdk

# Copy the built JAR file from Stage 1
COPY --from=build /target/*.jar /opt/app/app.jar

# Execute the application
ENTRYPOINT ["java","-jar","/opt/app/app.jar"]