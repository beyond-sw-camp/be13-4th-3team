# 스프링 부트 프로젝트를 도커 이미지로 만들기 위한 설정
FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache sed
LABEL maintainer="namoo36 wldnrkwhr36@naver.com"
LABEL version="1.0"
COPY ./build/libs/yyGang-0.0.1-SNAPSHOT.jar /root
# ARG BUILD_PROFILE=dev
# 이미지 빌드 시 사용할 변수
ARG BUILD_PORT=8000
ENV TZ=Asia/Seoul
# ENV APP_PROFILE=${BUILD_PROFILE}
EXPOSE ${BUILD_PORT}
WORKDIR /root
CMD ["java", "-jar", "yyGang-0.0.1-SNAPSHOT.jar"]