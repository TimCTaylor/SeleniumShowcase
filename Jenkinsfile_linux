pipeline {
    agent any
    options {
        skipStagesAfterUnstable()
    }
    tools {
        maven 'maven-default' // Name of the Maven installation configured in Jenkins that refers to my default local installation
    }
    stages {
        stage('Build') {

            steps {
                script {
                    // Use the Maven tool. Belt and bracers. We've already defined this tool, but the code below ensures that the PATH is correctly set.
                    // and so this is the version of Maven that is actually used.
                    def mvnHome = tool name: 'maven-default', type: 'maven'
                    env.PATH = "${mvnHome}/bin:${env.PATH}"
                }
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Deliver') { 
            steps {
                sh './jenkins/scripts/deliver.sh' 
            }
        }
    }
}