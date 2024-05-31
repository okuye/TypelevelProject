pipeline {
    agent any
    environment {
        GITHUB_TOKEN = credentials('github-to-jenkins') // Reference the stored GitHub token
    }
    stages {
        stage('Compile') {
            steps {
                script {
                    def commitSha = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
                    sh """
                    curl -s -X POST -H "Authorization: token ${GITHUB_TOKEN}" -H "Accept: application/vnd.github.v3+json" \
                    https://api.github.com/repos/okuye/TypelevelProject/statuses/${commitSha} -d '{
                        "state": "pending",
                        "target_url": "${env.BUILD_URL}",
                        "description": "Compiling the code...",
                        "context": "Compile"
                    }'
                    """
                }
                sh 'make build'
            }
        }
        stage('Run Tests') {
            steps {
                script {
                    def commitSha = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
                    sh """
                    curl -s -X POST -H "Authorization: token ${GITHUB_TOKEN}" -H "Accept: application/vnd.github.v3+json" \
                    https://api.github.com/repos/okuye/TypelevelProject/statuses/${commitSha} -d '{
                        "state": "pending",
                        "target_url": "${env.BUILD_URL}",
                        "description": "Running tests...",
                        "context": "Run Tests"
                    }'
                    """
                }
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
        success {
            script {
                def commitSha = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
                sh """
                curl -s -X POST -H "Authorization: token ${GITHUB_TOKEN}" -H "Accept: application/vnd.github.v3+json" \
                https://api.github.com/repos/okuye/TypelevelProject/statuses/${commitSha} -d '{
                    "state": "success",
                    "target_url": "${env.BUILD_URL}",
                    "description": "Build successful",
                    "context": "Overall"
                }'
                """
                def chat_id = '6840647775'
                def bot_token = '7031490653:AAGd5TQsjcWzgBXMs3TKF9ozxjXhnCz7LoM'
                def message = "Build Successful: ${env.JOB_NAME} ${env.BUILD_NUMBER}"
                sh "curl -s -X POST https://api.telegram.org/bot${bot_token}/sendMessage -d chat_id=${chat_id} -d text='${message}'"
            }
        }
        failure {
            script {
                def commitSha = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
                sh """
                curl -s -X POST -H "Authorization: token ${GITHUB_TOKEN}" -H "Accept: application/vnd.github.v3+json" \
                https://api.github.com/repos/<your-username>/<your-repo>/statuses/${commitSha} -d '{
                    "state": "failure",
                    "target_url": "${env.BUILD_URL}",
                    "description": "Build failed",
                    "context": "Overall"
                }'
                """
                def chat_id = '6840647775'
                def bot_token = '7031490653:AAGd5TQsjcWzgBXMs3TKF9ozxjXhnCz7LoM'
                def message = "Build Failed: ${env.JOB_NAME} ${env.BUILD_NUMBER}"
                sh "curl -s -X POST https://api.telegram.org/bot${bot_token}/sendMessage -d chat_id=${chat_id} -d text='${message}'"
            }
        }
    }
}
