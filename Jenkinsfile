pipeline {
    agent any
    parameters {
        booleanParam(name: 'DEPLOY_TO_DEV', defaultValue: true, description: 'Deploy to Dev')
        booleanParam(name: 'DEPLOY_TO_STAGING', defaultValue: false, description: 'Deploy to Staging')
        booleanParam(name: 'DEPLOY_TO_QA', defaultValue: false, description: 'Deploy to QA')
    }
    environment {
        PROJECT_DIR = '.' // Root directory of the project
        TELEGRAM_CHAT_ID = '6840647775'
        TELEGRAM_BOT_TOKEN = '7031490653:AAGd5TQsjcWzgBXMs3TKF9ozxjXhnCz7LoM'
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
            when {
                expression { return params.DEPLOY_TO_DEV }
            }
            steps {
                dir("${env.PROJECT_DIR}") {
                    sh 'chmod +x deploy.sh'
                    sh 'make deploy ENV=dev'
                }
            }
        }
        stage('Deploy to Staging') {
            when {
                expression { return params.DEPLOY_TO_STAGING }
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
                expression { return params.DEPLOY_TO_QA }
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
            sh 'curl -s -X POST https://api.telegram.org/bot${env.TELEGRAM_BOT_TOKEN}/sendMessage -d chat_id=${env.TELEGRAM_CHAT_ID} -d text="Build Successful: ${env.JOB_NAME} ${env.BUILD_NUMBER}"'
        }
        failure {
            slackSend(channel: '#typelevel-job-board', message: "Build Failed: ${env.JOB_NAME} ${env.BUILD_NUMBER}")
            sh 'curl -s -X POST https://api.telegram.org/bot${env.TELEGRAM_BOT_TOKEN}/sendMessage -d chat_id=${env.TELEGRAM_CHAT_ID} -d text="Build Failed: ${env.JOB_NAME} ${env.BUILD_NUMBER}"'
        }
    }
}
