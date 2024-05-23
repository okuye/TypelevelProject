pipeline {
    agent any
    stages {
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
        stage('Deploy to Production') {
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
