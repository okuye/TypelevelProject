pipeline {
    agent any
    environment {
        GITHUB_TOKEN = credentials('github-token')  // Replace with your credentials ID
    }
    stages {
        stage('Compile') {
            steps {
                script {
                    updateGithubStatus('Compile', 'pending', 'Compiling...')
                    try {
                        sh 'make build'
                        updateGithubStatus('Compile', 'success', 'Compile successful')
                    } catch (Exception e) {
                        updateGithubStatus('Compile', 'failure', 'Compile failed')
                        throw e
                    }
                }
            }
        }
        stage('Run Tests') {
            steps {
                script {
                    updateGithubStatus('Run Tests', 'pending', 'Running tests...')
                    try {
                        sh 'make test'
                        updateGithubStatus('Run Tests', 'success', 'Tests successful')
                    } catch (Exception e) {
                        updateGithubStatus('Run Tests', 'failure', 'Tests failed')
                        throw e
                    }
                }
            }
        }
        stage('Deploy to Dev') {
            steps {
                script {
                    updateGithubStatus('Deploy to Dev', 'pending', 'Deploying to dev...')
                    try {
                        sh 'chmod +x deploy.sh'
                        sh './deploy.sh dev'
                        updateGithubStatus('Deploy to Dev', 'success', 'Deployed to dev')
                    } catch (Exception e) {
                        updateGithubStatus('Deploy to Dev', 'failure', 'Deploy to dev failed')
                        throw e
                    }
                }
            }
        }
        stage('Deploy to Staging') {
            steps {
                script {
                    updateGithubStatus('Deploy to Staging', 'pending', 'Deploying to staging...')
                    try {
                        sh 'chmod +x deploy.sh'
                        sh './deploy.sh staging'
                        updateGithubStatus('Deploy to Staging', 'success', 'Deployed to staging')
                    } catch (Exception e) {
                        updateGithubStatus('Deploy to Staging', 'failure', 'Deploy to staging failed')
                        throw e
                    }
                }
            }
        }
        stage('Deploy to QA') {
            steps {
                script {
                    updateGithubStatus('Deploy to QA', 'pending', 'Deploying to QA...')
                    try {
                        sh 'chmod +x deploy.sh'
                        sh './deploy.sh qa'
                        updateGithubStatus('Deploy to QA', 'success', 'Deployed to QA')
                    } catch (Exception e) {
                        updateGithubStatus('Deploy to QA', 'failure', 'Deploy to QA failed')
                        throw e
                    }
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
                updateGithubStatus('Overall', 'success', 'Build and deploy successful')
            }
        }
        failure {
            script {
                def chat_id = '6840647775'
                def bot_token = '7031490653:AAGd5TQsjcWzgBXMs3TKF9ozxjXhnCz7LoM'
                def message = "Build Failed: ${env.JOB_NAME} ${env.BUILD_NUMBER}"
                sh "curl -s -X POST https://api.telegram.org/bot${bot_token}/sendMessage -d chat_id=${chat_id} -d text='${message}'"
                updateGithubStatus('Overall', 'failure', 'Build or deploy failed')
            }
        }
    }
}

def updateGithubStatus(context, status, description) {
    def commitSha = env.GIT_COMMIT ?: 'HEAD'
    def repo = env.GIT_URL.split('/')[4].replaceAll('.git$', '')

    sh """
        curl -s -X POST \
        -H "Authorization: token ${GITHUB_TOKEN}" \
        -H "Accept: application/vnd.github.v3+json" \
        https://api.github.com/repos/${repo}/statuses/${commitSha} \
        -d '{
            "state": "${status}",
            "target_url": "${env.BUILD_URL}",
            "description": "${description}",
            "context": "${context}"
        }'
    """
}
