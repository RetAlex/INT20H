FROM seypy/maven_jdk_11:initial
COPY pom.xml /Project/pom.xml
COPY src /Project/src
COPY front/dist /Project/src/main/resources
WORKDIR /Project
RUN mvn clean package -Dmaven.test.skip=trueg