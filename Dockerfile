FROM bellsoft/liberica-openjdk-alpine-musl:21
RUN set -xe && apk --no-cache add ttf-dejavu fontconfig
ENV TZ=GMT-8

WORKDIR /app
COPY business/target/moe-backend-main.jar .

CMD ["sh","-c","java -jar -Xmx256m -Xss512k -XX:+UseG1GC moe-backend-main.jar --spring.profiles.active=prod"]