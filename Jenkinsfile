pipeline {
    agent any
    environment {
        GITHUB_TOKEN = credentials('github-to-jenkins')  // Use the credentials ID from Jenkins
        PATH = "/root/.sdkman/candidates/sbt/current/bin:/root/.sdkman/candidates/scala/current/bin:${PATH}"
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
        stage('Compile') {
            steps {
                dir('project') {
                    script {
                        updateGithubStatus('Compile', 'pending', 'Compiling the code...')
                        try {
                            sh 'sbt clean compile'
                            updateGithubStatus('Compile', 'success', 'Compilation successful')
                        } catch (Exception e) {
                            updateGithubStatus('Compile', 'failure', 'Compilation failed')
                            throw e
                        }
                    }
                }
            }
        }
        stage('Run Tests') {
            steps {
                dir('project') {
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
                    
