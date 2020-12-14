#!groovy
pipeline {

  agent {
    docker {
      image 'scmmanager/java-build:11.0.9_11.1'
      label 'docker'
    }
  }

  stages {
    stage('Build') {
      steps {
        sh './gradlew build'
        archiveArtifacts artifacts: 'app/build/libs/*.jar'
      }
    }
  }
}
