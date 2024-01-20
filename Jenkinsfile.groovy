pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                ansiColor('xterm') {
                    sh 'docker compose build'
                    sh 'docker compose up'
                }
            }
        }

        stage('Test') {
            steps {
                echo "After tests"
            }
        }

        stage('Cleanup') {
            steps {
                echo 'Sprzątanie po testach'
            }
        }
    }

    post {
        always {
            deleteDir()
        }
        success {
            echo 'Testy zakończone sukcesem!'
        }
        failure {
            echo 'Testy zakończone niepowodzeniem.'
        }
    }
}