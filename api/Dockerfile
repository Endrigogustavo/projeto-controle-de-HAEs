FROM openjdk:21

WORKDIR /app

COPY . .

ENV JAVA_TOOL_OPTIONS="-Xmx300m"

EXPOSE 8080

CMD ["./mvnw", "spring-boot:run"]