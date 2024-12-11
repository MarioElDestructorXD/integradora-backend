FROM openjdk:22-slim AS build
RUN apt-get update && apt-get install -y \
    curl \
    tar \
    && curl -fsSL https://archive.apache.org/dist/maven/maven-3/3.8.5/binaries/apache-maven-3.8.5-bin.tar.gz -o /tmp/maven.tar.gz \
    && tar -xzf /tmp/maven.tar.gz -C /opt/ \
    && ln -s /opt/apache-maven-3.8.5/bin/mvn /usr/bin/mvn \
    && rm -f /tmp/maven.tar.gz
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:22-slim
WORKDIR /app
COPY --from=build /app/target/0.0.1-SNAPSHOT.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
