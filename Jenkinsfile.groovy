pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'docker compose build'
                sh 'docker compose up'
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
            echo 'Proces zakończony'
        }
        success {
            echo 'Testy zakończone sukcesem!'
        }
        failure {
            echo 'Testy zakończone niepowodzeniem.'
        }
    }
}