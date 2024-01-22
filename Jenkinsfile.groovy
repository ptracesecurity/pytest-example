pipeline {
    agent any

    options {
        ansiColor('xterm')
        timestamps ()
    }


    stages {
        stage('Build') {
            steps {
                echo "Docker compose up"
                sh 'docker compose up --build -d'
            }
        }

        stage('Test') {
            steps {
                script {
                    def serviceName = "test-service"
                    sh "docker compose exec -T ${serviceName} pytest || exit 0"

                    echo "--------------------------------------------"
                    // Prepare results to read in Jenkins
                    def containerId = sh(script: 'docker compose ps -q test-service', returnStdout: true).trim()
                    sh "docker cp ${containerId}:/code/tests/results/. ."
                    // Change code source in coverage.xml file - related to local/relative files
                    sh "sed -i 's|<source>.*</source>|<source>myapp</source>|g' coverage.xml"

                    echo "Sending results to Jenkins"

                    junit healthScaleFactor: 1.0,
                        testResults: 'report.xml',
                        keepLongStdio: true,
                        keepProperties: true,
                        skipPublishingChecks: true

                    recordCoverage skipPublishingChecks: true,
                        tools: [[
                            parser: 'COBERTURA',
                            pattern: '**/coverage.xml'
                        ]]

                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: false,
                        reportDir: '',
                        reportFiles: 'report.html',
                        reportName: 'Test Results',
                    ])
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: false,
                        reportDir: 'html_coverage',
                        reportFiles: 'index.html',
                        reportName: 'Test Coverage',
                    ])
                }
            }
        }

        stage('Cleanup') {
            steps {
                script {
                    echo "Nothing to do..."
                }
            }
        }
    }

    post {
        always {
            echo "Docker compose down..."
            sh 'docker compose down'

            echo "Delete build dir..."
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