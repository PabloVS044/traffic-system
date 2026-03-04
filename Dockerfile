FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY src/ src/

RUN mkdir -p out && \
    find src -name "*.java" > sources.txt && \
    javac -d out @sources.txt && \
    rm sources.txt

WORKDIR /app/out
