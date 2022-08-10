FROM openjdk:8
# MAINTAINER ford_ji@epam.com
VOLUME /data

# 简化 jar 的名字路径 （#：表示注释掉）
ADD  ./target/todo-usercenter.jar todo-usercenter.jar

# 执行 java -jar 命令 （CMD：在启动容器时才执行此行。RUN：构建镜像时就执行此行）
CMD java -jar -Xms128M -Xmx256M todo-usercenter.jar