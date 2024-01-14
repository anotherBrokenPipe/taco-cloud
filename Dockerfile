FROM azul/zulu-openjdk-alpine:17

ARG APP_HOME=/opt/taco-cloud
ARG APP_JAR=taco-cloud.jar

ENV HOME=$APP_HOME \
    JAR=$APP_JAR

WORKDIR $HOME
COPY build/libs/taco-cloud.jar $HOME/$JAR
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar $JAR"]