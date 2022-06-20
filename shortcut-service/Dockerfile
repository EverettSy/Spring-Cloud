FROM moxm/java:1.8-full

RUN mkdir -p /short-service

WORKDIR /short-service

ARG JAR_FILE=target/short-service.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8202

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m"

CMD sleep 60; java -jar app.jar $JAVA_OPTS
