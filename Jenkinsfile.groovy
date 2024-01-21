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
                    sh "docker cp ${containerId}:/code/tests/results/. ."

                    junit healthScaleFactor: 5.0,
                        testResults: 'report.xml',
                        keepLongStdio: true,
                        keepProperties: true,
                        skipPublishingChecks: true

                    sh 'cp coverage.xml myapp/'
                    // recordCoverage skipPublishingChecks: true,
                    //    tools: [[
                    //        parser: 'COBERTURA',
                    //        pattern: 'myapp/coverage.xml'
                    //    ]]

                     cobertura autoUpdateHealth: false,
                         autoUpdateStability: false,
                         coberturaReportFile: 'myapp/coverage.xml',
                         conditionalCoverageTargets: '70, 0, 0',
                         enableNewApi: true,
                         failUnhealthy: false,
                         failUnstable: false,
                         lineCoverageTargets: '80, 0, 0',
                         maxNumberOfBuilds: 0,
                         methodCoverageTargets: '80, 0, 0',
                         onlyStable: false,
                         sourceEncoding: 'ASCII',
                         zoomCoverageChart: false
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
                        echo 'Sprzątanie po testach'

                }
            }
        }
    }

    post {
        always {
            echo "always dir delete but not now"
            sh 'docker compose down'
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
