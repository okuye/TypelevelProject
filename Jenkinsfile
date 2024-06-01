pipeline {
    agent any
    environment {
        GITHUB_TOKEN = credentials('github-pat')
        TELEGRAM_BOT_TOKEN = credentials('telegram-bot-token')
    }
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/okuye/TypelevelProject.git', credentialsId: 'github-pat'
            }
        }
        stage('Compile') {
            steps {
                sh 'make build'
            }
        }
        stage('Run Tests') {
            steps {
                sh 'make test'
            }
        }
        stage('Deploy to Dev') {
            steps {
                sh 'chmod +x deploy.sh'
                sh './deploy.sh dev'
            }
        }
        stage('Deploy to Staging') {
            steps {
                sh 'chmod +x deploy.sh'
                sh './deploy.sh staging'
            }
        }
        stage('Deploy to QA') {
            steps {
                sh 'chmod +x deploy.sh'
                sh './deploy.sh qa'
            }
        }
    }
    post {
        always {
            script {
                echo 'Cleaning up workspace'
                cleanWs()
            }
        }
        success {
            script {
                def chat_id = '6840647775'
                def message = "Build Successful: ${env.JOB_NAME} ${env.BUILD_NUMBER}"
                sh "curl -s -X POST https://api.telegram.org/bot${env.TELEGRAM_BOT_TOKEN}/sendMessage -d chat_id=${chat_id} -d text='${message}'"
            }
        }
        failure {
            script {
                def chat_id = '6840647775'
                def message = "Build Failed: ${env.JOB_NAME} ${env.BUILD_NUMBER}"
                sh "curl -s -X POST https://api.telegram.org/bot${env.TELEGRAM_BOT_TOKEN}/sendMessage -d chat_id=${chat_id} -d text='${message}'"
            }
        }
    }
}