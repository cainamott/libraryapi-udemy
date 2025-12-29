# build
FROM maven:3.8.8-amazoncorretto-21-al2023
WORKDIR /build

COPY . .

RUN mvn clean package -Dskiptests

# run
FROM amazoncorreto:21.0.5
WORKDIR /app

COPY --from-build ./build/target/*.jar ./libraryapi.jar

EXPOSE 8080
EXPOSE 9090

ENV DATASOURCE_URL=''
ENV DATASOURCE_USERNAME=''
ENV DATASOURCE_PASSWORD=''
ENV SPRING_PROFILES_ACTIVE='production'
ENV TZ='America/Sao_Paulo'

ENTRYPOINT java -jar libraryapi.jar


