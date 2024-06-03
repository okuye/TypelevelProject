pipeline {
    agent any
    environment {
        GITHUB_TOKEN = credentials('github-token')  // Ensure the ID matches the credentials ID
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    updateGithubStatus('Checkout', 'pending', 'Checking out the code...')
                    try {
                        checkout scm
                        updateGithubStatus('Checkout', 'success', 'Code checked out successfully')
                    } catch (Exception e) {
                        updateGithubStatus('Checkout', 'failure', 'Checkout failed')
                        throw e
                    }
                }
            }
        }
        stage('Build and Test') {
            steps {
                script {
                    updateGithubStatus('Build and Test', 'pending', 'Building and testing...')
                    try {
                        sh 'sbt clean compile test'
                        updateGithubStatus('Build and Test', 'success', 'Build and tests successful')
                    } catch (Exception e) {
                        updateGithubStatus('Build and Test', 'failure', 'Build or tests failed')
                        throw e
                    }
                }
            }
        }
        stage('Deploy to Dev') {
            when {
                branch 'main'
            }
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
            when {
                branch 'staging'
            }
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
            when {
                branch 'qa'
            }
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
        stage('Deploy to Production') {
            when {
                branch 'production'
            }
            steps {
                script {
                    updateGithubStatus('Deploy to Production', 'pending', 'Deploying to production...')
                    try {
                        sh 'chmod +x deploy.sh'
                        sh './deploy.sh production'
                        updateGithubStatus('Deploy to Production', 'success', 'Deployed to production')
                    } catch (Exception e) {
                        updateGithubStatus('Deploy to Production', 'failure', 'Deploy to production failed')
                        throw e
                    }
                }
            }
        }
    }
    post {
        success {
            script {
                updateGithubStatus('Overall', 'success', 'Build and deploy successful')
                def chat_id = '6840647775'
                def bot_token = '7031490653:AAGd5TQsjcWzgBXMs3TKF9ozxjXhnCz7LoM'
                def message = "Build Successful: ${env.JOB_NAME} ${env.BUILD_NUMBER}"
                sh "curl -s -X POST https://api.telegram.org/bot${bot_token}/sendMessage -d chat_id=${chat_id} -d text='${message}'"
            }
        }
        failure {
            script {
                updateGithubStatus('Overall', 'failure', 'Build or deploy failed')
                def chat_id = '6840647775'
                def bot_token = '7031490653:AAGd5TQsjcWzgBXMs3TKF9ozxjXhnCz7LoM'
                def message = "Build Failed: ${env.JOB_NAME} ${env.BUILD_NUMBER}"
                sh "curl -s -X POST https://api.telegram.org/bot${bot_token}/sendMessage -d chat_id=${chat_id} -d text='${message}'"
            }
        }
    }
}

def updateGithubStatus(context, state, description) {
    def repoName = env.GIT_URL.tokenize('/').last().replaceAll(/\.git$/, '')
    def repoOwner = env.GIT_URL.tokenize('/')[3]
    def commitSha = env.GIT_COMMIT

    sh """
        curl -s -X POST -H "Authorization: token ${GITHUB_TOKEN}" \
        -H "Accept: application/vnd.github.v3+json" \
        https://api.github.com/repos/${repoOwner}/${repoName}/statuses/${commitSha} \
        -d '{
            "state": "${state}",
            "target_url": "${env.BUILD_URL}",
            "description": "${description}",
            "context": "${context}"
        }'
    """
}

