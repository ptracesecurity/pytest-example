pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo "Zakładam, że krok budowania nie jest potrzebny lub został już skonfigurowany."
            }
        }

        stage('Test') {
            steps {
                sh 'pytest'
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