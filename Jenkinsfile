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
    
    stage('Update GitHub') {
      when {
        branch pattern: 'main', comparator: 'GLOB'
	    expression { return isBuildSuccess() }
      }
      steps {
        sh 'git checkout main'
        
        // push changes to GitHub
        authGit 'cesmarvin', "push -f https://github.com/scm-manager/smp-maven-to-gradle main --tags"
      }
    }
  }
  
}



boolean isBuildSuccess() {
  return currentBuild.result == null || currentBuild.result == 'SUCCESS'
}

void authGit(String credentials, String command) {
  withCredentials([
    usernamePassword(credentialsId: credentials, usernameVariable: 'AUTH_USR', passwordVariable: 'AUTH_PSW')
  ]) {
    sh "git -c credential.helper=\"!f() { echo username='\$AUTH_USR'; echo password='\$AUTH_PSW'; }; f\" ${command}"
  }
}