pipeline {
  agent any

  stages {
    stage('Build & Test') {
      when {
        branch 'main'
      }
      agent {
        dockerfile {
          filename 'Dockerfile.jenkins'
          additionalBuildArgs '--build-arg JDK_VERSION=11 --build-arg TARGET_SDK=30 --build-arg GRADLE_VERSION=7.0.2'
          args '-u root'
          reuseNode true
        }
      }

      steps {
        sh 'gradle check'
      }
    }
  }
}