pipeline {
    agent {
        kubernetes {
            yaml '''
            apiVersion: v1
            kind: Pod
            metadata:
              name: jenkins-agent
            spec:
              containers:
              - name: gradle
                image: gradle:8.5-jdk21-alpine
                command:
                - cat
                tty: true
              - name: docker
                image: docker:27.2.0-alpine3.20
                command:
                - cat
                tty: true
                volumeMounts:
                - mountPath: "/var/run/docker.sock"
                  name: docker-socket
              volumes:
              - name: docker-socket
                hostPath:
                  path: "/var/run/docker.sock"
            '''
        }
    }
    // 전체 파이프라인에서 공통적으로 사용할 환경변수
    environment {
        // IMAGE_NAME : 도커hub에 올릴 이름
        // IMAGE_NAME = "namoo36/yyGang-api"
        TAG = "latest"
        DOCKER_IMAGE_NAME = 'namoo36/yygang-api'
        DOCKER_CREDENTIALS_ID = 'dockerhub-access'
    }

    stages {
        stage('Gradle Build') {
            steps {
                container('gradle') {
                    sh 'pwd'
                    sh 'ls -al'
                    sh './gradlew clean build' // 프로젝트 clean -> build
                    sh 'ls -al build/libs'
                }
            }
        }
        stage('Image build & Push'){
            steps {
                container('docker'){
                    script{
                        // 그루비 변수 사용 -> 환경 변수를 사용해 이름/버전정보를 가져오도록 설정
                        def dockerImageVersion = "${env.BUILD_NUMBER}"

                        sh 'docker logout'

                        // withCredentials
                        // - 파이프라인에서 자격 증명을 사용할 수 있는 블록을 생성한다.
                        // usernamePassword
                        // - 자격증명 중 사용자 이름과 비밀번호를 가져올 때 사용함
                        // - credentialsId는 자격 증명에서 가져온 사용자 이름, 아이디
                        // - usernameVariable은 자격 증명에서 가져온 사용자 이름을 저장하는 환경 변수의 이름을 작성한다.
                        // - passwordVariabl는 자격 증명에서 가져온 비밀번호를 저장하는 환경 변수의 이름을 작성
                        withCredentials([usernamePassword(
                            credentialsId: DOCKER_CREDENTIALS_ID, 
                            passwordVariable: 'DOCKER_PASSWORD', 
                            usernameVariable: 'DOCKER_USERNAME')]){
                            // 자격 증명은 이 블럭 안에서만 유효함
                            sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                        }

                        // 파이프라인 단계에서 환경 변수를 설정하는 역할을 한다.ㅇㅇ
                        withEnv(["DOCKER_IMAGE_VERSION=${dockerImageVersion}"]){
                            sh 'docker -v'
                            sh 'echo $DOCKER_IMAGE_NAME:$DOCKER_IMAGE_VERSION'
                            // sh 'docker images university-api'
                            sh 'docker build --no-cache -t $DOCKER_IMAGE_NAME:$DOCKER_IMAGE_VERSION ./'
                            sh 'docker image inspect $DOCKER_IMAGE_NAME:$DOCKER_IMAGE_VERSION'
                            sh 'docker push $DOCKER_IMAGE_NAME:$DOCKER_IMAGE_VERSION'
                        }

                    }
                }
            }
        }

        // stage('Run App') {
        //     steps {
        //         container('gradle') {
        //             sh 'java -jar build/libs/yyGang-0.0.1-SNAPSHOT.jar > app.log 2>&1 &'
        //         }
        //     }
        // }
        // manifest 실행 스테이지
        stage('Trigger yygang-manifest'){
            steps{
                script {
                    def dockerImageVersion = "${env.BUILD_NUMBER}"

                     withEnv(["DOCKER_IMAGE_VERSION=${dockerImageVersion}"]){
                            // 다른 잡을 빌드하면서 파라미터 전달
                            build job: 'yygang-manifest',
                                parameters: [
                                    string(name: 'DOCKER_IMAGE_VERSION',defaultValue: 'latest', value: "${DOCKER_IMAGE_VERSION}")
                                ],
                                wait: true // 하위 잡이 끝날 떄까지 기다림
                        }
                }
            }
        }
    }


    post {
        always{
            withCredentials([string(
                credentialsId: 'discord-webhook', 
                variable: 'DISCORD_WEBHOOK_URL'
            )]){
                discordSend description: """
                제목 : ${currentBuild.displayName}
                결과 : ${currentBuild.result}
                실행 시간 : ${currentBuild.duration / 1000}s
                """,
                result: currentBuild.currentResult,
                title: "${env.JOB_NAME} : ${currentBuild.displayName}", 
                webhookURL: "${DISCORD_WEBHOOK_URL}"
            }
        }
    }
}
