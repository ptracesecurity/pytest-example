pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                ansiColor('xterm') {
                    sh "mkdir -p ./test-results"
                    sh 'docker compose build'
                    sh 'docker compose up'
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
                echo 'Sprzątanie po testach'
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