FROM openjdk:21
ENV TZ=GMT+8
CMD ["sh","-c","cd /moe-backend && java -jar moe-backend-main.jar --spring.profiles.active=prod -XX:+UseZGC -XX:+ZGenerational -Xmx256m"]