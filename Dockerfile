FROM openjdk:15-slim

WORKDIR /app

COPY bin /app/bin
COPY src /app/src

RUN javac -source 15 -target 15 -d /app/bin $(find /app/src -name "*.java")

CMD ["java", "-cp", "bin", "Project.dest"]
