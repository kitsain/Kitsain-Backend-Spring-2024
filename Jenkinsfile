node("master") {
  def WORKSPACE = "/var/lib/jenkins/workspace/springboot-deploy"
  def dockerImageTag = "blogs${env.BUILD_NUMBER}"

  try {
    cleanWs()

    stage('Clone Repo') {
      git url: 'https://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@github.com/tuanbeovnn/Tampere_Software_ProjectV2.git',
        credentialsId: 'blogs',
        branch: 'main'
    }

    stage('Run check style') {
       sh '''
         export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64/
         cp /.env src/main/resources/
         mvn checkstyle:check
       '''
    }

    stage('Testing') {
      sh '''
        export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64/
        mvn test
      '''
    }

    stage('Build docker') {
      sh "whoami"
      sh "DOCKER_BUILDKIT=1 docker build -t blogs:${env.BUILD_NUMBER} ."
    }

    stage('Deploy docker') {
      echo "Docker Image Tag Name: ${dockerImageTag}"
      sh "docker stop blogs || true && docker rm blogs || true"
      sh "docker run --name blogs -d -p 9090:8080 blogs:${env.BUILD_NUMBER}"
    }
  } catch (e) {
    currentBuild.result = 'FAILURE'
    throw e
  }
}