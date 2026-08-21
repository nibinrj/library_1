pipeline {
    // This explicitly tells Jenkins to run this job ONLY on your EC2 Agent
    agent {
        label 'docker-agent'
    }

    triggers {
        // Triggers the build automatically on git push
        githubPush()
    }

    environment {
        // The ID of the Docker Hub credentials we created in Jenkins earlier
        DOCKERHUB_CREDENTIALS = 'dockerhub-creds-id'

        // REPLACE 'yourdockerhubuser' WITH YOUR ACTUAL DOCKER HUB USERNAME
        IMAGE_NAME = 'nibinrj/library_1'

        // Uses the Jenkins build number as a unique Docker image tag
        IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Pulling code from GitHub to the EC2 Agent..."
                checkout scm
            }
        }

      stage('Maven Build') {
                  steps {
                      echo "Compiling and packaging the Java application..."

                      // 1. Print the version for debugging
                      sh 'mvn -version'

                      // 2. Temporarily force JAVA_HOME to the Java 21 path just for this build
                      withEnv(['JAVA_HOME=/usr/lib/jvm/java-21-amazon-corretto.x86_64']) {
                          sh 'mvn clean package -DskipTests'
                      }
                  }
              }

        stage('Build Docker Image') {
            steps {
                echo "Building the Docker image..."
                // Builds the Dockerfile located in your repo root
                sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .'
                sh 'docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest'
            }
        }

        stage('Push Docker Image') {
            steps {
                echo "Pushing image to Docker Hub..."
                // Securely injects credentials to log in and push
                withCredentials([usernamePassword(credentialsId: "${DOCKERHUB_CREDENTIALS}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh 'docker push ${IMAGE_NAME}:${IMAGE_TAG}'
                    sh 'docker push ${IMAGE_NAME}:latest'
                }
            }
        }
    }

    post {
        always {
            // Cleans up the local EC2 agent's hard drive so it doesn't run out of space
            echo "Cleaning up local Docker images..."
            sh 'docker rmi ${IMAGE_NAME}:${IMAGE_TAG} || true'
            sh 'docker rmi ${IMAGE_NAME}:latest || true'
        }
        success {
            echo "✅ Pipeline succeeded! Image is now in Docker Hub."
        }
        failure {
            echo "❌ Pipeline failed. Check the Jenkins console output for errors."
        }
    }
}