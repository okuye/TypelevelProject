pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build and Test') {
            steps {
                sh 'sbt clean compile test'
            }
        }
        stage('Deploy to Dev') {
            when {
                branch 'main'
            }
            steps {
                sh 'chmod +x deploy.sh'
                sh './deploy.sh dev'
            }
        }
        stage('Deploy to Staging') {
            when {
                branch 'staging'
            }
            steps {
                sh 'chmod +x deploy.sh'
                sh './deploy.sh staging'
            }
        }
        stage('Deploy to QA') {
            when {
                branch 'qa'
            }
            steps {
                sh 'chmod +x deploy.sh'
                sh './deploy.sh qa'
            }
        }
        stage('Deploy to Production') {
            when {
                branch 'production'
            }
            steps {
                sh 'chmod +x deploy.sh'
                sh './deploy.sh production'
            }
        }
    }
    post {
        success {
            script {
                def chat_id = '6840647775'
                def bot_token = '7031490653:AAGd5TQsjcWzgBXMs3TKF9ozxjXhnCz7LoM'
                def message = "Build Successful: ${env.JOB_NAME} ${env.BUILD_NUMBER}"
                sh "curl -s -X POST https://api.telegram.org/bot${bot_token}/sendMessage -d chat_id=${chat_id} -d text='${message}'"
            }
        }
        failure {
            script {
                def chat_id = '6840647775'
                def bot_token = '7031490653:AAGd5TQsjcWzgBXMs3TKF9ozxjXhnCz7LoM'
                def message = "Build Failed: ${env.JOB_NAME} ${env.BUILD_NUMBER}"
                sh "curl -s -X POST https://api.telegram.org/bot${bot_token}/sendMessage -d chat_id=${chat_id} -d text='${message}'"
            }
        }
    }
}