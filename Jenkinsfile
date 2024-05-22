pipeline {
    agent any
    stages {
        stage('Compile') {
            steps {
                // Change to the directory containing the Makefile if necessary
                dir('path/to/Makefile') {
                    sh 'make build'
                }
            }
        }
        stage('Run Tests') {
            steps {
                // Change to the directory containing the Makefile if necessary
                dir('path/to/Makefile') {
                    sh 'make test'
                }
            }
        }
        stage('Deploy to Dev') {
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
                branch 'QA'
            }
            steps {
                sh 'chmod +x deploy.sh'
                sh './deploy.sh qa'
            }
        }
        stage('Deploy to Production') {
            when {
                branch 'master'
            }
            steps {
                script {
                    def userInput = input(id: 'DeployToProd', message: 'Deploy to Production?', parameters: [choice(name: 'Proceed?', choices: 'Yes\nNo', description: 'Choose Yes to deploy to Production')])
                    if (userInput == 'Yes') {
                        sh 'chmod +x deploy.sh'
                        sh './deploy.sh production'
                    } else {
                        echo 'Deployment to Production was not approved'
                    }
                }
            }
        }
    }
    post {
        success {
            mail to: 'olakuye@gmail.com', subject: 'Build Successful', body: 'Deployment complete.'
        }
        failure {
            mail to: 'olakuye@gmail.com', subject: 'Build Failed', body: 'Check the logs.'
        }
    }
}
