FROM openjdk:17

# 设置容器内工作目录为 /app
WORKDIR /ConvertIDtoName

# 复制 jar 到容器中的 /app 目录
COPY target/ConvertIDtoName-0.0.1-SNAPSHOT.jar ConvertIDtoName.jar

# 执行启动命令
ENTRYPOINT ["java", "-jar", "ConvertIDtoName.jar"]

