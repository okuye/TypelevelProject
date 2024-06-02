pipeline {
    agent { label 'main' }
    environment {
        GITHUB_TOKEN = credentials('github-pat')  // Use the credentials ID from Jenkins
        TMPDIR = "${env.WORKSPACE}/tmp"
        SBT_OPTS = "-Dsbt.boot.directory=${env.WORKSPACE}/.sbt/boot -Dsbt.global.base=${env.WORKSPACE}/.sbt/global"
    }
    stages {
        stage('Prepare Environment') {
            steps {
                script {
                    sh 'mkdir -p ${TMPDIR} ${env.WORKSPACE}/.sbt/boot ${env.WORKSPACE}/.sbt/global'
                }
            }
        }
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
        stage('Compile') {
            steps {
                script {
                    updateGithubStatus('Compile', 'pending', 'Compiling the code...')
                    try {
                        sh 'sbt compile'
                        updateGithubStatus('Compile', 'success', 'Compilation successful')
                    } catch (Exception e) {
                        updateGithubStatus('Compile', 'failure', 'Compilation failed')
                        throw e
                    }
                }
            }
        }
        stage('Run Tests') {
            steps {
                script {
                    updateGithubStatus('Tests', 'pending', 'Running tests...')
                    try {
                        sh 'sbt test'
                        updateGithubStatus('Tests', 'success', 'Tests passed')
                    } catch (Exception e) {
                        updateGithubStatus('Tests', 'failure', 'Tests failed')
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
        always {
            script {
                try {
                    cleanWs()
                } catch (Exception e) {
                    echo "Workspace cleanup failed: ${e.message}"
                }
            }
        }
    }
}

def updateGithubStatus(context, state, description) {
    def repoName = env.GIT_URL?.tokenize('/')?.last()?.replaceAll(/\.git$/, '')
    def repoOwner = env.GIT_URL?.tokenize('/')?.getAt(3)
    def commitSha = env.GIT_COMMIT

    if (repoName && repoOwner) {
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
    } else {
        echo "GitHub repository information is not available. Skipping GitHub status update."
    }
}
