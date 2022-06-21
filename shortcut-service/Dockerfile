FROM moxm/java:1.8-full

RUN mkdir -p /shortcut-service

WORKDIR /shortcut-service

ARG JAR_FILE=shortcut-service/target/shortcut-service.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8202

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m"

CMD sleep 60; java -jar app.jar $JAVA_OPTS
