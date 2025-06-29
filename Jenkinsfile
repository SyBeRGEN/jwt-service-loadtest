pipeline {
	agent any

    environment {
		JAVA_HOME = "/usr/lib/jvm/java-17-openjdk-amd64"
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
					sh 'chmod +x ./gradlew && ./gradlew build'
                }
            }
        }

        stage('Build verification-service') {
			steps {
				dir('verification-service') {
					sh 'chmod +x ./gradlew && ./gradlew build'
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

        stage('Install Docker Compose') {
			steps {
				sh '''
                    sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
                    sudo chmod +x /usr/local/bin/docker-compose
                    docker-compose --version
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
