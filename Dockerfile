FROM gradle:8.11.1-focal AS build

WORKDIR /workspace

COPY src ./src
COPY build.gradle.kts ./build.gradle.kts
COPY settings.gradle.kts ./settings.gradle.kts
RUN gradle build -x test

FROM bellsoft/liberica-openjdk-alpine:17

RUN adduser --system spring-boot && addgroup --system spring-boot && adduser spring-boot spring-boot
USER spring-boot

WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar ./application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]