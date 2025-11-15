FROM eclipse-temurin:21-jdk

ARG PORT

WORKDIR /app

COPY . .

RUN chmod +x mvnw && \
    if [ -f mvnw ]; then sed -i 's/\r$//' mvnw; fi

ENV JAVA_TOOL_OPTIONS="-Xmx300m"

EXPOSE ${PORT}

CMD ["./mvnw", "spring-boot:run"]