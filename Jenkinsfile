pipeline {
    agent any
    environment {
        PROJECT_DIR = '.' // Root directory of the project ooo
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
            script {
                def message = "Build Successful: ${env.JOB_NAME} ${env.BUILD_NUMBER}"
                sh "curl -s -X POST https://api.telegram.org/bot${env.TELEGRAM_BOT_TOKEN}/sendMessage -d chat_id=${env.TELEGRAM_CHAT_ID} -d text=\"${message}\""
            }
        }
        failure {
            script {
                def message = "Build Failed: ${env.JOB_NAME} ${env.BUILD_NUMBER}"
                sh "curl -s -X POST https://api.telegram.org/bot${env.TELEGRAM_BOT_TOKEN}/sendMessage -d chat_id=${env.TELEGRAM_CHAT_ID} -d text=\"${message}\""
            }
        }
    }
}
