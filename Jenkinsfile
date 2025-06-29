pipeline {
	agent any

    environment {
		JAVA_HOME = "/opt/java/openjdk"
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
    }

    stages {
		stage('Checkout') {
			steps {
				checkout scm
            }
        }

        stage('Check Java') {
			steps {
				sh 'java -version'
            }
        }

        stage('Build user-auth') {
			steps {
				dir('user-auth') {
					sh '''
                        chmod +x ./gradlew
                        ./gradlew clean build
                    '''
                }
            }
        }

        stage('Build verification-service') {
			steps {
				dir('verification-service') {
					sh '''
                        chmod +x ./gradlew
                        ./gradlew clean build
                    '''
                }
            }
        }

        stage('Ensure JMeter scripts are executable') {
			steps {
				sh '''
                    chmod +x tools/jmeter/bin/jmeter || true
                    chmod +x tools/jmeter/bin/*.sh || true
                    chmod +x tools/jmeter/bin/*.cmd || true
                    chmod +x tools/jmeter/bin/*.bat || true
                '''
            }
        }

        stage('Clean previous Docker setup') {
			steps {
				sh '''
            echo "[INFO] Removing old containers if exist..."
            docker rm -f prometheus influxdb redis || true
            docker-compose down || true
        '''
    		}
		}

        stage('Start Docker Compose') {
			steps {
				sh 'docker-compose up -d'
            }
        }

        stage('Wait for services') {
			steps {
				sh 'sleep 15'
            }
        }

        stage('Run JMeter test') {
			steps {
				sh 'tools/jmeter/bin/jmeter -n -t tools/jmeter/jwt_mock_test.jmx -l result.jtl -e -o report'
            }
        }

        stage('Archive Results') {
			steps {
				archiveArtifacts artifacts: 'result.jtl', fingerprint: true
                archiveArtifacts artifacts: 'report/**', fingerprint: true
            }
        }

        stage('Save Docker Logs') {
			steps {
				sh 'docker-compose logs > dc-logs.txt'
                archiveArtifacts artifacts: 'dc-logs.txt', fingerprint: true
            }
        }
    }
}
