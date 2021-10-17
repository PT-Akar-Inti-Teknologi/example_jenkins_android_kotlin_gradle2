pipeline {
  agent any

  stages {
    stage('Build & Test') {
      agent {
        dockerfile {
          filename 'Dockerfile.jenkins'
          additionalBuildArgs '--build-arg JDK_VERSION=11 --build-arg TARGET_SDK=30 --build-arg GRADLE_VERSION=7.0'
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