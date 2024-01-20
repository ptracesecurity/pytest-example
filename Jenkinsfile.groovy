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

                    junit healthScaleFactor: 5.0, testResults: 'test-results/report.xml'

                    cobertura coberturaReportFile: 'test-results/coverage.xml'
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: false,
                        reportDir: '',
                        reportFiles: 'test-results/report.html',
                        reportName: 'Test Results',
                    ])
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: false,
                        reportDir: 'test-results/html_coverage',
                        reportFiles: 'index.html',
                        reportName: 'Test Coverage',
                    ])
                        echo 'Sprzątanie po testach'

                }
            }
        }
    }

    post {
        always {
            echo "always dir delete but not now"
            sh 'docker-compose down'
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