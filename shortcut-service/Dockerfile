#FROM moxm/java:1.8-full
#
#RUN mkdir -p /shortcut-service
#
#WORKDIR /shortcut-service
#
#VOLUME /tmp
#
#ARG JAR_FILE=target/shortcut-service.jar
#
#ADD ${JAR_FILE} app.jar
#
#EXPOSE 8202
#
#ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m"
#
#CMD sleep 60; java -jar app.jar $JAVA_OPTS

# Docker image for springboot file run
# VERSION 0.0.1
# Author: sdp
# 基础镜像使用java
FROM java:8
# 作者
MAINTAINER sdp <xxx@xxx.com>
# VOLUME 指定了临时文件目录为/tmp。
# 效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
VOLUME /tmp
# 将jar包添加到容器中并更名为common-mail-service.jar
ADD target/shortcut-service.jar /shortcut-service.jar
# 运行jar包
RUN bash -c 'touch /shortcut-service.jar'
ENV TZ 'Asia/Shanghai'
EXPOSE 8089
ENTRYPOINT ["java","-jar","/shortcut-service.jar"]
