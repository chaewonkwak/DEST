FROM openjdk:11-jre-slim

WORKDIR /app

COPY bin /app/bin
COPY bin /app/src

CMD ["java", "-cp", "bin", "dest.class"
