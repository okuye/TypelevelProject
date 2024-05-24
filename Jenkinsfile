pipeline {
    agent any

    environment {
        GITHUB_TOKEN = credentials('github-token')
    }

    stages {
        stage('Compile') {
            steps {
                githubNotify context: 'Compile', status: 'PENDING'
                try {
                    sh 'make build'
                    githubNotify context: 'Compile', status: 'SUCCESS'
                } catch (Exception e) {
                    githubNotify context: 'Compile', status: 'FAILURE'
                    throw e
                }
            }
        }
        stage('Run Tests') {
            steps {
                githubNotify context: 'Run Tests', status: 'PENDING'
                try {
                    sh 'make test'
                    githubNotify context: 'Run Tests', status: 'SUCCESS'
                } catch (Exception e) {
                    githubNotify context: 'Run Tests', status: 'FAILURE'
                    throw e
                }
            }
        }
        stage('Deploy to Dev') {
            steps {
                githubNotify context: 'Deploy to Dev', status: 'PENDING'
                try {
                    sh 'chmod +x deploy.sh'
                    sh './deploy.sh dev'
                    githubNotify context: 'Deploy to Dev', status: 'SUCCESS'
                } catch (Exception e) {
                    githubNotify context: 'Deploy to Dev', status: 'FAILURE'
                    throw e
                }
            }
        }
        stage('Deploy to Staging') {
            steps {
                githubNotify context: 'Deploy to Staging', status: 'PENDING'
                try {
                    sh 'chmod +x deploy.sh'
                    sh './deploy.sh staging'
                    githubNotify context: 'Deploy to Staging', status: 'SUCCESS'
                } catch (Exception e) {
                    githubNotify context: 'Deploy to Staging', status: 'FAILURE'
                    throw e
                }
            }
        }
        stage('Deploy to QA') {
            steps {
                githubNotify context: 'Deploy to QA', status: 'PENDING'
                try {
                    sh 'chmod +x deploy.sh'
                    sh './deploy.sh qa'
                    githubNotify context: 'Deploy to QA', status: 'SUCCESS'
                } catch (Exception e) {
                    githubNotify context: 'Deploy to QA', status: 'FAILURE'
                    throw e
                }
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
