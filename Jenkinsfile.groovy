pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                ansiColor('xterm') {
                    sh 'ls'
                    sh 'pwd'
                    echo "---------------------------------"
                    sh 'docker compose up --build -d'
                }
            }
        }

        stage('Test') {
            steps {
                echo "After tests"
                sh 'ls'
                sh 'pwd'
            }
        }

        stage('Cleanup') {
            steps {
                script {
                    def containerId = sh(script: 'docker compose ps -q test-service', returnStdout: true).trim()
                    sh "docker cp ${containerId}:/code/tests/results/. ./test-results"
                    echo 'Sprzątanie po testach'
                    sh 'docker compose down'
                }
            }
        }
    }

    post {
        always {
            echo "always dir delete but not now"
            //             deleteDir()
        }
        success {
            echo 'Testy zakończone sukcesem!'
        }
        failure {
            echo 'Testy zakończone niepowodzeniem.'
        }
    }
}