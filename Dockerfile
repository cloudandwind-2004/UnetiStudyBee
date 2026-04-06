# ========= Stage 0: lấy docker CLI =========
FROM docker:27-cli AS dockercli

# ========= Stage 1: build jar =========
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

ENV MAVEN_OPTS="-Xmx512m -Xms256m -XX:MaxMetaspaceSize=192m"

RUN chmod +x mvnw \
  && sed -i 's/\r$//' mvnw \
  && ./mvnw -q -DskipTests dependency:go-offline \
  && ./mvnw -q -DskipTests package

# ========= Stage 2: runtime =========
FROM eclipse-temurin:17-jre
WORKDIR /app

# copy docker cli vào runtime image
COPY --from=dockercli /usr/local/bin/docker /usr/local/bin/docker

# (khuyến nghị) có CA cert để gọi HTTPS, và tzdata nếu bạn log giờ VN
RUN apt-get update \
  && apt-get install -y --no-install-recommends ca-certificates tzdata \
  && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8097

ENTRYPOINT ["sh", "-c", "java \
  -Xmx300m \
  -Xms200m \
  -XX:MaxMetaspaceSize=128m \
  -XX:+UseSerialGC \
  -XX:ReservedCodeCacheSize=64m \
  -Xss512k \
  -XX:+TieredCompilation \
  -XX:TieredStopAtLevel=1 \
  ${JAVA_OPTS} \
  -jar app.jar"]
