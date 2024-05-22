pipeline {
    agent any
    environment {
        PROJECT_DIR = '.' // Root directory of the project
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
        stage('Deploy to Production') {
            when {
                branch 'main'
            }
            steps {
                script {
                    def userInput = input(id: 'DeployToProd', message: 'Deploy to Production?', parameters: [choice(name: 'Proceed?', choices: 'Yes\nNo', description: 'Choose Yes to deploy to Production')])
                    if (userInput == 'Yes') {
                        dir("${env.PROJECT_DIR}") {
                            sh 'chmod +x deploy.sh'
                            sh 'make deploy ENV=production'
                        }
                    } else {
                        echo 'Deployment to Production was not approved'
                    }
                }
            }
        }
    }
    post {
        success {
            mail to: 'olakuye@gmail.com', subject: 'Built Successfully dude', body: 'Deployment complete.'
        }
        failure {
            mail to: 'olakuye@gmail.com', subject: 'Build Failed again man', body: 'Check the logs.'
        }
    }
}
