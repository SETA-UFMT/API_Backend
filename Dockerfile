# CORRIGIDO: Padronizado para Java 21 como no pom.xml
FROM maven:3.9.8-eclipse-temurin-21-alpine as dev-build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src /build/src
RUN ["mvn","clean","package","-DskipTests"]

# CORRIGIDO: JDK 21 para consistência
FROM eclipse-temurin:21-jdk-alpine
COPY --from=dev-build /build/target/*.jar /app/app.jar
WORKDIR /app
EXPOSE 8080

# MELHORADO: Adiciona configurações JVM para containers
CMD ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]