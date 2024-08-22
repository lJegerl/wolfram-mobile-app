FROM openjdk:17-jdk-slim
ARG LIBS

RUN apt-get update && apt-get upgrade -y && apt-get install build-essential -y

COPY ./target/DemoWolframSingle-0.0.1-SNAPSHOT.jar /app/DemoWolframSingle-0.0.1-SNAPSHOT.jar

ADD libs /app/libs

EXPOSE 465

CMD ["java", "-Xlog:library=info",  "-Djava.library.path=/app/libs", "-jar", "/app/DemoWolframSingle-0.0.1-SNAPSHOT.jar"]