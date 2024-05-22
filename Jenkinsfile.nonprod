pipeline {
    agent any
    environment {
        PROJECT_DIR = '.' // Root directory of the project
    }
    stages {
        stage('Compile') {
            steps {
                dir("${env.PROJECT_DIR}") {
                    sh 'make build'
                }
            }
        }
        stage('Run Tests') {
            steps {
                dir("${env.PROJECT_DIR}") {
                    sh 'make test'
                }
            }
        }
        stage('Deploy to Dev') {
            steps {
                dir("${env.PROJECT_DIR}") {
                    sh 'chmod +x deploy.sh'
                    sh 'make deploy ENV=dev'
                }
            }
        }
        stage('Deploy to Staging') {
            when {
                branch 'staging'
            }
            steps {
                dir("${env.PROJECT_DIR}") {
                    sh 'chmod +x deploy.sh'
                    sh 'make deploy ENV=staging'
                }
            }
        }
        stage('Deploy to QA') {
            when {
                branch 'QA'
            }
            steps {
                dir("${env.PROJECT_DIR}") {
                    sh 'chmod +x deploy.sh'
                    sh 'make deploy ENV=qa'
                }
            }
        }
    }
    post {
        success {
            slackSend(channel: '#typelevel-job-board', message: "Build Successful: ${env.JOB_NAME} ${env.BUILD_NUMBER}")
        }
        failure {
            slackSend(channel: '#typelevel-job-board', message: "Build Failed: ${env.JOB_NAME} ${env.BUILD_NUMBER}")
        }
    }
}
