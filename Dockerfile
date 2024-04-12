# 第一阶段：使用 Gradle 构建项目
FROM xldevops/jdk17-lts AS builder

# 设置工作目录
WORKDIR /app

# 复制 Gradle 相关文件以进行构建
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle ./gradle

# 复制项目源代码
COPY src ./src

# 复制本地的 Gradle 分发文件
COPY gradle-8.5-bin.zip .

# 解压 Gradle 分发文件
RUN unzip gradle-8.5-bin.zip

# 设置 Gradle 环境变量
ENV GRADLE_HOME=/app/gradle-8.5
ENV PATH=$PATH:$GRADLE_HOME/bin

# 构建项目并生成 JAR 文件
RUN gradle build

# 第二阶段：构建最终镜像
FROM xldevops/jdk17-lts

# 设置工作目录
WORKDIR /app

# 从第一阶段复制构建好的 JAR 文件到最终镜像
COPY --from=builder /app/build/libs/tally-0.0.1-SNAPSHOT-plain.jar app.jar

# 暴露应用程序的端口号
EXPOSE 8080

# 设置容器启动命令
CMD ["java", "-jar", "app.jar"]
